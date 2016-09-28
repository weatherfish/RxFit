package com.patloew.rxfit;

import android.support.annotation.NonNull;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.fitness.data.DataSource;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Subscription;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Single;
import rx.functions.Func1;

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
public class Recording {

    private final RxFit rxFit;

    Recording(RxFit rxFit) {
        this.rxFit = rxFit;
    }

    // listSubscriptions

    public Observable<Subscription> listSubscriptions() {
        return listSubscriptionsInternal(null, null, null);
    }

    public Observable<Subscription> listSubscriptions(long timeout, @NonNull TimeUnit timeUnit) {
        return listSubscriptionsInternal(null, timeout, timeUnit);
    }

    public Observable<Subscription> listSubscriptions(DataType dataType) {
        return listSubscriptionsInternal(dataType, null, null);
    }

    public Observable<Subscription> listSubscriptions(DataType dataType, long timeout, @NonNull TimeUnit timeUnit) {
        return listSubscriptionsInternal(dataType, timeout, timeUnit);
    }

    private Observable<Subscription> listSubscriptionsInternal(DataType dataType, Long timeout, TimeUnit timeUnit) {
        return Single.create(new RecordingListSubscriptionsSingle(rxFit, dataType, timeout, timeUnit)).flatMapObservable(new Func1<List<Subscription>, Observable<? extends Subscription>>() {
                    @Override
                    public Observable<? extends Subscription> call(List<Subscription> subscriptions) {
                        return Observable.from(subscriptions);
                    }
                });
    }

    // subscribe

    public Single<Status> subscribe(@NonNull DataSource dataSource) {
        return subscribeInternal(dataSource, null, null, null);
    }

    public Single<Status> subscribe(@NonNull DataSource dataSource, long timeout, @NonNull TimeUnit timeUnit) {
        return subscribeInternal(dataSource, null, timeout, timeUnit);
    }

    public Single<Status> subscribe(@NonNull DataType dataType) {
        return subscribeInternal(null, dataType, null, null);
    }

    public Single<Status> subscribe(@NonNull DataType dataType, long timeout, @NonNull TimeUnit timeUnit) {
        return subscribeInternal(null, dataType, timeout, timeUnit);
    }

    private Single<Status> subscribeInternal(DataSource dataSource, DataType dataType, Long timeout, TimeUnit timeUnit) {
        return Single.create(new RecordingSubscribeSingle(rxFit, dataSource, dataType, timeout, timeUnit));
    }

    // unsubscribe

    public Single<Status> unsubscribe(@NonNull DataSource dataSource) {
        return unsubscribeInternal(dataSource, null, null, null, null);
    }

    public Single<Status> unsubscribe(@NonNull DataSource dataSource, long timeout, @NonNull TimeUnit timeUnit) {
        return unsubscribeInternal(dataSource, null, null, timeout, timeUnit);
    }

    public Single<Status> unsubscribe(@NonNull DataType dataType) {
        return unsubscribeInternal(null, dataType, null, null, null);
    }

    public Single<Status> unsubscribe(@NonNull DataType dataType, long timeout, @NonNull TimeUnit timeUnit) {
        return unsubscribeInternal(null, dataType, null, timeout, timeUnit);
    }

    public Single<Status> unsubscribe(@NonNull Subscription subscription) {
        return unsubscribeInternal(null, null, subscription, null, null);
    }

    public Single<Status> unsubscribe(@NonNull Subscription subscription, long timeout, @NonNull TimeUnit timeUnit) {
        return unsubscribeInternal(null, null, subscription, timeout, timeUnit);
    }

    private Single<Status> unsubscribeInternal(DataSource dataSource, DataType dataType, Subscription subscription, Long timeout, TimeUnit timeUnit) {
        return Single.create(new RecordingUnsubscribeSingle(rxFit, dataSource, dataType, subscription, timeout, timeUnit));
    }

}
