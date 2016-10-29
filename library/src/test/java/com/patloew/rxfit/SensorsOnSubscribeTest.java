package com.patloew.rxfit;

import android.app.PendingIntent;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSource;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.request.DataSourcesRequest;
import com.google.android.gms.fitness.request.OnDataPointListener;
import com.google.android.gms.fitness.request.SensorRequest;
import com.google.android.gms.fitness.result.DataSourcesResult;

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

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.observers.TestObserver;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@SuppressStaticInitializationFor("com.google.android.gms.fitness.Fitness")
@PrepareOnlyThisForTest({ ContextCompat.class, Fitness.class, Status.class, ConnectionResult.class, DataPoint.class, DataType.class, BaseRx.class })
public class SensorsOnSubscribeTest extends BaseOnSubscribeTest {

    @Mock DataType dataType;
    @Mock DataSource dataSource;
    @Mock SensorRequest sensorRequest;

    @Override
    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        super.setup();
    }


    // SensorsAddDataPointIntentObservable

    @Test
    public void SensorsAddDataPointIntentObservable_Success() {
        PendingIntent pendingIntent = Mockito.mock(PendingIntent.class);
        SensorsAddDataPointIntentSingle single = PowerMockito.spy(new SensorsAddDataPointIntentSingle(rxFit, sensorRequest, pendingIntent, null, null));

        setPendingResultValue(status);
        when(status.isSuccess()).thenReturn(true);
        when(sensorsApi.add(apiClient, sensorRequest, pendingIntent)).thenReturn(pendingResult);

        setupBaseSingleSuccess(single);

        assertSingleValue(Single.create(single).test(), status);
    }

    @Test
    public void SensorsAddDataPointIntentObservable_StatusException() {
        PendingIntent pendingIntent = Mockito.mock(PendingIntent.class);
        SensorsAddDataPointIntentSingle single = PowerMockito.spy(new SensorsAddDataPointIntentSingle(rxFit, sensorRequest, pendingIntent, null, null));

        setPendingResultValue(status);
        when(status.isSuccess()).thenReturn(false);
        when(sensorsApi.add(apiClient, sensorRequest, pendingIntent)).thenReturn(pendingResult);

        setupBaseSingleSuccess(single);

        assertError(Single.create(single).test(), StatusException.class);
    }


    // SensorsRemoveDataPointIntentObservable

    @Test
    public void SensorsRemoveDataPointIntentObservable_Success() {
        PendingIntent pendingIntent = Mockito.mock(PendingIntent.class);
        SensorsRemoveDataPointIntentSingle single = PowerMockito.spy(new SensorsRemoveDataPointIntentSingle(rxFit, pendingIntent, null, null));

        setPendingResultValue(status);
        when(status.isSuccess()).thenReturn(true);
        when(sensorsApi.remove(apiClient, pendingIntent)).thenReturn(pendingResult);

        setupBaseSingleSuccess(single);

        assertSingleValue(Single.create(single).test(), status);
    }

    @Test
    public void SensorsRemoveDataPointIntentObservable_StatusException() {
        PendingIntent pendingIntent = Mockito.mock(PendingIntent.class);
        SensorsRemoveDataPointIntentSingle single = PowerMockito.spy(new SensorsRemoveDataPointIntentSingle(rxFit, pendingIntent, null, null));

        setPendingResultValue(status);
        when(status.isSuccess()).thenReturn(false);
        when(sensorsApi.remove(apiClient, pendingIntent)).thenReturn(pendingResult);

        setupBaseSingleSuccess(single);

        assertError(Single.create(single).test(), StatusException.class);
    }

    // SensorsDataPointObservable

    @Test
    public void SensorsDataPointObservable_Success() {
        DataPoint dataPoint = Mockito.mock(DataPoint.class);
        SensorsDataPointObservable observable = PowerMockito.spy(new SensorsDataPointObservable(rxFit, sensorRequest, null, null));

        setPendingResultValue(status);
        when(status.isSuccess()).thenReturn(true);
        when(sensorsApi.add(Matchers.any(GoogleApiClient.class), Matchers.any(SensorRequest.class), Matchers.any(OnDataPointListener.class))).thenReturn(pendingResult);
        when(apiClient.isConnected()).thenReturn(true);

        setupBaseObservableSuccess(observable);
        TestObserver<DataPoint> sub = Observable.create(observable).test();
        getSubscriber(observable, apiClient).onNext(dataPoint);

        verify(sensorsApi, never()).remove(Matchers.any(GoogleApiClient.class), Matchers.any(OnDataPointListener.class));
        sub.dispose();
        verify(sensorsApi).remove(Matchers.any(GoogleApiClient.class), Matchers.any(OnDataPointListener.class));

        sub.assertNotTerminated();
        sub.assertValue(dataPoint);
    }

    @Test
    public void SensorsDataPointObservable_StatusException() {
        SensorsDataPointObservable observable = PowerMockito.spy(new SensorsDataPointObservable(rxFit, sensorRequest, null, null));

        setPendingResultValue(status);
        when(status.isSuccess()).thenReturn(false);
        when(sensorsApi.add(Matchers.any(GoogleApiClient.class), Matchers.any(SensorRequest.class), Matchers.any(OnDataPointListener.class))).thenReturn(pendingResult);

        setupBaseObservableSuccess(observable);

        assertError(Observable.create(observable).test(), StatusException.class);
    }

    // SensorsFindDataSourcesObservable

    @Test
    public void SensorsFindDataSourcesObservable_Success() {
        DataSourcesRequest dataSourcesRequest = Mockito.mock(DataSourcesRequest.class);
        DataSourcesResult dataSourcesResult = Mockito.mock(DataSourcesResult.class);
        SensorsFindDataSourcesSingle single = PowerMockito.spy(new SensorsFindDataSourcesSingle(rxFit, dataSourcesRequest, null, null, null));

        List<DataSource> dataSourceList = new ArrayList<>();
        dataSourceList.add(dataSource);

        when(dataSourcesResult.getDataSources()).thenReturn(dataSourceList);

        setPendingResultValue(dataSourcesResult);
        when(dataSourcesResult.getStatus()).thenReturn(status);
        when(status.isSuccess()).thenReturn(true);
        when(sensorsApi.findDataSources(apiClient, dataSourcesRequest)).thenReturn(pendingResult);

        setupBaseSingleSuccess(single);

        assertSingleValue(Single.create(single).test(), dataSourceList);
    }

    @Test
    public void SensorsFindDataSourcesObservable_StatusException() {
        DataSourcesRequest dataSourcesRequest = Mockito.mock(DataSourcesRequest.class);
        DataSourcesResult dataSourcesResult = Mockito.mock(DataSourcesResult.class);
        SensorsFindDataSourcesSingle single = PowerMockito.spy(new SensorsFindDataSourcesSingle(rxFit, dataSourcesRequest, null, null, null));

        List<DataSource> dataSourceList = new ArrayList<>();
        dataSourceList.add(dataSource);

        when(dataSourcesResult.getDataSources()).thenReturn(dataSourceList);

        setPendingResultValue(dataSourcesResult);
        when(dataSourcesResult.getStatus()).thenReturn(status);
        when(status.isSuccess()).thenReturn(false);
        when(sensorsApi.findDataSources(apiClient, dataSourcesRequest)).thenReturn(pendingResult);

        setupBaseSingleSuccess(single);

        assertError(Single.create(single).test(), StatusException.class);
    }

    @Test
    public void SensorsFindDataSourcesObservable_WithDataType_Success() {
        DataSourcesRequest dataSourcesRequest = Mockito.mock(DataSourcesRequest.class);
        DataSourcesResult dataSourcesResult = Mockito.mock(DataSourcesResult.class);
        SensorsFindDataSourcesSingle single = PowerMockito.spy(new SensorsFindDataSourcesSingle(rxFit, dataSourcesRequest, dataType, null, null));

        List<DataSource> dataSourceList = new ArrayList<>();
        dataSourceList.add(dataSource);

        when(dataSourcesResult.getDataSources(dataType)).thenReturn(dataSourceList);

        setPendingResultValue(dataSourcesResult);
        when(dataSourcesResult.getStatus()).thenReturn(status);
        when(status.isSuccess()).thenReturn(true);
        when(sensorsApi.findDataSources(apiClient, dataSourcesRequest)).thenReturn(pendingResult);

        setupBaseSingleSuccess(single);

        assertSingleValue(Single.create(single).test(), dataSourceList);
    }

    @Test
    public void SensorsFindDataSourcesObservable_WithDataType_StatusException() {
        DataSourcesRequest dataSourcesRequest = Mockito.mock(DataSourcesRequest.class);
        DataSourcesResult dataSourcesResult = Mockito.mock(DataSourcesResult.class);
        SensorsFindDataSourcesSingle single = PowerMockito.spy(new SensorsFindDataSourcesSingle(rxFit, dataSourcesRequest, dataType, null, null));

        List<DataSource> dataSourceList = new ArrayList<>();
        dataSourceList.add(dataSource);

        when(dataSourcesResult.getDataSources(dataType)).thenReturn(dataSourceList);

        setPendingResultValue(dataSourcesResult);
        when(dataSourcesResult.getStatus()).thenReturn(status);
        when(status.isSuccess()).thenReturn(false);
        when(sensorsApi.findDataSources(apiClient, dataSourcesRequest)).thenReturn(pendingResult);

        setupBaseSingleSuccess(single);

        assertError(Single.create(single).test(), StatusException.class);
    }
}
