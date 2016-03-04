package com.patloew.rxfit;

import android.support.annotation.NonNull;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.request.DataUpdateRequest;

import rx.Observable;
import rx.Observer;

public class HistoryUpdateDataObservable extends BaseObservable<Status> {

    private final DataUpdateRequest dataUpdateRequest;

    static Observable<Status> create(@NonNull RxFit rxFit, @NonNull DataUpdateRequest dataUpdateRequest) {
        return Observable.create(new HistoryUpdateDataObservable(rxFit, dataUpdateRequest));
    }

    HistoryUpdateDataObservable(RxFit rxFit, DataUpdateRequest dataUpdateRequest) {
        super(rxFit);
        this.dataUpdateRequest = dataUpdateRequest;
    }

    @Override
    protected void onGoogleApiClientReady(GoogleApiClient apiClient, final Observer<? super Status> observer) {
        Fitness.HistoryApi.updateData(apiClient, dataUpdateRequest).setResultCallback(new StatusResultCallBack(observer));
    }
}
