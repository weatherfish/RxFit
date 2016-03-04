package com.patloew.rxfit;

import android.support.annotation.NonNull;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.Session;
import com.google.android.gms.fitness.result.SessionStopResult;

import java.util.List;

import rx.Observable;
import rx.Observer;

public class SessionStopObservable extends BaseObservable<List<Session>> {

    private final String identifier;

    static Observable<List<Session>> create(@NonNull RxFit rxFit, @NonNull String identifier) {
        return Observable.create(new SessionStopObservable(rxFit, identifier));
    }

    SessionStopObservable(RxFit rxFit, String identifier) {
        super(rxFit);
        this.identifier = identifier;
    }

    @Override
    protected void onGoogleApiClientReady(GoogleApiClient apiClient, final Observer<? super List<Session>> observer) {
        Fitness.SessionsApi.stopSession(apiClient, identifier).setResultCallback(new ResultCallback<SessionStopResult>() {
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
