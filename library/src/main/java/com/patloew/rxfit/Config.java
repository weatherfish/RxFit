package com.patloew.rxfit;

import android.support.annotation.NonNull;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.request.DataTypeCreateRequest;

import java.util.concurrent.TimeUnit;

import rx.Single;

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
public class Config {

    private final RxFit rxFit;

    Config(RxFit rxFit) {
        this.rxFit = rxFit;
    }

    // createCustomDataType

    public Single<DataType> createCustomDataType(@NonNull DataTypeCreateRequest dataTypeCreateRequest) {
        return createCustomDataTypeInternal(dataTypeCreateRequest, null, null);
    }

    public Single<DataType> createCustomDataType(@NonNull DataTypeCreateRequest dataTypeCreateRequest, long timeout, @NonNull TimeUnit timeUnit) {
        return createCustomDataTypeInternal(dataTypeCreateRequest, timeout, timeUnit);
    }

    private Single<DataType> createCustomDataTypeInternal(DataTypeCreateRequest dataTypeCreateRequest, Long timeout, TimeUnit timeUnit) {
        return Single.create(new ConfigCreateCustomDataTypeSingle(rxFit, dataTypeCreateRequest, timeout, timeUnit));
    }

    // disableFit

    public Single<Status> disableFit() {
        return disableFitInternal(null, null);
    }

    public Single<Status> disableFit(long timeout, @NonNull TimeUnit timeUnit) {
        return disableFitInternal(timeout, timeUnit);
    }

    private Single<Status> disableFitInternal(Long timeout, TimeUnit timeUnit) {
        return Single.create(new ConfigDisableFitSingle(rxFit, timeout, timeUnit));
    }

    // readDataType

    public Single<DataType> readDataType(@NonNull String dataTypeName) {
        return readDataTypeInternal(dataTypeName, null, null);
    }

    public Single<DataType> readDataType(@NonNull String dataTypeName, long timeout, @NonNull TimeUnit timeUnit) {
        return readDataTypeInternal(dataTypeName, timeout, timeUnit);
    }

    private Single<DataType> readDataTypeInternal(String dataTypeName, Long timeout, TimeUnit timeUnit) {
        return Single.create(new ConfigReadDataTypeSingle(rxFit, dataTypeName, timeout, timeUnit));
    }

}
