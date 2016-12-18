package com.patloew.rxfitsample;

import android.util.Log;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.fitness.data.Bucket;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.patloew.rxfit.RxFit;
import com.patloew.rxfit.RxFitOnExceptionResumeNext;
import com.patloew.rxfit.StatusException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

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
public class MainPresenter {

    private final CompositeDisposable disposable = new CompositeDisposable();

    private final RxFit rxFit;

    private MainView view;

    private List<FitnessSessionData> fitnessSessionDataList = new ArrayList<>();

    public MainPresenter(RxFit rxFit) {
        this.rxFit = rxFit;
    }

    public void attachView(MainView view) {
        this.view = view;
    }

    public void detachView() {
        this.view = null;
        disposable.clear();
    }

    void getFitnessData() {
        fitnessSessionDataList.clear();
        view.showLoading();

        DataReadRequest.Builder dataReadRequestBuilder = getDataReadRequestBuilder();
        DataReadRequest dataReadRequest = dataReadRequestBuilder.build();
        DataReadRequest dataReadRequestServer = dataReadRequestBuilder.enableServerQueries().build();

        // First, request all data from the server. If there is an error (e.g. timeout),
        // switch to normal request
        disposable.add(
                rxFit.history().readBuckets(dataReadRequestServer)
                    .doOnError(throwable -> { if(throwable instanceof StatusException && ((StatusException)throwable).getStatus().getStatusCode() == CommonStatusCodes.TIMEOUT) Log.e("MainActivity", "Timeout on server query request", throwable); })
                    .compose(RxFitOnExceptionResumeNext.with(rxFit.history().readBuckets(dataReadRequest)))
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::onBucketLoaded, this::onBucketLoadError, () -> view.onFitnessSessionDataLoaded(fitnessSessionDataList))
        );
    }

    private static DataReadRequest.Builder getDataReadRequestBuilder() {
        return new DataReadRequest.Builder()
                .aggregate(DataType.TYPE_STEP_COUNT_DELTA, DataType.AGGREGATE_STEP_COUNT_DELTA)
                .aggregate(DataType.TYPE_CALORIES_EXPENDED, DataType.AGGREGATE_CALORIES_EXPENDED)
                .bucketBySession(1, TimeUnit.MINUTES)
                // 2 weeks back
                .setTimeRange(System.currentTimeMillis() - 1209600000L, System.currentTimeMillis(), TimeUnit.MILLISECONDS);
    }

    private void onBucketLoadError(Throwable throwable) {
        Log.e("MainPresenter", "Error reading fitness data", throwable);
        view.showRetrySnackbar();
    }

    private void onBucketLoaded(Bucket bucket) {
        FitnessSessionData fitnessSessionData = new FitnessSessionData();
        fitnessSessionData.name = bucket.getSession().getName();
        fitnessSessionData.appName = bucket.getSession().getAppPackageName();
        fitnessSessionData.activity = bucket.getSession().getActivity();
        fitnessSessionData.start = new Date(bucket.getSession().getStartTime(TimeUnit.MILLISECONDS));
        fitnessSessionData.end = new Date(bucket.getSession().getEndTime(TimeUnit.MILLISECONDS));

        if(bucket.getDataSet(DataType.AGGREGATE_STEP_COUNT_DELTA).getDataPoints().isEmpty()) {
            fitnessSessionData.steps = 0;
        } else {
            fitnessSessionData.steps = bucket.getDataSet(DataType.AGGREGATE_STEP_COUNT_DELTA).getDataPoints().get(0).getValue(Field.FIELD_STEPS).asInt();
        }

        if(bucket.getDataSet(DataType.AGGREGATE_CALORIES_EXPENDED).getDataPoints().isEmpty()) {
            fitnessSessionData.calories = 0;
        } else {
            fitnessSessionData.calories = (int) bucket.getDataSet(DataType.AGGREGATE_CALORIES_EXPENDED).getDataPoints().get(0).getValue(Field.FIELD_CALORIES).asFloat();
        }

        fitnessSessionDataList.add(fitnessSessionData);

    }


}
