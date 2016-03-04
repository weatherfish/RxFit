package com.patloew.rxfit;

import android.support.annotation.NonNull;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.result.DailyTotalResult;

import rx.Observable;
import rx.Observer;

public class HistoryReadDailyTotalObservable extends BaseObservable<DataSet> {

    private final DataType dataType;

    static Observable<DataSet> create(@NonNull RxFit rxFit, @NonNull DataType dataType) {
        return Observable.create(new HistoryReadDailyTotalObservable(rxFit, dataType));
    }

    HistoryReadDailyTotalObservable(RxFit rxFit, DataType dataType) {
        super(rxFit);
        this.dataType = dataType;
    }

    @Override
    protected void onGoogleApiClientReady(GoogleApiClient apiClient, final Observer<? super DataSet> observer) {
        Fitness.HistoryApi.readDailyTotal(apiClient, dataType).setResultCallback(new ResultCallback<DailyTotalResult>() {
            @Override
            public void onResult(@NonNull DailyTotalResult dailyTotalResult) {
                if (!dailyTotalResult.getStatus().isSuccess()) {
                    observer.onError(new StatusException(dailyTotalResult.getStatus()));
                } else {
                    observer.onNext(dailyTotalResult.getTotal());
                    observer.onCompleted();
                }
            }
        });
    }
}
