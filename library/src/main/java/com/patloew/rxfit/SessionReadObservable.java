package com.patloew.rxfit;

import android.support.annotation.NonNull;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.request.SessionReadRequest;
import com.google.android.gms.fitness.result.SessionReadResult;

import java.util.concurrent.TimeUnit;

import rx.Observer;

public class SessionReadObservable extends BaseObservable<SessionReadResult> {

    private final SessionReadRequest sessionReadRequest;

    SessionReadObservable(RxFit rxFit, SessionReadRequest sessionReadRequest, Long timeout, TimeUnit timeUnit) {
        super(rxFit, timeout, timeUnit);
        this.sessionReadRequest = sessionReadRequest;
    }

    @Override
    protected void onGoogleApiClientReady(GoogleApiClient apiClient, final Observer<? super SessionReadResult> observer) {
        setupFitnessPendingResult(Fitness.SessionsApi.readSession(apiClient, sessionReadRequest), new ResultCallback<SessionReadResult>() {
            @Override
            public void onResult(@NonNull SessionReadResult sessionReadResult) {
                if (!sessionReadResult.getStatus().isSuccess()) {
                    observer.onError(new StatusException(sessionReadResult.getStatus()));
                } else {
                    observer.onNext(sessionReadResult);
                    observer.onCompleted();
                }
            }
        });
    }
}
