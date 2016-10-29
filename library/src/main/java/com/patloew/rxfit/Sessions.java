package com.patloew.rxfit;

import android.app.PendingIntent;
import android.support.annotation.NonNull;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.fitness.data.Session;
import com.google.android.gms.fitness.request.SessionInsertRequest;
import com.google.android.gms.fitness.request.SessionReadRequest;
import com.google.android.gms.fitness.result.SessionReadResult;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Single;

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
 * limitations under the License. */
public class Sessions {

    private final RxFit rxFit;

    Sessions(RxFit rxFit) {
        this.rxFit = rxFit;
    }

    // insert

    public Single<Status> insert(@NonNull SessionInsertRequest sessionInsertRequest) {
        return insertInternal(sessionInsertRequest, null, null);
    }

    public Single<Status> insert(@NonNull SessionInsertRequest sessionInsertRequest, long timeout, @NonNull TimeUnit timeUnit) {
        return insertInternal(sessionInsertRequest, timeout, timeUnit);
    }

    private Single<Status> insertInternal(SessionInsertRequest sessionInsertRequest, Long timeout, TimeUnit timeUnit) {
        return Single.create(new SessionInsertSingle(rxFit, sessionInsertRequest, timeout, timeUnit));
    }

    // read

    public Single<SessionReadResult> read(@NonNull SessionReadRequest sessionReadRequest) {
        return readInternal(sessionReadRequest, null, null);
    }

    public Single<SessionReadResult> read(@NonNull SessionReadRequest sessionReadRequest, long timeout, @NonNull TimeUnit timeUnit) {
        return readInternal(sessionReadRequest, timeout, timeUnit);
    }

    private Single<SessionReadResult> readInternal(SessionReadRequest sessionReadRequest, Long timeout, TimeUnit timeUnit) {
        return Single.create(new SessionReadSingle(rxFit, sessionReadRequest, timeout, timeUnit));
    }

    // registerForSessions

    public Single<Status> registerForSessions(@NonNull PendingIntent pendingIntent) {
        return registerForSessionsInternal(pendingIntent, null, null);
    }

    public Single<Status> registerForSessions(@NonNull PendingIntent pendingIntent, long timeout, @NonNull TimeUnit timeUnit) {
        return registerForSessionsInternal(pendingIntent, timeout, timeUnit);
    }

    private Single<Status> registerForSessionsInternal(PendingIntent pendingIntent, Long timeout, TimeUnit timeUnit) {
        return Single.create(new SessionRegisterSingle(rxFit, pendingIntent, timeout, timeUnit));
    }

    // unregisterForSessions

    public Single<Status> unregisterForSessions(@NonNull PendingIntent pendingIntent) {
        return unregisterForSessionsInternal(pendingIntent, null, null);
    }

    public Single<Status> unregisterForSessions(@NonNull PendingIntent pendingIntent, long timeout, @NonNull TimeUnit timeUnit) {
        return unregisterForSessionsInternal(pendingIntent, timeout, timeUnit);
    }

    private Single<Status> unregisterForSessionsInternal(PendingIntent pendingIntent, Long timeout, TimeUnit timeUnit) {
        return Single.create(new SessionUnregisterSingle(rxFit, pendingIntent, timeout, timeUnit));
    }

    // start

    public Single<Status> start(@NonNull Session session) {
        return startInternal(session, null, null);
    }

    public Single<Status> start(@NonNull Session session, long timeout, @NonNull TimeUnit timeUnit) {
        return startInternal(session, timeout, timeUnit);
    }

    private Single<Status> startInternal(Session session, Long timeout, TimeUnit timeUnit) {
        return Single.create(new SessionStartSingle(rxFit, session, timeout, timeUnit));
    }

    // stop

    public Observable<Session> stop(@NonNull String identifier) {
        return stopInternal(identifier, null, null);
    }

    public Observable<Session> stop(@NonNull String identifier, long timeout, @NonNull TimeUnit timeUnit) {
        return stopInternal(identifier, timeout, timeUnit);
    }

    private Observable<Session> stopInternal(String identifier, Long timeout, TimeUnit timeUnit) {
        return Single.create(new SessionStopSingle(rxFit, identifier, timeout, timeUnit))
                .flatMapObservable(Observable::from);
    }


}
