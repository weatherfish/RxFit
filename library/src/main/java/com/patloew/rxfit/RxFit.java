package com.patloew.rxfit;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Scope;

import java.util.concurrent.TimeUnit;

import rx.Completable;
import rx.Observable;

/* Copyright 2016 Patrick Löwenstein
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * -----------------------------
 *
 * Make sure to include all the APIs and Scopes that you need for your app.
 * Also make sure to have the Location and Body Sensors permission on
 * Marshmallow, if they are needed by your Fit API requests.
 */
public class RxFit {

    Long timeoutTime = null;
    TimeUnit timeoutUnit = null;

    final Context ctx;
    final Api<? extends Api.ApiOptions.NotRequiredOptions>[] apis;
    final Scope[] scopes;

    private final Ble ble = new Ble(this);
    private final Config config = new Config(this);
    private final Goals goals = new Goals(this);
    private final History history = new History(this);
    private final Recording recording = new Recording(this);
    private final Sensors sensors = new Sensors(this);
    private final Sessions sessions = new Sessions(this);


    /* Creates a new RxFit instance.
     *
     * @param ctx Context.
     * @param apis An array of Fitness APIs to be used in your app.
     * @param scopes An array of the Scopes to be requested for your app.
     */
    public RxFit(@NonNull Context ctx, @NonNull Api<? extends Api.ApiOptions.NotRequiredOptions>[] apis, @NonNull Scope[] scopes) {
        this.ctx = ctx.getApplicationContext();
        this.apis = apis;
        this.scopes = scopes;
    }

    /* Set a default timeout for all requests to the Fit API made in the lib.
     * When a timeout occurs, onError() is called with a StatusException.
     */
    public void setDefaultTimeout(long time, @NonNull TimeUnit timeUnit) {
        if(timeUnit != null) {
            timeoutTime = time;
            timeoutUnit = timeUnit;
        } else {
            throw new IllegalArgumentException("timeUnit parameter must not be null");
        }
    }

    /* Reset the default timeout.
     */
    public void resetDefaultTimeout() {
        timeoutTime = null;
        timeoutUnit = null;
    }

    /* Can be used to check whether connection to Fit API was successful.
     * For example, a wear app might need to be notified, if the user
     * allowed accessing fitness data (which means that the connection
     * was successful). As an alternative, use doOnCompleted(...) and
     * doOnError(...) on any other RxFit Observable.
     *
     * This Completable completes if the connection was successful.
     */
    public Completable checkConnection() {
        return Completable.fromObservable(Observable.create(new CheckConnectionObservable(this)));
    }

    public Ble ble() {
        return ble;
    }

    public Config config() {
        return config;
    }

    public Goals goals() {
        return goals;
    }

    public History history() {
        return history;
    }

    public Recording recording() {
        return recording;
    }

    public Sensors sensors() {
        return sensors;
    }

    public Sessions sessions() {
        return sessions;
    }
}
