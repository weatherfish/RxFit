package com.patloew.rxfit;

import android.app.PendingIntent;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.request.SensorRequest;

import java.util.concurrent.TimeUnit;

import rx.Observer;

public class SensorsAddDataPointIntentObservable extends BaseObservable<Status> {

    private final SensorRequest sensorRequest;
    private final PendingIntent pendingIntent;

    SensorsAddDataPointIntentObservable(RxFit rxFit, SensorRequest sensorRequest, PendingIntent pendingIntent, Long timeout, TimeUnit timeUnit) {
        super(rxFit, timeout, timeUnit);
        this.sensorRequest = sensorRequest;
        this.pendingIntent = pendingIntent;
    }

    @Override
    protected void onGoogleApiClientReady(GoogleApiClient apiClient, final Observer<? super Status> observer) {
        setupFitnessPendingResult(Fitness.SensorsApi.add(apiClient, sensorRequest, pendingIntent), new StatusResultCallBack(observer));
    }
}
