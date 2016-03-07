package com.patloew.rxfit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.functions.Action0;
import rx.subscriptions.Subscriptions;

/* Copyright (C) 2015 Michał Charmas (http://blog.charmas.pl)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * ---------------
 *
 * FILE MODIFIED by Patrick Löwenstein, 2016
 *
 */
public abstract class BaseObservable<T> implements Observable.OnSubscribe<T> {
    private static final List<BaseObservable> observableList = new ArrayList<>();

    private final Context ctx;
    private final Api<? extends Api.ApiOptions.NotRequiredOptions>[] services;
    private final Scope[] scopes;
    private final boolean handleResolution;
    private GoogleApiClient apiClient;
    Subscriber<? super T> subscriber;

    protected final Long timeoutTime;
    protected final TimeUnit timeoutUnit;

    protected BaseObservable(@NonNull RxFit rxFit, Long timeout, TimeUnit timeUnit) {
        this.ctx = rxFit.getContext();
        this.services = rxFit.getApis();
        this.scopes = rxFit.getScopes();
        handleResolution = true;

        if(timeout != null && timeUnit != null) {
            this.timeoutTime = RxFit.getTimeout(timeout);
            this.timeoutUnit = RxFit.getTimeoutUnit(timeUnit);
        } else {
            this.timeoutTime = RxFit.getTimeout(null);
            this.timeoutUnit = RxFit.getTimeoutUnit(null);
        }
    }

    protected BaseObservable(@NonNull Context ctx, @NonNull Api<? extends Api.ApiOptions.NotRequiredOptions>[] services, Scope[] scopes) {
        this.ctx = ctx;
        this.services = services;
        this.scopes = scopes;
        handleResolution = false;
        timeoutTime = null;
        timeoutUnit = null;
    }

    protected <T extends Result> void setupFitnessPendingResult(PendingResult<T> pendingResult, ResultCallback<? super T> resultCallback) {
        if(timeoutTime != null && timeoutUnit != null) {
            pendingResult.setResultCallback(resultCallback, timeoutTime, timeoutUnit);
        } else {
            pendingResult.setResultCallback(resultCallback);
        }
    }

    @Override
    public void call(Subscriber<? super T> subscriber) {
        this.subscriber = subscriber;

        apiClient = createApiClient(subscriber);

        try {
            apiClient.connect();
        } catch (Throwable ex) {
            subscriber.onError(ex);
        }

        subscriber.add(Subscriptions.create(new Action0() {
            @Override
            public void call() {
                if (apiClient.isConnected() || apiClient.isConnecting()) {
                    onUnsubscribed(apiClient);
                    apiClient.disconnect();
                }
            }
        }));
    }


    protected GoogleApiClient createApiClient(Subscriber<? super T> subscriber) {

        ApiClientConnectionCallbacks apiClientConnectionCallbacks = new ApiClientConnectionCallbacks(subscriber);

        GoogleApiClient.Builder apiClientBuilder = new GoogleApiClient.Builder(ctx);


        for (Api<? extends Api.ApiOptions.NotRequiredOptions> service : services) {
            apiClientBuilder.addApi(service);
        }

        if(scopes != null) {
            for (Scope scope : scopes) {
                apiClientBuilder.addScope(scope);
            }
        }

        apiClientBuilder.addConnectionCallbacks(apiClientConnectionCallbacks);
        apiClientBuilder.addOnConnectionFailedListener(apiClientConnectionCallbacks);

        GoogleApiClient apiClient = apiClientBuilder.build();

        apiClientConnectionCallbacks.setClient(apiClient);

        return apiClient;

    }

    protected void onUnsubscribed(GoogleApiClient locationClient) { }

    protected abstract void onGoogleApiClientReady(GoogleApiClient apiClient, Observer<? super T> observer);

    private class ApiClientConnectionCallbacks implements
            GoogleApiClient.ConnectionCallbacks,
            GoogleApiClient.OnConnectionFailedListener {

        final private Observer<? super T> observer;

        private GoogleApiClient apiClient;

        private ApiClientConnectionCallbacks(Observer<? super T> observer) {
            this.observer = observer;
        }

        @Override
        public void onConnected(Bundle bundle) {
            try {
                onGoogleApiClientReady(apiClient, observer);
            } catch (Throwable ex) {
                observer.onError(ex);
            }
        }

        @Override
        public void onConnectionSuspended(int cause) {
            observer.onError(new GoogleAPIConnectionSuspendedException(cause));
        }

        @Override
        public void onConnectionFailed(ConnectionResult connectionResult) {
            if(handleResolution && connectionResult.hasResolution()) {
                observableList.add(BaseObservable.this);

                if(!ResolutionActivity.isResolutionShown()) {
                    Intent intent = new Intent(ctx, ResolutionActivity.class);
                    intent.putExtra(ResolutionActivity.ARG_CONNECTION_RESULT, connectionResult);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    ctx.startActivity(intent);
                }
            } else {
                observer.onError(new GoogleAPIConnectionException("Error connecting to GoogleApiClient.", connectionResult));
            }
        }

        public void setClient(GoogleApiClient client) {
            this.apiClient = client;
        }
    }

    static void onResolutionResult(int resultCode, ConnectionResult connectionResult) {
        for(BaseObservable observable : observableList) {
            if(!observable.subscriber.isUnsubscribed()) {
                if (resultCode == Activity.RESULT_OK && observable.apiClient != null) {
                    observable.apiClient.connect();
                } else {
                    observable.subscriber.onError(new GoogleAPIConnectionException("Error connecting to GoogleApiClient, resolution was not successful.", connectionResult));
                }
            }
        }

        observableList.clear();
    }
}
