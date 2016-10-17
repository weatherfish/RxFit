package com.patloew.rxfit;

import android.app.PendingIntent;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.Session;
import com.google.android.gms.fitness.request.SessionInsertRequest;
import com.google.android.gms.fitness.request.SessionReadRequest;
import com.google.android.gms.fitness.result.SessionReadResult;
import com.google.android.gms.fitness.result.SessionStopResult;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareOnlyThisForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.List;

import rx.Single;
import rx.observers.TestSubscriber;

import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@SuppressStaticInitializationFor("com.google.android.gms.fitness.Fitness")
@PrepareOnlyThisForTest({ ContextCompat.class, Fitness.class, Status.class, ConnectionResult.class, BaseRx.class })
public class SessionOnSubScribeTest extends BaseOnSubscribeTest {

    @Mock Session session;

    @Override
    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        super.setup();
    }


    // SessionInsertObservable

    @Test
    public void SessionInsertObservable_Success() {
        TestSubscriber<Status> sub = new TestSubscriber<>();
        SessionInsertRequest sessionInsertRequest = Mockito.mock(SessionInsertRequest.class);
        SessionInsertSingle single = PowerMockito.spy(new SessionInsertSingle(rxFit, sessionInsertRequest, null, null));

        setPendingResultValue(status);
        when(status.isSuccess()).thenReturn(true);
        when(sessionsApi.insertSession(apiClient, sessionInsertRequest)).thenReturn(pendingResult);

        setupBaseSingleSuccess(single);
        Single.create(single).subscribe(sub);

        assertSingleValue(sub, status);
    }

    @Test
    public void SessionInsertObservable_StatusException() {
        TestSubscriber<Status> sub = new TestSubscriber<>();
        SessionInsertRequest sessionInsertRequest = Mockito.mock(SessionInsertRequest.class);
        SessionInsertSingle single = PowerMockito.spy(new SessionInsertSingle(rxFit, sessionInsertRequest, null, null));

        setPendingResultValue(status);
        when(status.isSuccess()).thenReturn(false);
        when(sessionsApi.insertSession(apiClient, sessionInsertRequest)).thenReturn(pendingResult);

        setupBaseSingleSuccess(single);
        Single.create(single).subscribe(sub);

        assertError(sub, StatusException.class);
    }

    // SessionRegisterObservable

    @Test
    public void SessionRegisterObservable_Success() {
        TestSubscriber<Status> sub = new TestSubscriber<>();
        PendingIntent pendingIntent = Mockito.mock(PendingIntent.class);
        SessionRegisterSingle single = PowerMockito.spy(new SessionRegisterSingle(rxFit, pendingIntent, null, null));

        setPendingResultValue(status);
        when(status.isSuccess()).thenReturn(true);
        when(sessionsApi.registerForSessions(apiClient, pendingIntent)).thenReturn(pendingResult);

        setupBaseSingleSuccess(single);
        Single.create(single).subscribe(sub);

        assertSingleValue(sub, status);
    }

    @Test
    public void SessionRegisterObservable_StatusException() {
        TestSubscriber<Status> sub = new TestSubscriber<>();
        PendingIntent pendingIntent = Mockito.mock(PendingIntent.class);
        SessionRegisterSingle single = PowerMockito.spy(new SessionRegisterSingle(rxFit, pendingIntent, null, null));

        setPendingResultValue(status);
        when(status.isSuccess()).thenReturn(false);
        when(sessionsApi.registerForSessions(apiClient, pendingIntent)).thenReturn(pendingResult);

        setupBaseSingleSuccess(single);
        Single.create(single).subscribe(sub);

        assertError(sub, StatusException.class);
    }

    // SessionUnregisterObservable

    @Test
    public void SessionUnregisterObservable_Success() {
        TestSubscriber<Status> sub = new TestSubscriber<>();
        PendingIntent pendingIntent = Mockito.mock(PendingIntent.class);
        SessionUnregisterSingle single = PowerMockito.spy(new SessionUnregisterSingle(rxFit, pendingIntent, null, null));

        setPendingResultValue(status);
        when(status.isSuccess()).thenReturn(true);
        when(sessionsApi.unregisterForSessions(apiClient, pendingIntent)).thenReturn(pendingResult);

        setupBaseSingleSuccess(single);
        Single.create(single).subscribe(sub);

        assertSingleValue(sub, status);
    }

    @Test
    public void SessionUnregisterObservable_StatusException() {
        TestSubscriber<Status> sub = new TestSubscriber<>();
        PendingIntent pendingIntent = Mockito.mock(PendingIntent.class);
        SessionUnregisterSingle single = PowerMockito.spy(new SessionUnregisterSingle(rxFit, pendingIntent, null, null));

        setPendingResultValue(status);
        when(status.isSuccess()).thenReturn(false);
        when(sessionsApi.unregisterForSessions(apiClient, pendingIntent)).thenReturn(pendingResult);

        setupBaseSingleSuccess(single);
        Single.create(single).subscribe(sub);

        assertError(sub, StatusException.class);
    }

    // SessionStartObservable

    @Test
    public void SessionStartObservable_Success() {
        TestSubscriber<Status> sub = new TestSubscriber<>();
        Session session = Mockito.mock(Session.class);
        SessionStartSingle single = PowerMockito.spy(new SessionStartSingle(rxFit, session, null, null));

        setPendingResultValue(status);
        when(status.isSuccess()).thenReturn(true);
        when(sessionsApi.startSession(apiClient, session)).thenReturn(pendingResult);

        setupBaseSingleSuccess(single);
        Single.create(single).subscribe(sub);

        assertSingleValue(sub, status);
    }

    @Test
    public void SessionStartObservable_StatusException() {
        TestSubscriber<Status> sub = new TestSubscriber<>();
        SessionStartSingle single = PowerMockito.spy(new SessionStartSingle(rxFit, session, null, null));

        setPendingResultValue(status);
        when(status.isSuccess()).thenReturn(false);
        when(sessionsApi.startSession(apiClient, session)).thenReturn(pendingResult);

        setupBaseSingleSuccess(single);
        Single.create(single).subscribe(sub);

        assertError(sub, StatusException.class);
    }

    // SessionStopObservable

    @Test
    public void SessionStopObservable_Success() {
        TestSubscriber<List<Session>> sub = new TestSubscriber<>();
        String identifier = "identifier";
        SessionStopResult sessionStopResult = Mockito.mock(SessionStopResult.class);
        SessionStopSingle single = PowerMockito.spy(new SessionStopSingle(rxFit, identifier, null, null));

        List<Session> sessionList = new ArrayList<>();
        sessionList.add(session);

        when(sessionStopResult.getSessions()).thenReturn(sessionList);

        setPendingResultValue(sessionStopResult);
        when(sessionStopResult.getStatus()).thenReturn(status);
        when(status.isSuccess()).thenReturn(true);
        when(sessionsApi.stopSession(apiClient, identifier)).thenReturn(pendingResult);

        setupBaseSingleSuccess(single);
        Single.create(single).subscribe(sub);

        assertSingleValue(sub, sessionList);
    }

    @Test
    public void SessionStopObservable_StatusException() {
        TestSubscriber<List<Session>> sub = new TestSubscriber<>();
        String identifier = "identifier";
        SessionStopResult sessionStopResult = Mockito.mock(SessionStopResult.class);
        SessionStopSingle single = PowerMockito.spy(new SessionStopSingle(rxFit, identifier, null, null));

        List<Session> sessionList = new ArrayList<>();
        sessionList.add(session);

        when(sessionStopResult.getSessions()).thenReturn(sessionList);

        setPendingResultValue(sessionStopResult);
        when(sessionStopResult.getStatus()).thenReturn(status);
        when(status.isSuccess()).thenReturn(false);
        when(sessionsApi.stopSession(apiClient, identifier)).thenReturn(pendingResult);

        setupBaseSingleSuccess(single);
        Single.create(single).subscribe(sub);

        assertError(sub, StatusException.class);
    }

    // SessionReadObservable

    @Test
    public void SessionReadObservable_Success() {
        TestSubscriber<SessionReadResult> sub = new TestSubscriber<>();
        SessionReadRequest sessionReadRequest = Mockito.mock(SessionReadRequest.class);
        SessionReadResult sessionReadResult = Mockito.mock(SessionReadResult.class);
        SessionReadSingle single = PowerMockito.spy(new SessionReadSingle(rxFit, sessionReadRequest, null, null));

        setPendingResultValue(sessionReadResult);
        when(sessionReadResult.getStatus()).thenReturn(status);
        when(status.isSuccess()).thenReturn(true);
        when(sessionsApi.readSession(apiClient, sessionReadRequest)).thenReturn(pendingResult);

        setupBaseSingleSuccess(single);
        Single.create(single).subscribe(sub);

        assertSingleValue(sub, sessionReadResult);
    }

    @Test
    public void SessionReadObservable_StatusException() {
        TestSubscriber<SessionReadResult> sub = new TestSubscriber<>();
        SessionReadRequest sessionReadRequest = Mockito.mock(SessionReadRequest.class);
        SessionReadResult sessionReadResult = Mockito.mock(SessionReadResult.class);
        SessionReadSingle single = PowerMockito.spy(new SessionReadSingle(rxFit, sessionReadRequest, null, null));

        setPendingResultValue(sessionReadResult);
        when(sessionReadResult.getStatus()).thenReturn(status);
        when(status.isSuccess()).thenReturn(false);
        when(sessionsApi.readSession(apiClient, sessionReadRequest)).thenReturn(pendingResult);

        setupBaseSingleSuccess(single);
        Single.create(single).subscribe(sub);

        assertError(sub, StatusException.class);
    }


}
