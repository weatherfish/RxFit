package com.patloew.rxfit;

import android.support.annotation.NonNull;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.Session;
import com.google.android.gms.fitness.result.SessionStopResult;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observer;

public class SessionStopObservable extends BaseObservable<List<Session>> {

    private final String identifier;

    SessionStopObservable(RxFit rxFit, String identifier, Long timeout, TimeUnit timeUnit) {
        super(rxFit, timeout, timeUnit);
        this.identifier = identifier;
    }

    @Override
    protected void onGoogleApiClientReady(GoogleApiClient apiClient, final Observer<? super List<Session>> observer) {
        setupFitnessPendingResult(Fitness.SessionsApi.stopSession(apiClient, identifier), new ResultCallback<SessionStopResult>() {
            @Override
            public void onResult(@NonNull SessionStopResult sessionStopResult) {
                if (!sessionStopResult.getStatus().isSuccess()) {
                    observer.onError(new StatusException(sessionStopResult.getStatus()));
                } else {
                    observer.onNext(sessionStopResult.getSessions());
                    observer.onCompleted();
                }
            }
        });
    }
}
