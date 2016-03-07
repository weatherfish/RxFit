package com.patloew.rxfit;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.DataSource;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Subscription;

import java.util.concurrent.TimeUnit;

import rx.Observer;

public class RecordingUnsubscribeObservable extends BaseObservable<Status> {

    private final DataSource dataSource;
    private final DataType dataType;
    private final Subscription subscription;

    RecordingUnsubscribeObservable(RxFit rxFit, DataSource dataSource, DataType dataType, Subscription subscription, Long timeout, TimeUnit timeUnit) {
        super(rxFit, timeout, timeUnit);
        this.dataSource = dataSource;
        this.dataType = dataType;
        this.subscription = subscription;
    }

    @Override
    protected void onGoogleApiClientReady(GoogleApiClient apiClient, final Observer<? super Status> observer) {
        ResultCallback<Status> resultCallback = new StatusResultCallBack(observer);

        if(dataSource != null) {
            setupFitnessPendingResult(Fitness.RecordingApi.unsubscribe(apiClient, dataSource), resultCallback);
        } else if(dataType != null) {
            setupFitnessPendingResult(Fitness.RecordingApi.unsubscribe(apiClient, dataType), resultCallback);
        } else {
            setupFitnessPendingResult(Fitness.RecordingApi.unsubscribe(apiClient, subscription), resultCallback);
        }
    }
}
