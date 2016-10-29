package com.patloew.rxfit;

import android.app.PendingIntent;
import android.support.annotation.NonNull;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.fitness.data.Bucket;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataSource;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.request.DataDeleteRequest;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.request.DataUpdateRequest;
import com.google.android.gms.fitness.result.DataReadResult;

import java.util.concurrent.TimeUnit;

import rx.Observable;
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
public class History {

    private final RxFit rxFit;

    History(RxFit rxFit) {
        this.rxFit = rxFit;
    }

    // delete

    public Single<Status> delete(@NonNull DataDeleteRequest dataDeleteRequest) {
        return deleteInternal(dataDeleteRequest, null, null);
    }

    public Single<Status> delete(@NonNull DataDeleteRequest dataDeleteRequest, long timeout, @NonNull TimeUnit timeUnit) {
        return deleteInternal(dataDeleteRequest, timeout, timeUnit);
    }

    private Single<Status> deleteInternal(DataDeleteRequest dataDeleteRequest, Long timeout, TimeUnit timeUnit) {
        return Single.create(new HistoryDeleteDataSingle(rxFit, dataDeleteRequest, timeout, timeUnit));
    }

    // insert

    public Single<Status> insert(@NonNull DataSet dataSet) {
        return insertInternal(dataSet, null, null);
    }

    public Single<Status> insert(@NonNull DataSet dataSet, long timeout, @NonNull TimeUnit timeUnit) {
        return insertInternal(dataSet, timeout, timeUnit);
    }

    private Single<Status> insertInternal(DataSet dataSet, Long timeout, TimeUnit timeUnit) {
        return Single.create(new HistoryInsertDataSingle(rxFit, dataSet, timeout, timeUnit));
    }

    // readDailyTotal

    public Single<DataSet> readDailyTotal(@NonNull DataType dataType) {
        return readDailyTotalInternal(dataType, null, null);
    }

    public Single<DataSet> readDailyTotal(@NonNull DataType dataType, long timeout, @NonNull TimeUnit timeUnit) {
        return readDailyTotalInternal(dataType, timeout, timeUnit);
    }

    private Single<DataSet> readDailyTotalInternal(DataType dataType, Long timeout, TimeUnit timeUnit) {
        return Single.create(new HistoryReadDailyTotalSingle(rxFit, dataType, timeout, timeUnit));
    }

    // read

    public Single<DataReadResult> read(@NonNull DataReadRequest dataReadRequest) {
        return readInternal(dataReadRequest, null, null);
    }

    public Single<DataReadResult> read(@NonNull DataReadRequest dataReadRequest, long timeout, @NonNull TimeUnit timeUnit) {
        return readInternal(dataReadRequest, timeout, timeUnit);
    }

    private Single<DataReadResult> readInternal(DataReadRequest dataReadRequest, Long timeout, TimeUnit timeUnit) {
        return Single.create(new HistoryReadDataSingle(rxFit, dataReadRequest, timeout, timeUnit));
    }

    public Observable<Bucket> readBuckets(@NonNull DataReadRequest dataReadRequest) {
        return readBucketsInternal(dataReadRequest, null, null);
    }

    public Observable<Bucket> readBuckets(@NonNull DataReadRequest dataReadRequest, long timeout, @NonNull TimeUnit timeUnit) {
        return readBucketsInternal(dataReadRequest, timeout, timeUnit);
    }

    private Observable<Bucket> readBucketsInternal(DataReadRequest dataReadRequest, Long timeout, TimeUnit timeUnit) {
        return readInternal(dataReadRequest, timeout, timeUnit)
                .flatMapObservable(dataReadResult -> Observable.from(dataReadResult.getBuckets()));
    }

    public Observable<DataSet> readDataSets(@NonNull DataReadRequest dataReadRequest) {
        return readDataSetsInternal(dataReadRequest, null, null);
    }

    public Observable<DataSet> readDataSets(@NonNull DataReadRequest dataReadRequest, long timeout, @NonNull TimeUnit timeUnit) {
        return readDataSetsInternal(dataReadRequest, timeout, timeUnit);
    }

    private Observable<DataSet> readDataSetsInternal(DataReadRequest dataReadRequest, Long timeout, TimeUnit timeUnit) {
        return readInternal(dataReadRequest, timeout, timeUnit)
                .flatMapObservable(dataReadResult -> Observable.from(dataReadResult.getDataSets()));
    }


    // update

    public Single<Status> update(@NonNull DataUpdateRequest dataUpdateRequest) {
        return updateInternal(dataUpdateRequest, null, null);
    }

    public Single<Status> update(@NonNull DataUpdateRequest dataUpdateRequest, long timeout, @NonNull TimeUnit timeUnit) {
        return updateInternal(dataUpdateRequest, timeout, timeUnit);
    }

    private Single<Status> updateInternal(DataUpdateRequest dataUpdateRequest, Long timeout, TimeUnit timeUnit) {
        return Single.create(new HistoryUpdateDataSingle(rxFit, dataUpdateRequest, timeout, timeUnit));
    }

    // register data update listener

    public Single<Status> registerDataUpdateListener(@NonNull PendingIntent pendingIntent, @NonNull DataSource dataSource) {
        return registerDataUpdateListenerInternal(pendingIntent, dataSource, null, null, null);
    }

    public Single<Status> registerDataUpdateListener(@NonNull PendingIntent pendingIntent, @NonNull DataSource dataSource, long timeout, @NonNull TimeUnit timeUnit) {
        return registerDataUpdateListenerInternal(pendingIntent, dataSource, null, timeout, timeUnit);
    }

    public Single<Status> registerDataUpdateListener(@NonNull PendingIntent pendingIntent, @NonNull DataType dataType) {
        return registerDataUpdateListenerInternal(pendingIntent, null, dataType, null, null);
    }

    public Single<Status> registerDataUpdateListener(@NonNull PendingIntent pendingIntent, @NonNull DataType dataType, long timeout, @NonNull TimeUnit timeUnit) {
        return registerDataUpdateListenerInternal(pendingIntent, null, dataType, timeout, timeUnit);
    }

    public Single<Status> registerDataUpdateListener(@NonNull PendingIntent pendingIntent, @NonNull DataSource dataSource, @NonNull DataType dataType) {
        return registerDataUpdateListenerInternal(pendingIntent, dataSource, dataType, null, null);
    }

    public Single<Status> registerDataUpdateListener(@NonNull PendingIntent pendingIntent, @NonNull DataSource dataSource, @NonNull DataType dataType, long timeout, @NonNull TimeUnit timeUnit) {
        return registerDataUpdateListenerInternal(pendingIntent, dataSource, dataType, timeout, timeUnit);
    }

    private Single<Status> registerDataUpdateListenerInternal(PendingIntent pendingIntent, DataSource dataSource, DataType dataType, Long timeout, TimeUnit timeUnit) {
        return Single.create(new HistoryRegisterDataUpdateListenerSingle(rxFit, pendingIntent, dataSource, dataType, timeout, timeUnit));
    }

    // unregister data update listener

    public Single<Status> unregisterDataUpdateListener(@NonNull PendingIntent pendingIntent) {
        return unregisterDataUpdateListenerInternal(pendingIntent, null, null);
    }

    public Single<Status> unregisterDataUpdateListener(@NonNull PendingIntent pendingIntent, long timeout, @NonNull TimeUnit timeUnit) {
        return unregisterDataUpdateListenerInternal(pendingIntent, timeout, timeUnit);
    }

    private Single<Status> unregisterDataUpdateListenerInternal(PendingIntent pendingIntent, Long timeout, TimeUnit timeUnit) {
        return Single.create(new HistoryUnregisterDataUpdateListenerSingle(rxFit, pendingIntent, timeout, timeUnit));
    }

}
