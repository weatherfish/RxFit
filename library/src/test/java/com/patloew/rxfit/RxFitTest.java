package com.patloew.rxfit;

import android.app.PendingIntent;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.fitness.BleApi;
import com.google.android.gms.fitness.ConfigApi;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.HistoryApi;
import com.google.android.gms.fitness.RecordingApi;
import com.google.android.gms.fitness.SensorsApi;
import com.google.android.gms.fitness.SessionsApi;
import com.google.android.gms.fitness.data.BleDevice;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataSource;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Session;
import com.google.android.gms.fitness.data.Subscription;
import com.google.android.gms.fitness.request.BleScanCallback;
import com.google.android.gms.fitness.request.DataDeleteRequest;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.request.DataSourcesRequest;
import com.google.android.gms.fitness.request.DataTypeCreateRequest;
import com.google.android.gms.fitness.request.DataUpdateRequest;
import com.google.android.gms.fitness.request.OnDataPointListener;
import com.google.android.gms.fitness.request.SensorRequest;
import com.google.android.gms.fitness.request.SessionInsertRequest;
import com.google.android.gms.fitness.request.SessionReadRequest;
import com.google.android.gms.fitness.request.StartBleScanRequest;
import com.google.android.gms.fitness.result.BleDevicesResult;
import com.google.android.gms.fitness.result.DailyTotalResult;
import com.google.android.gms.fitness.result.DataReadResult;
import com.google.android.gms.fitness.result.DataSourcesResult;
import com.google.android.gms.fitness.result.DataTypeResult;
import com.google.android.gms.fitness.result.ListSubscriptionsResult;
import com.google.android.gms.fitness.result.SessionReadResult;
import com.google.android.gms.fitness.result.SessionStopResult;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareOnlyThisForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import java.util.ArrayList;
import java.util.List;

import rx.Completable;
import rx.Observable;
import rx.Subscriber;
import rx.observers.TestSubscriber;

import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareOnlyThisForTest({ ContextCompat.class, Fitness.class, Status.class, ConnectionResult.class, DataType.class, DataSet.class, DataPoint.class })
@SuppressStaticInitializationFor("com.google.android.gms.fitness.Fitness")
public class RxFitTest {

    @Mock Context ctx;

    @Mock GoogleApiClient apiClient;
    @Mock Status status;
    @Mock ConnectionResult connectionResult;
    @Mock PendingResult pendingResult;

    @Mock DataType dataType;
    @Mock DataSource dataSource;
    @Mock DataSet dataSet;
    @Mock BleDevice bleDevice;
    @Mock Subscription subscription;
    @Mock SensorRequest sensorRequest;
    @Mock Session session;

    @Mock BleApi bleApi;
    @Mock ConfigApi configApi;
    @Mock HistoryApi historyApi;
    @Mock RecordingApi recordingApi;
    @Mock SensorsApi sensorsApi;
    @Mock SessionsApi sessionsApi;

    @Mock RxFit rxFit;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        PowerMockito.mockStatic(Fitness.class);
        Whitebox.setInternalState(Fitness.class, bleApi);
        Whitebox.setInternalState(Fitness.class, configApi);
        Whitebox.setInternalState(Fitness.class, historyApi);
        Whitebox.setInternalState(Fitness.class, recordingApi);
        Whitebox.setInternalState(Fitness.class, sensorsApi);
        Whitebox.setInternalState(Fitness.class, sessionsApi);

        when(ctx.getApplicationContext()).thenReturn(ctx);
    }

    //////////////////
    // UTIL METHODS //
    //////////////////

    @SuppressWarnings("unchecked")
    // Mock GoogleApiClient connection success behaviour
    private void setupBaseObservableSuccess(final BaseObservable baseObservable) {
        doReturn(apiClient).when(baseObservable).createApiClient(Matchers.<Subscriber>any());
        doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                baseObservable.onGoogleApiClientReady(apiClient, baseObservable.subscriber);
                return null;
            }
        }).when(apiClient).connect();
    }

    @SuppressWarnings("unchecked")
    // Mock GoogleApiClient connection error behaviour
    private void setupBaseObservableError(final BaseObservable baseObservable) {
        doReturn(apiClient).when(baseObservable).createApiClient(Matchers.<Subscriber>any());
        doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                baseObservable.subscriber.onError(new GoogleAPIConnectionException("Error connecting to GoogleApiClient.", connectionResult));
                return null;
            }
        }).when(apiClient).connect();
    }

    @SuppressWarnings("unchecked")
    private void setPendingResultValue(final Result result) {
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ((ResultCallback)invocation.getArguments()[0]).onResult(result);
                return null;
            }
        }).when(pendingResult).setResultCallback(Matchers.<ResultCallback>any());
    }

    private void assertError(TestSubscriber sub, Class<? extends Throwable> errorClass) {
        sub.assertError(errorClass);
        sub.assertNoValues();
        sub.assertUnsubscribed();
    }

    @SuppressWarnings("unchecked")
    private void assertSingleValue(TestSubscriber sub, Object value) {
        sub.assertCompleted();
        sub.assertUnsubscribed();
        sub.assertValue(value);
    }


    //////////////////////
    // OBSERVABLE TESTS //
    //////////////////////


    // GoogleApiClientObservable

    @Test
    public void GoogleAPIClientObservable_Success() {
        TestSubscriber<GoogleApiClient> sub = new TestSubscriber<>();
        GoogleAPIClientObservable observable = spy(new GoogleAPIClientObservable(ctx, new Api[] {}, new Scope[] {}));

        setupBaseObservableSuccess(observable);
        Observable.create(observable).subscribe(sub);

        assertSingleValue(sub, apiClient);
    }

    @Test
    public void GoogleAPIClientObservable_ConnectionException() {
        TestSubscriber<GoogleApiClient> sub = new TestSubscriber<>();
        final GoogleAPIClientObservable observable = spy(new GoogleAPIClientObservable(ctx, new Api[] {}, new Scope[] {}));

        setupBaseObservableError(observable);
        Observable.create(observable).subscribe(sub);

        assertError(sub, GoogleAPIConnectionException.class);
    }

    // CheckConnectionCompletable

    @Test
    public void CheckConnectionCompletable_Success() {
        TestSubscriber<GoogleApiClient> sub = new TestSubscriber<>();
        CheckConnectionCompletable observable = spy(new CheckConnectionCompletable(rxFit));

        setupBaseObservableSuccess(observable);
        Completable.fromObservable(Observable.create(observable)).subscribe(sub);

        sub.assertCompleted();
        sub.assertNoValues();
    }

    @Test
    public void CheckConnectionCompletable_Error() {
        TestSubscriber<GoogleApiClient> sub = new TestSubscriber<>();
        CheckConnectionCompletable observable = spy(new CheckConnectionCompletable(rxFit));

        setupBaseObservableError(observable);
        Completable.fromObservable(Observable.create(observable)).subscribe(sub);

        sub.assertError(GoogleAPIConnectionException.class);
        sub.assertNoValues();
    }

    /*******
     * BLE *
     *******/

    // BleClaimDeviceObservable

    @Test
    public void BleClaimDeviceObservable_BleDevice_Success() {
        TestSubscriber<Status> sub = new TestSubscriber<>();
        BleDevice bleDevice = Mockito.mock(BleDevice.class);
        BleClaimDeviceObservable observable = spy(new BleClaimDeviceObservable(rxFit, bleDevice, null));

        setPendingResultValue(status);
        when(status.isSuccess()).thenReturn(true);
        when(bleApi.claimBleDevice(apiClient, bleDevice)).thenReturn(pendingResult);

        setupBaseObservableSuccess(observable);
        Observable.create(observable).subscribe(sub);

        assertSingleValue(sub, status);
    }

    @Test
    public void BleClaimDeviceObservable_DeviceAddress_Success() {
        TestSubscriber<Status> sub = new TestSubscriber<>();
        String deviceAddress = "deviceAddress";
        BleClaimDeviceObservable observable = spy(new BleClaimDeviceObservable(rxFit, null, deviceAddress));

        setPendingResultValue(status);
        when(status.isSuccess()).thenReturn(true);
        when(bleApi.claimBleDevice(apiClient, deviceAddress)).thenReturn(pendingResult);

        setupBaseObservableSuccess(observable);
        Observable.create(observable).subscribe(sub);

        assertSingleValue(sub, status);
    }

    @Test
    public void BleClaimDeviceObservable_BleDevice_StatusException() {
        TestSubscriber<Status> sub = new TestSubscriber<>();
        BleDevice bleDevice = Mockito.mock(BleDevice.class);
        BleClaimDeviceObservable observable = spy(new BleClaimDeviceObservable(rxFit, bleDevice, null));

        setPendingResultValue(status);
        when(status.isSuccess()).thenReturn(false);
        when(bleApi.claimBleDevice(apiClient, bleDevice)).thenReturn(pendingResult);

        setupBaseObservableSuccess(observable);
        Observable.create(observable).subscribe(sub);

        assertError(sub, StatusException.class);
    }

    @Test
    public void BleClaimDeviceObservable_DeviceAddress_StatusException() {
        TestSubscriber<Status> sub = new TestSubscriber<>();
        String deviceAddress = "deviceAddress";
        BleClaimDeviceObservable observable = spy(new BleClaimDeviceObservable(rxFit, null, deviceAddress));

        setPendingResultValue(status);
        when(status.isSuccess()).thenReturn(false);
        when(bleApi.claimBleDevice(apiClient, deviceAddress)).thenReturn(pendingResult);

        setupBaseObservableSuccess(observable);
        Observable.create(observable).subscribe(sub);

        assertError(sub, StatusException.class);
    }


    // BleUnclaimDeviceObservable

    @Test
    public void BleUnclaimDeviceObservable_BleDevice_Success() {
        TestSubscriber<Status> sub = new TestSubscriber<>();
        BleDevice bleDevice = Mockito.mock(BleDevice.class);
        BleUnclaimDeviceObservable observable = spy(new BleUnclaimDeviceObservable(rxFit, bleDevice, null));

        setPendingResultValue(status);
        when(status.isSuccess()).thenReturn(true);
        when(bleApi.unclaimBleDevice(apiClient, bleDevice)).thenReturn(pendingResult);

        setupBaseObservableSuccess(observable);
        Observable.create(observable).subscribe(sub);

        assertSingleValue(sub, status);
    }

    @Test
    public void BleUnclaimDeviceObservable_DeviceAddress_Success() {
        TestSubscriber<Status> sub = new TestSubscriber<>();
        String deviceAddress = "deviceAddress";
        BleUnclaimDeviceObservable observable = spy(new BleUnclaimDeviceObservable(rxFit, null, deviceAddress));

        setPendingResultValue(status);
        when(status.isSuccess()).thenReturn(true);
        when(bleApi.unclaimBleDevice(apiClient, deviceAddress)).thenReturn(pendingResult);

        setupBaseObservableSuccess(observable);
        Observable.create(observable).subscribe(sub);

        assertSingleValue(sub, status);
    }

    @Test
    public void BleUnclaimDeviceObservable_BleDevice_StatusException() {
        TestSubscriber<Status> sub = new TestSubscriber<>();
        BleDevice bleDevice = Mockito.mock(BleDevice.class);
        BleUnclaimDeviceObservable observable = spy(new BleUnclaimDeviceObservable(rxFit, bleDevice, null));

        setPendingResultValue(status);
        when(status.isSuccess()).thenReturn(false);
        when(bleApi.unclaimBleDevice(apiClient, bleDevice)).thenReturn(pendingResult);

        setupBaseObservableSuccess(observable);
        Observable.create(observable).subscribe(sub);

        assertError(sub, StatusException.class);
    }

    @Test
    public void BleUnclaimDeviceObservable_DeviceAddress_StatusException() {
        TestSubscriber<Status> sub = new TestSubscriber<>();
        String deviceAddress = "deviceAddress";
        BleUnclaimDeviceObservable observable = spy(new BleUnclaimDeviceObservable(rxFit, null, deviceAddress));

        setPendingResultValue(status);
        when(status.isSuccess()).thenReturn(false);
        when(bleApi.unclaimBleDevice(apiClient, deviceAddress)).thenReturn(pendingResult);

        setupBaseObservableSuccess(observable);
        Observable.create(observable).subscribe(sub);

        assertError(sub, StatusException.class);
    }

    // BleListClaimedDevicesObservable

    @Test
    public void BleListClaimedDevicesObservable_WithDataType_Success() {
        TestSubscriber<List<BleDevice>> sub = new TestSubscriber<>();
        BleListClaimedDevicesObservable observable = spy(new BleListClaimedDevicesObservable(rxFit, dataType));

        BleDevicesResult bleDevicesResult = Mockito.mock(BleDevicesResult.class);

        List<BleDevice> bleDeviceList = new ArrayList<>();
        bleDeviceList.add(bleDevice);

        when(bleDevicesResult.getClaimedBleDevices(dataType)).thenReturn(bleDeviceList);

        setPendingResultValue(bleDevicesResult);
        when(bleDevicesResult.getStatus()).thenReturn(status);
        when(status.isSuccess()).thenReturn(true);
        when(bleApi.listClaimedBleDevices(apiClient)).thenReturn(pendingResult);

        setupBaseObservableSuccess(observable);
        Observable.create(observable).subscribe(sub);

        assertSingleValue(sub, bleDeviceList);
    }

    @Test
    public void BleListClaimedDevicesObservable_WithDataType_StatusException() {
        TestSubscriber<List<BleDevice>> sub = new TestSubscriber<>();
        BleListClaimedDevicesObservable observable = spy(new BleListClaimedDevicesObservable(rxFit, dataType));

        BleDevicesResult bleDevicesResult = Mockito.mock(BleDevicesResult.class);

        List<BleDevice> bleDeviceList = new ArrayList<>();
        bleDeviceList.add(bleDevice);

        when(bleDevicesResult.getClaimedBleDevices(dataType)).thenReturn(bleDeviceList);

        setPendingResultValue(bleDevicesResult);
        when(bleDevicesResult.getStatus()).thenReturn(status);
        when(status.isSuccess()).thenReturn(false);
        when(bleApi.listClaimedBleDevices(apiClient)).thenReturn(pendingResult);

        setupBaseObservableSuccess(observable);
        Observable.create(observable).subscribe(sub);

        assertError(sub, StatusException.class);
    }

    @Test
    public void BleListClaimedDevicesObservable_Success() {
        TestSubscriber<List<BleDevice>> sub = new TestSubscriber<>();
        BleListClaimedDevicesObservable observable = spy(new BleListClaimedDevicesObservable(rxFit, null));

        BleDevicesResult bleDevicesResult = Mockito.mock(BleDevicesResult.class);

        List<BleDevice> bleDeviceList = new ArrayList<>();
        bleDeviceList.add(bleDevice);

        when(bleDevicesResult.getClaimedBleDevices()).thenReturn(bleDeviceList);

        setPendingResultValue(bleDevicesResult);
        when(bleDevicesResult.getStatus()).thenReturn(status);
        when(status.isSuccess()).thenReturn(true);
        when(bleApi.listClaimedBleDevices(apiClient)).thenReturn(pendingResult);

        setupBaseObservableSuccess(observable);
        Observable.create(observable).subscribe(sub);

        assertSingleValue(sub, bleDeviceList);
    }

    @Test
    public void BleListClaimedDevicesObservable_StatusException() {
        TestSubscriber<List<BleDevice>> sub = new TestSubscriber<>();
        BleListClaimedDevicesObservable observable = spy(new BleListClaimedDevicesObservable(rxFit, null));

        BleDevicesResult bleDevicesResult = Mockito.mock(BleDevicesResult.class);

        List<BleDevice> bleDeviceList = new ArrayList<>();
        bleDeviceList.add(bleDevice);

        when(bleDevicesResult.getClaimedBleDevices()).thenReturn(bleDeviceList);

        setPendingResultValue(bleDevicesResult);
        when(bleDevicesResult.getStatus()).thenReturn(status);
        when(status.isSuccess()).thenReturn(false);
        when(bleApi.listClaimedBleDevices(apiClient)).thenReturn(pendingResult);

        setupBaseObservableSuccess(observable);
        Observable.create(observable).subscribe(sub);

        assertError(sub, StatusException.class);
    }

    // BleStartScanObservable

    @Test
    public void BleStartScanObservable_Success() {
        TestSubscriber<Status> sub = new TestSubscriber<>();
        StartBleScanRequest startBleScanRequest = Mockito.mock(StartBleScanRequest.class);
        BleStartScanObservable observable = spy(new BleStartScanObservable(rxFit, startBleScanRequest));

        setPendingResultValue(status);
        when(status.isSuccess()).thenReturn(true);
        //noinspection MissingPermission
        when(bleApi.startBleScan(apiClient, startBleScanRequest)).thenReturn(pendingResult);

        setupBaseObservableSuccess(observable);
        Observable.create(observable).subscribe(sub);

        assertSingleValue(sub, status);
    }

    @Test
    public void BleStartScanObservable_StatusException() {
        TestSubscriber<Status> sub = new TestSubscriber<>();
        StartBleScanRequest startBleScanRequest = Mockito.mock(StartBleScanRequest.class);
        BleStartScanObservable observable = spy(new BleStartScanObservable(rxFit, startBleScanRequest));

        setPendingResultValue(status);
        when(status.isSuccess()).thenReturn(false);
        //noinspection MissingPermission
        when(bleApi.startBleScan(apiClient, startBleScanRequest)).thenReturn(pendingResult);

        setupBaseObservableSuccess(observable);
        Observable.create(observable).subscribe(sub);

        assertError(sub, StatusException.class);
    }

    @SuppressWarnings("MissingPermission")
    @Test
    public void BleStartScanObservable_PermissionRequiredException() throws Exception {
        TestSubscriber<Status> sub = new TestSubscriber<>();

        PowerMockito.doThrow(new SecurityException("Missing Bluetooth Admin permission")).when(bleApi).startBleScan(Matchers.any(GoogleApiClient.class), Matchers.any(StartBleScanRequest.class));

        StartBleScanRequest startBleScanRequest = Mockito.mock(StartBleScanRequest.class);
        BleStartScanObservable observable = spy(new BleStartScanObservable(rxFit, startBleScanRequest));

        setupBaseObservableSuccess(observable);
        Observable.create(observable).subscribe(sub);

        assertError(sub, SecurityException.class);
    }

    // BleStopScanObservable

    @Test
    public void BleStopScanObservable_Success() {
        TestSubscriber<Status> sub = new TestSubscriber<>();
        BleScanCallback bleScanCallback = Mockito.mock(BleScanCallback.class);
        BleStopScanObservable observable = spy(new BleStopScanObservable(rxFit, bleScanCallback));

        setPendingResultValue(status);
        when(status.isSuccess()).thenReturn(true);
        //noinspection MissingPermission
        when(bleApi.stopBleScan(apiClient, bleScanCallback)).thenReturn(pendingResult);

        setupBaseObservableSuccess(observable);
        Observable.create(observable).subscribe(sub);

        assertSingleValue(sub, status);
    }

    @Test
    public void BleStopScanObservable_StatusException() {
        TestSubscriber<Status> sub = new TestSubscriber<>();
        BleScanCallback bleScanCallback = Mockito.mock(BleScanCallback.class);
        BleStopScanObservable observable = spy(new BleStopScanObservable(rxFit, bleScanCallback));

        setPendingResultValue(status);
        when(status.isSuccess()).thenReturn(false);
        //noinspection MissingPermission
        when(bleApi.stopBleScan(apiClient, bleScanCallback)).thenReturn(pendingResult);

        setupBaseObservableSuccess(observable);
        Observable.create(observable).subscribe(sub);

        assertError(sub, StatusException.class);
    }


    /**********
     * Config *
     **********/

    // ConfigCreateCustomDataTypeObservable

    @Test
    public void ConfigCreateCustomDataTypeObservable_Success() {
        TestSubscriber<DataType> sub = new TestSubscriber<>();
        DataTypeCreateRequest dataTypeCreateRequest = Mockito.mock(DataTypeCreateRequest.class);
        DataTypeResult dataTypeResult = Mockito.mock(DataTypeResult.class);
        ConfigCreateCustomDataTypeObservable observable = spy(new ConfigCreateCustomDataTypeObservable(rxFit, dataTypeCreateRequest));

        setPendingResultValue(dataTypeResult);
        when(dataTypeResult.getStatus()).thenReturn(status);
        when(dataTypeResult.getDataType()).thenReturn(dataType);
        when(status.isSuccess()).thenReturn(true);
        when(configApi.createCustomDataType(apiClient, dataTypeCreateRequest)).thenReturn(pendingResult);

        setupBaseObservableSuccess(observable);
        Observable.create(observable).subscribe(sub);

        assertSingleValue(sub, dataType);
    }

    @Test
    public void ConfigCreateCustomDataTypeObservable_StatusException() {
        TestSubscriber<DataType> sub = new TestSubscriber<>();
        DataTypeCreateRequest dataTypeCreateRequest = Mockito.mock(DataTypeCreateRequest.class);
        DataTypeResult dataTypeResult = Mockito.mock(DataTypeResult.class);
        ConfigCreateCustomDataTypeObservable observable = spy(new ConfigCreateCustomDataTypeObservable(rxFit, dataTypeCreateRequest));

        setPendingResultValue(dataTypeResult);
        when(dataTypeResult.getStatus()).thenReturn(status);
        when(dataTypeResult.getDataType()).thenReturn(dataType);
        when(status.isSuccess()).thenReturn(false);
        when(configApi.createCustomDataType(apiClient, dataTypeCreateRequest)).thenReturn(pendingResult);

        setupBaseObservableSuccess(observable);
        Observable.create(observable).subscribe(sub);

        assertError(sub, StatusException.class);
    }

    // ConfigDisableFitObservable

    @Test
    public void ConfigDisableFitObservable_Success() {
        TestSubscriber<Status> sub = new TestSubscriber<>();
        ConfigDisableFitObservable observable = spy(new ConfigDisableFitObservable(rxFit));

        setPendingResultValue(status);
        when(status.isSuccess()).thenReturn(true);
        when(configApi.disableFit(apiClient)).thenReturn(pendingResult);

        setupBaseObservableSuccess(observable);
        Observable.create(observable).subscribe(sub);

        assertSingleValue(sub, status);
    }

    @Test
    public void ConfigDisableFitObservable_StatusException() {
        TestSubscriber<Status> sub = new TestSubscriber<>();
        ConfigDisableFitObservable observable = spy(new ConfigDisableFitObservable(rxFit));

        setPendingResultValue(status);
        when(status.isSuccess()).thenReturn(false);
        when(configApi.disableFit(apiClient)).thenReturn(pendingResult);

        setupBaseObservableSuccess(observable);
        Observable.create(observable).subscribe(sub);

        assertError(sub, StatusException.class);
    }

    // ConfigReadDataTypeObservable

    @Test
    public void ConfigReadDataTypeObservable_Success() {
        TestSubscriber<DataType> sub = new TestSubscriber<>();
        String dataTypeName = "dataTypeName";
        DataTypeResult dataTypeResult = Mockito.mock(DataTypeResult.class);
        ConfigReadDataTypeObservable observable = spy(new ConfigReadDataTypeObservable(rxFit, dataTypeName));

        setPendingResultValue(dataTypeResult);
        when(dataTypeResult.getStatus()).thenReturn(status);
        when(dataTypeResult.getDataType()).thenReturn(dataType);
        when(status.isSuccess()).thenReturn(true);
        when(configApi.readDataType(apiClient, dataTypeName)).thenReturn(pendingResult);

        setupBaseObservableSuccess(observable);
        Observable.create(observable).subscribe(sub);

        assertSingleValue(sub, dataType);
    }

    @Test
    public void ConfigReadDataTypeObservable_StatusException() {
        TestSubscriber<DataType> sub = new TestSubscriber<>();
        String dataTypeName = "dataTypeName";
        DataTypeResult dataTypeResult = Mockito.mock(DataTypeResult.class);
        ConfigReadDataTypeObservable observable = spy(new ConfigReadDataTypeObservable(rxFit, dataTypeName));

        setPendingResultValue(dataTypeResult);
        when(dataTypeResult.getStatus()).thenReturn(status);
        when(dataTypeResult.getDataType()).thenReturn(dataType);
        when(status.isSuccess()).thenReturn(false);
        when(configApi.readDataType(apiClient, dataTypeName)).thenReturn(pendingResult);

        setupBaseObservableSuccess(observable);
        Observable.create(observable).subscribe(sub);

        assertError(sub, StatusException.class);
    }

    /***********
     * History *
     ***********/

    // HistoryDeleteDataObservable

    @Test
    public void HistoryDeleteDataObservable_Success() {
        TestSubscriber<Status> sub = new TestSubscriber<>();
        DataDeleteRequest dataDeleteRequest = Mockito.mock(DataDeleteRequest.class);
        HistoryDeleteDataObservable observable = spy(new HistoryDeleteDataObservable(rxFit, dataDeleteRequest));

        setPendingResultValue(status);
        when(status.isSuccess()).thenReturn(true);
        when(historyApi.deleteData(apiClient, dataDeleteRequest)).thenReturn(pendingResult);

        setupBaseObservableSuccess(observable);
        Observable.create(observable).subscribe(sub);

        assertSingleValue(sub, status);
    }

    @Test
    public void HistoryDeleteDataObservable_StatusException() {
        TestSubscriber<Status> sub = new TestSubscriber<>();
        DataDeleteRequest dataDeleteRequest = Mockito.mock(DataDeleteRequest.class);
        HistoryDeleteDataObservable observable = spy(new HistoryDeleteDataObservable(rxFit, dataDeleteRequest));

        setPendingResultValue(status);
        when(status.isSuccess()).thenReturn(false);
        when(historyApi.deleteData(apiClient, dataDeleteRequest)).thenReturn(pendingResult);

        setupBaseObservableSuccess(observable);
        Observable.create(observable).subscribe(sub);

        assertError(sub, StatusException.class);
    }

    // HistoryInsertDataObservable

    @Test
    public void HistoryInsertDataObservable_Success() {
        TestSubscriber<Status> sub = new TestSubscriber<>();
        HistoryInsertDataObservable observable = spy(new HistoryInsertDataObservable(rxFit, dataSet));

        setPendingResultValue(status);
        when(status.isSuccess()).thenReturn(true);
        when(historyApi.insertData(apiClient, dataSet)).thenReturn(pendingResult);

        setupBaseObservableSuccess(observable);
        Observable.create(observable).subscribe(sub);

        assertSingleValue(sub, status);
    }

    @Test
    public void HistoryInsertDataObservable_StatusException() {
        TestSubscriber<Status> sub = new TestSubscriber<>();
        HistoryInsertDataObservable observable = spy(new HistoryInsertDataObservable(rxFit, dataSet));

        setPendingResultValue(status);
        when(status.isSuccess()).thenReturn(false);
        when(historyApi.insertData(apiClient, dataSet)).thenReturn(pendingResult);

        setupBaseObservableSuccess(observable);
        Observable.create(observable).subscribe(sub);

        assertError(sub, StatusException.class);
    }

    // HistoryReadDailyTotalObservable

    @Test
    public void HistoryReadDailyTotalObservable_Success() {
        TestSubscriber<DataSet> sub = new TestSubscriber<>();
        DailyTotalResult dailyTotalResult = Mockito.mock(DailyTotalResult.class);
        HistoryReadDailyTotalObservable observable = spy(new HistoryReadDailyTotalObservable(rxFit, dataType));

        setPendingResultValue(dailyTotalResult);
        when(dailyTotalResult.getStatus()).thenReturn(status);
        when(dailyTotalResult.getTotal()).thenReturn(dataSet);
        when(status.isSuccess()).thenReturn(true);
        when(historyApi.readDailyTotal(apiClient, dataType)).thenReturn(pendingResult);

        setupBaseObservableSuccess(observable);
        Observable.create(observable).subscribe(sub);

        assertSingleValue(sub, dataSet);
    }

    @Test
    public void HistoryReadDailyTotalObservable_StatusException() {
        TestSubscriber<DataSet> sub = new TestSubscriber<>();
        DailyTotalResult dailyTotalResult = Mockito.mock(DailyTotalResult.class);
        HistoryReadDailyTotalObservable observable = spy(new HistoryReadDailyTotalObservable(rxFit, dataType));

        setPendingResultValue(dailyTotalResult);
        when(dailyTotalResult.getStatus()).thenReturn(status);
        when(dailyTotalResult.getTotal()).thenReturn(dataSet);
        when(status.isSuccess()).thenReturn(false);
        when(historyApi.readDailyTotal(apiClient, dataType)).thenReturn(pendingResult);

        setupBaseObservableSuccess(observable);
        Observable.create(observable).subscribe(sub);

        assertError(sub, StatusException.class);
    }

    // HistoryReadDataObservable

    @Test
    public void HistoryReadDataObservable_Success() {
        TestSubscriber<DataReadResult> sub = new TestSubscriber<>();
        DataReadRequest dataReadRequest = Mockito.mock(DataReadRequest.class);
        DataReadResult dataReadResult = Mockito.mock(DataReadResult.class);
        HistoryReadDataObservable observable = spy(new HistoryReadDataObservable(rxFit, dataReadRequest));

        setPendingResultValue(dataReadResult);
        when(dataReadResult.getStatus()).thenReturn(status);
        when(status.isSuccess()).thenReturn(true);
        when(historyApi.readData(apiClient, dataReadRequest)).thenReturn(pendingResult);

        setupBaseObservableSuccess(observable);
        Observable.create(observable).subscribe(sub);

        assertSingleValue(sub, dataReadResult);
    }

    @Test
    public void HistoryReadDataObservable_StatusException() {
        TestSubscriber<DataReadResult> sub = new TestSubscriber<>();
        DataReadRequest dataReadRequest = Mockito.mock(DataReadRequest.class);
        DataReadResult dataReadResult = Mockito.mock(DataReadResult.class);
        HistoryReadDataObservable observable = spy(new HistoryReadDataObservable(rxFit, dataReadRequest));

        setPendingResultValue(dataReadResult);
        when(dataReadResult.getStatus()).thenReturn(status);
        when(status.isSuccess()).thenReturn(false);
        when(historyApi.readData(apiClient, dataReadRequest)).thenReturn(pendingResult);

        setupBaseObservableSuccess(observable);
        Observable.create(observable).subscribe(sub);

        assertError(sub, StatusException.class);
    }

    // HistoryUpdateDataObservable

    @Test
    public void HistoryUpdateDataObservable_Success() {
        TestSubscriber<Status> sub = new TestSubscriber<>();
        DataUpdateRequest dataUpdateRequest = Mockito.mock(DataUpdateRequest.class);
        HistoryUpdateDataObservable observable = spy(new HistoryUpdateDataObservable(rxFit, dataUpdateRequest));

        setPendingResultValue(status);
        when(status.isSuccess()).thenReturn(true);
        when(historyApi.updateData(apiClient, dataUpdateRequest)).thenReturn(pendingResult);

        setupBaseObservableSuccess(observable);
        Observable.create(observable).subscribe(sub);

        assertSingleValue(sub, status);
    }

    @Test
    public void HistoryUpdateDataObservable_StatusException() {
        TestSubscriber<Status> sub = new TestSubscriber<>();
        DataUpdateRequest dataUpdateRequest = Mockito.mock(DataUpdateRequest.class);
        HistoryUpdateDataObservable observable = spy(new HistoryUpdateDataObservable(rxFit, dataUpdateRequest));

        setPendingResultValue(status);
        when(status.isSuccess()).thenReturn(false);
        when(historyApi.updateData(apiClient, dataUpdateRequest)).thenReturn(pendingResult);

        setupBaseObservableSuccess(observable);
        Observable.create(observable).subscribe(sub);

        assertError(sub, StatusException.class);
    }


    /*************
     * Recording *
     *************/

    // RecordingListSubscriptionsObservable

    @Test
    public void RecordingListSubscriptionsObservable_Success() {
        TestSubscriber<List<Subscription>> sub = new TestSubscriber<>();
        RecordingListSubscriptionsObservable observable = spy(new RecordingListSubscriptionsObservable(rxFit, null));

        ListSubscriptionsResult listSubscriptionsResult = Mockito.mock(ListSubscriptionsResult.class);

        List<Subscription> subscriptionList = new ArrayList<>();
        subscriptionList.add(Mockito.mock(Subscription.class));

        when(listSubscriptionsResult.getSubscriptions()).thenReturn(subscriptionList);

        setPendingResultValue(listSubscriptionsResult);
        when(listSubscriptionsResult.getStatus()).thenReturn(status);
        when(status.isSuccess()).thenReturn(true);
        when(recordingApi.listSubscriptions(apiClient)).thenReturn(pendingResult);

        setupBaseObservableSuccess(observable);
        Observable.create(observable).subscribe(sub);

        assertSingleValue(sub, subscriptionList);
    }

    @Test
    public void RecordingListSubscriptionsObservable_StatusException() {
        TestSubscriber<List<Subscription>> sub = new TestSubscriber<>();
        RecordingListSubscriptionsObservable observable = spy(new RecordingListSubscriptionsObservable(rxFit, null));

        ListSubscriptionsResult listSubscriptionsResult = Mockito.mock(ListSubscriptionsResult.class);

        List<Subscription> subscriptionList = new ArrayList<>();
        subscriptionList.add(Mockito.mock(Subscription.class));

        when(listSubscriptionsResult.getSubscriptions()).thenReturn(subscriptionList);

        setPendingResultValue(listSubscriptionsResult);
        when(listSubscriptionsResult.getStatus()).thenReturn(status);
        when(status.isSuccess()).thenReturn(false);
        when(recordingApi.listSubscriptions(apiClient)).thenReturn(pendingResult);

        setupBaseObservableSuccess(observable);
        Observable.create(observable).subscribe(sub);

        assertError(sub, StatusException.class);
    }

    @Test
    public void RecordingListSubscriptionsObservable_WithDataType_Success() {
        TestSubscriber<List<Subscription>> sub = new TestSubscriber<>();
        RecordingListSubscriptionsObservable observable = spy(new RecordingListSubscriptionsObservable(rxFit, dataType));

        ListSubscriptionsResult listSubscriptionsResult = Mockito.mock(ListSubscriptionsResult.class);

        List<Subscription> subscriptionList = new ArrayList<>();
        subscriptionList.add(Mockito.mock(Subscription.class));

        when(listSubscriptionsResult.getSubscriptions()).thenReturn(subscriptionList);

        setPendingResultValue(listSubscriptionsResult);
        when(listSubscriptionsResult.getStatus()).thenReturn(status);
        when(status.isSuccess()).thenReturn(true);
        when(recordingApi.listSubscriptions(apiClient, dataType)).thenReturn(pendingResult);

        setupBaseObservableSuccess(observable);
        Observable.create(observable).subscribe(sub);

        assertSingleValue(sub, subscriptionList);
    }

    @Test
    public void RecordingListSubscriptionsObservable_WithDataType_StatusException() {
        TestSubscriber<List<Subscription>> sub = new TestSubscriber<>();
        RecordingListSubscriptionsObservable observable = spy(new RecordingListSubscriptionsObservable(rxFit, dataType));

        ListSubscriptionsResult listSubscriptionsResult = Mockito.mock(ListSubscriptionsResult.class);

        List<Subscription> subscriptionList = new ArrayList<>();
        subscriptionList.add(subscription);

        when(listSubscriptionsResult.getSubscriptions()).thenReturn(subscriptionList);

        setPendingResultValue(listSubscriptionsResult);
        when(listSubscriptionsResult.getStatus()).thenReturn(status);
        when(status.isSuccess()).thenReturn(false);
        when(recordingApi.listSubscriptions(apiClient, dataType)).thenReturn(pendingResult);

        setupBaseObservableSuccess(observable);
        Observable.create(observable).subscribe(sub);

        assertError(sub, StatusException.class);
    }

    // RecordingSubscribeObservable

    @Test
    public void RecordingSubscribeObservable_DataType_Success() {
        TestSubscriber<Status> sub = new TestSubscriber<>();
        RecordingSubscribeObservable observable = spy(new RecordingSubscribeObservable(rxFit, null, dataType));

        setPendingResultValue(status);
        when(status.isSuccess()).thenReturn(true);
        when(recordingApi.subscribe(apiClient, dataType)).thenReturn(pendingResult);

        setupBaseObservableSuccess(observable);
        Observable.create(observable).subscribe(sub);

        assertSingleValue(sub, status);
    }


    @Test
    public void RecordingSubscribeObservable_DataType_StatusException() {
        TestSubscriber<Status> sub = new TestSubscriber<>();
        RecordingSubscribeObservable observable = spy(new RecordingSubscribeObservable(rxFit, null, dataType));

        setPendingResultValue(status);
        when(status.isSuccess()).thenReturn(false);
        when(recordingApi.subscribe(apiClient, dataType)).thenReturn(pendingResult);

        setupBaseObservableSuccess(observable);
        Observable.create(observable).subscribe(sub);

        assertError(sub, StatusException.class);
    }

    @Test
    public void RecordingSubscribeObservable_DataSource_Success() {
        TestSubscriber<Status> sub = new TestSubscriber<>();
        RecordingSubscribeObservable observable = spy(new RecordingSubscribeObservable(rxFit, dataSource, null));

        setPendingResultValue(status);
        when(status.isSuccess()).thenReturn(true);
        when(recordingApi.subscribe(apiClient, dataSource)).thenReturn(pendingResult);

        setupBaseObservableSuccess(observable);
        Observable.create(observable).subscribe(sub);

        assertSingleValue(sub, status);
    }


    @Test
    public void RecordingSubscribeObservable_DataSource_StatusException() {
        TestSubscriber<Status> sub = new TestSubscriber<>();
        RecordingSubscribeObservable observable = spy(new RecordingSubscribeObservable(rxFit, dataSource, null));

        setPendingResultValue(status);
        when(status.isSuccess()).thenReturn(false);
        when(recordingApi.subscribe(apiClient, dataSource)).thenReturn(pendingResult);

        setupBaseObservableSuccess(observable);
        Observable.create(observable).subscribe(sub);

        assertError(sub, StatusException.class);
    }

    // RecordingSubscribeObservable

    @Test
    public void RecordingUnsubscribeObservable_DataType_Success() {
        TestSubscriber<Status> sub = new TestSubscriber<>();
        RecordingUnsubscribeObservable observable = spy(new RecordingUnsubscribeObservable(rxFit, null, dataType, null));

        setPendingResultValue(status);
        when(status.isSuccess()).thenReturn(true);
        when(recordingApi.unsubscribe(apiClient, dataType)).thenReturn(pendingResult);

        setupBaseObservableSuccess(observable);
        Observable.create(observable).subscribe(sub);

        assertSingleValue(sub, status);
    }


    @Test
    public void RecordingUnsubscribeObservable_DataType_StatusException() {
        TestSubscriber<Status> sub = new TestSubscriber<>();
        RecordingUnsubscribeObservable observable = spy(new RecordingUnsubscribeObservable(rxFit, null, dataType, null));

        setPendingResultValue(status);
        when(status.isSuccess()).thenReturn(false);
        when(recordingApi.unsubscribe(apiClient, dataType)).thenReturn(pendingResult);

        setupBaseObservableSuccess(observable);
        Observable.create(observable).subscribe(sub);

        assertError(sub, StatusException.class);
    }

    @Test
    public void RecordingUnsubscribeObservable_DataSource_Success() {
        TestSubscriber<Status> sub = new TestSubscriber<>();
        RecordingUnsubscribeObservable observable = spy(new RecordingUnsubscribeObservable(rxFit, dataSource, null, null));

        setPendingResultValue(status);
        when(status.isSuccess()).thenReturn(true);
        when(recordingApi.unsubscribe(apiClient, dataSource)).thenReturn(pendingResult);

        setupBaseObservableSuccess(observable);
        Observable.create(observable).subscribe(sub);

        assertSingleValue(sub, status);
    }

    @Test
    public void RecordingUnsubscribeObservable_DataSource_StatusException() {
        TestSubscriber<Status> sub = new TestSubscriber<>();
        RecordingUnsubscribeObservable observable = spy(new RecordingUnsubscribeObservable(rxFit, dataSource, null, null));

        setPendingResultValue(status);
        when(status.isSuccess()).thenReturn(false);
        when(recordingApi.unsubscribe(apiClient, dataSource)).thenReturn(pendingResult);

        setupBaseObservableSuccess(observable);
        Observable.create(observable).subscribe(sub);

        assertError(sub, StatusException.class);
    }

    @Test
    public void RecordingUnsubscribeObservable_Subscription_Success() {
        TestSubscriber<Status> sub = new TestSubscriber<>();
        RecordingUnsubscribeObservable observable = spy(new RecordingUnsubscribeObservable(rxFit, null, null, subscription));

        setPendingResultValue(status);
        when(status.isSuccess()).thenReturn(true);
        when(recordingApi.unsubscribe(apiClient, subscription)).thenReturn(pendingResult);

        setupBaseObservableSuccess(observable);
        Observable.create(observable).subscribe(sub);

        assertSingleValue(sub, status);
    }

    @Test
    public void RecordingUnsubscribeObservable_Subscription_StatusException() {
        TestSubscriber<Status> sub = new TestSubscriber<>();
        RecordingUnsubscribeObservable observable = spy(new RecordingUnsubscribeObservable(rxFit, null, null, subscription));

        setPendingResultValue(status);
        when(status.isSuccess()).thenReturn(false);
        when(recordingApi.unsubscribe(apiClient, subscription)).thenReturn(pendingResult);

        setupBaseObservableSuccess(observable);
        Observable.create(observable).subscribe(sub);

        assertError(sub, StatusException.class);
    }


    /***********
     * Sensors *
     ***********/

    // SensorsAddDataPointIntentObservable

    @Test
    public void SensorsAddDataPointIntentObservable_Success() {
        TestSubscriber<Status> sub = new TestSubscriber<>();
        PendingIntent pendingIntent = Mockito.mock(PendingIntent.class);
        SensorsAddDataPointIntentObservable observable = spy(new SensorsAddDataPointIntentObservable(rxFit, sensorRequest, pendingIntent));

        setPendingResultValue(status);
        when(status.isSuccess()).thenReturn(true);
        when(sensorsApi.add(apiClient, sensorRequest, pendingIntent)).thenReturn(pendingResult);

        setupBaseObservableSuccess(observable);
        Observable.create(observable).subscribe(sub);

        assertSingleValue(sub, status);
    }

    @Test
    public void SensorsAddDataPointIntentObservable_StatusException() {
        TestSubscriber<Status> sub = new TestSubscriber<>();
        PendingIntent pendingIntent = Mockito.mock(PendingIntent.class);
        SensorsAddDataPointIntentObservable observable = spy(new SensorsAddDataPointIntentObservable(rxFit, sensorRequest, pendingIntent));

        setPendingResultValue(status);
        when(status.isSuccess()).thenReturn(false);
        when(sensorsApi.add(apiClient, sensorRequest, pendingIntent)).thenReturn(pendingResult);

        setupBaseObservableSuccess(observable);
        Observable.create(observable).subscribe(sub);

        assertError(sub, StatusException.class);
    }


    // SensorsRemoveDataPointIntentObservable

    @Test
    public void SensorsRemoveDataPointIntentObservable_Success() {
        TestSubscriber<Status> sub = new TestSubscriber<>();
        PendingIntent pendingIntent = Mockito.mock(PendingIntent.class);
        SensorsRemoveDataPointIntentObservable observable = spy(new SensorsRemoveDataPointIntentObservable(rxFit, pendingIntent));

        setPendingResultValue(status);
        when(status.isSuccess()).thenReturn(true);
        when(sensorsApi.remove(apiClient, pendingIntent)).thenReturn(pendingResult);

        setupBaseObservableSuccess(observable);
        Observable.create(observable).subscribe(sub);

        assertSingleValue(sub, status);
    }

    @Test
    public void SensorsRemoveDataPointIntentObservable_StatusException() {
        TestSubscriber<Status> sub = new TestSubscriber<>();
        PendingIntent pendingIntent = Mockito.mock(PendingIntent.class);
        SensorsRemoveDataPointIntentObservable observable = spy(new SensorsRemoveDataPointIntentObservable(rxFit, pendingIntent));

        setPendingResultValue(status);
        when(status.isSuccess()).thenReturn(false);
        when(sensorsApi.remove(apiClient, pendingIntent)).thenReturn(pendingResult);

        setupBaseObservableSuccess(observable);
        Observable.create(observable).subscribe(sub);

        assertError(sub, StatusException.class);
    }

    // SensorsDataPointObservable

    @Test
    public void SensorsDataPointObservable_Success() {
        TestSubscriber<DataPoint> sub = new TestSubscriber<>();
        DataPoint dataPoint = Mockito.mock(DataPoint.class);
        SensorsDataPointObservable observable = spy(new SensorsDataPointObservable(rxFit, sensorRequest));

        setPendingResultValue(status);
        when(status.isSuccess()).thenReturn(true);
        when(sensorsApi.add(Matchers.any(GoogleApiClient.class), Matchers.any(SensorRequest.class), Matchers.any(OnDataPointListener.class))).thenReturn(pendingResult);
        when(apiClient.isConnected()).thenReturn(true);

        setupBaseObservableSuccess(observable);
        Observable.create(observable).subscribe(sub);

        observable.subscriber.onNext(dataPoint);

        verify(sensorsApi, never()).remove(Matchers.any(GoogleApiClient.class), Matchers.any(OnDataPointListener.class));
        sub.unsubscribe();
        verify(sensorsApi).remove(Matchers.any(GoogleApiClient.class), Matchers.any(OnDataPointListener.class));

        sub.assertNoTerminalEvent();
        sub.assertValue(dataPoint);
    }

    @Test
    public void SensorsDataPointObservable_StatusException() {
        TestSubscriber<DataPoint> sub = new TestSubscriber<>();
        SensorsDataPointObservable observable = spy(new SensorsDataPointObservable(rxFit, sensorRequest));

        setPendingResultValue(status);
        when(status.isSuccess()).thenReturn(false);
        when(sensorsApi.add(Matchers.any(GoogleApiClient.class), Matchers.any(SensorRequest.class), Matchers.any(OnDataPointListener.class))).thenReturn(pendingResult);

        setupBaseObservableSuccess(observable);
        Observable.create(observable).subscribe(sub);

        assertError(sub, StatusException.class);
    }

    // SensorsFindDataSourcesObservable

    @Test
    public void SensorsFindDataSourcesObservable_Success() {
        TestSubscriber<List<DataSource>> sub = new TestSubscriber<>();
        DataSourcesRequest dataSourcesRequest = Mockito.mock(DataSourcesRequest.class);
        DataSourcesResult dataSourcesResult = Mockito.mock(DataSourcesResult.class);
        SensorsFindDataSourcesObservable observable = spy(new SensorsFindDataSourcesObservable(rxFit, dataSourcesRequest, null));

        List<DataSource> dataSourceList = new ArrayList<>();
        dataSourceList.add(dataSource);

        when(dataSourcesResult.getDataSources()).thenReturn(dataSourceList);

        setPendingResultValue(dataSourcesResult);
        when(dataSourcesResult.getStatus()).thenReturn(status);
        when(status.isSuccess()).thenReturn(true);
        when(sensorsApi.findDataSources(apiClient, dataSourcesRequest)).thenReturn(pendingResult);

        setupBaseObservableSuccess(observable);
        Observable.create(observable).subscribe(sub);

        assertSingleValue(sub, dataSourceList);
    }

    @Test
    public void SensorsFindDataSourcesObservable_StatusException() {
        TestSubscriber<List<DataSource>> sub = new TestSubscriber<>();
        DataSourcesRequest dataSourcesRequest = Mockito.mock(DataSourcesRequest.class);
        DataSourcesResult dataSourcesResult = Mockito.mock(DataSourcesResult.class);
        SensorsFindDataSourcesObservable observable = spy(new SensorsFindDataSourcesObservable(rxFit, dataSourcesRequest, null));

        List<DataSource> dataSourceList = new ArrayList<>();
        dataSourceList.add(dataSource);

        when(dataSourcesResult.getDataSources()).thenReturn(dataSourceList);

        setPendingResultValue(dataSourcesResult);
        when(dataSourcesResult.getStatus()).thenReturn(status);
        when(status.isSuccess()).thenReturn(false);
        when(sensorsApi.findDataSources(apiClient, dataSourcesRequest)).thenReturn(pendingResult);

        setupBaseObservableSuccess(observable);
        Observable.create(observable).subscribe(sub);

        assertError(sub, StatusException.class);
    }

    @Test
    public void SensorsFindDataSourcesObservable_WithDataType_Success() {
        TestSubscriber<List<DataSource>> sub = new TestSubscriber<>();
        DataSourcesRequest dataSourcesRequest = Mockito.mock(DataSourcesRequest.class);
        DataSourcesResult dataSourcesResult = Mockito.mock(DataSourcesResult.class);
        SensorsFindDataSourcesObservable observable = spy(new SensorsFindDataSourcesObservable(rxFit, dataSourcesRequest, dataType));

        List<DataSource> dataSourceList = new ArrayList<>();
        dataSourceList.add(dataSource);

        when(dataSourcesResult.getDataSources(dataType)).thenReturn(dataSourceList);

        setPendingResultValue(dataSourcesResult);
        when(dataSourcesResult.getStatus()).thenReturn(status);
        when(status.isSuccess()).thenReturn(true);
        when(sensorsApi.findDataSources(apiClient, dataSourcesRequest)).thenReturn(pendingResult);

        setupBaseObservableSuccess(observable);
        Observable.create(observable).subscribe(sub);

        assertSingleValue(sub, dataSourceList);
    }

    @Test
    public void SensorsFindDataSourcesObservable_WithDataType_StatusException() {
        TestSubscriber<List<DataSource>> sub = new TestSubscriber<>();
        DataSourcesRequest dataSourcesRequest = Mockito.mock(DataSourcesRequest.class);
        DataSourcesResult dataSourcesResult = Mockito.mock(DataSourcesResult.class);
        SensorsFindDataSourcesObservable observable = spy(new SensorsFindDataSourcesObservable(rxFit, dataSourcesRequest, dataType));

        List<DataSource> dataSourceList = new ArrayList<>();
        dataSourceList.add(dataSource);

        when(dataSourcesResult.getDataSources(dataType)).thenReturn(dataSourceList);

        setPendingResultValue(dataSourcesResult);
        when(dataSourcesResult.getStatus()).thenReturn(status);
        when(status.isSuccess()).thenReturn(false);
        when(sensorsApi.findDataSources(apiClient, dataSourcesRequest)).thenReturn(pendingResult);

        setupBaseObservableSuccess(observable);
        Observable.create(observable).subscribe(sub);

        assertError(sub, StatusException.class);
    }


    /************
     * Sessions *
     ************/

    // SessionInsertObservable

    @Test
    public void SessionInsertObservable_Success() {
        TestSubscriber<Status> sub = new TestSubscriber<>();
        SessionInsertRequest sessionInsertRequest = Mockito.mock(SessionInsertRequest.class);
        SessionInsertObservable observable = spy(new SessionInsertObservable(rxFit, sessionInsertRequest));

        setPendingResultValue(status);
        when(status.isSuccess()).thenReturn(true);
        when(sessionsApi.insertSession(apiClient, sessionInsertRequest)).thenReturn(pendingResult);

        setupBaseObservableSuccess(observable);
        Observable.create(observable).subscribe(sub);

        assertSingleValue(sub, status);
    }

    @Test
    public void SessionInsertObservable_StatusException() {
        TestSubscriber<Status> sub = new TestSubscriber<>();
        SessionInsertRequest sessionInsertRequest = Mockito.mock(SessionInsertRequest.class);
        SessionInsertObservable observable = spy(new SessionInsertObservable(rxFit, sessionInsertRequest));

        setPendingResultValue(status);
        when(status.isSuccess()).thenReturn(false);
        when(sessionsApi.insertSession(apiClient, sessionInsertRequest)).thenReturn(pendingResult);

        setupBaseObservableSuccess(observable);
        Observable.create(observable).subscribe(sub);

        assertError(sub, StatusException.class);
    }

    // SessionRegisterObservable

    @Test
    public void SessionRegisterObservable_Success() {
        TestSubscriber<Status> sub = new TestSubscriber<>();
        PendingIntent pendingIntent = Mockito.mock(PendingIntent.class);
        SessionRegisterObservable observable = spy(new SessionRegisterObservable(rxFit, pendingIntent));

        setPendingResultValue(status);
        when(status.isSuccess()).thenReturn(true);
        when(sessionsApi.registerForSessions(apiClient, pendingIntent)).thenReturn(pendingResult);

        setupBaseObservableSuccess(observable);
        Observable.create(observable).subscribe(sub);

        assertSingleValue(sub, status);
    }

    @Test
    public void SessionRegisterObservable_StatusException() {
        TestSubscriber<Status> sub = new TestSubscriber<>();
        PendingIntent pendingIntent = Mockito.mock(PendingIntent.class);
        SessionRegisterObservable observable = spy(new SessionRegisterObservable(rxFit, pendingIntent));

        setPendingResultValue(status);
        when(status.isSuccess()).thenReturn(false);
        when(sessionsApi.registerForSessions(apiClient, pendingIntent)).thenReturn(pendingResult);

        setupBaseObservableSuccess(observable);
        Observable.create(observable).subscribe(sub);

        assertError(sub, StatusException.class);
    }

    // SessionUnregisterObservable

    @Test
    public void SessionUnregisterObservable_Success() {
        TestSubscriber<Status> sub = new TestSubscriber<>();
        PendingIntent pendingIntent = Mockito.mock(PendingIntent.class);
        SessionUnregisterObservable observable = spy(new SessionUnregisterObservable(rxFit, pendingIntent));

        setPendingResultValue(status);
        when(status.isSuccess()).thenReturn(true);
        when(sessionsApi.unregisterForSessions(apiClient, pendingIntent)).thenReturn(pendingResult);

        setupBaseObservableSuccess(observable);
        Observable.create(observable).subscribe(sub);

        assertSingleValue(sub, status);
    }

    @Test
    public void SessionUnregisterObservable_StatusException() {
        TestSubscriber<Status> sub = new TestSubscriber<>();
        PendingIntent pendingIntent = Mockito.mock(PendingIntent.class);
        SessionUnregisterObservable observable = spy(new SessionUnregisterObservable(rxFit, pendingIntent));

        setPendingResultValue(status);
        when(status.isSuccess()).thenReturn(false);
        when(sessionsApi.unregisterForSessions(apiClient, pendingIntent)).thenReturn(pendingResult);

        setupBaseObservableSuccess(observable);
        Observable.create(observable).subscribe(sub);

        assertError(sub, StatusException.class);
    }

    // SessionStartObservable

    @Test
    public void SessionStartObservable_Success() {
        TestSubscriber<Status> sub = new TestSubscriber<>();
        Session session = Mockito.mock(Session.class);
        SessionStartObservable observable = spy(new SessionStartObservable(rxFit, session));

        setPendingResultValue(status);
        when(status.isSuccess()).thenReturn(true);
        when(sessionsApi.startSession(apiClient, session)).thenReturn(pendingResult);

        setupBaseObservableSuccess(observable);
        Observable.create(observable).subscribe(sub);

        assertSingleValue(sub, status);
    }

    @Test
    public void SessionStartObservable_StatusException() {
        TestSubscriber<Status> sub = new TestSubscriber<>();
        SessionStartObservable observable = spy(new SessionStartObservable(rxFit, session));

        setPendingResultValue(status);
        when(status.isSuccess()).thenReturn(false);
        when(sessionsApi.startSession(apiClient, session)).thenReturn(pendingResult);

        setupBaseObservableSuccess(observable);
        Observable.create(observable).subscribe(sub);

        assertError(sub, StatusException.class);
    }

    // SessionStopObservable

    @Test
    public void SessionStopObservable_Success() {
        TestSubscriber<List<Session>> sub = new TestSubscriber<>();
        String identifier = "identifier";
        SessionStopResult sessionStopResult = Mockito.mock(SessionStopResult.class);
        SessionStopObservable observable = spy(new SessionStopObservable(rxFit, identifier));

        List<Session> sessionList = new ArrayList<>();
        sessionList.add(session);

        when(sessionStopResult.getSessions()).thenReturn(sessionList);

        setPendingResultValue(sessionStopResult);
        when(sessionStopResult.getStatus()).thenReturn(status);
        when(status.isSuccess()).thenReturn(true);
        when(sessionsApi.stopSession(apiClient, identifier)).thenReturn(pendingResult);

        setupBaseObservableSuccess(observable);
        Observable.create(observable).subscribe(sub);

        assertSingleValue(sub, sessionList);
    }

    @Test
    public void SessionStopObservable_StatusException() {
        TestSubscriber<List<Session>> sub = new TestSubscriber<>();
        String identifier = "identifier";
        SessionStopResult sessionStopResult = Mockito.mock(SessionStopResult.class);
        SessionStopObservable observable = spy(new SessionStopObservable(rxFit, identifier));

        List<Session> sessionList = new ArrayList<>();
        sessionList.add(session);

        when(sessionStopResult.getSessions()).thenReturn(sessionList);

        setPendingResultValue(sessionStopResult);
        when(sessionStopResult.getStatus()).thenReturn(status);
        when(status.isSuccess()).thenReturn(false);
        when(sessionsApi.stopSession(apiClient, identifier)).thenReturn(pendingResult);

        setupBaseObservableSuccess(observable);
        Observable.create(observable).subscribe(sub);

        assertError(sub, StatusException.class);
    }

    // SessionReadObservable

    @Test
    public void SessionReadObservable_Success() {
        TestSubscriber<SessionReadResult> sub = new TestSubscriber<>();
        SessionReadRequest sessionReadRequest = Mockito.mock(SessionReadRequest.class);
        SessionReadResult sessionReadResult = Mockito.mock(SessionReadResult.class);
        SessionReadObservable observable = spy(new SessionReadObservable(rxFit, sessionReadRequest));

        setPendingResultValue(sessionReadResult);
        when(sessionReadResult.getStatus()).thenReturn(status);
        when(status.isSuccess()).thenReturn(true);
        when(sessionsApi.readSession(apiClient, sessionReadRequest)).thenReturn(pendingResult);

        setupBaseObservableSuccess(observable);
        Observable.create(observable).subscribe(sub);

        assertSingleValue(sub, sessionReadResult);
    }

    @Test
    public void SessionReadObservable_StatusException() {
        TestSubscriber<SessionReadResult> sub = new TestSubscriber<>();
        SessionReadRequest sessionReadRequest = Mockito.mock(SessionReadRequest.class);
        SessionReadResult sessionReadResult = Mockito.mock(SessionReadResult.class);
        SessionReadObservable observable = spy(new SessionReadObservable(rxFit, sessionReadRequest));

        setPendingResultValue(sessionReadResult);
        when(sessionReadResult.getStatus()).thenReturn(status);
        when(status.isSuccess()).thenReturn(false);
        when(sessionsApi.readSession(apiClient, sessionReadRequest)).thenReturn(pendingResult);

        setupBaseObservableSuccess(observable);
        Observable.create(observable).subscribe(sub);

        assertError(sub, StatusException.class);
    }

}
