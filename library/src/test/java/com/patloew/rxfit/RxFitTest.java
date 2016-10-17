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
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareOnlyThisForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;

import rx.Completable;
import rx.Observable;
import rx.Single;
import rx.Subscriber;
import rx.observers.TestSubscriber;

import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareOnlyThisForTest({ ContextCompat.class, Fitness.class, Status.class, ConnectionResult.class, BaseRx.class })
@SuppressStaticInitializationFor("com.google.android.gms.fitness.Fitness")
public class RxFitTest extends BaseOnSubscribeTest {

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        super.setup();
    }

    // GoogleApiClientObservable

    @Test
    public void GoogleAPIClientObservable_Success() {
        TestSubscriber<GoogleApiClient> sub = new TestSubscriber<>();
        GoogleAPIClientSingle single = PowerMockito.spy(new GoogleAPIClientSingle(ctx, new Api[] {}, new Scope[] {}));

        setupBaseSingleSuccess(single);
        Single.create(single).subscribe(sub);

        assertSingleValue(sub, apiClient);
    }

    @Test
    public void GoogleAPIClientObservable_ConnectionException() {
        TestSubscriber<GoogleApiClient> sub = new TestSubscriber<>();
        final GoogleAPIClientSingle single = PowerMockito.spy(new GoogleAPIClientSingle(ctx, new Api[] {}, new Scope[] {}));

        setupBaseSingleError(single);
        Single.create(single).subscribe(sub);

        assertError(sub, GoogleAPIConnectionException.class);
    }

    // CheckConnectionCompletable

    @Test
    public void CheckConnectionCompletable_Success() {
        TestSubscriber<GoogleApiClient> sub = new TestSubscriber<>();
        CheckConnectionObservable observable = PowerMockito.spy(new CheckConnectionObservable(rxFit));

        setupBaseObservableSuccess(observable);
        Completable.fromObservable(Observable.create(observable)).subscribe(sub);

        sub.assertCompleted();
        sub.assertNoValues();
    }

    @Test
    public void CheckConnectionCompletable_Error() {
        TestSubscriber<GoogleApiClient> sub = new TestSubscriber<>();
        CheckConnectionObservable observable = PowerMockito.spy(new CheckConnectionObservable(rxFit));

        setupBaseObservableError(observable);
        Completable.fromObservable(Observable.create(observable)).subscribe(sub);

        sub.assertError(GoogleAPIConnectionException.class);
        sub.assertNoValues();
    }


    @Test
    public void CheckConnectionCompletable_Resolution_Success() {
        TestSubscriber<GoogleApiClient> sub1 = new TestSubscriber<>();
        TestSubscriber<GoogleApiClient> sub2 = new TestSubscriber<>();
        final GoogleApiClient apiClient2 = Mockito.mock(GoogleApiClient.class);
        final CheckConnectionObservable observable = PowerMockito.spy(new CheckConnectionObservable(rxFit));

        Completable completable = Completable.fromObservable(Observable.create(observable));

        setupBaseObservableResolution(observable, apiClient);
        completable.subscribe(sub1);

        setupBaseObservableResolution(observable, apiClient2);
        completable.subscribe(sub2);

        doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                observable.onGoogleApiClientReady(apiClient, getSubscriber(observable, apiClient));
                return null;
            }
        }).when(apiClient).connect();

        doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                observable.onGoogleApiClientReady(apiClient2, getSubscriber(observable, apiClient2));
                return null;
            }
        }).when(apiClient2).connect();

        BaseRx.onResolutionResult(Activity.RESULT_OK, Mockito.mock(ConnectionResult.class));

        sub1.assertCompleted();
        sub1.assertNoValues();

        sub2.assertCompleted();
        sub2.assertNoValues();
    }

    @Test
    public void CheckConnectionCompletable_Resolution_Error() {
        TestSubscriber<GoogleApiClient> sub1 = new TestSubscriber<>();
        TestSubscriber<GoogleApiClient> sub2 = new TestSubscriber<>();
        final GoogleApiClient apiClient2 = Mockito.mock(GoogleApiClient.class);
        final CheckConnectionObservable observable = PowerMockito.spy(new CheckConnectionObservable(rxFit));

        Completable completable = Completable.fromObservable(Observable.create(observable));

        setupBaseObservableResolution(observable, apiClient);
        completable.subscribe(sub1);

        setupBaseObservableResolution(observable, apiClient2);
        completable.subscribe(sub2);

        BaseObservable.onResolutionResult(Activity.RESULT_CANCELED, Mockito.mock(ConnectionResult.class));

        sub1.assertError(GoogleAPIConnectionException.class);
        sub1.assertNoValues();

        sub2.assertError(GoogleAPIConnectionException.class);
        sub2.assertNoValues();
    }

    @Test
    public void CheckConnectionCompletable_Resolution_Error_ResumeNext_Resolution_Success() {
        TestSubscriber<Void> sub = new TestSubscriber<>();
        ConnectionResult connectionResult = Mockito.mock(ConnectionResult.class);
        final CheckConnectionObservable observable = PowerMockito.spy(new CheckConnectionObservable(rxFit));

        setupBaseObservableResolution(observable, apiClient);
        Completable.fromObservable(Observable.create(observable).compose(RxFitOnExceptionResumeNext.with(Observable.<Void>just(null))))
                .subscribe(sub);

        when(connectionResult.hasResolution()).thenReturn(true);
        BaseObservable.onResolutionResult(Activity.RESULT_CANCELED, connectionResult);

        sub.assertError(GoogleAPIConnectionException.class);
        sub.assertNoValues();
    }

    @Test
    public void CheckConnectionCompletable_Resolution_Error_ResumeNext_NoResolution_Success() {
        TestSubscriber<Void> sub = new TestSubscriber<>();
        ConnectionResult connectionResult = Mockito.mock(ConnectionResult.class);
        final CheckConnectionObservable observable = PowerMockito.spy(new CheckConnectionObservable(rxFit));

        setupBaseObservableResolution(observable, apiClient);
        Completable.fromObservable(Observable.create(observable).compose(RxFitOnExceptionResumeNext.with(Observable.<Void>just(null))))
                .subscribe(sub);

        when(connectionResult.hasResolution()).thenReturn(false);
        BaseObservable.onResolutionResult(Activity.RESULT_CANCELED, connectionResult);

        sub.assertCompleted();
        sub.assertNoValues();
    }

    @Test
    public void CheckConnectionCompletable_Resolution_Success_ResumeNext_Error() {
        TestSubscriber<Void> sub = new TestSubscriber<>();
        ConnectionResult connectionResult = Mockito.mock(ConnectionResult.class);
        final CheckConnectionObservable observable = PowerMockito.spy(new CheckConnectionObservable(rxFit));

        setupBaseObservableResolution(observable, apiClient);
        Completable.fromObservable(Observable.create(observable).compose(RxFitOnExceptionResumeNext.with(Observable.<Void>just(null))))
                .subscribe(sub);

        doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                observable.onGoogleApiClientReady(apiClient, getSubscriber(observable, apiClient));
                return null;
            }
        }).when(apiClient).connect();

        doThrow(new Error("Generic error")).when(observable).onGoogleApiClientReady(Matchers.any(GoogleApiClient.class), Matchers.any(Subscriber.class));
        BaseObservable.onResolutionResult(Activity.RESULT_OK, connectionResult);

        sub.assertError(Error.class);
        sub.assertNoValues();
    }

}
