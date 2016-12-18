package com.patloew.rxfit;

import android.support.v4.content.ContextCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataSource;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.request.DataDeleteRequest;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.request.DataUpdateListenerRegistrationRequest;
import com.google.android.gms.fitness.request.DataUpdateRequest;
import com.google.android.gms.fitness.result.DailyTotalResult;
import com.google.android.gms.fitness.result.DataReadResult;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareOnlyThisForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;

import io.reactivex.Single;

import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@SuppressStaticInitializationFor("com.google.android.gms.fitness.Fitness")
@PrepareOnlyThisForTest({ ContextCompat.class, Fitness.class, Status.class, ConnectionResult.class, DataType.class, DataSet.class, BaseRx.class })
public class HistoryOnSubscribeTest extends BaseOnSubscribeTest {

    @Mock DataType dataType;
    @Mock DataSet dataSet;
    @Mock DataSource dataSource;

    @Override
    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        super.setup();
    }

    // HistoryDeleteDataObservable

    @Test
    public void HistoryDeleteDataObservable_Success() {
        DataDeleteRequest dataDeleteRequest = Mockito.mock(DataDeleteRequest.class);
        HistoryDeleteDataSingle single = PowerMockito.spy(new HistoryDeleteDataSingle(rxFit, dataDeleteRequest, null, null));

        setPendingResultValue(status);
        when(status.isSuccess()).thenReturn(true);
        when(historyApi.deleteData(apiClient, dataDeleteRequest)).thenReturn(pendingResult);

        setupBaseSingleSuccess(single);

        assertSingleValue(Single.create(single).test(), status);
    }

    @Test
    public void HistoryDeleteDataObservable_StatusException() {
        DataDeleteRequest dataDeleteRequest = Mockito.mock(DataDeleteRequest.class);
        HistoryDeleteDataSingle single = PowerMockito.spy(new HistoryDeleteDataSingle(rxFit, dataDeleteRequest, null, null));

        setPendingResultValue(status);
        when(status.isSuccess()).thenReturn(false);
        when(historyApi.deleteData(apiClient, dataDeleteRequest)).thenReturn(pendingResult);

        setupBaseSingleSuccess(single);

        assertError(Single.create(single).test(), StatusException.class);
    }

    // HistoryInsertDataObservable

    @Test
    public void HistoryInsertDataObservable_Success() {
        HistoryInsertDataSingle single = PowerMockito.spy(new HistoryInsertDataSingle(rxFit, dataSet, null, null));

        setPendingResultValue(status);
        when(status.isSuccess()).thenReturn(true);
        when(historyApi.insertData(apiClient, dataSet)).thenReturn(pendingResult);

        setupBaseSingleSuccess(single);

        assertSingleValue(Single.create(single).test(), status);
    }

    @Test
    public void HistoryInsertDataObservable_StatusException() {
        HistoryInsertDataSingle single = PowerMockito.spy(new HistoryInsertDataSingle(rxFit, dataSet, null, null));

        setPendingResultValue(status);
        when(status.isSuccess()).thenReturn(false);
        when(historyApi.insertData(apiClient, dataSet)).thenReturn(pendingResult);

        setupBaseSingleSuccess(single);

        assertError(Single.create(single).test(), StatusException.class);
    }

    // HistoryReadDailyTotalObservable

    @Test
    public void HistoryReadDailyTotalObservable_Success() {
        DailyTotalResult dailyTotalResult = Mockito.mock(DailyTotalResult.class);
        HistoryReadDailyTotalSingle single = PowerMockito.spy(new HistoryReadDailyTotalSingle(rxFit, dataType, null, null));

        setPendingResultValue(dailyTotalResult);
        when(dailyTotalResult.getStatus()).thenReturn(status);
        when(dailyTotalResult.getTotal()).thenReturn(dataSet);
        when(status.isSuccess()).thenReturn(true);
        when(historyApi.readDailyTotal(apiClient, dataType)).thenReturn(pendingResult);

        setupBaseSingleSuccess(single);

        assertSingleValue(Single.create(single).test(), dataSet);
    }

    @Test
    public void HistoryReadDailyTotalObservable_StatusException() {
        DailyTotalResult dailyTotalResult = Mockito.mock(DailyTotalResult.class);
        HistoryReadDailyTotalSingle single = PowerMockito.spy(new HistoryReadDailyTotalSingle(rxFit, dataType, null, null));

        setPendingResultValue(dailyTotalResult);
        when(dailyTotalResult.getStatus()).thenReturn(status);
        when(dailyTotalResult.getTotal()).thenReturn(dataSet);
        when(status.isSuccess()).thenReturn(false);
        when(historyApi.readDailyTotal(apiClient, dataType)).thenReturn(pendingResult);

        setupBaseSingleSuccess(single);

        assertError(Single.create(single).test(), StatusException.class);
    }

    // HistoryReadDataObservable

    @Test
    public void HistoryReadDataObservable_Success() {
        DataReadRequest dataReadRequest = Mockito.mock(DataReadRequest.class);
        DataReadResult dataReadResult = Mockito.mock(DataReadResult.class);
        HistoryReadDataSingle single = PowerMockito.spy(new HistoryReadDataSingle(rxFit, dataReadRequest, null, null));

        setPendingResultValue(dataReadResult);
        when(dataReadResult.getStatus()).thenReturn(status);
        when(status.isSuccess()).thenReturn(true);
        when(historyApi.readData(apiClient, dataReadRequest)).thenReturn(pendingResult);

        setupBaseSingleSuccess(single);

        assertSingleValue(Single.create(single).test(), dataReadResult);
    }

    @Test
    public void HistoryReadDataObservable_StatusException() {
        DataReadRequest dataReadRequest = Mockito.mock(DataReadRequest.class);
        DataReadResult dataReadResult = Mockito.mock(DataReadResult.class);
        HistoryReadDataSingle single = PowerMockito.spy(new HistoryReadDataSingle(rxFit, dataReadRequest, null, null));

        setPendingResultValue(dataReadResult);
        when(dataReadResult.getStatus()).thenReturn(status);
        when(status.isSuccess()).thenReturn(false);
        when(historyApi.readData(apiClient, dataReadRequest)).thenReturn(pendingResult);

        setupBaseSingleSuccess(single);

        assertError(Single.create(single).test(), StatusException.class);
    }

    // HistoryUpdateDataObservable

    @Test
    public void HistoryUpdateDataObservable_Success() {
        DataUpdateRequest dataUpdateRequest = Mockito.mock(DataUpdateRequest.class);
        HistoryUpdateDataSingle single = PowerMockito.spy(new HistoryUpdateDataSingle(rxFit, dataUpdateRequest, null, null));

        setPendingResultValue(status);
        when(status.isSuccess()).thenReturn(true);
        when(historyApi.updateData(apiClient, dataUpdateRequest)).thenReturn(pendingResult);

        setupBaseSingleSuccess(single);

        assertSingleValue(Single.create(single).test(), status);
    }

    @Test
    public void HistoryUpdateDataObservable_StatusException() {
        DataUpdateRequest dataUpdateRequest = Mockito.mock(DataUpdateRequest.class);
        HistoryUpdateDataSingle single = PowerMockito.spy(new HistoryUpdateDataSingle(rxFit, dataUpdateRequest, null, null));

        setPendingResultValue(status);
        when(status.isSuccess()).thenReturn(false);
        when(historyApi.updateData(apiClient, dataUpdateRequest)).thenReturn(pendingResult);

        setupBaseSingleSuccess(single);

        assertError(Single.create(single).test(), StatusException.class);
    }

    // HistoryRegisterDataUpdateListenerSingle

    @Test
    public void HistoryRegisterDataUpdateListenerSingle_DataSource_Success() {
        HistoryRegisterDataUpdateListenerSingle single = PowerMockito.spy(new HistoryRegisterDataUpdateListenerSingle(rxFit, pendingIntent, dataSource, null, null, null));

        setPendingResultValue(status);
        when(status.isSuccess()).thenReturn(true);
        when(historyApi.registerDataUpdateListener(Matchers.eq(apiClient), Matchers.any(DataUpdateListenerRegistrationRequest.class))).thenReturn(pendingResult);

        setupBaseSingleSuccess(single);

        assertSingleValue(Single.create(single).test(), status);
    }

    @Test
    public void HistoryRegisterDataUpdateListenerSingle_DataSource_StatusException() {
        HistoryRegisterDataUpdateListenerSingle single = PowerMockito.spy(new HistoryRegisterDataUpdateListenerSingle(rxFit, pendingIntent, dataSource, null, null, null));

        setPendingResultValue(status);
        when(status.isSuccess()).thenReturn(false);
        when(historyApi.registerDataUpdateListener(Matchers.eq(apiClient), Matchers.any(DataUpdateListenerRegistrationRequest.class))).thenReturn(pendingResult);

        setupBaseSingleSuccess(single);

        assertError(Single.create(single).test(), StatusException.class);
    }

    @Test
    public void HistoryRegisterDataUpdateListenerSingle_DataType_Success() {
        HistoryRegisterDataUpdateListenerSingle single = PowerMockito.spy(new HistoryRegisterDataUpdateListenerSingle(rxFit, pendingIntent, null, dataType, null, null));

        setPendingResultValue(status);
        when(status.isSuccess()).thenReturn(true);
        when(historyApi.registerDataUpdateListener(Matchers.eq(apiClient), Matchers.any(DataUpdateListenerRegistrationRequest.class))).thenReturn(pendingResult);

        setupBaseSingleSuccess(single);

        assertSingleValue(Single.create(single).test(), status);
    }

    @Test
    public void HistoryRegisterDataUpdateListenerSingle_DataType_StatusException() {
        HistoryRegisterDataUpdateListenerSingle single = PowerMockito.spy(new HistoryRegisterDataUpdateListenerSingle(rxFit, pendingIntent, null, dataType, null, null));

        setPendingResultValue(status);
        when(status.isSuccess()).thenReturn(false);
        when(historyApi.registerDataUpdateListener(Matchers.eq(apiClient), Matchers.any(DataUpdateListenerRegistrationRequest.class))).thenReturn(pendingResult);

        setupBaseSingleSuccess(single);

        assertError(Single.create(single).test(), StatusException.class);
    }

    @Test
    public void HistoryRegisterDataUpdateListenerSingle_DataType_DataSource_Success() {
        HistoryRegisterDataUpdateListenerSingle single = PowerMockito.spy(new HistoryRegisterDataUpdateListenerSingle(rxFit, pendingIntent, dataSource, dataType, null, null));

        setPendingResultValue(status);
        when(status.isSuccess()).thenReturn(true);
        when(historyApi.registerDataUpdateListener(Matchers.eq(apiClient), Matchers.any(DataUpdateListenerRegistrationRequest.class))).thenReturn(pendingResult);

        setupBaseSingleSuccess(single);

        assertSingleValue(Single.create(single).test(), status);
    }

    @Test
    public void HistoryRegisterDataUpdateListenerSingle_DataType_DataSource_StatusException() {
        HistoryRegisterDataUpdateListenerSingle single = PowerMockito.spy(new HistoryRegisterDataUpdateListenerSingle(rxFit, pendingIntent, dataSource, dataType, null, null));

        setPendingResultValue(status);
        when(status.isSuccess()).thenReturn(false);
        when(historyApi.registerDataUpdateListener(Matchers.eq(apiClient), Matchers.any(DataUpdateListenerRegistrationRequest.class))).thenReturn(pendingResult);

        setupBaseSingleSuccess(single);

        assertError(Single.create(single).test(), StatusException.class);
    }

    // HistoryUnregisterDataUpdateListenerSingle

    @Test
    public void HistoryUnregisterDataUpdateListenerSingle_Success() {
        HistoryUnregisterDataUpdateListenerSingle single = PowerMockito.spy(new HistoryUnregisterDataUpdateListenerSingle(rxFit, pendingIntent, null, null));

        setPendingResultValue(status);
        when(status.isSuccess()).thenReturn(true);
        when(historyApi.unregisterDataUpdateListener(apiClient, pendingIntent)).thenReturn(pendingResult);

        setupBaseSingleSuccess(single);

        assertSingleValue(Single.create(single).test(), status);
    }

    @Test
    public void HistoryUnregisterDataUpdateListenerSingle_StatusException() {
        HistoryUnregisterDataUpdateListenerSingle single = PowerMockito.spy(new HistoryUnregisterDataUpdateListenerSingle(rxFit, pendingIntent, null, null));

        setPendingResultValue(status);
        when(status.isSuccess()).thenReturn(false);
        when(historyApi.unregisterDataUpdateListener(apiClient, pendingIntent)).thenReturn(pendingResult);

        setupBaseSingleSuccess(single);

        assertError(Single.create(single).test(), StatusException.class);
    }


}
