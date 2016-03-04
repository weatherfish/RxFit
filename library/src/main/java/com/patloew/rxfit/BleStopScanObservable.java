package com.patloew.rxfit;

import android.support.annotation.NonNull;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.request.BleScanCallback;

import rx.Observable;
import rx.Observer;

public class BleStopScanObservable extends BaseObservable<Status> {

    private final BleScanCallback bleScanCallback;

    static Observable<Status> create(@NonNull RxFit rxFit, @NonNull BleScanCallback bleScanCallback) {
        return Observable.create(new BleStopScanObservable(rxFit, bleScanCallback));
    }

    BleStopScanObservable(RxFit rxFit, BleScanCallback bleScanCallback) {
        super(rxFit);
        this.bleScanCallback = bleScanCallback;
    }

    @Override
    protected void onGoogleApiClientReady(GoogleApiClient apiClient, final Observer<? super Status> observer) {
        Fitness.BleApi.stopBleScan(apiClient, bleScanCallback).setResultCallback(new StatusResultCallBack(observer));
    }
}
