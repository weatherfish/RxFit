package com.patloew.rxfit;

import android.support.annotation.NonNull;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.BleDevice;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.result.BleDevicesResult;

import java.util.List;

import rx.Observable;
import rx.Observer;

public class BleListClaimedDevicesObservable extends BaseObservable<List<BleDevice>> {

    private final DataType dataType;

    static Observable<List<BleDevice>> create(@NonNull RxFit rxFit) {
        return Observable.create(new BleListClaimedDevicesObservable(rxFit, null));
    }

    static Observable<List<BleDevice>> create(@NonNull RxFit rxFit, DataType dataType) {
        return Observable.create(new BleListClaimedDevicesObservable(rxFit, dataType));
    }

    BleListClaimedDevicesObservable(RxFit rxFit, DataType dataType) {
        super(rxFit);
        this.dataType = dataType;
    }

    @Override
    protected void onGoogleApiClientReady(GoogleApiClient apiClient, final Observer<? super List<BleDevice>> observer) {
        Fitness.BleApi.listClaimedBleDevices(apiClient).setResultCallback(new ResultCallback<BleDevicesResult>() {
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
