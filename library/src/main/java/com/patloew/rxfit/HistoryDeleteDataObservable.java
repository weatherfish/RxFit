package com.patloew.rxfit;

import android.support.annotation.NonNull;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.request.DataDeleteRequest;

import rx.Observable;
import rx.Observer;

public class HistoryDeleteDataObservable extends BaseObservable<Status> {

    private final DataDeleteRequest dataDeleteRequest;

    static Observable<Status> create(@NonNull RxFit rxFit, @NonNull DataDeleteRequest dataDeleteRequest) {
        return Observable.create(new HistoryDeleteDataObservable(rxFit, dataDeleteRequest));
    }

    HistoryDeleteDataObservable(RxFit rxFit, DataDeleteRequest dataDeleteRequest) {
        super(rxFit);
        this.dataDeleteRequest = dataDeleteRequest;
    }

    @Override
    protected void onGoogleApiClientReady(GoogleApiClient apiClient, final Observer<? super Status> observer) {
        Fitness.HistoryApi.deleteData(apiClient, dataDeleteRequest).setResultCallback(new StatusResultCallBack(observer));
    }
}
