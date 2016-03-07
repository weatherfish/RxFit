package com.patloew.rxfit;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.request.DataDeleteRequest;

import java.util.concurrent.TimeUnit;

import rx.Observer;

public class HistoryDeleteDataObservable extends BaseObservable<Status> {

    private final DataDeleteRequest dataDeleteRequest;

    HistoryDeleteDataObservable(RxFit rxFit, DataDeleteRequest dataDeleteRequest, Long timeout, TimeUnit timeUnit) {
        super(rxFit, timeout, timeUnit);
        this.dataDeleteRequest = dataDeleteRequest;
    }

    @Override
    protected void onGoogleApiClientReady(GoogleApiClient apiClient, final Observer<? super Status> observer) {
        setupFitnessPendingResult(Fitness.HistoryApi.deleteData(apiClient, dataDeleteRequest), new StatusResultCallBack(observer));
    }
}
