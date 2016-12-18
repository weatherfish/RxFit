package com.patloew.rxfit;

import android.support.v4.content.ContextCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.Goal;
import com.google.android.gms.fitness.request.GoalsReadRequest;
import com.google.android.gms.fitness.result.GoalsResult;

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

import io.reactivex.Single;

import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@SuppressStaticInitializationFor("com.google.android.gms.fitness.Fitness")
@PrepareOnlyThisForTest({ ContextCompat.class, Fitness.class, Status.class, ConnectionResult.class, BaseRx.class })
public class GoalsOnSubscribeTest extends BaseOnSubscribeTest {

    @Mock GoalsReadRequest goalsReadRequest;
    @Mock Goal goal;

    @Override
    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        super.setup();
    }

    // GoalsReadCurrentSingle

    @Test
    public void GoalsReadCurrentSingle_Success() {
        GoalsResult goalsResult = Mockito.mock(GoalsResult.class);

        GoalsReadCurrentSingle single = PowerMockito.spy(new GoalsReadCurrentSingle(rxFit, goalsReadRequest, null, null));

        List<Goal> goalList = new ArrayList<>();
        goalList.add(goal);
        when(goalsResult.getGoals()).thenReturn(goalList);

        setPendingResultValue(goalsResult);
        when(goalsResult.getStatus()).thenReturn(status);
        when(status.isSuccess()).thenReturn(true);
        when(goalsApi.readCurrentGoals(apiClient, goalsReadRequest)).thenReturn(pendingResult);

        setupBaseSingleSuccess(single);

        assertSingleValue(Single.create(single).test(), goalList);
    }

    @Test
    public void GoalsReadCurrentSingle_StatusException() {
        GoalsResult goalsResult = Mockito.mock(GoalsResult.class);

        GoalsReadCurrentSingle single = PowerMockito.spy(new GoalsReadCurrentSingle(rxFit, goalsReadRequest, null, null));

        List<Goal> goalList = new ArrayList<>();
        goalList.add(goal);
        when(goalsResult.getGoals()).thenReturn(goalList);

        setPendingResultValue(goalsResult);
        when(goalsResult.getStatus()).thenReturn(status);
        when(status.isSuccess()).thenReturn(false);
        when(goalsApi.readCurrentGoals(apiClient, goalsReadRequest)).thenReturn(pendingResult);

        setupBaseSingleSuccess(single);

        assertError(Single.create(single).test(), StatusException.class);
    }
}
