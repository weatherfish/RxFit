package com.patloew.rxfit;

import android.support.annotation.NonNull;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.result.DataReadResult;

import java.util.concurrent.TimeUnit;

import rx.Observer;

public class HistoryReadDataObservable extends BaseObservable<DataReadResult> {

    private final DataReadRequest dataReadRequest;

    HistoryReadDataObservable(RxFit rxFit, DataReadRequest dataReadRequest, Long timeout, TimeUnit timeUnit) {
        super(rxFit, timeout, timeUnit);
        this.dataReadRequest = dataReadRequest;
    }

    @Override
    protected void onGoogleApiClientReady(GoogleApiClient apiClient, final Observer<? super DataReadResult> observer) {
        setupFitnessPendingResult(Fitness.HistoryApi.readData(apiClient, dataReadRequest), new ResultCallback<DataReadResult>() {
            @Override
            public void onResult(@NonNull DataReadResult dataReadResult) {
                if (!dataReadResult.getStatus().isSuccess()) {
                    observer.onError(new StatusException(dataReadResult.getStatus()));
                } else {
                    observer.onNext(dataReadResult);
                    observer.onCompleted();
                }
            }
        });
    }
}
