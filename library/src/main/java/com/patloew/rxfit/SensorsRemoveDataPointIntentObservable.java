package com.patloew.rxfit;

import android.app.PendingIntent;
import android.support.annotation.NonNull;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.fitness.Fitness;

import rx.Observable;
import rx.Observer;

public class SensorsRemoveDataPointIntentObservable extends BaseObservable<Status> {

    private final PendingIntent pendingIntent;

    static Observable<Status> create(@NonNull RxFit rxFit, @NonNull PendingIntent pendingIntent) {
        return Observable.create(new SensorsRemoveDataPointIntentObservable(rxFit, pendingIntent));
    }

    SensorsRemoveDataPointIntentObservable(RxFit rxFit, PendingIntent pendingIntent) {
        super(rxFit);
        this.pendingIntent = pendingIntent;
    }

    @Override
    protected void onGoogleApiClientReady(GoogleApiClient apiClient, final Observer<? super Status> observer) {
        Fitness.SensorsApi.remove(apiClient, pendingIntent).setResultCallback(new StatusResultCallBack(observer));
    }
}
