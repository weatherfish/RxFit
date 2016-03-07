package com.patloew.rxfit;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresPermission;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.request.StartBleScanRequest;

import rx.Observable;
import rx.Observer;

public class BleStartScanObservable extends BaseObservable<Status> {

    private final StartBleScanRequest startBleScanRequest;

    @RequiresPermission("android.permission.BLUETOOTH_ADMIN")
    static Observable<Status> create(@NonNull RxFit rxFit, @NonNull StartBleScanRequest startBleScanRequest) {
        return Observable.create(new BleStartScanObservable(rxFit, startBleScanRequest));
    }

    BleStartScanObservable(RxFit rxFit, StartBleScanRequest startBleScanRequest) {
        super(rxFit);
        this.startBleScanRequest = startBleScanRequest;
    }

    @Override
    protected void onGoogleApiClientReady(GoogleApiClient apiClient, final Observer<? super Status> observer) {
        //noinspection MissingPermission
        Fitness.BleApi.startBleScan(apiClient, startBleScanRequest).setResultCallback(new StatusResultCallBack(observer));
    }
}
