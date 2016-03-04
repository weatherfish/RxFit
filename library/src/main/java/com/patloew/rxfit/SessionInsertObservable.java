package com.patloew.rxfit;

import android.support.annotation.NonNull;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.request.SessionInsertRequest;

import rx.Observable;
import rx.Observer;

public class SessionInsertObservable extends BaseObservable<Status> {

    private final SessionInsertRequest sessionInsertRequest;

    static Observable<Status> create(@NonNull RxFit rxFit, @NonNull SessionInsertRequest sessionInsertRequest) {
        return Observable.create(new SessionInsertObservable(rxFit, sessionInsertRequest));
    }

    SessionInsertObservable(RxFit rxFit, SessionInsertRequest sessionInsertRequest) {
        super(rxFit);
        this.sessionInsertRequest = sessionInsertRequest;
    }

    @Override
    protected void onGoogleApiClientReady(GoogleApiClient apiClient, final Observer<? super Status> observer) {
        Fitness.SessionsApi.insertSession(apiClient, sessionInsertRequest).setResultCallback(new StatusResultCallBack(observer));
    }
}
