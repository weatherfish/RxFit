package com.patloew.rxfit;

import android.support.annotation.NonNull;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.request.OnDataPointListener;
import com.google.android.gms.fitness.request.SensorRequest;

import java.util.concurrent.TimeUnit;

import rx.Observer;

public class SensorsDataPointObservable extends BaseObservable<DataPoint> {

    private final SensorRequest sensorRequest;
    private OnDataPointListener dataPointListener = null;

    SensorsDataPointObservable(RxFit rxFit, SensorRequest sensorRequest, Long timeout, TimeUnit timeUnit) {
        super(rxFit, timeout, timeUnit);
        this.sensorRequest = sensorRequest;
    }

    @Override
    protected void onGoogleApiClientReady(GoogleApiClient apiClient, final Observer<? super DataPoint> observer) {
        dataPointListener = new OnDataPointListener() {
            @Override
            public void onDataPoint(DataPoint dataPoint) {
                observer.onNext(dataPoint);
            }
        };

        setupFitnessPendingResult(Fitness.SensorsApi.add(apiClient, sensorRequest, dataPointListener), new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                if (!status.isSuccess()) {
                    observer.onError(new StatusException(status));
                }
            }
        });
    }

    @Override
    protected void onUnsubscribed(GoogleApiClient apiClient) {
        Fitness.SensorsApi.remove(apiClient, dataPointListener);
    }
}
