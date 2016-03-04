package com.patloew.rxfit;

import android.support.annotation.NonNull;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.result.DataReadResult;

import rx.Observable;
import rx.Observer;

public class HistoryReadDataObservable extends BaseObservable<DataReadResult> {

    private final DataReadRequest dataReadRequest;

    static Observable<DataReadResult> create(@NonNull RxFit rxFit, @NonNull DataReadRequest dataReadRequest) {
        return Observable.create(new HistoryReadDataObservable(rxFit, dataReadRequest));
    }

    HistoryReadDataObservable(RxFit rxFit, DataReadRequest dataReadRequest) {
        super(rxFit);
        this.dataReadRequest = dataReadRequest;
    }

    @Override
    protected void onGoogleApiClientReady(GoogleApiClient apiClient, final Observer<? super DataReadResult> observer) {
        Fitness.HistoryApi.readData(apiClient, dataReadRequest).setResultCallback(new ResultCallback<DataReadResult>() {
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
