package com.patloew.rxfit;

import android.support.annotation.NonNull;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.DataSource;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Subscription;

import rx.Observable;
import rx.Observer;

public class RecordingUnsubscribeObservable extends BaseObservable<Status> {

    private final DataSource dataSource;
    private final DataType dataType;
    private final Subscription subscription;

    static Observable<Status> create(@NonNull RxFit rxFit, @NonNull DataSource dataSource) {
        return Observable.create(new RecordingUnsubscribeObservable(rxFit, dataSource, null, null));
    }

    static Observable<Status> create(@NonNull RxFit rxFit, @NonNull DataType dataType) {
        return Observable.create(new RecordingUnsubscribeObservable(rxFit, null, dataType, null));
    }

    static Observable<Status> create(@NonNull RxFit rxFit, @NonNull Subscription subscription) {
        return Observable.create(new RecordingUnsubscribeObservable(rxFit, null, null, subscription));
    }

    RecordingUnsubscribeObservable(RxFit rxFit, DataSource dataSource, DataType dataType, Subscription subscription) {
        super(rxFit);
        this.dataSource = dataSource;
        this.dataType = dataType;
        this.subscription = subscription;
    }

    @Override
    protected void onGoogleApiClientReady(GoogleApiClient apiClient, final Observer<? super Status> observer) {
        ResultCallback<Status> resultCallback = new StatusResultCallBack(observer);

        if(dataSource != null) {
            Fitness.RecordingApi.unsubscribe(apiClient, dataSource).setResultCallback(resultCallback);
        } else if(dataType != null) {
            Fitness.RecordingApi.unsubscribe(apiClient, dataType).setResultCallback(resultCallback);
        } else {
            Fitness.RecordingApi.unsubscribe(apiClient, subscription).setResultCallback(resultCallback);
        }
    }
}
