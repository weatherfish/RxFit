package com.patloew.rxfit;

import android.support.annotation.NonNull;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.DataSource;
import com.google.android.gms.fitness.data.DataType;

import rx.Observable;
import rx.Observer;

public class RecordingSubscribeObservable extends BaseObservable<Status> {

    private final DataSource dataSource;
    private final DataType dataType;

    static Observable<Status> create(@NonNull RxFit rxFit, @NonNull DataSource dataSource) {
        return Observable.create(new RecordingSubscribeObservable(rxFit, dataSource, null));
    }

    static Observable<Status> create(@NonNull RxFit rxFit, @NonNull DataType dataType) {
        return Observable.create(new RecordingSubscribeObservable(rxFit, null, dataType));
    }

    RecordingSubscribeObservable(RxFit rxFit, DataSource dataSource, DataType dataType) {
        super(rxFit);
        this.dataSource = dataSource;
        this.dataType = dataType;
    }

    @Override
    protected void onGoogleApiClientReady(GoogleApiClient apiClient, final Observer<? super Status> observer) {
        ResultCallback<Status> resultCallback = new StatusResultCallBack(observer);

        if(dataSource != null) {
            Fitness.RecordingApi.subscribe(apiClient, dataSource).setResultCallback(resultCallback);
        } else {
            Fitness.RecordingApi.subscribe(apiClient, dataType).setResultCallback(resultCallback);
        }
    }
}
