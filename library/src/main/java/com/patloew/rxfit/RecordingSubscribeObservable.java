package com.patloew.rxfit;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.DataSource;
import com.google.android.gms.fitness.data.DataType;

import java.util.concurrent.TimeUnit;

import rx.Observer;

public class RecordingSubscribeObservable extends BaseObservable<Status> {

    private final DataSource dataSource;
    private final DataType dataType;

    RecordingSubscribeObservable(RxFit rxFit, DataSource dataSource, DataType dataType, Long timeout, TimeUnit timeUnit) {
        super(rxFit, timeout, timeUnit);
        this.dataSource = dataSource;
        this.dataType = dataType;
    }

    @Override
    protected void onGoogleApiClientReady(GoogleApiClient apiClient, final Observer<? super Status> observer) {
        ResultCallback<Status> resultCallback = new StatusResultCallBack(observer);

        if(dataSource != null) {
            setupFitnessPendingResult(Fitness.RecordingApi.subscribe(apiClient, dataSource), resultCallback);
        } else {
            setupFitnessPendingResult(Fitness.RecordingApi.subscribe(apiClient, dataType), resultCallback);
        }
    }
}
