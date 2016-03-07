package com.patloew.rxfit;

import android.support.annotation.NonNull;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.result.DailyTotalResult;

import java.util.concurrent.TimeUnit;

import rx.Observer;

public class HistoryReadDailyTotalObservable extends BaseObservable<DataSet> {

    private final DataType dataType;

    HistoryReadDailyTotalObservable(RxFit rxFit, DataType dataType, Long timeout, TimeUnit timeUnit) {
        super(rxFit, timeout, timeUnit);
        this.dataType = dataType;
    }

    @Override
    protected void onGoogleApiClientReady(GoogleApiClient apiClient, final Observer<? super DataSet> observer) {
        setupFitnessPendingResult(Fitness.HistoryApi.readDailyTotal(apiClient, dataType), new ResultCallback<DailyTotalResult>() {
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
