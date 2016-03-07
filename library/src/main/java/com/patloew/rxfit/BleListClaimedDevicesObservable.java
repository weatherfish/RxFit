package com.patloew.rxfit;

import android.support.annotation.NonNull;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.BleDevice;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.result.BleDevicesResult;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observer;

public class BleListClaimedDevicesObservable extends BaseObservable<List<BleDevice>> {

    private final DataType dataType;

    BleListClaimedDevicesObservable(RxFit rxFit, DataType dataType, Long timeout, TimeUnit timeUnit) {
        super(rxFit, timeout, timeUnit);
        this.dataType = dataType;
    }

    @Override
    protected void onGoogleApiClientReady(GoogleApiClient apiClient, final Observer<? super List<BleDevice>> observer) {
        setupFitnessPendingResult(Fitness.BleApi.listClaimedBleDevices(apiClient), new ResultCallback<BleDevicesResult>() {
            @Override
            public void onResult(@NonNull BleDevicesResult bleDevicesResult) {
                if (!bleDevicesResult.getStatus().isSuccess()) {
                    observer.onError(new StatusException(bleDevicesResult.getStatus()));
                } else {
                    if(dataType == null) {
                        observer.onNext(bleDevicesResult.getClaimedBleDevices());
                    } else {
                        observer.onNext(bleDevicesResult.getClaimedBleDevices(dataType));
                    }

                    observer.onCompleted();
                }
            }
        });
    }
}
