package com.patloew.rxfit;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.fitness.Fitness;

import java.util.concurrent.TimeUnit;

import rx.Observer;

public class ConfigDisableFitObservable extends BaseObservable<Status> {

    ConfigDisableFitObservable(RxFit rxFit, Long timeout, TimeUnit timeUnit) {
        super(rxFit, timeout, timeUnit);
    }

    @Override
    protected void onGoogleApiClientReady(GoogleApiClient apiClient, final Observer<? super Status> observer) {
        setupFitnessPendingResult(Fitness.ConfigApi.disableFit(apiClient), new StatusResultCallBack(observer));
    }
}
