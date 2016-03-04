package com.patloew.rxfit;

import android.support.annotation.NonNull;

import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import rx.Observer;

public class StatusResultCallBack implements ResultCallback<Status> {

    private final Observer<? super Status> observer;

    public StatusResultCallBack(@NonNull Observer<? super Status> observer) {
        this.observer = observer;
    }

    @Override
    public void onResult(@NonNull Status status) {
        if (!status.isSuccess()) {
            observer.onError(new StatusException(status));
        } else {
            observer.onNext(status);
            observer.onCompleted();
        }
    }
}
