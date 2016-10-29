package com.patloew.rxfit;

import android.support.v4.content.ContextCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.BleDevice;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.request.BleScanCallback;
import com.google.android.gms.fitness.request.StartBleScanRequest;
import com.google.android.gms.fitness.result.BleDevicesResult;

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
@PrepareOnlyThisForTest({ ContextCompat.class, Fitness.class, Status.class, ConnectionResult.class, DataType.class, BaseRx.class })
public class BleOnSubscribeTest extends BaseOnSubscribeTest {

    @Mock BleDevice bleDevice;
    @Mock DataType dataType;

    @Override
    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        super.setup();
    }

    // BleClaimDeviceObservable

    @Test
    public void BleClaimDeviceObservable_BleDevice_Success() {
        BleClaimDeviceSingle single = PowerMockito.spy(new BleClaimDeviceSingle(rxFit, bleDevice, null, null, null));

        setPendingResultValue(status);
        when(status.isSuccess()).thenReturn(true);
        when(bleApi.claimBleDevice(apiClient, bleDevice)).thenReturn(pendingResult);

        setupBaseSingleSuccess(single);

        assertSingleValue(Single.create(single).test(), status);
    }

    @Test
    public void BleClaimDeviceObservable_DeviceAddress_Success() {
        String deviceAddress = "deviceAddress";
        BleClaimDeviceSingle single = PowerMockito.spy(new BleClaimDeviceSingle(rxFit, null, deviceAddress, null, null));

        setPendingResultValue(status);
        when(status.isSuccess()).thenReturn(true);
        when(bleApi.claimBleDevice(apiClient, deviceAddress)).thenReturn(pendingResult);

        setupBaseSingleSuccess(single);

        assertSingleValue(Single.create(single).test(), status);
    }

    @Test
    public void BleClaimDeviceObservable_BleDevice_StatusException() {
        BleClaimDeviceSingle single = PowerMockito.spy(new BleClaimDeviceSingle(rxFit, bleDevice, null, null, null));

        setPendingResultValue(status);
        when(status.isSuccess()).thenReturn(false);
        when(bleApi.claimBleDevice(apiClient, bleDevice)).thenReturn(pendingResult);

        setupBaseSingleSuccess(single);

        assertError(Single.create(single).test(), StatusException.class);
    }

    @Test
    public void BleClaimDeviceObservable_DeviceAddress_StatusException() {
        String deviceAddress = "deviceAddress";
        BleClaimDeviceSingle single = PowerMockito.spy(new BleClaimDeviceSingle(rxFit, null, deviceAddress, null, null));

        setPendingResultValue(status);
        when(status.isSuccess()).thenReturn(false);
        when(bleApi.claimBleDevice(apiClient, deviceAddress)).thenReturn(pendingResult);

        setupBaseSingleSuccess(single);

        assertError(Single.create(single).test(), StatusException.class);
    }


    // BleUnclaimDeviceObservable

    @Test
    public void BleUnclaimDeviceObservable_BleDevice_Success() {
        BleUnclaimDeviceSingle single = PowerMockito.spy(new BleUnclaimDeviceSingle(rxFit, bleDevice, null, null, null));

        setPendingResultValue(status);
        when(status.isSuccess()).thenReturn(true);
        when(bleApi.unclaimBleDevice(apiClient, bleDevice)).thenReturn(pendingResult);

        setupBaseSingleSuccess(single);

        assertSingleValue(Single.create(single).test(), status);
    }

    @Test
    public void BleUnclaimDeviceObservable_DeviceAddress_Success() {
        String deviceAddress = "deviceAddress";
        BleUnclaimDeviceSingle single = PowerMockito.spy(new BleUnclaimDeviceSingle(rxFit, null, deviceAddress, null, null));

        setPendingResultValue(status);
        when(status.isSuccess()).thenReturn(true);
        when(bleApi.unclaimBleDevice(apiClient, deviceAddress)).thenReturn(pendingResult);

        setupBaseSingleSuccess(single);

        assertSingleValue(Single.create(single).test(), status);
    }

    @Test
    public void BleUnclaimDeviceObservable_BleDevice_StatusException() {
        BleUnclaimDeviceSingle single = PowerMockito.spy(new BleUnclaimDeviceSingle(rxFit, bleDevice, null, null, null));

        setPendingResultValue(status);
        when(status.isSuccess()).thenReturn(false);
        when(bleApi.unclaimBleDevice(apiClient, bleDevice)).thenReturn(pendingResult);

        setupBaseSingleSuccess(single);

        assertError(Single.create(single).test(), StatusException.class);
    }

    @Test
    public void BleUnclaimDeviceObservable_DeviceAddress_StatusException() {
        String deviceAddress = "deviceAddress";
        BleUnclaimDeviceSingle single = PowerMockito.spy(new BleUnclaimDeviceSingle(rxFit, null, deviceAddress, null, null));

        setPendingResultValue(status);
        when(status.isSuccess()).thenReturn(false);
        when(bleApi.unclaimBleDevice(apiClient, deviceAddress)).thenReturn(pendingResult);

        setupBaseSingleSuccess(single);

        assertError(Single.create(single).test(), StatusException.class);
    }

    // BleListClaimedDevicesObservable

    @Test
    public void BleListClaimedDevicesObservable_WithDataType_Success() {
        BleListClaimedDevicesSingle single = PowerMockito.spy(new BleListClaimedDevicesSingle(rxFit, dataType, null, null));

        BleDevicesResult bleDevicesResult = Mockito.mock(BleDevicesResult.class);

        List<BleDevice> bleDeviceList = new ArrayList<>();
        bleDeviceList.add(bleDevice);

        when(bleDevicesResult.getClaimedBleDevices(dataType)).thenReturn(bleDeviceList);

        setPendingResultValue(bleDevicesResult);
        when(bleDevicesResult.getStatus()).thenReturn(status);
        when(status.isSuccess()).thenReturn(true);
        when(bleApi.listClaimedBleDevices(apiClient)).thenReturn(pendingResult);

        setupBaseSingleSuccess(single);

        assertSingleValue(Single.create(single).test(), bleDeviceList);
    }

    @Test
    public void BleListClaimedDevicesObservable_WithDataType_StatusException() {
        BleListClaimedDevicesSingle single = PowerMockito.spy(new BleListClaimedDevicesSingle(rxFit, dataType, null, null));

        BleDevicesResult bleDevicesResult = Mockito.mock(BleDevicesResult.class);

        List<BleDevice> bleDeviceList = new ArrayList<>();
        bleDeviceList.add(bleDevice);

        when(bleDevicesResult.getClaimedBleDevices(dataType)).thenReturn(bleDeviceList);

        setPendingResultValue(bleDevicesResult);
        when(bleDevicesResult.getStatus()).thenReturn(status);
        when(status.isSuccess()).thenReturn(false);
        when(bleApi.listClaimedBleDevices(apiClient)).thenReturn(pendingResult);

        setupBaseSingleSuccess(single);

        assertError(Single.create(single).test(), StatusException.class);
    }

    @Test
    public void BleListClaimedDevicesObservable_Success() {
        BleListClaimedDevicesSingle single = PowerMockito.spy(new BleListClaimedDevicesSingle(rxFit, null, null, null));

        BleDevicesResult bleDevicesResult = Mockito.mock(BleDevicesResult.class);

        List<BleDevice> bleDeviceList = new ArrayList<>();
        bleDeviceList.add(bleDevice);

        when(bleDevicesResult.getClaimedBleDevices()).thenReturn(bleDeviceList);

        setPendingResultValue(bleDevicesResult);
        when(bleDevicesResult.getStatus()).thenReturn(status);
        when(status.isSuccess()).thenReturn(true);
        when(bleApi.listClaimedBleDevices(apiClient)).thenReturn(pendingResult);

        setupBaseSingleSuccess(single);

        assertSingleValue(Single.create(single).test(), bleDeviceList);
    }

    @Test
    public void BleListClaimedDevicesObservable_StatusException() {
        BleListClaimedDevicesSingle single = PowerMockito.spy(new BleListClaimedDevicesSingle(rxFit, null, null, null));

        BleDevicesResult bleDevicesResult = Mockito.mock(BleDevicesResult.class);

        List<BleDevice> bleDeviceList = new ArrayList<>();
        bleDeviceList.add(bleDevice);

        when(bleDevicesResult.getClaimedBleDevices()).thenReturn(bleDeviceList);

        setPendingResultValue(bleDevicesResult);
        when(bleDevicesResult.getStatus()).thenReturn(status);
        when(status.isSuccess()).thenReturn(false);
        when(bleApi.listClaimedBleDevices(apiClient)).thenReturn(pendingResult);

        setupBaseSingleSuccess(single);

        assertError(Single.create(single).test(), StatusException.class);
    }

    // BleScanObservable

    @SuppressWarnings("MissingPermission")
    @Test
    public void BleScanObservable_Success() {
        BleScanObservable observable = PowerMockito.spy(new BleScanObservable(rxFit, null, null, null, null));

        setPendingResultValue(status);
        when(status.isSuccess()).thenReturn(true);
        when(bleApi.startBleScan(Matchers.any(GoogleApiClient.class), Matchers.any(StartBleScanRequest.class))).thenReturn(pendingResult);
        when(apiClient.isConnected()).thenReturn(true);

        setupBaseObservableSuccess(observable);
        TestObserver<BleDevice> sub = Observable.create(observable).test();
        getSubscriber(observable, apiClient).onNext(bleDevice);

        verify(bleApi, never()).stopBleScan(Matchers.any(GoogleApiClient.class), Matchers.any(BleScanCallback.class));
        sub.dispose();
        verify(bleApi).stopBleScan(Matchers.any(GoogleApiClient.class), Matchers.any(BleScanCallback.class));

        sub.assertNotTerminated();
        sub.assertValue(bleDevice);
    }

    @SuppressWarnings("MissingPermission")
    @Test
    public void BleScanObservable_StatusException() {
        BleScanObservable observable = PowerMockito.spy(new BleScanObservable(rxFit, null, null, null, null));

        setPendingResultValue(status);
        when(status.isSuccess()).thenReturn(false);
        when(bleApi.startBleScan(Matchers.any(GoogleApiClient.class), Matchers.any(StartBleScanRequest.class))).thenReturn(pendingResult);
        when(apiClient.isConnected()).thenReturn(true);

        setupBaseObservableSuccess(observable);

        assertError(Observable.create(observable).test(), StatusException.class);
    }

    @SuppressWarnings("MissingPermission")
    @Test
    public void BleScanObservable_SecurityException() throws Exception {

        PowerMockito.doThrow(new SecurityException("Missing Bluetooth Admin permission")).when(bleApi).startBleScan(Matchers.any(GoogleApiClient.class), Matchers.any(StartBleScanRequest.class));

        BleScanObservable observable = PowerMockito.spy(new BleScanObservable(rxFit, null, null, null, null));

        setupBaseObservableSuccess(observable);

        assertError(Observable.create(observable).test(), SecurityException.class);
    }
}
