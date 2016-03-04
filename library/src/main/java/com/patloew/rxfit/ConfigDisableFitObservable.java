package com.patloew.rxfit;

import android.support.annotation.NonNull;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.fitness.Fitness;

import rx.Observable;
import rx.Observer;

public class ConfigDisableFitObservable extends BaseObservable<Status> {

    static Observable<Status> create(@NonNull RxFit rxFit) {
        return Observable.create(new ConfigDisableFitObservable(rxFit));
    }

    ConfigDisableFitObservable(RxFit rxFit) {
        super(rxFit);
    }

    @Override
    protected void onGoogleApiClientReady(GoogleApiClient apiClient, final Observer<? super Status> observer) {
        Fitness.ConfigApi.disableFit(apiClient).setResultCallback(new StatusResultCallBack(observer));
    }
}
