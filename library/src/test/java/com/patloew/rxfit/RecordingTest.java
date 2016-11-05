package com.patloew.rxfit;

import android.support.v4.content.ContextCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.DataSource;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Subscription;

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

import rx.Single;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.times;

@RunWith(PowerMockRunner.class)
@SuppressStaticInitializationFor("com.google.android.gms.fitness.Fitness")
@PrepareOnlyThisForTest({ Single.class, ContextCompat.class, Fitness.class, Status.class, ConnectionResult.class, DataType.class })
public class RecordingTest extends BaseTest {

    @Override
    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        PowerMockito.spy(Single.class);
        super.setup();
    }


    // List Subscriptions

    @Test
    public void Recording_ListSubscriptions() throws Exception {
        ArgumentCaptor<RecordingListSubscriptionsSingle> captor = ArgumentCaptor.forClass(RecordingListSubscriptionsSingle.class);

        rxFit.recording().listSubscriptions();
        rxFit.recording().listSubscriptions(TIMEOUT_TIME, TIMEOUT_TIMEUNIT);

        PowerMockito.verifyStatic(atLeast(2));
        Single.create(captor.capture());

        RecordingListSubscriptionsSingle single = captor.getAllValues().get(0);
        assertNull(single.dataType);
        assertNoTimeoutSet(single);

        single = captor.getAllValues().get(2);
        assertNull(single.dataType);
        assertTimeoutSet(single);
    }


    @Test
    public void Recording_ListSubscriptions_DataType() throws Exception {
        ArgumentCaptor<RecordingListSubscriptionsSingle> captor = ArgumentCaptor.forClass(RecordingListSubscriptionsSingle.class);

        final DataType dataType = Mockito.mock(DataType.class);
        rxFit.recording().listSubscriptions(dataType);
        rxFit.recording().listSubscriptions(dataType, TIMEOUT_TIME, TIMEOUT_TIMEUNIT);

        PowerMockito.verifyStatic(atLeast(2));
        Single.create(captor.capture());

        RecordingListSubscriptionsSingle single = captor.getAllValues().get(0);
        assertEquals(dataType, single.dataType);
        assertNoTimeoutSet(single);

        single = captor.getAllValues().get(2);
        assertEquals(dataType, single.dataType);
        assertTimeoutSet(single);
    }

    // Unsubscribe

    @Test
    public void Recording_Unsubscribe_DataSource() throws Exception {
        ArgumentCaptor<RecordingUnsubscribeSingle> captor = ArgumentCaptor.forClass(RecordingUnsubscribeSingle.class);

        final DataSource dataSource = Mockito.mock(DataSource.class);
        rxFit.recording().unsubscribe(dataSource);
        rxFit.recording().unsubscribe(dataSource, TIMEOUT_TIME, TIMEOUT_TIMEUNIT);

        PowerMockito.verifyStatic(times(2));
        Single.create(captor.capture());

        RecordingUnsubscribeSingle single = captor.getAllValues().get(0);
        assertNull(single.dataType);
        assertEquals(dataSource, single.dataSource);
        assertNull(single.subscription);
        assertNoTimeoutSet(single);

        single = captor.getAllValues().get(1);
        assertNull(single.dataType);
        assertEquals(dataSource, single.dataSource);
        assertNull(single.subscription);
        assertTimeoutSet(single);
    }

    @Test
    public void Recording_Unsubscribe_DataType() throws Exception {
        ArgumentCaptor<RecordingUnsubscribeSingle> captor = ArgumentCaptor.forClass(RecordingUnsubscribeSingle.class);

        final DataType dataType = Mockito.mock(DataType.class);
        rxFit.recording().unsubscribe(dataType);
        rxFit.recording().unsubscribe(dataType, TIMEOUT_TIME, TIMEOUT_TIMEUNIT);

        PowerMockito.verifyStatic(times(2));
        Single.create(captor.capture());

        RecordingUnsubscribeSingle single = captor.getAllValues().get(0);
        assertEquals(dataType, single.dataType);
        assertNull(single.dataSource);
        assertNoTimeoutSet(single);

        single = captor.getAllValues().get(1);
        assertEquals(dataType, single.dataType);
        assertNull(single.dataSource);
        assertNull(single.subscription);
        assertTimeoutSet(single);
    }

    @Test
    public void Recording_Unsubscribe_Subscription() throws Exception {
        ArgumentCaptor<RecordingUnsubscribeSingle> captor = ArgumentCaptor.forClass(RecordingUnsubscribeSingle.class);

        final Subscription subscription = Mockito.mock(Subscription.class);
        rxFit.recording().unsubscribe(subscription);
        rxFit.recording().unsubscribe(subscription, TIMEOUT_TIME, TIMEOUT_TIMEUNIT);

        PowerMockito.verifyStatic(times(2));
        Single.create(captor.capture());

        RecordingUnsubscribeSingle single = captor.getAllValues().get(0);
        assertNull(single.dataType);
        assertNull(single.dataSource);
        assertEquals(subscription, single.subscription);
        assertNoTimeoutSet(single);

        single = captor.getAllValues().get(1);
        assertNull(single.dataType);
        assertNull(single.dataSource);
        assertEquals(subscription, single.subscription);
        assertTimeoutSet(single);
    }


    // Subscribe

    @Test
    public void Recording_Subscribe_DataSource() throws Exception {
        ArgumentCaptor<RecordingSubscribeSingle> captor = ArgumentCaptor.forClass(RecordingSubscribeSingle.class);

        final DataSource dataSource = Mockito.mock(DataSource.class);
        rxFit.recording().subscribe(dataSource);
        rxFit.recording().subscribe(dataSource, TIMEOUT_TIME, TIMEOUT_TIMEUNIT);

        PowerMockito.verifyStatic(times(2));
        Single.create(captor.capture());

        RecordingSubscribeSingle single = captor.getAllValues().get(0);
        assertNull(single.dataType);
        assertEquals(dataSource, single.dataSource);
        assertNoTimeoutSet(single);

        single = captor.getAllValues().get(1);
        assertNull(single.dataType);
        assertEquals(dataSource, single.dataSource);
        assertTimeoutSet(single);
    }

    @Test
    public void Recording_Subscribe_DataType() throws Exception {
        ArgumentCaptor<RecordingSubscribeSingle> captor = ArgumentCaptor.forClass(RecordingSubscribeSingle.class);

        final DataType dataType = Mockito.mock(DataType.class);
        rxFit.recording().subscribe(dataType);
        rxFit.recording().subscribe(dataType, TIMEOUT_TIME, TIMEOUT_TIMEUNIT);

        PowerMockito.verifyStatic(times(2));
        Single.create(captor.capture());

        RecordingSubscribeSingle single = captor.getAllValues().get(0);
        assertEquals(dataType, single.dataType);
        assertNull(single.dataSource);
        assertNoTimeoutSet(single);

        single = captor.getAllValues().get(1);
        assertEquals(dataType, single.dataType);
        assertNull(single.dataSource);
        assertTimeoutSet(single);
    }

}
