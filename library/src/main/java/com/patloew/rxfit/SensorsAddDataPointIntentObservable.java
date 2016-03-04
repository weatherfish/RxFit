package com.patloew.rxfit;

import android.app.PendingIntent;
import android.support.annotation.NonNull;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.request.SensorRequest;

import rx.Observable;
import rx.Observer;

public class SensorsAddDataPointIntentObservable extends BaseObservable<Status> {

    private final SensorRequest sensorRequest;
    private final PendingIntent pendingIntent;

    static Observable<Status> create(@NonNull RxFit rxFit, @NonNull SensorRequest sensorRequest, @NonNull PendingIntent pendingIntent) {
        return Observable.create(new SensorsAddDataPointIntentObservable(rxFit, sensorRequest, pendingIntent));
    }

    SensorsAddDataPointIntentObservable(RxFit rxFit, SensorRequest sensorRequest, PendingIntent pendingIntent) {
        super(rxFit);
        this.sensorRequest = sensorRequest;
        this.pendingIntent = pendingIntent;
    }

    @Override
    protected void onGoogleApiClientReady(GoogleApiClient apiClient, final Observer<? super Status> observer) {
        Fitness.SensorsApi.add(apiClient, sensorRequest, pendingIntent).setResultCallback(new StatusResultCallBack(observer));
    }
}
