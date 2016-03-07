package com.patloew.rxfit;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.request.BleScanCallback;

import java.util.concurrent.TimeUnit;

import rx.Observer;

public class BleStopScanObservable extends BaseObservable<Status> {

    private final BleScanCallback bleScanCallback;

    BleStopScanObservable(RxFit rxFit, BleScanCallback bleScanCallback, Long timeout, TimeUnit timeUnit) {
        super(rxFit, timeout, timeUnit);
        this.bleScanCallback = bleScanCallback;
    }

    @Override
    protected void onGoogleApiClientReady(GoogleApiClient apiClient, final Observer<? super Status> observer) {
        setupFitnessPendingResult(Fitness.BleApi.stopBleScan(apiClient, bleScanCallback), new StatusResultCallBack(observer));
    }
}
