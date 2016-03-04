package com.patloew.rxfit;

import android.support.annotation.NonNull;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Subscription;
import com.google.android.gms.fitness.result.ListSubscriptionsResult;

import java.util.List;

import rx.Observable;
import rx.Observer;

public class RecordingListSubscriptionsObservable extends BaseObservable<List<Subscription>> {

    private final DataType dataType;

    static Observable<List<Subscription>> create(@NonNull RxFit rxFit) {
        return Observable.create(new RecordingListSubscriptionsObservable(rxFit, null));
    }

    static Observable<List<Subscription>> create(@NonNull RxFit rxFit, DataType dataType) {
        return Observable.create(new RecordingListSubscriptionsObservable(rxFit, dataType));
    }

    RecordingListSubscriptionsObservable(RxFit rxFit, DataType dataType) {
        super(rxFit);
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
            Fitness.RecordingApi.listSubscriptions(apiClient).setResultCallback(resultCallback);
        } else {
            Fitness.RecordingApi.listSubscriptions(apiClient, dataType).setResultCallback(resultCallback);
        }
    }
}
