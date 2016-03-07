package com.patloew.rxfit;

import android.support.annotation.NonNull;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Subscription;
import com.google.android.gms.fitness.result.ListSubscriptionsResult;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observer;

public class RecordingListSubscriptionsObservable extends BaseObservable<List<Subscription>> {

    private final DataType dataType;

    RecordingListSubscriptionsObservable(RxFit rxFit, DataType dataType, Long timeout, TimeUnit timeUnit) {
        super(rxFit, timeout, timeUnit);
        this.dataType = dataType;
    }

    @Override
    protected void onGoogleApiClientReady(GoogleApiClient apiClient, final Observer<? super List<Subscription>> observer) {
        ResultCallback<ListSubscriptionsResult> resultCallback = new ResultCallback<ListSubscriptionsResult>() {
            @Override
            public void onResult(@NonNull ListSubscriptionsResult listSubscriptionsResult) {
                if(!listSubscriptionsResult.getStatus().isSuccess()) {
                    observer.onError(new StatusException(listSubscriptionsResult.getStatus()));
                } else {
                    observer.onNext(listSubscriptionsResult.getSubscriptions());
                    observer.onCompleted();
                }
            }
        };

        if(dataType == null) {
            setupFitnessPendingResult(Fitness.RecordingApi.listSubscriptions(apiClient), resultCallback);
        } else {
            setupFitnessPendingResult(Fitness.RecordingApi.listSubscriptions(apiClient, dataType), resultCallback);
        }
    }
}
