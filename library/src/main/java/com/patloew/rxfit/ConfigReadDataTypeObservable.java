package com.patloew.rxfit;

import android.support.annotation.NonNull;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.result.DataTypeResult;

import java.util.concurrent.TimeUnit;

import rx.Observer;

public class ConfigReadDataTypeObservable extends BaseObservable<DataType> {

    private final String dataTypeName;

    ConfigReadDataTypeObservable(RxFit rxFit, String dataTypeName, Long timeout, TimeUnit timeUnit) {
        super(rxFit, timeout, timeUnit);
        this.dataTypeName = dataTypeName;
    }

    @Override
    protected void onGoogleApiClientReady(GoogleApiClient apiClient, final Observer<? super DataType> observer) {
        setupFitnessPendingResult(Fitness.ConfigApi.readDataType(apiClient, dataTypeName), new ResultCallback<DataTypeResult>() {
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
