package com.patloew.rxfit;

import android.app.Activity;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.fitness.Fitness;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareOnlyThisForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.Single;
import io.reactivex.exceptions.CompositeException;
import io.reactivex.observers.TestObserver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareOnlyThisForTest({ Observable.class, ContextCompat.class, Fitness.class, Status.class, ConnectionResult.class, BaseRx.class })
@SuppressStaticInitializationFor("com.google.android.gms.fitness.Fitness")
public class RxFitTest extends BaseOnSubscribeTest {

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        super.setup();
    }

    // RxFit

    @Test
    public void setTimeout() {
        rxFit.setDefaultTimeout(TIMEOUT_TIME, TIMEOUT_TIMEUNIT);
        assertEquals(TIMEOUT_TIME, (long) rxFit.timeoutTime);
        assertEquals(TIMEOUT_TIMEUNIT, rxFit.timeoutUnit);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setTimeout_TimeUnitMissing() {
        rxFit.setDefaultTimeout(TIMEOUT_TIME, null);
        assertNull(rxFit.timeoutTime);
        assertNull(rxFit.timeoutUnit);
    }

    @Test
    public void resetDefaultTimeout() {
        rxFit.setDefaultTimeout(TIMEOUT_TIME, TIMEOUT_TIMEUNIT);
        rxFit.resetDefaultTimeout();
        assertNull(rxFit.timeoutTime);
        assertNull(rxFit.timeoutUnit);
    }


    // Check Connection

    @Test
    public void checkConnection() {
        final Observable<Void> observable = Observable.empty();

        PowerMockito.mockStatic(Observable.class, invocation -> observable);
        ArgumentCaptor<CheckConnectionObservable> captor = ArgumentCaptor.forClass(CheckConnectionObservable.class);

        rxFit.checkConnection();

        PowerMockito.verifyStatic(times(1));
        Observable.create(captor.capture());

        CheckConnectionObservable checkConnectionObservable = captor.getValue();
        assertNotNull(checkConnectionObservable);
    }

    // GoogleApiClientObservable

    @Test
    public void GoogleAPIClientObservable_Success() {
        GoogleAPIClientSingle single = PowerMockito.spy(new GoogleAPIClientSingle(ctx, new Api[] {}, new Scope[] {}));

        setupBaseSingleSuccess(single);

        assertSingleValue(Single.create(single).test(), apiClient);
    }

    @Test
    public void GoogleAPIClientObservable_ConnectionException() {
        final GoogleAPIClientSingle single = PowerMockito.spy(new GoogleAPIClientSingle(ctx, new Api[] {}, new Scope[] {}));

        setupBaseSingleError(single);

        assertError(Single.create(single).test(), GoogleAPIConnectionException.class);
    }

    // CheckConnectionCompletable

    @Test
    public void CheckConnectionCompletable_Success() {
        CheckConnectionObservable observable = PowerMockito.spy(new CheckConnectionObservable(rxFit));

        setupBaseObservableSuccess(observable);
        TestObserver<Void> sub = Completable.fromObservable(Observable.create(observable)).test();

        sub.assertComplete();
        sub.assertNoValues();
    }

    @Test
    public void CheckConnectionCompletable_Error() {
        CheckConnectionObservable observable = PowerMockito.spy(new CheckConnectionObservable(rxFit));

        setupBaseObservableError(observable);
        TestObserver<Void> sub = Completable.fromObservable(Observable.create(observable)).test();

        sub.assertError(GoogleAPIConnectionException.class);
        sub.assertNoValues();
    }


    @Test
    public void CheckConnectionCompletable_Resolution_Success() {
        final GoogleApiClient apiClient2 = Mockito.mock(GoogleApiClient.class);
        final CheckConnectionObservable observable = PowerMockito.spy(new CheckConnectionObservable(rxFit));

        Completable completable = Completable.fromObservable(Observable.create(observable));

        setupBaseObservableResolution(observable, apiClient);
        TestObserver<Void> sub1 = completable.test();

        setupBaseObservableResolution(observable, apiClient2);
        TestObserver<Void> sub2 = completable.test();

        doAnswer(invocation -> {
            observable.onGoogleApiClientReady(apiClient, getSubscriber(observable, apiClient));
            return null;
        }).when(apiClient).connect();

        doAnswer(invocation -> {
            observable.onGoogleApiClientReady(apiClient2, getSubscriber(observable, apiClient2));
            return null;
        }).when(apiClient2).connect();

        BaseRx.onResolutionResult(Activity.RESULT_OK, Mockito.mock(ConnectionResult.class));

        sub1.assertComplete();
        sub1.assertNoValues();

        sub2.assertComplete();
        sub2.assertNoValues();
    }

    @Test
    public void CheckConnectionCompletable_Resolution_Error() {
        final GoogleApiClient apiClient2 = Mockito.mock(GoogleApiClient.class);
        final CheckConnectionObservable observable = PowerMockito.spy(new CheckConnectionObservable(rxFit));

        Completable completable = Completable.fromObservable(Observable.create(observable));

        setupBaseObservableResolution(observable, apiClient);
        TestObserver<Void> sub1 = completable.test();

        setupBaseObservableResolution(observable, apiClient2);
        TestObserver<Void> sub2 = completable.test();

        BaseObservable.onResolutionResult(Activity.RESULT_CANCELED, Mockito.mock(ConnectionResult.class));

        sub1.assertError(GoogleAPIConnectionException.class);
        sub1.assertNoValues();

        sub2.assertError(GoogleAPIConnectionException.class);
        sub2.assertNoValues();
    }

    @Test
    public void CheckConnectionCompletable_Resolution_Error_ResumeNext_Resolution_Success() {
        ConnectionResult connectionResult = Mockito.mock(ConnectionResult.class);
        final CheckConnectionObservable observable = PowerMockito.spy(new CheckConnectionObservable(rxFit));

        setupBaseObservableResolution(observable, apiClient);
        TestObserver<Void> sub = Completable.fromObservable(Observable.create(observable).compose(RxFitOnExceptionResumeNext.with(Observable.empty()))).test();

        when(connectionResult.hasResolution()).thenReturn(true);
        BaseObservable.onResolutionResult(Activity.RESULT_CANCELED, connectionResult);

        sub.assertError(throwable -> ((CompositeException)throwable).getExceptions().get(0) instanceof GoogleAPIConnectionException);
        sub.assertNoValues();
    }

    @Test
    public void CheckConnectionCompletable_Resolution_Error_ResumeNext_NoResolution_Success() {
        ConnectionResult connectionResult = Mockito.mock(ConnectionResult.class);
        final CheckConnectionObservable observable = PowerMockito.spy(new CheckConnectionObservable(rxFit));

        setupBaseObservableResolution(observable, apiClient);
        TestObserver<Void> sub = Completable.fromObservable(Observable.create(observable).compose(RxFitOnExceptionResumeNext.with(Observable.empty()))).test();

        when(connectionResult.hasResolution()).thenReturn(false);
        BaseObservable.onResolutionResult(Activity.RESULT_CANCELED, connectionResult);

        sub.assertComplete();
        sub.assertNoValues();
    }

    @Test
    public void CheckConnectionCompletable_Resolution_Success_ResumeNext_Error() {
        ConnectionResult connectionResult = Mockito.mock(ConnectionResult.class);
        final CheckConnectionObservable observable = PowerMockito.spy(new CheckConnectionObservable(rxFit));

        setupBaseObservableResolution(observable, apiClient);
        TestObserver<Void> sub = Completable.fromObservable(Observable.create(observable).compose(RxFitOnExceptionResumeNext.with(Observable.empty()))).test();

        doAnswer(invocation -> {
            observable.onGoogleApiClientReady(apiClient, getSubscriber(observable, apiClient));
            return null;
        }).when(apiClient).connect();

        Error error = new Error("Generic error");
        doThrow(error).when(observable).onGoogleApiClientReady(Matchers.any(GoogleApiClient.class), Matchers.any(ObservableEmitter.class));
        BaseObservable.onResolutionResult(Activity.RESULT_OK, connectionResult);

        sub.assertError(throwable -> ((CompositeException)throwable).getExceptions().get(0) == error);
        sub.assertNoValues();
    }

}
