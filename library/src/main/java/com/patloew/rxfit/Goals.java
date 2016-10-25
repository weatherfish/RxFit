package com.patloew.rxfit;

import android.support.annotation.NonNull;

import com.google.android.gms.fitness.data.Goal;
import com.google.android.gms.fitness.request.GoalsReadRequest;

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
public class Goals {

    private final RxFit rxFit;

    Goals(RxFit rxFit) {
        this.rxFit = rxFit;
    }


    // read current

    public Observable<Goal> readCurrent(@NonNull GoalsReadRequest goalsReadRequest) {
        return readCurrentInternal(goalsReadRequest, null, null);
    }

    public Observable<Goal> readCurrent(@NonNull GoalsReadRequest goalsReadRequest, long timeout, @NonNull TimeUnit timeUnit) {
        return readCurrentInternal(goalsReadRequest, timeout, timeUnit);
    }

    private Observable<Goal> readCurrentInternal(GoalsReadRequest goalsReadRequest, Long timeout, TimeUnit timeUnit) {
        return Single.create(new GoalsReadCurrentSingle(rxFit, goalsReadRequest, timeout, timeUnit))
                .flatMapObservable(new Func1<List<Goal>, Observable<? extends Goal>>() {
                    @Override
                    public Observable<? extends Goal> call(List<Goal> goals) {
                        return Observable.from(goals);
                    }
                });
    }

}
