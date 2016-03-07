package com.patloew.rxfit;

import android.support.annotation.RequiresPermission;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.request.StartBleScanRequest;

import java.util.concurrent.TimeUnit;

import rx.Observer;

public class BleStartScanObservable extends BaseObservable<Status> {

    private final StartBleScanRequest startBleScanRequest;

    @RequiresPermission("android.permission.BLUETOOTH_ADMIN")
    BleStartScanObservable(RxFit rxFit, StartBleScanRequest startBleScanRequest, Long timeout, TimeUnit timeUnit) {
        super(rxFit, timeout, timeUnit);
        this.startBleScanRequest = startBleScanRequest;
    }

    @SuppressWarnings("MissingPermission")
    @Override
    protected void onGoogleApiClientReady(GoogleApiClient apiClient, final Observer<? super Status> observer) {
        setupFitnessPendingResult(Fitness.BleApi.startBleScan(apiClient, startBleScanRequest), new StatusResultCallBack(observer));
    }
}
