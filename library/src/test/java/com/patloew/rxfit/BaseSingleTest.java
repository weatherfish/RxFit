package com.patloew.rxfit;

import android.app.Activity;
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
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareOnlyThisForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;

import rx.Single;
import rx.SingleSubscriber;
import rx.observers.TestSubscriber;

import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

@RunWith(PowerMockRunner.class)
@PrepareOnlyThisForTest({ ContextCompat.class, Fitness.class, Status.class, ConnectionResult.class, SingleSubscriber.class })
@SuppressStaticInitializationFor("com.google.android.gms.fitness.Fitness")
public class BaseSingleTest extends BaseOnSubscribeTest {

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        super.setup();
    }

    @Test
    public void BaseObservable_ApiClient_Connected() {
        final Object object = new Object();
        TestSubscriber<Object> sub = new TestSubscriber<>();
        BaseSingle<Object> single = spy(new BaseSingle<Object>(ctx, new Api[] {}, null) {
            @Override
            protected void onGoogleApiClientReady(GoogleApiClient apiClient, SingleSubscriber<? super Object> subscriber) {
                subscriber.onSuccess(object);
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
        }).when(single).createApiClient(Matchers.any(BaseRx.ApiClientConnectionCallbacks.class));

        Single.create(single).subscribe(sub);

        sub.assertValue(object);
        sub.assertCompleted();
    }

    @Test
    public void BaseObservable_ApiClient_ConnectionSuspended() {
        final Object object = new Object();
        TestSubscriber<Object> sub = new TestSubscriber<>();
        BaseSingle<Object> single = spy(new BaseSingle<Object>(ctx, new Api[] {}, null) {
            @Override
            protected void onGoogleApiClientReady(GoogleApiClient apiClient, SingleSubscriber<? super Object> subscriber) {
                subscriber.onSuccess(object);
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
        }).when(single).createApiClient(Matchers.any(BaseRx.ApiClientConnectionCallbacks.class));

        Single.create(single).subscribe(sub);

        sub.assertNoValues();
        sub.assertError(GoogleAPIConnectionSuspendedException.class);
    }

    @Test
    public void BaseObservable_ApiClient_ConnectionFailed_NoResulution() {
        final Object object = new Object();
        TestSubscriber<Object> sub = new TestSubscriber<>();
        BaseSingle<Object> single = spy(new BaseSingle<Object>(ctx, new Api[] {}, null) {
            @Override
            protected void onGoogleApiClientReady(GoogleApiClient apiClient, SingleSubscriber<? super Object> subscriber) {
                subscriber.onSuccess(object);
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
        }).when(single).createApiClient(Matchers.any(BaseRx.ApiClientConnectionCallbacks.class));

        Single.create(single).subscribe(sub);

        sub.assertNoValues();
        sub.assertError(GoogleAPIConnectionException.class);
    }

    @Test
    public void BaseObservable_ApiClient_ConnectionFailed_Resolution() {
        final Object object = new Object();
        TestSubscriber<Object> sub = new TestSubscriber<>();
        BaseSingle<Object> single = spy(new BaseSingle<Object>(rxFit, null, null) {
            @Override
            protected void onGoogleApiClientReady(GoogleApiClient apiClient, SingleSubscriber<? super Object> subscriber) {
                subscriber.onSuccess(object);
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
        }).when(single).createApiClient(Matchers.any(BaseRx.ApiClientConnectionCallbacks.class));

        Single.create(single).subscribe(sub);

        sub.assertNoValues();
        sub.assertNoTerminalEvent();

        verify(ctx).startActivity(Matchers.any(Intent.class));
    }

    @Test
    public void handleResolutionResult_ResultOK() {
        final Object object = new Object();
        BaseSingle<Object> single = spy(new BaseSingle<Object>(rxFit, null, null) {
            @Override
            protected void onGoogleApiClientReady(GoogleApiClient apiClient, SingleSubscriber<? super Object> subscriber) {
                subscriber.onSuccess(object);
            }
        });

        SingleSubscriber sub = PowerMockito.spy(new SingleSubscriber() {
            @Override public void onSuccess(Object value) { }
            @Override public void onError(Throwable error) { }
        });

        PowerMockito.doReturn(false).when(sub).isUnsubscribed();
        single.subscriptionInfoMap.put(apiClient, sub);

        single.handleResolutionResult(Activity.RESULT_OK, connectionResult);

        verify(apiClient).connect();
        verifyNoMoreInteractions(apiClient);
        verify(sub, never()).onError(Matchers.any(Throwable.class));
    }

    @Test
    public void handleResolutionResult_ResultOK_ConnectionException() {
        final Object object = new Object();
        BaseSingle<Object> single = spy(new BaseSingle<Object>(rxFit, null, null) {
            @Override
            protected void onGoogleApiClientReady(GoogleApiClient apiClient, SingleSubscriber<? super Object> subscriber) {
                subscriber.onSuccess(object);
            }
        });

        SingleSubscriber sub = PowerMockito.spy(new SingleSubscriber() {
            @Override public void onSuccess(Object value) { }
            @Override public void onError(Throwable error) { }
        });

        RuntimeException exception = new RuntimeException();
        doThrow(exception).when(apiClient).connect();
        PowerMockito.doReturn(false).when(sub).isUnsubscribed();
        single.subscriptionInfoMap.put(apiClient, sub);

        single.handleResolutionResult(Activity.RESULT_OK, connectionResult);

        verify(apiClient).connect();
        verifyNoMoreInteractions(apiClient);
        verify(sub).onError(exception);
    }

    @Test
    public void handleResolutionResult_ResultCanceled() {
        final Object object = new Object();
        BaseSingle<Object> single = spy(new BaseSingle<Object>(rxFit, null, null) {
            @Override
            protected void onGoogleApiClientReady(GoogleApiClient apiClient, SingleSubscriber<? super Object> subscriber) {
                subscriber.onSuccess(object);
            }
        });

        SingleSubscriber sub = PowerMockito.spy(new SingleSubscriber() {
            @Override public void onSuccess(Object value) { }
            @Override public void onError(Throwable error) { }
        });

        PowerMockito.doReturn(false).when(sub).isUnsubscribed();
        single.subscriptionInfoMap.put(apiClient, sub);

        single.handleResolutionResult(Activity.RESULT_CANCELED, connectionResult);

        verifyZeroInteractions(apiClient);
        verify(sub).onError(Matchers.any(GoogleAPIConnectionException.class));
    }

}
