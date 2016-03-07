package com.patloew.rxfit;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.BleDevice;

import java.util.concurrent.TimeUnit;

import rx.Observer;

public class BleUnclaimDeviceObservable extends BaseObservable<Status> {

    private final BleDevice bleDevice;
    private final String deviceAddress;

    BleUnclaimDeviceObservable(RxFit rxFit, BleDevice bleDevice, String deviceAddress, Long timeout, TimeUnit timeUnit) {
        super(rxFit, timeout, timeUnit);
        this.bleDevice = bleDevice;
        this.deviceAddress = deviceAddress;
    }

    @Override
    protected void onGoogleApiClientReady(GoogleApiClient apiClient, final Observer<? super Status> observer) {
        ResultCallback<Status> resultCallback = new StatusResultCallBack(observer);

        if(bleDevice != null) {
            setupFitnessPendingResult(Fitness.BleApi.unclaimBleDevice(apiClient, bleDevice), resultCallback);
        } else {
            setupFitnessPendingResult(Fitness.BleApi.unclaimBleDevice(apiClient, deviceAddress), resultCallback);
        }
    }
}
