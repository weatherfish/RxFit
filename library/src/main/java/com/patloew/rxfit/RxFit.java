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

import rx.Observable;

/* Factory for Google Fit API observables. Make sure to include all the APIs
 * and Scopes that you need for your app. Also make sure to have the Location
 * and Body Sensors permission on Marshmallow, if they are needed by your
 * Fit API requests.
 */
public class RxFit {

    private static RxFit instance = null;

    private final Context ctx;
    private final Api<? extends Api.ApiOptions.NotRequiredOptions>[] apis;
    private final Scope[] scopes;

    /* Initializes the singleton instance of RxFitProvider
     *
     * @param ctx Context.
     * @param apis An array of Fitness APIs to be used in your app.
     * @param scopes An array of the Scopes to be requested for your app.
     */
    public static void init(@NonNull Context ctx, @NonNull Api<? extends Api.ApiOptions.NotRequiredOptions>[] apis, @NonNull Scope[] scopes) {
        if(instance == null) { instance = new RxFit(ctx, apis, scopes); }
    }

    /* Gets the singleton instance of RxFitProvider, after it was
     * initialized.
     */
    private static RxFit get() {
        if(instance == null) { throw new IllegalStateException("RxFitProvider not initialized"); }
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
    
    
    
    public static class Ble {

        private Ble() { }

        public static Observable<Status> claimDevice(@NonNull BleDevice bleDevice) {
            return BleClaimDeviceObservable.create(RxFit.get(), bleDevice);
        }

        public static Observable<Status> claimDevice(@NonNull String deviceAddress) {
            return BleClaimDeviceObservable.create(RxFit.get(), deviceAddress);
        }

        public static Observable<List<BleDevice>> getClaimedDeviceList() {
            return BleListClaimedDevicesObservable.create(RxFit.get());
        }

        public static Observable<List<BleDevice>> getClaimedDeviceList(DataType dataType) {
            return BleListClaimedDevicesObservable.create(RxFit.get(), dataType);
        }

        @RequiresPermission("android.permission.BLUETOOTH_ADMIN")
        public static Observable<Status> startScan(@NonNull StartBleScanRequest startBleScanRequest) {
            return BleStartScanObservable.create(RxFit.get(), startBleScanRequest);
        }

        public static Observable<Status> stopScan(@NonNull BleScanCallback bleScanCallback) {
            return BleStopScanObservable.create(RxFit.get(), bleScanCallback);
        }

        public static Observable<Status> unclaimDevice(@NonNull BleDevice bleDevice) {
            return BleUnclaimDeviceObservable.create(RxFit.get(), bleDevice);
        }

        public static Observable<Status> unclaimDevice(@NonNull String deviceAddress) {
            return BleUnclaimDeviceObservable.create(RxFit.get(), deviceAddress);
        }

    }


    public static class Config {

        private Config() { }

        public static Observable<DataType> createCustomDataType(@NonNull DataTypeCreateRequest dataTypeCreateRequest) {
            return ConfigCreateCustomDataTypeObservable.create(RxFit.get(), dataTypeCreateRequest);
        }

        public static Observable<Status> disableFit() {
            return ConfigDisableFitObservable.create(RxFit.get());
        }

        public static Observable<DataType> readDataType(@NonNull String dataTypeName) {
            return ConfigReadDataTypeObservable.create(RxFit.get(), dataTypeName);
        }

    }


   public static class History {

       private History() { }

       public static Observable<Status> delete(@NonNull DataDeleteRequest dataDeleteRequest) {
           return HistoryDeleteDataObservable.create(RxFit.get(), dataDeleteRequest);
       }

       public static Observable<Status> insert(@NonNull DataSet dataSet) {
           return HistoryInsertDataObservable.create(RxFit.get(), dataSet);
       }

       public static Observable<DataSet> readDailyTotal(@NonNull DataType dataType) {
           return HistoryReadDailyTotalObservable.create(RxFit.get(), dataType);
       }

       public static Observable<DataReadResult> read(@NonNull DataReadRequest dataReadRequest) {
           return HistoryReadDataObservable.create(RxFit.get(), dataReadRequest);
       }

       public static Observable<Status> update(@NonNull DataUpdateRequest dataUpdateRequest) {
           return HistoryUpdateDataObservable.create(RxFit.get(), dataUpdateRequest);
       }

   }


    public static class Recording {

        private Recording() { }

        public static Observable<List<Subscription>> listSubscriptions() {
            return RecordingListSubscriptionsObservable.create(RxFit.get());
        }

        public static Observable<List<Subscription>> listSubscriptions(DataType dataType) {
            return RecordingListSubscriptionsObservable.create(RxFit.get(), dataType);
        }

        public static Observable<Status> subscribe(@NonNull DataSource dataSource) {
            return RecordingSubscribeObservable.create(RxFit.get(), dataSource);
        }

        public static Observable<Status> subscribe(@NonNull DataType dataType) {
            return RecordingSubscribeObservable.create(RxFit.get(), dataType);
        }

        public static Observable<Status> unsubscribe(@NonNull DataSource dataSource) {
            return RecordingUnsubscribeObservable.create(RxFit.get(), dataSource);
        }

        public static Observable<Status> unsubscribe(@NonNull DataType dataType) {
            return RecordingUnsubscribeObservable.create(RxFit.get(), dataType);
        }

        public static Observable<Status> unsubscribe(@NonNull Subscription subscription) {
            return RecordingUnsubscribeObservable.create(RxFit.get(), subscription);
        }

    }


    public static class Sensors {

        private Sensors() { }

        public static Observable<Status> addDataPointIntent(@NonNull SensorRequest sensorRequest, @NonNull PendingIntent pendingIntent) {
            return SensorsAddDataPointIntentObservable.create(RxFit.get(), sensorRequest, pendingIntent);
        }

        public static Observable<Status> removeDataPointIntent(@NonNull PendingIntent pendingIntent) {
            return SensorsRemoveDataPointIntentObservable.create(RxFit.get(), pendingIntent);
        }

        public static Observable<DataPoint> getDataPoints(@NonNull SensorRequest sensorRequest) {
            return SensorsDataPointObservable.create(RxFit.get(), sensorRequest);
        }

        public static Observable<List<DataSource>> findDataSources(@NonNull DataSourcesRequest dataSourcesRequest) {
            return SensorsFindDataSourcesObservable.create(RxFit.get(), dataSourcesRequest);
        }

        public static Observable<List<DataSource>> findDataSources(@NonNull DataSourcesRequest dataSourcesRequest, DataType dataType) {
            return SensorsFindDataSourcesObservable.create(RxFit.get(), dataSourcesRequest, dataType);
        }

    }


    public static class Sessions {

        private Sessions() { }

        public static Observable<Status> insert(@NonNull SessionInsertRequest sessionInsertRequest) {
            return SessionInsertObservable.create(RxFit.get(), sessionInsertRequest);
        }

        public static Observable<SessionReadResult> read(@NonNull SessionReadRequest sessionReadRequest) {
            return SessionReadObservable.create(RxFit.get(), sessionReadRequest);
        }

        public static Observable<Status> registerForSessions(@NonNull PendingIntent pendingIntent) {
            return SessionRegisterObservable.create(RxFit.get(), pendingIntent);
        }

        public static Observable<Status> unregisterForSessions(@NonNull PendingIntent pendingIntent) {
            return SessionUnregisterObservable.create(RxFit.get(), pendingIntent);
        }

        public static Observable<Status> start(@NonNull Session session) {
            return SessionStartObservable.create(RxFit.get(), session);
        }

        public static Observable<List<Session>> stop(@NonNull String identifier) {
            return SessionStopObservable.create(RxFit.get(), identifier);
        }
    }

}
