package com.patloew.rxfit;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.DataSet;

import java.util.concurrent.TimeUnit;

import rx.Observer;

public class HistoryInsertDataObservable extends BaseObservable<Status> {

    private final DataSet dataSet;

    HistoryInsertDataObservable(RxFit rxFit, DataSet dataSet, Long timeout, TimeUnit timeUnit) {
        super(rxFit, timeout, timeUnit);
        this.dataSet = dataSet;
    }

    @Override
    protected void onGoogleApiClientReady(GoogleApiClient apiClient, final Observer<? super Status> observer) {
        setupFitnessPendingResult(Fitness.HistoryApi.insertData(apiClient, dataSet), new StatusResultCallBack(observer));
    }
}
