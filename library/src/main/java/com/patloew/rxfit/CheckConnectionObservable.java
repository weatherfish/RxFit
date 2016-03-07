package com.patloew.rxfit;

import com.google.android.gms.common.api.GoogleApiClient;

import rx.Observer;

public class CheckConnectionObservable extends BaseObservable<Void> {

    CheckConnectionObservable(RxFit rxFit) {
        super(rxFit, null, null);
    }

    @Override
    protected void onGoogleApiClientReady(GoogleApiClient apiClient, Observer<? super Void> observer) {
        observer.onCompleted();
    }
}
