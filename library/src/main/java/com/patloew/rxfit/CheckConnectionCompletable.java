package com.patloew.rxfit;

import android.support.annotation.NonNull;

import com.google.android.gms.common.api.GoogleApiClient;

import rx.Completable;
import rx.Observable;
import rx.Observer;

public class CheckConnectionCompletable extends BaseObservable<Void> {

    static Completable create(@NonNull RxFit rxFit) {
        return Completable.fromObservable(Observable.create(new CheckConnectionCompletable(rxFit)));
    }

    CheckConnectionCompletable(RxFit rxFit) {
        super(rxFit);
    }

    @Override
    protected void onGoogleApiClientReady(GoogleApiClient apiClient, Observer<? super Void> observer) {
        observer.onCompleted();
    }
}
