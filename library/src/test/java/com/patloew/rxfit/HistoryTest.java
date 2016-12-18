package com.patloew.rxfit;

import android.app.PendingIntent;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataSource;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.request.DataDeleteRequest;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.request.DataUpdateRequest;

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
import static junit.framework.Assert.assertNull;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.times;
import static org.powermock.api.mockito.PowerMockito.doReturn;

@RunWith(PowerMockRunner.class)
@SuppressStaticInitializationFor("com.google.android.gms.fitness.Fitness")
@PrepareOnlyThisForTest({ Observable.class, Single.class, ContextCompat.class, Fitness.class, Status.class, ConnectionResult.class, DataType.class, DataSet.class })
public class HistoryTest extends BaseTest {

    @Override
    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        PowerMockito.spy(Single.class);
        PowerMockito.mockStatic(Observable.class);
        doReturn(100).when(Observable.class, "bufferSize");
        super.setup();
    }

    // Data Delete Request

    @Test
    public void History_DataDeleteRequest() throws Exception {
        ArgumentCaptor<HistoryDeleteDataSingle> captor = ArgumentCaptor.forClass(HistoryDeleteDataSingle.class);

        final DataDeleteRequest request = Mockito.mock(DataDeleteRequest.class);
        rxFit.history().delete(request);
        rxFit.history().delete(request, TIMEOUT_TIME, TIMEOUT_TIMEUNIT);

        PowerMockito.verifyStatic(times(2));
        Single.create(captor.capture());

        HistoryDeleteDataSingle single = captor.getAllValues().get(0);
        assertEquals(request, single.dataDeleteRequest);
        assertNoTimeoutSet(single);

        single = captor.getAllValues().get(1);
        assertEquals(request, single.dataDeleteRequest);
        assertTimeoutSet(single);
    }

    // Insert Data Set

    @Test
    public void History_InsertDataSet() throws Exception {
        ArgumentCaptor<HistoryInsertDataSingle> captor = ArgumentCaptor.forClass(HistoryInsertDataSingle.class);

        final DataSet dataSet = Mockito.mock(DataSet.class);
        rxFit.history().insert(dataSet);
        rxFit.history().insert(dataSet, TIMEOUT_TIME, TIMEOUT_TIMEUNIT);

        PowerMockito.verifyStatic(times(2));
        Single.create(captor.capture());

        HistoryInsertDataSingle single = captor.getAllValues().get(0);
        assertEquals(dataSet, single.dataSet);
        assertNoTimeoutSet(single);

        single = captor.getAllValues().get(1);
        assertEquals(dataSet, single.dataSet);
        assertTimeoutSet(single);
    }

    // Read Daily Total

    @Test
    public void History_ReadDailyTotal() throws Exception {
        ArgumentCaptor<HistoryReadDailyTotalSingle> captor = ArgumentCaptor.forClass(HistoryReadDailyTotalSingle.class);

        final DataType dataType = Mockito.mock(DataType.class);
        rxFit.history().readDailyTotal(dataType);
        rxFit.history().readDailyTotal(dataType, TIMEOUT_TIME, TIMEOUT_TIMEUNIT);

        PowerMockito.verifyStatic(times(2));
        Single.create(captor.capture());

        HistoryReadDailyTotalSingle single = captor.getAllValues().get(0);
        assertEquals(dataType, single.dataType);
        assertNoTimeoutSet(single);

        single = captor.getAllValues().get(1);
        assertEquals(dataType, single.dataType);
        assertTimeoutSet(single);
    }

    // Read

    @Test
    public void History_Read() throws Exception {
        ArgumentCaptor<HistoryReadDataSingle> captor = ArgumentCaptor.forClass(HistoryReadDataSingle.class);

        final DataReadRequest request = Mockito.mock(DataReadRequest.class);
        rxFit.history().read(request);
        rxFit.history().read(request, TIMEOUT_TIME, TIMEOUT_TIMEUNIT);

        PowerMockito.verifyStatic(times(2));
        Single.create(captor.capture());

        HistoryReadDataSingle single = captor.getAllValues().get(0);
        assertEquals(request, single.dataReadRequest);
        assertNoTimeoutSet(single);

        single = captor.getAllValues().get(1);
        assertEquals(request, single.dataReadRequest);
        assertTimeoutSet(single);
    }

    // Read Buckets

    @Test
    public void History_ReadBuckets() throws Exception {
        ArgumentCaptor<HistoryReadDataSingle> captor = ArgumentCaptor.forClass(HistoryReadDataSingle.class);

        final DataReadRequest request = Mockito.mock(DataReadRequest.class);
        rxFit.history().readBuckets(request);
        rxFit.history().readBuckets(request, TIMEOUT_TIME, TIMEOUT_TIMEUNIT);

        PowerMockito.verifyStatic(atLeast(2));
        Single.create(captor.capture());

        HistoryReadDataSingle single = captor.getAllValues().get(0);
        assertEquals(request, single.dataReadRequest);
        assertNoTimeoutSet(single);

        single = captor.getAllValues().get(1);
        assertEquals(request, single.dataReadRequest);
        assertTimeoutSet(single);
    }

    // Read DataSets

    @Test
    public void History_ReadDataSets() throws Exception {
        ArgumentCaptor<HistoryReadDataSingle> captor = ArgumentCaptor.forClass(HistoryReadDataSingle.class);

        final DataReadRequest request = Mockito.mock(DataReadRequest.class);
        rxFit.history().readDataSets(request);
        rxFit.history().readDataSets(request, TIMEOUT_TIME, TIMEOUT_TIMEUNIT);

        PowerMockito.verifyStatic(atLeast(2));
        Single.create(captor.capture());

        HistoryReadDataSingle single = captor.getAllValues().get(0);
        assertEquals(request, single.dataReadRequest);
        assertNoTimeoutSet(single);

        single = captor.getAllValues().get(1);
        assertEquals(request, single.dataReadRequest);
        assertTimeoutSet(single);
    }


    // Update

    @Test
    public void History_Update() throws Exception {
        ArgumentCaptor<HistoryUpdateDataSingle> captor = ArgumentCaptor.forClass(HistoryUpdateDataSingle.class);

        final DataUpdateRequest request = Mockito.mock(DataUpdateRequest.class);
        rxFit.history().update(request);
        rxFit.history().update(request, TIMEOUT_TIME, TIMEOUT_TIMEUNIT);

        PowerMockito.verifyStatic(times(2));
        Single.create(captor.capture());

        HistoryUpdateDataSingle single = captor.getAllValues().get(0);
        assertEquals(request, single.dataUpdateRequest);
        assertNoTimeoutSet(single);

        single = captor.getAllValues().get(1);
        assertEquals(request, single.dataUpdateRequest);
        assertTimeoutSet(single);
    }

    // RegisterDataUpdateListener

    @Test
    public void History_RegisterDataUpdateListener_DataSource() throws Exception {
        ArgumentCaptor<HistoryRegisterDataUpdateListenerSingle> captor = ArgumentCaptor.forClass(HistoryRegisterDataUpdateListenerSingle.class);

        final PendingIntent pendingIntent = Mockito.mock(PendingIntent.class);
        final DataSource dataSource = Mockito.mock(DataSource.class);
        rxFit.history().registerDataUpdateListener(pendingIntent, dataSource);
        rxFit.history().registerDataUpdateListener(pendingIntent, dataSource, TIMEOUT_TIME, TIMEOUT_TIMEUNIT);

        PowerMockito.verifyStatic(times(2));
        Single.create(captor.capture());

        HistoryRegisterDataUpdateListenerSingle single = captor.getAllValues().get(0);
        assertEquals(pendingIntent, single.request.getIntent());
        assertEquals(dataSource, single.request.getDataSource());
        assertNull(single.request.getDataType());
        assertNoTimeoutSet(single);

        single = captor.getAllValues().get(1);
        assertEquals(pendingIntent, single.request.getIntent());
        assertEquals(dataSource, single.request.getDataSource());
        assertNull(single.request.getDataType());
        assertTimeoutSet(single);
    }

    @Test
    public void History_RegisterDataUpdateListener_DataType() throws Exception {
        ArgumentCaptor<HistoryRegisterDataUpdateListenerSingle> captor = ArgumentCaptor.forClass(HistoryRegisterDataUpdateListenerSingle.class);

        final PendingIntent pendingIntent = Mockito.mock(PendingIntent.class);
        final DataType dataType = Mockito.mock(DataType.class);
        rxFit.history().registerDataUpdateListener(pendingIntent, dataType);
        rxFit.history().registerDataUpdateListener(pendingIntent, dataType, TIMEOUT_TIME, TIMEOUT_TIMEUNIT);

        PowerMockito.verifyStatic(times(2));
        Single.create(captor.capture());

        HistoryRegisterDataUpdateListenerSingle single = captor.getAllValues().get(0);
        assertEquals(pendingIntent, single.request.getIntent());
        assertNull(single.request.getDataSource());
        assertEquals(dataType, single.request.getDataType());
        assertNoTimeoutSet(single);

        single = captor.getAllValues().get(1);
        assertEquals(pendingIntent, single.request.getIntent());
        assertNull(single.request.getDataSource());
        assertEquals(dataType, single.request.getDataType());
        assertTimeoutSet(single);
    }


    @Test
    public void History_RegisterDataUpdateListener_DataSource_DataType() throws Exception {
        ArgumentCaptor<HistoryRegisterDataUpdateListenerSingle> captor = ArgumentCaptor.forClass(HistoryRegisterDataUpdateListenerSingle.class);

        final PendingIntent pendingIntent = Mockito.mock(PendingIntent.class);
        final DataSource dataSource = Mockito.mock(DataSource.class);
        final DataType dataType = Mockito.mock(DataType.class);
        rxFit.history().registerDataUpdateListener(pendingIntent, dataSource, dataType);
        rxFit.history().registerDataUpdateListener(pendingIntent, dataSource, dataType, TIMEOUT_TIME, TIMEOUT_TIMEUNIT);

        PowerMockito.verifyStatic(times(2));
        Single.create(captor.capture());

        HistoryRegisterDataUpdateListenerSingle single = captor.getAllValues().get(0);
        assertEquals(pendingIntent, single.request.getIntent());
        assertEquals(dataSource, single.request.getDataSource());
        assertEquals(dataType, single.request.getDataType());
        assertNoTimeoutSet(single);

        single = captor.getAllValues().get(1);
        assertEquals(pendingIntent, single.request.getIntent());
        assertEquals(dataSource, single.request.getDataSource());
        assertEquals(dataType, single.request.getDataType());
        assertTimeoutSet(single);
    }

    // UnregisterDataUpdateListener

    @Test
    public void History_UnregisterDataUpdateListener() throws Exception {
        ArgumentCaptor<HistoryUnregisterDataUpdateListenerSingle> captor = ArgumentCaptor.forClass(HistoryUnregisterDataUpdateListenerSingle.class);

        final PendingIntent pendingIntent = Mockito.mock(PendingIntent.class);
        rxFit.history().unregisterDataUpdateListener(pendingIntent);
        rxFit.history().unregisterDataUpdateListener(pendingIntent, TIMEOUT_TIME, TIMEOUT_TIMEUNIT);

        PowerMockito.verifyStatic(times(2));
        Single.create(captor.capture());

        HistoryUnregisterDataUpdateListenerSingle single = captor.getAllValues().get(0);
        assertEquals(pendingIntent, single.pendingIntent);
        assertNoTimeoutSet(single);

        single = captor.getAllValues().get(1);
        assertEquals(pendingIntent, single.pendingIntent);
        assertTimeoutSet(single);
    }


}
