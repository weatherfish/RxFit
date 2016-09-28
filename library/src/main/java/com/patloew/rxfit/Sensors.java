package com.patloew.rxfit;

import android.app.PendingIntent;
import android.support.annotation.NonNull;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSource;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.request.DataSourcesRequest;
import com.google.android.gms.fitness.request.SensorRequest;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Single;
import rx.functions.Func1;

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
public class Sensors {

    private final RxFit rxFit;

    Sensors(RxFit rxFit) {
        this.rxFit = rxFit;
    }

    // addDataPointIntent

    public Single<Status> addDataPointIntent(@NonNull SensorRequest sensorRequest, @NonNull PendingIntent pendingIntent) {
        return addDataPointIntentInternal(sensorRequest, pendingIntent, null, null);
    }

    public Single<Status> addDataPointIntent(@NonNull SensorRequest sensorRequest, @NonNull PendingIntent pendingIntent, long timeout, @NonNull TimeUnit timeUnit) {
        return addDataPointIntentInternal(sensorRequest, pendingIntent, timeout, timeUnit);
    }

    private Single<Status> addDataPointIntentInternal(SensorRequest sensorRequest, PendingIntent pendingIntent, Long timeout, TimeUnit timeUnit) {
        return Single.create(new SensorsAddDataPointIntentSingle(rxFit, sensorRequest, pendingIntent, timeout, timeUnit));
    }

    // removeDataPointIntent

    public Single<Status> removeDataPointIntent(@NonNull PendingIntent pendingIntent) {
        return removeDataPointIntentInternal(pendingIntent, null, null);
    }

    public Single<Status> removeDataPointIntent(@NonNull PendingIntent pendingIntent, long timeout, @NonNull TimeUnit timeUnit) {
        return removeDataPointIntentInternal(pendingIntent, timeout, timeUnit);
    }

    private Single<Status> removeDataPointIntentInternal(PendingIntent pendingIntent, Long timeout, TimeUnit timeUnit) {
        return Single.create(new SensorsRemoveDataPointIntentSingle(rxFit, pendingIntent, timeout, timeUnit));
    }

    // getDataPoints

    public Observable<DataPoint> getDataPoints(@NonNull SensorRequest sensorRequest) {
        return getDataPointsInternal(sensorRequest, null, null);
    }

    public Observable<DataPoint> getDataPoints(@NonNull SensorRequest sensorRequest, long timeout, @NonNull TimeUnit timeUnit) {
        return getDataPointsInternal(sensorRequest, timeout, timeUnit);
    }

    private Observable<DataPoint> getDataPointsInternal(SensorRequest sensorRequest, Long timeout, TimeUnit timeUnit) {
        return Observable.create(new SensorsDataPointObservable(rxFit, sensorRequest, timeout, timeUnit));
    }

    // findDataSources

    public Observable<DataSource> findDataSources(@NonNull DataSourcesRequest dataSourcesRequest) {
        return findDataSourcesInternal(dataSourcesRequest, null, null, null);
    }

    public Observable<DataSource> findDataSources(@NonNull DataSourcesRequest dataSourcesRequest, long timeout, @NonNull TimeUnit timeUnit) {
        return findDataSourcesInternal(dataSourcesRequest, null, timeout, timeUnit);
    }

    public Observable<DataSource> findDataSources(@NonNull DataSourcesRequest dataSourcesRequest, DataType dataType) {
        return findDataSourcesInternal(dataSourcesRequest, dataType, null, null);
    }

    public Observable<DataSource> findDataSources(@NonNull DataSourcesRequest dataSourcesRequest, DataType dataType, long timeout, @NonNull TimeUnit timeUnit) {
        return findDataSourcesInternal(dataSourcesRequest, dataType, timeout, timeUnit);
    }

    private Observable<DataSource> findDataSourcesInternal(DataSourcesRequest dataSourcesRequest, DataType dataType, Long timeout, TimeUnit timeUnit) {
        return Single.create(new SensorsFindDataSourcesSingle(rxFit, dataSourcesRequest, dataType, timeout, timeUnit)).flatMapObservable(new Func1<List<DataSource>, Observable<? extends DataSource>>() {
            @Override
            public Observable<? extends DataSource> call(List<DataSource> dataSources) {
                return Observable.from(dataSources);
            }
        });
    }

}
