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

import rx.Observable;
import rx.Observer;

public class SensorsFindDataSourcesObservable extends BaseObservable<List<DataSource>> {

    private final DataSourcesRequest dataSourcesRequest;
    private final DataType dataType;

    static Observable<List<DataSource>> create(@NonNull RxFit rxFit, @NonNull DataSourcesRequest dataSourcesRequest) {
        return Observable.create(new SensorsFindDataSourcesObservable(rxFit, dataSourcesRequest, null));
    }

    static Observable<List<DataSource>> create(@NonNull RxFit rxFit, @NonNull DataSourcesRequest dataSourcesRequest, DataType dataType) {
        return Observable.create(new SensorsFindDataSourcesObservable(rxFit, dataSourcesRequest, dataType));
    }

    SensorsFindDataSourcesObservable(RxFit rxFit, DataSourcesRequest dataSourcesRequest, DataType dataType) {
        super(rxFit);
        this.dataSourcesRequest = dataSourcesRequest;
        this.dataType = dataType;
    }

    @Override
    protected void onGoogleApiClientReady(GoogleApiClient apiClient, final Observer<? super List<DataSource>> observer) {
        Fitness.SensorsApi.findDataSources(apiClient, dataSourcesRequest).setResultCallback(new ResultCallback<DataSourcesResult>() {
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
