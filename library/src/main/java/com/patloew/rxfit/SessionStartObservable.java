package com.patloew.rxfit;

import android.support.annotation.NonNull;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.Session;

import rx.Observable;
import rx.Observer;

public class SessionStartObservable extends BaseObservable<Status> {

    private final Session session;

    static Observable<Status> create(@NonNull RxFit rxFit, @NonNull Session session) {
        return Observable.create(new SessionStartObservable(rxFit, session));
    }

    SessionStartObservable(RxFit rxFit, Session session) {
        super(rxFit);
        this.session = session;
    }

    @Override
    protected void onGoogleApiClientReady(GoogleApiClient apiClient, final Observer<? super Status> observer) {
        Fitness.SessionsApi.startSession(apiClient, session).setResultCallback(new StatusResultCallBack(observer));
    }
}
