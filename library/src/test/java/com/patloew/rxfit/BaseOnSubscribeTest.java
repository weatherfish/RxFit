package com.patloew.rxfit;

import android.app.PendingIntent;
import android.support.annotation.CallSuper;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.fitness.BleApi;
import com.google.android.gms.fitness.ConfigApi;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.GoalsApi;
import com.google.android.gms.fitness.HistoryApi;
import com.google.android.gms.fitness.RecordingApi;
import com.google.android.gms.fitness.SensorsApi;
import com.google.android.gms.fitness.SessionsApi;

import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.reflect.Whitebox;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;

import rx.SingleSubscriber;
import rx.Subscriber;
import rx.observers.TestSubscriber;

import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;

public abstract class BaseOnSubscribeTest extends BaseTest {

    @Mock GoogleApiClient apiClient;
    @Mock Status status;
    @Mock ConnectionResult connectionResult;
    @Mock PendingResult pendingResult;
    @Mock PendingIntent pendingIntent;

    @Mock BleApi bleApi;
    @Mock ConfigApi configApi;
    @Mock GoalsApi goalsApi;
    @Mock HistoryApi historyApi;
    @Mock RecordingApi recordingApi;
    @Mock SensorsApi sensorsApi;
    @Mock SessionsApi sessionsApi;

    @CallSuper
    public void setup() throws Exception {
        PowerMockito.mockStatic(Fitness.class);
        Whitebox.setInternalState(Fitness.class, bleApi);
        Whitebox.setInternalState(Fitness.class, configApi);
        Whitebox.setInternalState(Fitness.class, goalsApi);
        Whitebox.setInternalState(Fitness.class, historyApi);
        Whitebox.setInternalState(Fitness.class, recordingApi);
        Whitebox.setInternalState(Fitness.class, sensorsApi);
        Whitebox.setInternalState(Fitness.class, sessionsApi);

        doReturn(status).when(status).getStatus();

        super.setup();
    }

    @SuppressWarnings("unchecked")
    protected static <T> Subscriber<? super T> getSubscriber(BaseObservable<T> baseObservable, GoogleApiClient apiClient) {
        try {
            final Field subscriberField = BaseObservable.class.getDeclaredField("subscriptionInfoMap");
            subscriberField.setAccessible(true);
            return ((Map<GoogleApiClient, Subscriber<? super T>>) subscriberField.get(baseObservable)).get(apiClient);
        } catch(Exception e) {
            return null;
        }
    }

    // Mock GoogleApiClient connection success behaviour
    protected <T> void setupBaseObservableSuccess(final BaseObservable<T> baseObservable) {
        setupBaseObservableSuccess(baseObservable, apiClient);
    }

    // Mock GoogleApiClient connection success behaviour
    protected <T> void setupBaseObservableSuccess(final BaseObservable<T> baseObservable, final GoogleApiClient apiClient) {
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                final Subscriber<? super T> subscriber = ((BaseObservable.ApiClientConnectionCallbacks)invocation.getArguments()[0]).subscriber;

                doAnswer(new Answer<Object>() {
                    @Override
                    public Object answer(InvocationOnMock invocation) throws Throwable {
                        baseObservable.onGoogleApiClientReady(apiClient, subscriber);
                        return null;
                    }
                }).when(apiClient).connect();

                return apiClient;
            }
        }).when(baseObservable).createApiClient(Matchers.any(BaseRx.ApiClientConnectionCallbacks.class));
    }

    // Mock GoogleApiClient resolution behaviour
    protected <T> void setupBaseObservableResolution(final BaseObservable<T> baseObservable, final GoogleApiClient apiClient) {
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                doAnswer(new Answer<Object>() {
                    @Override
                    public Object answer(InvocationOnMock invocation) throws Throwable {
                        try {
                            final Field observableSetField = BaseRx.class.getDeclaredField("observableSet");
                            observableSetField.setAccessible(true);
                            ((Set<BaseRx>)observableSetField.get(baseObservable)).add(baseObservable);
                        } catch(Exception e) { }
                        return null;
                    }
                }).when(apiClient).connect();

                return apiClient;
            }
        }).when(baseObservable).createApiClient(Matchers.any(BaseRx.ApiClientConnectionCallbacks.class));
    }

    // Mock GoogleApiClient connection success behaviour
    protected <T> void setupBaseSingleSuccess(final BaseSingle<T> baseSingle) {
        setupBaseSingleSuccess(baseSingle, apiClient);
    }

    // Mock GoogleApiClient connection success behaviour
    protected <T> void setupBaseSingleSuccess(final BaseSingle<T> baseSingle, final GoogleApiClient apiClient) {
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                final SingleSubscriber<? super T> subscriber = ((BaseSingle.ApiClientConnectionCallbacks)invocation.getArguments()[0]).subscriber;

                doAnswer(new Answer<Object>() {
                    @Override
                    public Object answer(InvocationOnMock invocation) throws Throwable {
                        baseSingle.onGoogleApiClientReady(apiClient, subscriber);
                        return null;
                    }
                }).when(apiClient).connect();

                return apiClient;
            }
        }).when(baseSingle).createApiClient(Matchers.any(BaseRx.ApiClientConnectionCallbacks.class));
    }

    // Mock GoogleApiClient connection error behaviour
    protected <T> void setupBaseObservableError(final BaseObservable<T> baseObservable) {
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                final Subscriber<? super T> subscriber = ((BaseObservable.ApiClientConnectionCallbacks)invocation.getArguments()[0]).subscriber;

                doAnswer(new Answer<Object>() {
                    @Override
                    public Object answer(InvocationOnMock invocation) throws Throwable {
                        subscriber.onError(new GoogleAPIConnectionException("Error connecting to GoogleApiClient.", connectionResult));
                        return null;
                    }
                }).when(apiClient).connect();

                return apiClient;
            }
        }).when(baseObservable).createApiClient(Matchers.any(BaseRx.ApiClientConnectionCallbacks.class));
    }

    // Mock GoogleApiClient connection error behaviour
    protected <T> void setupBaseSingleError(final BaseSingle<T> baseSingle) {
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                final SingleSubscriber<? super T> subscriber = ((BaseSingle.ApiClientConnectionCallbacks)invocation.getArguments()[0]).subscriber;

                doAnswer(new Answer<Object>() {
                    @Override
                    public Object answer(InvocationOnMock invocation) throws Throwable {
                        subscriber.onError(new GoogleAPIConnectionException("Error connecting to GoogleApiClient.", connectionResult));
                        return null;
                    }
                }).when(apiClient).connect();

                return apiClient;
            }
        }).when(baseSingle).createApiClient(Matchers.any(BaseRx.ApiClientConnectionCallbacks.class));
    }

    @SuppressWarnings("unchecked")
    protected void setPendingResultValue(final Result result) {
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ((ResultCallback)invocation.getArguments()[0]).onResult(result);
                return null;
            }
        }).when(pendingResult).setResultCallback(Matchers.<ResultCallback>any());
    }

    protected static void assertError(TestSubscriber sub, Class<? extends Throwable> errorClass) {
        sub.assertError(errorClass);
        sub.assertNoValues();
        sub.assertUnsubscribed();
    }

    @SuppressWarnings("unchecked")
    protected static void assertSingleValue(TestSubscriber sub, Object value) {
        sub.assertCompleted();
        sub.assertUnsubscribed();
        sub.assertValue(value);
    }
}
