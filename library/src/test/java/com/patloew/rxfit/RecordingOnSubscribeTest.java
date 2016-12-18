package com.patloew.rxfit;

import android.support.v4.content.ContextCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.DataSource;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Subscription;
import com.google.android.gms.fitness.result.ListSubscriptionsResult;

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
@PrepareOnlyThisForTest({ ContextCompat.class, Fitness.class, Status.class, ConnectionResult.class, DataType.class, BaseRx.class })
public class RecordingOnSubscribeTest extends BaseOnSubscribeTest {

    @Mock DataType dataType;
    @Mock DataSource dataSource;
    @Mock Subscription subscription;

    @Override
    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        super.setup();
    }


    // RecordingListSubscriptionsObservable

    @Test
    public void RecordingListSubscriptionsObservable_Success() {
        RecordingListSubscriptionsSingle single = PowerMockito.spy(new RecordingListSubscriptionsSingle(rxFit, null, null, null));

        ListSubscriptionsResult listSubscriptionsResult = Mockito.mock(ListSubscriptionsResult.class);

        List<Subscription> subscriptionList = new ArrayList<>();
        subscriptionList.add(Mockito.mock(Subscription.class));

        when(listSubscriptionsResult.getSubscriptions()).thenReturn(subscriptionList);

        setPendingResultValue(listSubscriptionsResult);
        when(listSubscriptionsResult.getStatus()).thenReturn(status);
        when(status.isSuccess()).thenReturn(true);
        when(recordingApi.listSubscriptions(apiClient)).thenReturn(pendingResult);

        setupBaseSingleSuccess(single);

        assertSingleValue(Single.create(single).test(), subscriptionList);
    }

    @Test
    public void RecordingListSubscriptionsObservable_StatusException() {
        RecordingListSubscriptionsSingle single = PowerMockito.spy(new RecordingListSubscriptionsSingle(rxFit, null, null, null));

        ListSubscriptionsResult listSubscriptionsResult = Mockito.mock(ListSubscriptionsResult.class);

        List<Subscription> subscriptionList = new ArrayList<>();
        subscriptionList.add(Mockito.mock(Subscription.class));

        when(listSubscriptionsResult.getSubscriptions()).thenReturn(subscriptionList);

        setPendingResultValue(listSubscriptionsResult);
        when(listSubscriptionsResult.getStatus()).thenReturn(status);
        when(status.isSuccess()).thenReturn(false);
        when(recordingApi.listSubscriptions(apiClient)).thenReturn(pendingResult);

        setupBaseSingleSuccess(single);

        assertError(Single.create(single).test(), StatusException.class);
    }

    @Test
    public void RecordingListSubscriptionsObservable_WithDataType_Success() {
        RecordingListSubscriptionsSingle single = PowerMockito.spy(new RecordingListSubscriptionsSingle(rxFit, dataType, null, null));

        ListSubscriptionsResult listSubscriptionsResult = Mockito.mock(ListSubscriptionsResult.class);

        List<Subscription> subscriptionList = new ArrayList<>();
        subscriptionList.add(Mockito.mock(Subscription.class));

        when(listSubscriptionsResult.getSubscriptions()).thenReturn(subscriptionList);

        setPendingResultValue(listSubscriptionsResult);
        when(listSubscriptionsResult.getStatus()).thenReturn(status);
        when(status.isSuccess()).thenReturn(true);
        when(recordingApi.listSubscriptions(apiClient, dataType)).thenReturn(pendingResult);

        setupBaseSingleSuccess(single);

        assertSingleValue(Single.create(single).test(), subscriptionList);
    }

    @Test
    public void RecordingListSubscriptionsObservable_WithDataType_StatusException() {
        RecordingListSubscriptionsSingle single = PowerMockito.spy(new RecordingListSubscriptionsSingle(rxFit, dataType, null, null));

        ListSubscriptionsResult listSubscriptionsResult = Mockito.mock(ListSubscriptionsResult.class);

        List<Subscription> subscriptionList = new ArrayList<>();
        subscriptionList.add(subscription);

        when(listSubscriptionsResult.getSubscriptions()).thenReturn(subscriptionList);

        setPendingResultValue(listSubscriptionsResult);
        when(listSubscriptionsResult.getStatus()).thenReturn(status);
        when(status.isSuccess()).thenReturn(false);
        when(recordingApi.listSubscriptions(apiClient, dataType)).thenReturn(pendingResult);

        setupBaseSingleSuccess(single);

        assertError(Single.create(single).test(), StatusException.class);
    }

    // RecordingSubscribeObservable

    @Test
    public void RecordingSubscribeObservable_DataType_Success() {
        RecordingSubscribeSingle single = PowerMockito.spy(new RecordingSubscribeSingle(rxFit, null, dataType, null, null));

        setPendingResultValue(status);
        when(status.isSuccess()).thenReturn(true);
        when(recordingApi.subscribe(apiClient, dataType)).thenReturn(pendingResult);

        setupBaseSingleSuccess(single);

        assertSingleValue(Single.create(single).test(), status);
    }


    @Test
    public void RecordingSubscribeObservable_DataType_StatusException() {
        RecordingSubscribeSingle single = PowerMockito.spy(new RecordingSubscribeSingle(rxFit, null, dataType, null, null));

        setPendingResultValue(status);
        when(status.isSuccess()).thenReturn(false);
        when(recordingApi.subscribe(apiClient, dataType)).thenReturn(pendingResult);

        setupBaseSingleSuccess(single);

        assertError(Single.create(single).test(), StatusException.class);
    }

    @Test
    public void RecordingSubscribeObservable_DataSource_Success() {
        RecordingSubscribeSingle single = PowerMockito.spy(new RecordingSubscribeSingle(rxFit, dataSource, null, null, null));

        setPendingResultValue(status);
        when(status.isSuccess()).thenReturn(true);
        when(recordingApi.subscribe(apiClient, dataSource)).thenReturn(pendingResult);

        setupBaseSingleSuccess(single);

        assertSingleValue(Single.create(single).test(), status);
    }


    @Test
    public void RecordingSubscribeObservable_DataSource_StatusException() {
        RecordingSubscribeSingle single = PowerMockito.spy(new RecordingSubscribeSingle(rxFit, dataSource, null, null, null));

        setPendingResultValue(status);
        when(status.isSuccess()).thenReturn(false);
        when(recordingApi.subscribe(apiClient, dataSource)).thenReturn(pendingResult);

        setupBaseSingleSuccess(single);

        assertError(Single.create(single).test(), StatusException.class);
    }

    // RecordingSubscribeObservable

    @Test
    public void RecordingUnsubscribeObservable_DataType_Success() {
        RecordingUnsubscribeSingle single = PowerMockito.spy(new RecordingUnsubscribeSingle(rxFit, null, dataType, null, null, null));

        setPendingResultValue(status);
        when(status.isSuccess()).thenReturn(true);
        when(recordingApi.unsubscribe(apiClient, dataType)).thenReturn(pendingResult);

        setupBaseSingleSuccess(single);

        assertSingleValue(Single.create(single).test(), status);
    }


    @Test
    public void RecordingUnsubscribeObservable_DataType_StatusException() {
        RecordingUnsubscribeSingle single = PowerMockito.spy(new RecordingUnsubscribeSingle(rxFit, null, dataType, null, null, null));

        setPendingResultValue(status);
        when(status.isSuccess()).thenReturn(false);
        when(recordingApi.unsubscribe(apiClient, dataType)).thenReturn(pendingResult);

        setupBaseSingleSuccess(single);

        assertError(Single.create(single).test(), StatusException.class);
    }

    @Test
    public void RecordingUnsubscribeObservable_DataSource_Success() {
        RecordingUnsubscribeSingle single = PowerMockito.spy(new RecordingUnsubscribeSingle(rxFit, dataSource, null, null, null, null));

        setPendingResultValue(status);
        when(status.isSuccess()).thenReturn(true);
        when(recordingApi.unsubscribe(apiClient, dataSource)).thenReturn(pendingResult);

        setupBaseSingleSuccess(single);

        assertSingleValue(Single.create(single).test(), status);
    }

    @Test
    public void RecordingUnsubscribeObservable_DataSource_StatusException() {
        RecordingUnsubscribeSingle single = PowerMockito.spy(new RecordingUnsubscribeSingle(rxFit, dataSource, null, null, null, null));

        setPendingResultValue(status);
        when(status.isSuccess()).thenReturn(false);
        when(recordingApi.unsubscribe(apiClient, dataSource)).thenReturn(pendingResult);

        setupBaseSingleSuccess(single);

        assertError(Single.create(single).test(), StatusException.class);
    }

    @Test
    public void RecordingUnsubscribeObservable_Subscription_Success() {
        RecordingUnsubscribeSingle single = PowerMockito.spy(new RecordingUnsubscribeSingle(rxFit, null, null, subscription, null, null));

        setPendingResultValue(status);
        when(status.isSuccess()).thenReturn(true);
        when(recordingApi.unsubscribe(apiClient, subscription)).thenReturn(pendingResult);

        setupBaseSingleSuccess(single);

        assertSingleValue(Single.create(single).test(), status);
    }

    @Test
    public void RecordingUnsubscribeObservable_Subscription_StatusException() {
        RecordingUnsubscribeSingle single = PowerMockito.spy(new RecordingUnsubscribeSingle(rxFit, null, null, subscription, null, null));

        setPendingResultValue(status);
        when(status.isSuccess()).thenReturn(false);
        when(recordingApi.unsubscribe(apiClient, subscription)).thenReturn(pendingResult);

        setupBaseSingleSuccess(single);

        assertError(Single.create(single).test(), StatusException.class);
    }

}
