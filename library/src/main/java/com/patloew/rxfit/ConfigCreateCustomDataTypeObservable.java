package com.patloew.rxfit;

import android.support.annotation.NonNull;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.request.DataTypeCreateRequest;
import com.google.android.gms.fitness.result.DataTypeResult;

import rx.Observable;
import rx.Observer;

public class ConfigCreateCustomDataTypeObservable extends BaseObservable<DataType> {

    private final DataTypeCreateRequest dataTypeCreateRequest;

    static Observable<DataType> create(@NonNull RxFit rxFit, @NonNull DataTypeCreateRequest dataTypeCreateRequest) {
        return Observable.create(new ConfigCreateCustomDataTypeObservable(rxFit, dataTypeCreateRequest));
    }

    ConfigCreateCustomDataTypeObservable(RxFit rxFit, DataTypeCreateRequest dataTypeCreateRequest) {
        super(rxFit);
        this.dataTypeCreateRequest = dataTypeCreateRequest;
    }

    @Override
    protected void onGoogleApiClientReady(GoogleApiClient apiClient, final Observer<? super DataType> observer) {
        Fitness.ConfigApi.createCustomDataType(apiClient, dataTypeCreateRequest).setResultCallback(new ResultCallback<DataTypeResult>() {
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
