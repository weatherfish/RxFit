package com.patloew.rxfit;

import android.support.annotation.RequiresPermission;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.BleDevice;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.request.BleScanCallback;
import com.google.android.gms.fitness.request.StartBleScanRequest;

import java.util.concurrent.TimeUnit;

import rx.Subscriber;

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
class BleScanObservable extends BaseObservable<BleDevice> {

    final DataType[] dataTypes;
    final Integer stopTimeSecs;

    private BleScanCallback bleScanCallback;

    @RequiresPermission("android.permission.BLUETOOTH_ADMIN")
    BleScanObservable(RxFit rxFit, DataType[] dataTypes, Integer stopTimeSecs, Long timeout, TimeUnit timeUnit) {
        super(rxFit, timeout, timeUnit);
        this.dataTypes = dataTypes;
        this.stopTimeSecs = stopTimeSecs;
    }

    @SuppressWarnings("MissingPermission")
    @Override
    protected void onGoogleApiClientReady(GoogleApiClient apiClient, final Subscriber<? super BleDevice> subscriber) {
        bleScanCallback = new BleScanCallback() {
            @Override public void onDeviceFound(BleDevice bleDevice) { subscriber.onNext(bleDevice); }
            @Override public void onScanStopped() { subscriber.onCompleted(); }
        };

        StartBleScanRequest.Builder startBleScanRequest = new StartBleScanRequest.Builder().setBleScanCallback(bleScanCallback);
        if(dataTypes != null) { startBleScanRequest.setDataTypes(dataTypes); }
        if(stopTimeSecs != null) { startBleScanRequest.setTimeoutSecs(stopTimeSecs); }

        setupFitnessPendingResult(
                Fitness.BleApi.startBleScan(apiClient, startBleScanRequest.build()),
                new StatusErrorResultCallBack(subscriber)
        );
    }

    @Override
    protected void onUnsubscribed(GoogleApiClient apiClient) {
        Fitness.BleApi.stopBleScan(apiClient, bleScanCallback);
    }
}
