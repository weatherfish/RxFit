package com.patloew.rxfit;

import android.app.PendingIntent;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresPermission;

import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.fitness.data.BleDevice;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataSource;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Session;
import com.google.android.gms.fitness.data.Subscription;
import com.google.android.gms.fitness.request.BleScanCallback;
import com.google.android.gms.fitness.request.DataDeleteRequest;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.request.DataSourcesRequest;
import com.google.android.gms.fitness.request.DataTypeCreateRequest;
import com.google.android.gms.fitness.request.DataUpdateRequest;
import com.google.android.gms.fitness.request.SensorRequest;
import com.google.android.gms.fitness.request.SessionInsertRequest;
import com.google.android.gms.fitness.request.SessionReadRequest;
import com.google.android.gms.fitness.request.StartBleScanRequest;
import com.google.android.gms.fitness.result.DataReadResult;
import com.google.android.gms.fitness.result.SessionReadResult;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Completable;
import rx.Observable;
import rx.exceptions.Exceptions;
import rx.functions.Func1;

/* Factory for Google Fit API observables. Make sure to include all the APIs
 * and Scopes that you need for your app. Also make sure to have the Location
 * and Body Sensors permission on Marshmallow, if they are needed by your
 * Fit API requests.
 */
public class RxFit {

    private static RxFit instance = null;

    private static Long timeoutTime = null;
    private static TimeUnit timeoutUnit = null;

    private final Context ctx;
    private final Api<? extends Api.ApiOptions.NotRequiredOptions>[] apis;
    private final Scope[] scopes;

    /* Initializes the singleton instance of RxFit
     *
     * @param ctx Context.
     * @param apis An array of Fitness APIs to be used in your app.
     * @param scopes An array of the Scopes to be requested for your app.
     */
    public static void init(@NonNull Context ctx, @NonNull Api<? extends Api.ApiOptions.NotRequiredOptions>[] apis, @NonNull Scope[] scopes) {
        if(instance == null) { instance = new RxFit(ctx, apis, scopes); }
    }

    /* Set a default timeout for all requests to the Fit API made in the lib.
     * When a timeout occurs, onError() is called with a StatusException.
     */
    public static void setDefaultTimeout(long time, @NonNull TimeUnit timeUnit) {
        if(timeUnit != null) {
            timeoutTime = time;
            timeoutUnit = timeUnit;
        } else {
            throw new IllegalArgumentException("timeUnit parameter must not be null");
        }
    }

    /* Reset the default timeout.
     */
    public static void resetDefaultTimeout() {
        timeoutTime = null;
        timeoutUnit = null;
    }

    /* Gets the singleton instance of RxFit, after it was initialized.
     */
    private static RxFit get() {
        if(instance == null) { throw new IllegalStateException("RxFit not initialized"); }
        return instance;
    }


    private RxFit(@NonNull Context ctx, @NonNull Api<? extends Api.ApiOptions.NotRequiredOptions>[] apis, @NonNull Scope[] scopes) {
        this.ctx = ctx.getApplicationContext();
        this.apis = apis;
        this.scopes = scopes;
    }

    Context getContext() {
        return ctx;
    }

    Api<? extends Api.ApiOptions.NotRequiredOptions>[] getApis() {
        return apis;
    }

    Scope[] getScopes() {
        return scopes;
    }

    static Long getDefaultTimeout() {
        return timeoutTime;
    }

    static TimeUnit getDefaultTimeoutUnit() {
        return timeoutUnit;
    }


    /* Can be used to check whether connection to Fit API was successful.
     * For example, a wear app might need to be notified, if the user
     * allowed accessing fitness data (which means that the connection
     * was successful). As an alternative, use doOnCompleted(...) and
     * doOnError(...) on any other RxFit Observable.
     *
     * This Completable completes if the connection was successful.
     */
    public static Completable checkConnection() {
        return Completable.fromObservable(Observable.create(new CheckConnectionObservable(RxFit.get())));
    }


    public static class Ble {

        private Ble() { }

        public static Observable<Status> claimDevice(@NonNull BleDevice bleDevice) {
            return Observable.create(new BleClaimDeviceObservable(RxFit.get(), bleDevice, null, null, null));
        }

        public static Observable<Status> claimDevice(@NonNull BleDevice bleDevice, long timeout, @NonNull TimeUnit timeUnit) {
            return Observable.create(new BleClaimDeviceObservable(RxFit.get(), bleDevice, null, timeout, timeUnit));
        }

        public static Observable<Status> claimDevice(@NonNull String deviceAddress) {
            return Observable.create(new BleClaimDeviceObservable(RxFit.get(), null, deviceAddress, null, null));
        }

        public static Observable<Status> claimDevice(@NonNull String deviceAddress, long timeout, @NonNull TimeUnit timeUnit) {
            return Observable.create(new BleClaimDeviceObservable(RxFit.get(), null, deviceAddress, timeout, timeUnit));
        }

        public static Observable<List<BleDevice>> getClaimedDeviceList() {
            return Observable.create(new BleListClaimedDevicesObservable(RxFit.get(), null, null, null));
        }

        public static Observable<List<BleDevice>> getClaimedDeviceList(long timeout, @NonNull TimeUnit timeUnit) {
            return Observable.create(new BleListClaimedDevicesObservable(RxFit.get(), null, timeout, timeUnit));
        }

        public static Observable<List<BleDevice>> getClaimedDeviceList(DataType dataType) {
            return Observable.create(new BleListClaimedDevicesObservable(RxFit.get(), dataType, null, null));
        }

        public static Observable<List<BleDevice>> getClaimedDeviceList(DataType dataType, long timeout, @NonNull TimeUnit timeUnit) {
            return Observable.create(new BleListClaimedDevicesObservable(RxFit.get(), dataType, timeout, timeUnit));
        }

        @RequiresPermission("android.permission.BLUETOOTH_ADMIN")
        public static Observable<Status> startScan(@NonNull StartBleScanRequest startBleScanRequest) {
            return Observable.create(new BleStartScanObservable(RxFit.get(), startBleScanRequest, null, null));
        }

        @RequiresPermission("android.permission.BLUETOOTH_ADMIN")
        public static Observable<Status> startScan(@NonNull StartBleScanRequest startBleScanRequest, long timeout, @NonNull TimeUnit timeUnit) {
            return Observable.create(new BleStartScanObservable(RxFit.get(), startBleScanRequest, timeout, timeUnit));
        }

        public static Observable<Status> stopScan(@NonNull BleScanCallback bleScanCallback) {
            return Observable.create(new BleStopScanObservable(RxFit.get(), bleScanCallback, null, null));
        }

        public static Observable<Status> stopScan(@NonNull BleScanCallback bleScanCallback, long timeout, @NonNull TimeUnit timeUnit) {
            return Observable.create(new BleStopScanObservable(RxFit.get(), bleScanCallback, timeout, timeUnit));
        }

        public static Observable<Status> unclaimDevice(@NonNull BleDevice bleDevice) {
            return Observable.create(new BleUnclaimDeviceObservable(RxFit.get(), bleDevice, null, null, null));
        }

        public static Observable<Status> unclaimDevice(@NonNull BleDevice bleDevice, long timeout, @NonNull TimeUnit timeUnit) {
            return Observable.create(new BleUnclaimDeviceObservable(RxFit.get(), bleDevice, null, timeout, timeUnit));
        }

        public static Observable<Status> unclaimDevice(@NonNull String deviceAddress) {
            return Observable.create(new BleUnclaimDeviceObservable(RxFit.get(), null, deviceAddress, null, null));
        }

        public static Observable<Status> unclaimDevice(@NonNull String deviceAddress, long timeout, @NonNull TimeUnit timeUnit) {
            return Observable.create(new BleUnclaimDeviceObservable(RxFit.get(), null, deviceAddress, timeout, timeUnit));
        }

    }


    public static class Config {

        private Config() { }

        public static Observable<DataType> createCustomDataType(@NonNull DataTypeCreateRequest dataTypeCreateRequest) {
            return Observable.create(new ConfigCreateCustomDataTypeObservable(RxFit.get(), dataTypeCreateRequest, null, null));
        }

        public static Observable<DataType> createCustomDataType(@NonNull DataTypeCreateRequest dataTypeCreateRequest, long timeout, @NonNull TimeUnit timeUnit) {
            return Observable.create(new ConfigCreateCustomDataTypeObservable(RxFit.get(), dataTypeCreateRequest, timeout, timeUnit));
        }

        public static Observable<Status> disableFit() {
            return Observable.create(new ConfigDisableFitObservable(RxFit.get(), null, null));
        }

        public static Observable<Status> disableFit(long timeout, @NonNull TimeUnit timeUnit) {
            return Observable.create(new ConfigDisableFitObservable(RxFit.get(), timeout, timeUnit));
        }

        public static Observable<DataType> readDataType(@NonNull String dataTypeName) {
            return Observable.create(new ConfigReadDataTypeObservable(RxFit.get(), dataTypeName, null, null));
        }

        public static Observable<DataType> readDataType(@NonNull String dataTypeName, long timeout, @NonNull TimeUnit timeUnit) {
            return Observable.create(new ConfigReadDataTypeObservable(RxFit.get(), dataTypeName, timeout, timeUnit));
        }

    }


   public static class History {

       private History() { }

       public static Observable<Status> delete(@NonNull DataDeleteRequest dataDeleteRequest) {
           return Observable.create(new HistoryDeleteDataObservable(RxFit.get(), dataDeleteRequest, null, null));
       }

       public static Observable<Status> delete(@NonNull DataDeleteRequest dataDeleteRequest, long timeout, @NonNull TimeUnit timeUnit) {
           return Observable.create(new HistoryDeleteDataObservable(RxFit.get(), dataDeleteRequest, timeout, timeUnit));
       }

       public static Observable<Status> insert(@NonNull DataSet dataSet) {
           return Observable.create(new HistoryInsertDataObservable(RxFit.get(), dataSet, null, null));
       }

       public static Observable<Status> insert(@NonNull DataSet dataSet, long timeout, @NonNull TimeUnit timeUnit) {
           return Observable.create(new HistoryInsertDataObservable(RxFit.get(), dataSet, timeout, timeUnit));
       }

       public static Observable<DataSet> readDailyTotal(@NonNull DataType dataType) {
           return Observable.create(new HistoryReadDailyTotalObservable(RxFit.get(), dataType, null, null));
       }

       public static Observable<DataSet> readDailyTotal(@NonNull DataType dataType, long timeout, @NonNull TimeUnit timeUnit) {
           return Observable.create(new HistoryReadDailyTotalObservable(RxFit.get(), dataType, timeout, timeUnit));
       }

       public static Observable<DataReadResult> read(@NonNull DataReadRequest dataReadRequest) {
           return Observable.create(new HistoryReadDataObservable(RxFit.get(), dataReadRequest, null, null));
       }

       public static Observable<DataReadResult> read(@NonNull DataReadRequest dataReadRequest, long timeout, @NonNull TimeUnit timeUnit) {
           return Observable.create(new HistoryReadDataObservable(RxFit.get(), dataReadRequest, timeout, timeUnit));
       }

       public static Observable<Status> update(@NonNull DataUpdateRequest dataUpdateRequest) {
           return Observable.create(new HistoryUpdateDataObservable(RxFit.get(), dataUpdateRequest, null, null));
       }

       public static Observable<Status> update(@NonNull DataUpdateRequest dataUpdateRequest, long timeout, @NonNull TimeUnit timeUnit) {
           return Observable.create(new HistoryUpdateDataObservable(RxFit.get(), dataUpdateRequest, timeout, timeUnit));
       }

   }


    public static class Recording {

        private Recording() { }

        public static Observable<List<Subscription>> listSubscriptions() {
            return Observable.create(new RecordingListSubscriptionsObservable(RxFit.get(), null, null, null));
        }

        public static Observable<List<Subscription>> listSubscriptions(long timeout, @NonNull TimeUnit timeUnit) {
            return Observable.create(new RecordingListSubscriptionsObservable(RxFit.get(), null, timeout, timeUnit));
        }

        public static Observable<List<Subscription>> listSubscriptions(DataType dataType) {
            return Observable.create(new RecordingListSubscriptionsObservable(RxFit.get(), dataType, null, null));
        }

        public static Observable<List<Subscription>> listSubscriptions(DataType dataType, long timeout, @NonNull TimeUnit timeUnit) {
            return Observable.create(new RecordingListSubscriptionsObservable(RxFit.get(), dataType, timeout, timeUnit));
        }

        public static Observable<Status> subscribe(@NonNull DataSource dataSource) {
            return Observable.create(new RecordingSubscribeObservable(RxFit.get(), dataSource, null, null, null));
        }

        public static Observable<Status> subscribe(@NonNull DataSource dataSource, long timeout, @NonNull TimeUnit timeUnit) {
            return Observable.create(new RecordingSubscribeObservable(RxFit.get(), dataSource, null, timeout, timeUnit));
        }

        public static Observable<Status> subscribe(@NonNull DataType dataType) {
            return Observable.create(new RecordingSubscribeObservable(RxFit.get(), null, dataType, null, null));
        }

        public static Observable<Status> subscribe(@NonNull DataType dataType, long timeout, @NonNull TimeUnit timeUnit) {
            return Observable.create(new RecordingSubscribeObservable(RxFit.get(), null, dataType, timeout, timeUnit));
        }

        public static Observable<Status> unsubscribe(@NonNull DataSource dataSource) {
            return Observable.create(new RecordingUnsubscribeObservable(RxFit.get(), dataSource, null, null, null, null));
        }

        public static Observable<Status> unsubscribe(@NonNull DataSource dataSource, long timeout, @NonNull TimeUnit timeUnit) {
            return Observable.create(new RecordingUnsubscribeObservable(RxFit.get(), dataSource, null, null, timeout, timeUnit));
        }

        public static Observable<Status> unsubscribe(@NonNull DataType dataType) {
            return Observable.create(new RecordingUnsubscribeObservable(RxFit.get(), null, dataType, null, null, null));
        }

        public static Observable<Status> unsubscribe(@NonNull DataType dataType, long timeout, @NonNull TimeUnit timeUnit) {
            return Observable.create(new RecordingUnsubscribeObservable(RxFit.get(), null, dataType, null, timeout, timeUnit));
        }

        public static Observable<Status> unsubscribe(@NonNull Subscription subscription) {
            return Observable.create(new RecordingUnsubscribeObservable(RxFit.get(), null, null, subscription, null, null));
        }

        public static Observable<Status> unsubscribe(@NonNull Subscription subscription, long timeout, @NonNull TimeUnit timeUnit) {
            return Observable.create(new RecordingUnsubscribeObservable(RxFit.get(), null, null, subscription, timeout, timeUnit));
        }

    }


    public static class Sensors {

        private Sensors() { }

        public static Observable<Status> addDataPointIntent(@NonNull SensorRequest sensorRequest, @NonNull PendingIntent pendingIntent) {
            return Observable.create(new SensorsAddDataPointIntentObservable(RxFit.get(), sensorRequest, pendingIntent, null, null));
        }

        public static Observable<Status> addDataPointIntent(@NonNull SensorRequest sensorRequest, @NonNull PendingIntent pendingIntent, long timeout, @NonNull TimeUnit timeUnit) {
            return Observable.create(new SensorsAddDataPointIntentObservable(RxFit.get(), sensorRequest, pendingIntent, timeout, timeUnit));
        }

        public static Observable<Status> removeDataPointIntent(@NonNull PendingIntent pendingIntent) {
            return Observable.create(new SensorsRemoveDataPointIntentObservable(RxFit.get(), pendingIntent, null, null));
        }

        public static Observable<Status> removeDataPointIntent(@NonNull PendingIntent pendingIntent, long timeout, @NonNull TimeUnit timeUnit) {
            return Observable.create(new SensorsRemoveDataPointIntentObservable(RxFit.get(), pendingIntent, timeout, timeUnit));
        }

        public static Observable<DataPoint> getDataPoints(@NonNull SensorRequest sensorRequest) {
            return Observable.create(new SensorsDataPointObservable(RxFit.get(), sensorRequest, null, null));
        }

        public static Observable<DataPoint> getDataPoints(@NonNull SensorRequest sensorRequest, long timeout, @NonNull TimeUnit timeUnit) {
            return Observable.create(new SensorsDataPointObservable(RxFit.get(), sensorRequest, timeout, timeUnit));
        }

        public static Observable<List<DataSource>> findDataSources(@NonNull DataSourcesRequest dataSourcesRequest) {
            return Observable.create(new SensorsFindDataSourcesObservable(RxFit.get(), dataSourcesRequest, null, null, null));
        }

        public static Observable<List<DataSource>> findDataSources(@NonNull DataSourcesRequest dataSourcesRequest, long timeout, @NonNull TimeUnit timeUnit) {
            return Observable.create(new SensorsFindDataSourcesObservable(RxFit.get(), dataSourcesRequest, null, timeout, timeUnit));
        }

        public static Observable<List<DataSource>> findDataSources(@NonNull DataSourcesRequest dataSourcesRequest, DataType dataType) {
            return Observable.create(new SensorsFindDataSourcesObservable(RxFit.get(), dataSourcesRequest, dataType, null, null));
        }

        public static Observable<List<DataSource>> findDataSources(@NonNull DataSourcesRequest dataSourcesRequest, DataType dataType, long timeout, @NonNull TimeUnit timeUnit) {
            return Observable.create(new SensorsFindDataSourcesObservable(RxFit.get(), dataSourcesRequest, dataType, timeout, timeUnit));
        }

    }


    public static class Sessions {

        private Sessions() { }

        public static Observable<Status> insert(@NonNull SessionInsertRequest sessionInsertRequest) {
            return Observable.create(new SessionInsertObservable(RxFit.get(), sessionInsertRequest, null, null));
        }

        public static Observable<Status> insert(@NonNull SessionInsertRequest sessionInsertRequest, long timeout, @NonNull TimeUnit timeUnit) {
            return Observable.create(new SessionInsertObservable(RxFit.get(), sessionInsertRequest, timeout, timeUnit));
        }

        public static Observable<SessionReadResult> read(@NonNull SessionReadRequest sessionReadRequest) {
            return Observable.create(new SessionReadObservable(RxFit.get(), sessionReadRequest, null, null));
        }

        public static Observable<SessionReadResult> read(@NonNull SessionReadRequest sessionReadRequest, long timeout, @NonNull TimeUnit timeUnit) {
            return Observable.create(new SessionReadObservable(RxFit.get(), sessionReadRequest, timeout, timeUnit));
        }

        public static Observable<Status> registerForSessions(@NonNull PendingIntent pendingIntent) {
            return Observable.create(new SessionRegisterObservable(RxFit.get(), pendingIntent, null, null));
        }

        public static Observable<Status> registerForSessions(@NonNull PendingIntent pendingIntent, long timeout, @NonNull TimeUnit timeUnit) {
            return Observable.create(new SessionRegisterObservable(RxFit.get(), pendingIntent, timeout, timeUnit));
        }

        public static Observable<Status> unregisterForSessions(@NonNull PendingIntent pendingIntent) {
            return Observable.create(new SessionUnregisterObservable(RxFit.get(), pendingIntent, null, null));
        }

        public static Observable<Status> unregisterForSessions(@NonNull PendingIntent pendingIntent, long timeout, @NonNull TimeUnit timeUnit) {
            return Observable.create(new SessionUnregisterObservable(RxFit.get(), pendingIntent, timeout, timeUnit));
        }

        public static Observable<Status> start(@NonNull Session session) {
            return Observable.create(new SessionStartObservable(RxFit.get(), session, null, null));
        }

        public static Observable<Status> start(@NonNull Session session, long timeout, @NonNull TimeUnit timeUnit) {
            return Observable.create(new SessionStartObservable(RxFit.get(), session, timeout, timeUnit));
        }

        public static Observable<List<Session>> stop(@NonNull String identifier) {
            return Observable.create(new SessionStopObservable(RxFit.get(), identifier, null, null));
        }

        public static Observable<List<Session>> stop(@NonNull String identifier, long timeout, @NonNull TimeUnit timeUnit) {
            return Observable.create(new SessionStopObservable(RxFit.get(), identifier, timeout, timeUnit));
        }


    }


    /* Transformer that behaves like onExceptionResumeNext(Observable o), but propagates
     * a GoogleAPIConnectionException, which was caused by an unsuccessful resolution.
     * This can be helpful if you want to resume with another RxFit Observable when
     * an Exception occurs, but don't want to show the resolution dialog multiple times.
     *
     * An example use case: Fetch fitness data with server queries enabled, but provide
     * a timeout. When an exception occurs (e.g. timeout), switch to cached fitness data.
     * Using this Transformer prevents showing the authorization dialog twice, if the user
     * denys access for the first read. See MainActivity in sample project.
     */
    public static class OnExceptionResumeNext<T, R extends T> implements Observable.Transformer<T, T> {

        private final Observable<R> other;

        public OnExceptionResumeNext(Observable<R> other) {
            this.other = other;
        }

        @Override
        public Observable<T> call(Observable<T> source) {
            return source.onErrorResumeNext(new Func1<Throwable, Observable<R>>() {
                @Override
                public Observable<R> call(Throwable throwable) {
                    if (!(throwable instanceof Exception) || (throwable instanceof GoogleAPIConnectionException && ((GoogleAPIConnectionException) throwable).wasResolutionUnsuccessful())) {
                        Exceptions.propagate(throwable);
                    }

                    return other;
                }
            });
        }
    }

}
