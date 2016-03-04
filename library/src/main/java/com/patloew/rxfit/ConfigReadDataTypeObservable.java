package com.patloew.rxfit;

import android.support.annotation.NonNull;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.result.DataTypeResult;

import rx.Observable;
import rx.Observer;

public class ConfigReadDataTypeObservable extends BaseObservable<DataType> {

    private final String dataTypeName;

    static Observable<DataType> create(@NonNull RxFit rxFit, @NonNull String dataTypeName) {
        return Observable.create(new ConfigReadDataTypeObservable(rxFit, dataTypeName));
    }

    ConfigReadDataTypeObservable(RxFit rxFit, String dataTypeName) {
        super(rxFit);
        this.dataTypeName = dataTypeName;
    }

    @Override
    protected void onGoogleApiClientReady(GoogleApiClient apiClient, final Observer<? super DataType> observer) {
        Fitness.ConfigApi.readDataType(apiClient, dataTypeName).setResultCallback(new ResultCallback<DataTypeResult>() {
            @Override
            public void onResult(@NonNull DataTypeResult dataTypeResult) {
                if (!dataTypeResult.getStatus().isSuccess()) {
                    observer.onError(new StatusException(dataTypeResult.getStatus()));
                } else {
                    observer.onNext(dataTypeResult.getDataType());
                    observer.onCompleted();
                }
            }
        });
    }
}
