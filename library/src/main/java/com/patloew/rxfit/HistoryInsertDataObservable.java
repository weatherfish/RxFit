package com.patloew.rxfit;

import android.support.annotation.NonNull;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.DataSet;

import rx.Observable;
import rx.Observer;

public class HistoryInsertDataObservable extends BaseObservable<Status> {

    private final DataSet dataSet;

    static Observable<Status> create(@NonNull RxFit rxFit, @NonNull DataSet dataSet) {
        return Observable.create(new HistoryInsertDataObservable(rxFit, dataSet));
    }

    HistoryInsertDataObservable(RxFit rxFit, DataSet dataSet) {
        super(rxFit);
        this.dataSet = dataSet;
    }

    @Override
    protected void onGoogleApiClientReady(GoogleApiClient apiClient, final Observer<? super Status> observer) {
        Fitness.HistoryApi.insertData(apiClient, dataSet).setResultCallback(new StatusResultCallBack(observer));
    }
}
