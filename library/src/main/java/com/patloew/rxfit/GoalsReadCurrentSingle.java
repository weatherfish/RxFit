package com.patloew.rxfit;

import android.support.annotation.NonNull;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.Goal;
import com.google.android.gms.fitness.request.GoalsReadRequest;
import com.google.android.gms.fitness.result.GoalsResult;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.SingleSubscriber;

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
class GoalsReadCurrentSingle extends BaseSingle<List<Goal>> {

    final GoalsReadRequest goalsReadRequest;

    GoalsReadCurrentSingle(RxFit rxFit, GoalsReadRequest goalsReadRequest, Long timeout, TimeUnit timeUnit) {
        super(rxFit, timeout, timeUnit);
        this.goalsReadRequest = goalsReadRequest;
    }

    @Override
    protected void onGoogleApiClientReady(GoogleApiClient apiClient, final SingleSubscriber<? super List<Goal>> subscriber) {
        setupFitnessPendingResult(Fitness.GoalsApi.readCurrentGoals(apiClient, goalsReadRequest), new ResultCallback<GoalsResult>() {
            @Override
            public void onResult(@NonNull GoalsResult goalsResult) {
                if (!goalsResult.getStatus().isSuccess()) {
                    subscriber.onError(new StatusException(goalsResult.getStatus()));
                } else {
                    subscriber.onSuccess(goalsResult.getGoals());
                }
            }
        });
    }
}
