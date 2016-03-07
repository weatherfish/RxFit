package com.patloew.rxfit;

import android.app.PendingIntent;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.fitness.Fitness;

import java.util.concurrent.TimeUnit;

import rx.Observer;

public class SessionRegisterObservable extends BaseObservable<Status> {

    private final PendingIntent pendingIntent;

    SessionRegisterObservable(RxFit rxFit, PendingIntent pendingIntent, Long timeout, TimeUnit timeUnit) {
        super(rxFit, timeout, timeUnit);
        this.pendingIntent = pendingIntent;
    }

    @Override
    protected void onGoogleApiClientReady(GoogleApiClient apiClient, final Observer<? super Status> observer) {
        setupFitnessPendingResult(Fitness.SessionsApi.registerForSessions(apiClient, pendingIntent), new StatusResultCallBack(observer));
    }
}
