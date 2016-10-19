package com.patloew.rxfit;

import android.support.v4.content.ContextCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.fitness.Fitness;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareOnlyThisForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

@RunWith(PowerMockRunner.class)
@PrepareOnlyThisForTest({ ContextCompat.class, Fitness.class, Status.class, ConnectionResult.class, GoogleApiClient.Builder.class })
@SuppressStaticInitializationFor("com.google.android.gms.fitness.Fitness")
public class BaseRxTest extends BaseOnSubscribeTest {

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        super.setup();
    }

    @Test
    public void setupFitnessPendingResult_NoTimeout() {
        BaseRx<Object> baseRx = spy(new BaseRx<Object>(rxFit, null, null) {
            @Override
            protected void handleResolutionResult(int resultCode, ConnectionResult connectionResult) {}
        });

        ResultCallback resultCallback = Mockito.mock(ResultCallback.class);

        baseRx.setupFitnessPendingResult(pendingResult, resultCallback);

        verify(pendingResult).setResultCallback(resultCallback);
    }

    @Test
    public void setupFitnessPendingResult_Timeout() {
        BaseRx<Object> baseRx = spy(new BaseRx<Object>(rxFit, TIMEOUT_TIME, TIMEOUT_TIMEUNIT) {
            @Override
            protected void handleResolutionResult(int resultCode, ConnectionResult connectionResult) {}
        });

        ResultCallback resultCallback = Mockito.mock(ResultCallback.class);

        baseRx.setupFitnessPendingResult(pendingResult, resultCallback);

        verify(pendingResult).setResultCallback(resultCallback, TIMEOUT_TIME, TIMEOUT_TIMEUNIT);
    }

    @Test
    public void createApiClient_NoScopes() {
        GoogleApiClient.Builder builder = Mockito.mock(GoogleApiClient.Builder.class);

        BaseRx<Object> baseRx = spy(new BaseRx<Object>(ctx, new Api[]{ Fitness.HISTORY_API}, null) {
            @Override
            protected void handleResolutionResult(int resultCode, ConnectionResult connectionResult) {}
        });

        doReturn(builder).when(baseRx).getApiClientBuilder();
        doReturn(apiClient).when(builder).build();

        BaseRx.ApiClientConnectionCallbacks callbacks = Mockito.mock(BaseRx.ApiClientConnectionCallbacks.class);

        assertEquals(apiClient, baseRx.createApiClient(callbacks));
        verify(builder).addApi(Fitness.HISTORY_API);
        verify(builder).addConnectionCallbacks(callbacks);
        verify(builder).addOnConnectionFailedListener(callbacks);
        verify(builder, never()).addScope(Matchers.any(Scope.class));
        verify(callbacks).setClient(Matchers.any(GoogleApiClient.class));
    }

    @Test
    public void createApiClient_Scopes() {
        GoogleApiClient.Builder builder = Mockito.mock(GoogleApiClient.Builder.class);

        BaseRx<Object> baseRx = spy(new BaseRx<Object>(ctx, new Api[]{ Fitness.HISTORY_API}, new Scope[]{ Fitness.SCOPE_ACTIVITY_READ }) {
            @Override
            protected void handleResolutionResult(int resultCode, ConnectionResult connectionResult) {}
        });

        doReturn(builder).when(baseRx).getApiClientBuilder();
        doReturn(apiClient).when(builder).build();

        BaseRx.ApiClientConnectionCallbacks callbacks = Mockito.mock(BaseRx.ApiClientConnectionCallbacks.class);

        assertEquals(apiClient, baseRx.createApiClient(callbacks));
        verify(builder).addApi(Fitness.HISTORY_API);
        verify(builder).addScope(Fitness.SCOPE_ACTIVITY_READ);
        verify(builder).addConnectionCallbacks(callbacks);
        verify(builder).addOnConnectionFailedListener(callbacks);
        verify(callbacks).setClient(Matchers.any(GoogleApiClient.class));
    }

    @Test
    public void onResolutionResult() {
        BaseRx<Object> baseRx = spy(new BaseRx<Object>(ctx, null, null) {
            @Override
            protected void handleResolutionResult(int resultCode, ConnectionResult connectionResult) {}
        });

        BaseRx.observableSet.add(baseRx);

        BaseRx.onResolutionResult(0, connectionResult);
        verify(baseRx).handleResolutionResult(0, connectionResult);
        assertEquals(0, BaseRx.observableSet.size());
    }

}
