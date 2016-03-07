package com.patloew.rxfit;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.request.DataUpdateRequest;

import java.util.concurrent.TimeUnit;

import rx.Observer;

public class HistoryUpdateDataObservable extends BaseObservable<Status> {

    private final DataUpdateRequest dataUpdateRequest;

    HistoryUpdateDataObservable(RxFit rxFit, DataUpdateRequest dataUpdateRequest, Long timeout, TimeUnit timeUnit) {
        super(rxFit, timeout, timeUnit);
        this.dataUpdateRequest = dataUpdateRequest;
    }

    @Override
    protected void onGoogleApiClientReady(GoogleApiClient apiClient, final Observer<? super Status> observer) {
        setupFitnessPendingResult(Fitness.HistoryApi.updateData(apiClient, dataUpdateRequest), new StatusResultCallBack(observer));
    }
}
