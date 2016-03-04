package com.patloew.rxfit;

import android.support.annotation.NonNull;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.request.SessionReadRequest;
import com.google.android.gms.fitness.result.SessionReadResult;

import rx.Observable;
import rx.Observer;

public class SessionReadObservable extends BaseObservable<SessionReadResult> {

    private final SessionReadRequest sessionReadRequest;

    static Observable<SessionReadResult> create(@NonNull RxFit rxFit, @NonNull SessionReadRequest sessionReadRequest) {
        return Observable.create(new SessionReadObservable(rxFit, sessionReadRequest));
    }

    SessionReadObservable(RxFit rxFit, SessionReadRequest sessionReadRequest) {
        super(rxFit);
        this.sessionReadRequest = sessionReadRequest;
    }

    @Override
    protected void onGoogleApiClientReady(GoogleApiClient apiClient, final Observer<? super SessionReadResult> observer) {
        Fitness.SessionsApi.readSession(apiClient, sessionReadRequest).setResultCallback(new ResultCallback<SessionReadResult>() {
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
