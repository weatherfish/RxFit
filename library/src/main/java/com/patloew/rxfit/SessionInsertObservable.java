package com.patloew.rxfit;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.request.SessionInsertRequest;

import java.util.concurrent.TimeUnit;

import rx.Observer;

public class SessionInsertObservable extends BaseObservable<Status> {

    private final SessionInsertRequest sessionInsertRequest;

    SessionInsertObservable(RxFit rxFit, SessionInsertRequest sessionInsertRequest, Long timeout, TimeUnit timeUnit) {
        super(rxFit, timeout, timeUnit);
        this.sessionInsertRequest = sessionInsertRequest;
    }

    @Override
    protected void onGoogleApiClientReady(GoogleApiClient apiClient, final Observer<? super Status> observer) {
        setupFitnessPendingResult(Fitness.SessionsApi.insertSession(apiClient, sessionInsertRequest), new StatusResultCallBack(observer));
    }
}
