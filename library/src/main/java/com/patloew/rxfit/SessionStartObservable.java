package com.patloew.rxfit;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.Session;

import java.util.concurrent.TimeUnit;

import rx.Observer;

public class SessionStartObservable extends BaseObservable<Status> {

    private final Session session;

    SessionStartObservable(RxFit rxFit, Session session, Long timeout, TimeUnit timeUnit) {
        super(rxFit, timeout, timeUnit);
        this.session = session;
    }

    @Override
    protected void onGoogleApiClientReady(GoogleApiClient apiClient, final Observer<? super Status> observer) {
        setupFitnessPendingResult(Fitness.SessionsApi.startSession(apiClient, session), new StatusResultCallBack(observer));
    }
}
