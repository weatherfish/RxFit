package com.patloew.rxfit;

import android.content.Intent;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.fitness.Fitness;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.core.classloader.annotations.PrepareOnlyThisForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;

import rx.Observable;
import rx.Subscriber;
import rx.observers.TestSubscriber;

import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

@RunWith(PowerMockRunner.class)
@PrepareOnlyThisForTest({ ContextCompat.class, Fitness.class, Status.class, ConnectionResult.class })
@SuppressStaticInitializationFor("com.google.android.gms.fitness.Fitness")
public class BaseObservableTest extends BaseOnSubscribeTest {

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        super.setup();
    }

    @Test
    public void BaseObservable_ApiClient_Connected() {
        final Object object = new Object();
        TestSubscriber<Object> sub = new TestSubscriber<>();
        BaseObservable<Object> observable = spy(new BaseObservable<Object>(ctx, new Api[] {}, null) {
            @Override
            protected void onGoogleApiClientReady(GoogleApiClient apiClient, Subscriber<? super Object> subscriber) {
                subscriber.onNext(object);
                subscriber.onCompleted();
            }
        });

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                BaseRx.ApiClientConnectionCallbacks callbacks = invocation.getArgumentAt(0, BaseRx.ApiClientConnectionCallbacks.class);
                callbacks.setClient(apiClient);
                callbacks.onConnected(null);
                return apiClient;
            }
        }).when(observable).createApiClient(Matchers.any(BaseRx.ApiClientConnectionCallbacks.class));

        Observable.create(observable).subscribe(sub);

        sub.assertValue(object);
        sub.assertCompleted();
    }

    @Test
    public void BaseObservable_ApiClient_ConnectionSuspended() {
        final Object object = new Object();
        TestSubscriber<Object> sub = new TestSubscriber<>();
        BaseObservable<Object> observable = spy(new BaseObservable<Object>(ctx, new Api[] {}, null) {
            @Override
            protected void onGoogleApiClientReady(GoogleApiClient apiClient, Subscriber<? super Object> subscriber) {
                subscriber.onNext(object);
                subscriber.onCompleted();
            }
        });

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                BaseRx.ApiClientConnectionCallbacks callbacks = invocation.getArgumentAt(0, BaseRx.ApiClientConnectionCallbacks.class);
                callbacks.setClient(apiClient);
                callbacks.onConnectionSuspended(0);
                return apiClient;
            }
        }).when(observable).createApiClient(Matchers.any(BaseRx.ApiClientConnectionCallbacks.class));

        Observable.create(observable).subscribe(sub);

        sub.assertNoValues();
        sub.assertError(GoogleAPIConnectionSuspendedException.class);
    }

    @Test
    public void BaseObservable_ApiClient_ConnectionFailed_NoResulution() {
        final Object object = new Object();
        TestSubscriber<Object> sub = new TestSubscriber<>();
        BaseObservable<Object> observable = spy(new BaseObservable<Object>(ctx, new Api[] {}, null) {
            @Override
            protected void onGoogleApiClientReady(GoogleApiClient apiClient, Subscriber<? super Object> subscriber) {
                subscriber.onNext(object);
                subscriber.onCompleted();
            }
        });

        doReturn(false).when(connectionResult).hasResolution();

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                BaseRx.ApiClientConnectionCallbacks callbacks = invocation.getArgumentAt(0, BaseRx.ApiClientConnectionCallbacks.class);
                callbacks.setClient(apiClient);
                callbacks.onConnectionFailed(connectionResult);
                return apiClient;
            }
        }).when(observable).createApiClient(Matchers.any(BaseRx.ApiClientConnectionCallbacks.class));

        Observable.create(observable).subscribe(sub);

        sub.assertNoValues();
        sub.assertError(GoogleAPIConnectionException.class);
    }

    @Test
    public void BaseObservable_ApiClient_ConnectionFailed_Resolution() {
        final Object object = new Object();
        TestSubscriber<Object> sub = new TestSubscriber<>();
        BaseObservable<Object> observable = spy(new BaseObservable<Object>(rxFit, null, null) {
            @Override
            protected void onGoogleApiClientReady(GoogleApiClient apiClient, Subscriber<? super Object> subscriber) {
                subscriber.onNext(object);
                subscriber.onCompleted();
            }
        });

        doReturn(true).when(connectionResult).hasResolution();

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                BaseRx.ApiClientConnectionCallbacks callbacks = invocation.getArgumentAt(0, BaseRx.ApiClientConnectionCallbacks.class);
                callbacks.setClient(apiClient);
                callbacks.onConnectionFailed(connectionResult);
                return apiClient;
            }
        }).when(observable).createApiClient(Matchers.any(BaseRx.ApiClientConnectionCallbacks.class));

        Observable.create(observable).subscribe(sub);

        sub.assertNoValues();
        sub.assertNoTerminalEvent();

        verify(ctx).startActivity(Matchers.any(Intent.class));
    }

}
