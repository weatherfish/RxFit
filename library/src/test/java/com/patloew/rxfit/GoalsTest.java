package com.patloew.rxfit;

import android.support.v4.content.ContextCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.request.GoalsReadRequest;

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

import rx.Observable;
import rx.Single;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.atLeast;

@RunWith(PowerMockRunner.class)
@SuppressStaticInitializationFor("com.google.android.gms.fitness.Fitness")
@PrepareOnlyThisForTest({ Observable.class, Single.class, ContextCompat.class, Fitness.class, Status.class, ConnectionResult.class })
public class GoalsTest extends BaseTest {

    @Override
    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        PowerMockito.spy(Single.class);
        PowerMockito.mockStatic(Observable.class);
        super.setup();
    }

    // Read Current

    @Test
    public void Goals_ReadCurrent() throws Exception {
        ArgumentCaptor<GoalsReadCurrentSingle> captor = ArgumentCaptor.forClass(GoalsReadCurrentSingle.class);

        final GoalsReadRequest request = Mockito.mock(GoalsReadRequest.class);
        rxFit.goals().readCurrent(request);
        rxFit.goals().readCurrent(request, TIMEOUT_TIME, TIMEOUT_TIMEUNIT);
        
        PowerMockito.verifyStatic(atLeast(2));
        Single.create(captor.capture());

        GoalsReadCurrentSingle single = captor.getAllValues().get(0);
        assertEquals(request, single.goalsReadRequest);
        assertNoTimeoutSet(single);

        single = captor.getAllValues().get(2);
        assertEquals(request, single.goalsReadRequest);
        assertTimeoutSet(single);
    }

}
