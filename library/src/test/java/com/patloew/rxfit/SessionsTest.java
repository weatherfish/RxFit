package com.patloew.rxfit;

import android.app.PendingIntent;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.Session;
import com.google.android.gms.fitness.request.SessionInsertRequest;
import com.google.android.gms.fitness.request.SessionReadRequest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareOnlyThisForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;

import io.reactivex.Observable;
import io.reactivex.Single;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.times;
import static org.powermock.api.mockito.PowerMockito.doReturn;

@RunWith(PowerMockRunner.class)
@SuppressStaticInitializationFor("com.google.android.gms.fitness.Fitness")
@PrepareOnlyThisForTest({ Observable.class, Single.class, ContextCompat.class, Fitness.class, Status.class, ConnectionResult.class })
public class SessionsTest extends BaseTest {

    @Override
    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        PowerMockito.spy(Single.class);
        PowerMockito.mockStatic(Observable.class);
        doReturn(100).when(Observable.class, "bufferSize");
        super.setup();
    }


    // Insert

    @Test
    public void Sessions_Insert() throws Exception {
        ArgumentCaptor<SessionInsertSingle> captor = ArgumentCaptor.forClass(SessionInsertSingle.class);

        final SessionInsertRequest request = Mockito.mock(SessionInsertRequest.class);
        rxFit.sessions().insert(request);
        rxFit.sessions().insert(request, TIMEOUT_TIME, TIMEOUT_TIMEUNIT);

        PowerMockito.verifyStatic(times(2));
        Single.create(captor.capture());

        SessionInsertSingle single = captor.getAllValues().get(0);
        assertEquals(request, single.sessionInsertRequest);
        assertNoTimeoutSet(single);

        single = captor.getAllValues().get(1);
        assertEquals(request, single.sessionInsertRequest);
        assertTimeoutSet(single);
    }

    // Read

    @Test
    public void Sessions_Read() throws Exception {
        ArgumentCaptor<SessionReadSingle> captor = ArgumentCaptor.forClass(SessionReadSingle.class);

        final SessionReadRequest request = Mockito.mock(SessionReadRequest.class);
        rxFit.sessions().read(request);
        rxFit.sessions().read(request, TIMEOUT_TIME, TIMEOUT_TIMEUNIT);


        PowerMockito.verifyStatic(times(2));
        Single.create(captor.capture());

        SessionReadSingle single = captor.getAllValues().get(0);
        assertEquals(request, single.sessionReadRequest);
        assertNoTimeoutSet(single);

        single = captor.getAllValues().get(1);
        assertEquals(request, single.sessionReadRequest);
        assertTimeoutSet(single);
    }

    // Register For Sessions

    @Test
    public void Sessions_RegisterForSessions() throws Exception {
        ArgumentCaptor<SessionRegisterSingle> captor = ArgumentCaptor.forClass(SessionRegisterSingle.class);

        final PendingIntent pendingIntent = Mockito.mock(PendingIntent.class);
        rxFit.sessions().registerForSessions(pendingIntent);
        rxFit.sessions().registerForSessions(pendingIntent, TIMEOUT_TIME, TIMEOUT_TIMEUNIT);

        PowerMockito.verifyStatic(times(2));
        Single.create(captor.capture());

        SessionRegisterSingle single = captor.getAllValues().get(0);
        assertEquals(pendingIntent, single.pendingIntent);
        assertNoTimeoutSet(single);

        single = captor.getAllValues().get(1);
        assertEquals(pendingIntent, single.pendingIntent);
        assertTimeoutSet(single);
    }

    // Unregister For Sessions

    @Test
    public void Sessions_UnregisterForSessions() throws Exception {
        ArgumentCaptor<SessionUnregisterSingle> captor = ArgumentCaptor.forClass(SessionUnregisterSingle.class);

        final PendingIntent pendingIntent = Mockito.mock(PendingIntent.class);
        rxFit.sessions().unregisterForSessions(pendingIntent);
        rxFit.sessions().unregisterForSessions(pendingIntent, TIMEOUT_TIME, TIMEOUT_TIMEUNIT);

        PowerMockito.verifyStatic(times(2));
        Single.create(captor.capture());

        SessionUnregisterSingle single = captor.getAllValues().get(0);
        assertEquals(pendingIntent, single.pendingIntent);
        assertNoTimeoutSet(single);

        single = captor.getAllValues().get(1);
        assertEquals(pendingIntent, single.pendingIntent);
        assertTimeoutSet(single);
    }

    // Start

    @Test
    public void Sessions_Start() throws Exception {
        ArgumentCaptor<SessionStartSingle> captor = ArgumentCaptor.forClass(SessionStartSingle.class);

        final Session session = Mockito.mock(Session.class);
        rxFit.sessions().start(session);
        rxFit.sessions().start(session, TIMEOUT_TIME, TIMEOUT_TIMEUNIT);

        PowerMockito.verifyStatic(times(2));
        Single.create(captor.capture());

        SessionStartSingle single = captor.getAllValues().get(0);
        assertEquals(session, single.session);
        assertNoTimeoutSet(single);

        single = captor.getAllValues().get(1);
        assertEquals(session, single.session);
        assertTimeoutSet(single);
    }


    // Stop

    @Test
    public void Sessions_Stop() throws Exception {
        ArgumentCaptor<SessionStopSingle> captor = ArgumentCaptor.forClass(SessionStopSingle.class);

        final String identifier = "identifier";
        rxFit.sessions().stop(identifier);
        rxFit.sessions().stop(identifier, TIMEOUT_TIME, TIMEOUT_TIMEUNIT);


        PowerMockito.verifyStatic(atLeast(2));
        Single.create(captor.capture());

        SessionStopSingle single = captor.getAllValues().get(0);
        assertEquals(identifier, single.identifier);
        assertNoTimeoutSet(single);

        single = captor.getAllValues().get(1);
        assertEquals(identifier, single.identifier);
        assertTimeoutSet(single);
    }

}
