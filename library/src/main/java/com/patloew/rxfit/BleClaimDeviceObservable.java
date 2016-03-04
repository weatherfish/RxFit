package com.patloew.rxfit;

import android.support.annotation.NonNull;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.BleDevice;

import rx.Observable;
import rx.Observer;

public class BleClaimDeviceObservable extends BaseObservable<Status> {

    private final BleDevice bleDevice;
    private final String deviceAddress;

    static Observable<Status> create(@NonNull RxFit rxFit, @NonNull BleDevice bleDevice) {
        return Observable.create(new BleClaimDeviceObservable(rxFit, bleDevice, null));
    }

    static Observable<Status> create(@NonNull RxFit rxFit, @NonNull String deviceAddress) {
        return Observable.create(new BleClaimDeviceObservable(rxFit, null, deviceAddress));
    }

    BleClaimDeviceObservable(RxFit rxFit, BleDevice bleDevice, String deviceAddress) {
        super(rxFit);
        this.bleDevice = bleDevice;
        this.deviceAddress = deviceAddress;
    }

    @Override
    protected void onGoogleApiClientReady(GoogleApiClient apiClient, final Observer<? super Status> observer) {
        ResultCallback<Status> resultCallback = new StatusResultCallBack(observer);

        if(bleDevice != null) {
            Fitness.BleApi.claimBleDevice(apiClient, bleDevice).setResultCallback(resultCallback);
        } else {
            Fitness.BleApi.claimBleDevice(apiClient, deviceAddress).setResultCallback(resultCallback);
        }
    }
}
