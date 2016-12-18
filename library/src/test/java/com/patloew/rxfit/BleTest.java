package com.patloew.rxfit;

import android.support.v4.content.ContextCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.BleDevice;
import com.google.android.gms.fitness.data.DataType;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
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
@PrepareOnlyThisForTest({ Observable.class, Single.class, ContextCompat.class, Fitness.class, Status.class, ConnectionResult.class, DataType.class })
public class BleTest extends BaseTest {

    @Mock BleDevice bleDevice;
    @Mock DataType dataType;
    DataType[] dataTypes = new DataType[] {};

    @Override
    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        PowerMockito.spy(Single.class);
        PowerMockito.mockStatic(Observable.class);
        doReturn(100).when(Observable.class, "bufferSize");
        super.setup();
    }


    // Claim Device

    @Test
    public void Ble_ClaimDevice_BleDevice() throws Exception {
        ArgumentCaptor<BleClaimDeviceSingle> captor = ArgumentCaptor.forClass(BleClaimDeviceSingle.class);

        rxFit.ble().claimDevice(bleDevice);
        rxFit.ble().claimDevice(bleDevice, TIMEOUT_TIME, TIMEOUT_TIMEUNIT);

        PowerMockito.verifyStatic(times(2));
        Single.create(captor.capture());

        BleClaimDeviceSingle single = captor.getAllValues().get(0);
        assertEquals(bleDevice, single.bleDevice);
        assertNull(single.deviceAddress);
        assertNoTimeoutSet(single);

        single = captor.getAllValues().get(1);
        assertEquals(bleDevice, single.bleDevice);
        assertNull(single.deviceAddress);
        assertTimeoutSet(single);
    }

    @Test
    public void Ble_ClaimDevice_DeviceAddress() throws Exception {
        ArgumentCaptor<BleClaimDeviceSingle> captor = ArgumentCaptor.forClass(BleClaimDeviceSingle.class);
        String deviceAddress = "deviceAddress";

        rxFit.ble().claimDevice(deviceAddress);
        rxFit.ble().claimDevice(deviceAddress, TIMEOUT_TIME, TIMEOUT_TIMEUNIT);

        PowerMockito.verifyStatic(times(2));
        Single.create(captor.capture());

        BleClaimDeviceSingle single = captor.getAllValues().get(0);
        assertEquals(deviceAddress, single.deviceAddress);
        assertNoTimeoutSet(single);
        assertNull(single.bleDevice);

        single = captor.getAllValues().get(1);
        assertEquals(deviceAddress, single.deviceAddress);
        assertTimeoutSet(single);
        assertNull(single.bleDevice);
    }

    // Get Claimed Devices

    @Test
    public void Ble_ListClaimedDevices() throws Exception {
        ArgumentCaptor<BleListClaimedDevicesSingle> captor = ArgumentCaptor.forClass(BleListClaimedDevicesSingle.class);

        rxFit.ble().getClaimedDevices();
        rxFit.ble().getClaimedDevices(TIMEOUT_TIME, TIMEOUT_TIMEUNIT);

        PowerMockito.verifyStatic(atLeast(2));
        Single.create(captor.capture());

        BleListClaimedDevicesSingle single = captor.getAllValues().get(0);
        assertNull(single.dataType);
        assertNoTimeoutSet(single);

        single = captor.getAllValues().get(1);
        assertNull(single.dataType);
        assertTimeoutSet(single);
    }

    @Test
    public void Ble_ListClaimedDevices_DataType() throws Exception {
        ArgumentCaptor<BleListClaimedDevicesSingle> captor = ArgumentCaptor.forClass(BleListClaimedDevicesSingle.class);

        rxFit.ble().getClaimedDevices(dataType);
        rxFit.ble().getClaimedDevices(dataType, TIMEOUT_TIME, TIMEOUT_TIMEUNIT);

        PowerMockito.verifyStatic(atLeast(2));
        Single.create(captor.capture());

        BleListClaimedDevicesSingle single = captor.getAllValues().get(0);
        assertEquals(dataType, single.dataType);
        assertNoTimeoutSet(single);

        single = captor.getAllValues().get(1);
        assertEquals(dataType, single.dataType);
        assertTimeoutSet(single);
    }

    // Scan

    @Test
    @SuppressWarnings("MissingPermission")
    public void Ble_Scan() throws Exception {
        ArgumentCaptor<BleScanObservable> captor = ArgumentCaptor.forClass(BleScanObservable.class);

        rxFit.ble().scan();
        rxFit.ble().scan(TIMEOUT_TIME, TIMEOUT_TIMEUNIT);

        PowerMockito.verifyStatic(times(2));
        Observable.create(captor.capture());

        BleScanObservable observable = captor.getAllValues().get(0);
        assertNull(observable.dataTypes);
        assertNull(observable.stopTimeSecs);
        assertNoTimeoutSet(observable);

        observable = captor.getAllValues().get(1);
        assertNull(observable.dataTypes);
        assertNull(observable.stopTimeSecs);
        assertTimeoutSet(observable);
    }

    @Test
    @SuppressWarnings("MissingPermission")
    public void Ble_Scan_StopTime() throws Exception {
        ArgumentCaptor<BleScanObservable> captor = ArgumentCaptor.forClass(BleScanObservable.class);

        final int stopTimeSecs = 2;
        rxFit.ble().scan(stopTimeSecs);
        rxFit.ble().scan(stopTimeSecs, TIMEOUT_TIME, TIMEOUT_TIMEUNIT);

        PowerMockito.verifyStatic(times(2));
        Observable.create(captor.capture());

        BleScanObservable observable = captor.getAllValues().get(0);
        assertNull(observable.dataTypes);
        assertEquals(stopTimeSecs, (int) observable.stopTimeSecs);
        assertNoTimeoutSet(observable);

        observable = captor.getAllValues().get(1);
        assertNull(observable.dataTypes);
        assertEquals(stopTimeSecs, (int) observable.stopTimeSecs);
        assertTimeoutSet(observable);
    }

    @Test
    @SuppressWarnings("MissingPermission")
    public void Ble_Scan_DateTypes() throws Exception {
        ArgumentCaptor<BleScanObservable> captor = ArgumentCaptor.forClass(BleScanObservable.class);

        rxFit.ble().scan(dataTypes);
        rxFit.ble().scan(dataTypes, TIMEOUT_TIME, TIMEOUT_TIMEUNIT);

        PowerMockito.verifyStatic(times(2));
        Observable.create(captor.capture());

        BleScanObservable observable = captor.getAllValues().get(0);
        assertEquals(dataTypes, observable.dataTypes);
        assertNull(observable.stopTimeSecs);
        assertNoTimeoutSet(observable);

        observable = captor.getAllValues().get(1);
        assertEquals(dataTypes, observable.dataTypes);
        assertNull(observable.stopTimeSecs);
        assertTimeoutSet(observable);
    }

    @Test
    @SuppressWarnings("MissingPermission")
    public void Ble_Scan_DateTypes_StopTime() throws Exception {
        ArgumentCaptor<BleScanObservable> captor = ArgumentCaptor.forClass(BleScanObservable.class);

        final int stopTimeSecs = 2;
        rxFit.ble().scan(dataTypes, stopTimeSecs);
        rxFit.ble().scan(dataTypes, stopTimeSecs, TIMEOUT_TIME, TIMEOUT_TIMEUNIT);

        PowerMockito.verifyStatic(times(2));
        Observable.create(captor.capture());

        BleScanObservable observable = captor.getAllValues().get(0);
        assertEquals(dataTypes, observable.dataTypes);
        assertEquals(stopTimeSecs, (int) observable.stopTimeSecs);
        assertNoTimeoutSet(observable);

        observable = captor.getAllValues().get(1);
        assertEquals(dataTypes, observable.dataTypes);
        assertEquals(stopTimeSecs, (int) observable.stopTimeSecs);
        assertTimeoutSet(observable);
    }

    // Unclaim Device

    @Test
    public void Ble_UnclaimDevice_BleDevice() throws Exception {
        ArgumentCaptor<BleUnclaimDeviceSingle> captor = ArgumentCaptor.forClass(BleUnclaimDeviceSingle.class);

        rxFit.ble().unclaimDevice(bleDevice);
        rxFit.ble().unclaimDevice(bleDevice, TIMEOUT_TIME, TIMEOUT_TIMEUNIT);

        PowerMockito.verifyStatic(times(2));
        Single.create(captor.capture());

        BleUnclaimDeviceSingle single = captor.getAllValues().get(0);
        assertEquals(bleDevice, single.bleDevice);
        assertNull(single.deviceAddress);
        assertNoTimeoutSet(single);

        single = captor.getAllValues().get(1);
        assertEquals(bleDevice, single.bleDevice);
        assertNull(single.deviceAddress);
        assertTimeoutSet(single);
    }

    @Test
    public void Ble_UnclaimDevice_DeviceAddress() throws Exception {
        ArgumentCaptor<BleUnclaimDeviceSingle> captor = ArgumentCaptor.forClass(BleUnclaimDeviceSingle.class);

        final String deviceAddress = "deviceAddress";
        rxFit.ble().unclaimDevice(deviceAddress);
        rxFit.ble().unclaimDevice(deviceAddress, TIMEOUT_TIME, TIMEOUT_TIMEUNIT);

        PowerMockito.verifyStatic(times(2));
        Single.create(captor.capture());

        BleUnclaimDeviceSingle single = captor.getAllValues().get(0);
        assertNull(single.bleDevice);
        assertEquals(deviceAddress, single.deviceAddress);
        assertNoTimeoutSet(single);

        single = captor.getAllValues().get(1);
        assertNull(single.bleDevice);
        assertEquals(deviceAddress, single.deviceAddress);
        assertTimeoutSet(single);
    }

}
