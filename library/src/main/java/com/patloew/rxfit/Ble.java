package com.patloew.rxfit;

import android.support.annotation.NonNull;
import android.support.annotation.RequiresPermission;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.fitness.data.BleDevice;
import com.google.android.gms.fitness.data.DataType;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Single;
import rx.functions.Func1;

/* Copyright 2016 Patrick Löwenstein
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. */
public class Ble {

    private final RxFit rxFit;

    Ble(RxFit rxFit) {
        this.rxFit = rxFit;
    }

    // claimDevice

    public Single<Status> claimDevice(@NonNull BleDevice bleDevice) {
        return claimDeviceInternal(bleDevice, null, null, null);
    }

    public Single<Status> claimDevice(@NonNull BleDevice bleDevice, long timeout, @NonNull TimeUnit timeUnit) {
        return claimDeviceInternal(bleDevice, null, timeout, timeUnit);
    }

    public Single<Status> claimDevice(@NonNull String deviceAddress) {
        return claimDeviceInternal(null, deviceAddress, null, null);
    }

    public Single<Status> claimDevice(@NonNull String deviceAddress, long timeout, @NonNull TimeUnit timeUnit) {
        return claimDeviceInternal(null, deviceAddress, timeout, timeUnit);
    }

    private Single<Status> claimDeviceInternal(BleDevice bleDevice, String deviceAddress, Long timeout, TimeUnit timeUnit) {
        return Single.create(new BleClaimDeviceSingle(rxFit, bleDevice, deviceAddress, timeout, timeUnit));
    }

    // getClaimedDevices

    public Observable<BleDevice> getClaimedDevices() {
        return getClaimedDeviceListInternal(null, null, null);
    }

    public Observable<BleDevice> getClaimedDevices(long timeout, @NonNull TimeUnit timeUnit) {
        return getClaimedDeviceListInternal(null, timeout, timeUnit);
    }

    public Observable<BleDevice> getClaimedDevices(DataType dataType) {
        return getClaimedDeviceListInternal(dataType, null, null);
    }

    public Observable<BleDevice> getClaimedDevices(DataType dataType, long timeout, @NonNull TimeUnit timeUnit) {
        return getClaimedDeviceListInternal(dataType, timeout, timeUnit);
    }

    private Observable<BleDevice> getClaimedDeviceListInternal(DataType dataType, Long timeout, TimeUnit timeUnit) {
        return Single.create(new BleListClaimedDevicesSingle(rxFit, dataType, timeout, timeUnit))
                .flatMapObservable(new Func1<List<BleDevice>, Observable<BleDevice>>() {
                    @Override
                    public Observable<BleDevice> call(List<BleDevice> bleDevices) {
                        return Observable.from(bleDevices);
                    }
                });
    }

    // scan

    @RequiresPermission("android.permission.BLUETOOTH_ADMIN")
    public Observable<BleDevice> scan() {
        return scanInternal(null, null, null, null);
    }

    @RequiresPermission("android.permission.BLUETOOTH_ADMIN")
    public Observable<BleDevice> scan(long timeout, @NonNull TimeUnit timeUnit) {
        return scanInternal(null, null, timeout, timeUnit);
    }

    @RequiresPermission("android.permission.BLUETOOTH_ADMIN")
    public Observable<BleDevice> scan(@NonNull DataType... dataTypes) {
        return scanInternal(dataTypes, null, null, null);
    }

    @RequiresPermission("android.permission.BLUETOOTH_ADMIN")
    public Observable<BleDevice> scan(@NonNull DataType[] dataTypes, long timeout, @NonNull TimeUnit timeUnit) {
        return scanInternal(dataTypes, null, timeout, timeUnit);
    }

    @RequiresPermission("android.permission.BLUETOOTH_ADMIN")
    public Observable<BleDevice> scan(int stopTimeSecs) {
        return scanInternal(null, stopTimeSecs, null, null);
    }

    @RequiresPermission("android.permission.BLUETOOTH_ADMIN")
    public Observable<BleDevice> scan(int stopTimeSecs, long timeout, @NonNull TimeUnit timeUnit) {
        return scanInternal(null, stopTimeSecs, timeout, timeUnit);
    }

    @RequiresPermission("android.permission.BLUETOOTH_ADMIN")
    public Observable<BleDevice> scan(@NonNull DataType[] dataTypes, int stopTimeSecs) {
        return scanInternal(dataTypes, stopTimeSecs, null, null);
    }

    @RequiresPermission("android.permission.BLUETOOTH_ADMIN")
    public Observable<BleDevice> scan(@NonNull DataType[] dataTypes, int stopTimeSecs, long timeout, @NonNull TimeUnit timeUnit) {
        return scanInternal(dataTypes, stopTimeSecs, timeout, timeUnit);
    }

    @SuppressWarnings("MissingPermission")
    private Observable<BleDevice> scanInternal(DataType[] dataTypes, Integer stopTimeSecs, Long timeout, TimeUnit timeUnit) {
        return Observable.create(new BleScanObservable(rxFit, dataTypes, stopTimeSecs, timeout, timeUnit));
    }

    // unclaim Device

    public Single<Status> unclaimDevice(@NonNull BleDevice bleDevice) {
        return unclaimDeviceInternal(bleDevice, null, null, null);
    }

    public Single<Status> unclaimDevice(@NonNull BleDevice bleDevice, long timeout, @NonNull TimeUnit timeUnit) {
        return unclaimDeviceInternal(bleDevice, null, timeout, timeUnit);
    }

    public Single<Status> unclaimDevice(@NonNull String deviceAddress) {
        return unclaimDeviceInternal(null, deviceAddress, null, null);
    }

    public Single<Status> unclaimDevice(@NonNull String deviceAddress, long timeout, @NonNull TimeUnit timeUnit) {
        return unclaimDeviceInternal(null, deviceAddress, timeout, timeUnit);
    }

    private Single<Status> unclaimDeviceInternal(BleDevice bleDevice, String deviceAddress, Long timeout, TimeUnit timeUnit) {
        return Single.create(new BleUnclaimDeviceSingle(rxFit, bleDevice, deviceAddress, timeout, timeUnit));
    }

}
