package com.patloew.rxfit;

import android.support.annotation.NonNull;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.DataSource;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.request.DataSourcesRequest;
import com.google.android.gms.fitness.result.DataSourcesResult;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observer;

public class SensorsFindDataSourcesObservable extends BaseObservable<List<DataSource>> {

    private final DataSourcesRequest dataSourcesRequest;
    private final DataType dataType;

    SensorsFindDataSourcesObservable(RxFit rxFit, DataSourcesRequest dataSourcesRequest, DataType dataType, Long timeout, TimeUnit timeUnit) {
        super(rxFit, timeout, timeUnit);
        this.dataSourcesRequest = dataSourcesRequest;
        this.dataType = dataType;
    }

    @Override
    protected void onGoogleApiClientReady(GoogleApiClient apiClient, final Observer<? super List<DataSource>> observer) {
        setupFitnessPendingResult(Fitness.SensorsApi.findDataSources(apiClient, dataSourcesRequest), new ResultCallback<DataSourcesResult>() {
            @Override
            public void onResult(@NonNull DataSourcesResult dataSourcesResult) {
                if (!dataSourcesResult.getStatus().isSuccess()) {
                    observer.onError(new StatusException(dataSourcesResult.getStatus()));
                } else {
                    if(dataType == null) {
                        observer.onNext(dataSourcesResult.getDataSources());
                    } else {
                        observer.onNext(dataSourcesResult.getDataSources(dataType));
                    }

                    observer.onCompleted();
                }
            }
        });
    }
}
