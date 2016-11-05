package com.patloew.rxfit;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.BleDevice;
import com.google.android.gms.fitness.data.DataType;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.SingleSubscriber;

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
class BleListClaimedDevicesSingle extends BaseSingle<List<BleDevice>> {

    final DataType dataType;

    BleListClaimedDevicesSingle(RxFit rxFit, DataType dataType, Long timeout, TimeUnit timeUnit) {
        super(rxFit, timeout, timeUnit);
        this.dataType = dataType;
    }

    @Override
    protected void onGoogleApiClientReady(GoogleApiClient apiClient, final SingleSubscriber<? super List<BleDevice>> subscriber) {
        setupFitnessPendingResult(
                Fitness.BleApi.listClaimedBleDevices(apiClient),
                SingleResultCallBack.get(subscriber, bleDevicesResult -> {
                    if(dataType == null) {
                        return bleDevicesResult.getClaimedBleDevices();
                    } else {
                        return bleDevicesResult.getClaimedBleDevices(dataType);
                    }
                })
        );
    }
}
