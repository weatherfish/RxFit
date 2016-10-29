package com.patloew.rxfit;

import android.app.PendingIntent;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.request.DataSourcesRequest;
import com.google.android.gms.fitness.request.SensorRequest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareOnlyThisForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;

import io.reactivex.Observable;
import io.reactivex.Single;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.times;
import static org.powermock.api.mockito.PowerMockito.doReturn;

@RunWith(PowerMockRunner.class)
@SuppressStaticInitializationFor("com.google.android.gms.fitness.Fitness")
@PrepareOnlyThisForTest({ Observable.class, Single.class, ContextCompat.class, Fitness.class, Status.class, DataType.class, ConnectionResult.class })
public class SensorsTest extends BaseTest {

    @Override
    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        PowerMockito.spy(Single.class);
        PowerMockito.mockStatic(Observable.class);
        doReturn(100).when(Observable.class, "bufferSize");
        super.setup();
    }

    // Add DataPoint Intent

    @Test
    public void Sensors_AddDataPointIntent() throws Exception {
        ArgumentCaptor<SensorsAddDataPointIntentSingle> captor = ArgumentCaptor.forClass(SensorsAddDataPointIntentSingle.class);

        final SensorRequest request = Mockito.mock(SensorRequest.class);
        final PendingIntent pendingIntent = Mockito.mock(PendingIntent.class);
        rxFit.sensors().addDataPointIntent(request, pendingIntent);
        rxFit.sensors().addDataPointIntent(request, pendingIntent, TIMEOUT_TIME, TIMEOUT_TIMEUNIT);


        PowerMockito.verifyStatic(times(2));
        Single.create(captor.capture());

        SensorsAddDataPointIntentSingle single = captor.getAllValues().get(0);
        assertEquals(request, single.sensorRequest);
        assertEquals(pendingIntent, single.pendingIntent);
        assertNoTimeoutSet(single);

        single = captor.getAllValues().get(1);
        assertEquals(request, single.sensorRequest);
        assertEquals(pendingIntent, single.pendingIntent);
        assertTimeoutSet(single);
    }

    // Remove DataPoint Intent

    @Test
    public void Sensors_RemoveDataPointIntent() throws Exception {
        ArgumentCaptor<SensorsRemoveDataPointIntentSingle> captor = ArgumentCaptor.forClass(SensorsRemoveDataPointIntentSingle.class);

        final PendingIntent pendingIntent = Mockito.mock(PendingIntent.class);
        rxFit.sensors().removeDataPointIntent(pendingIntent);
        rxFit.sensors().removeDataPointIntent(pendingIntent, TIMEOUT_TIME, TIMEOUT_TIMEUNIT);

        PowerMockito.verifyStatic(times(2));
        Single.create(captor.capture());

        SensorsRemoveDataPointIntentSingle single = captor.getAllValues().get(0);
        assertEquals(pendingIntent, single.pendingIntent);
        assertNoTimeoutSet(single);

        single = captor.getAllValues().get(1);
        assertEquals(pendingIntent, single.pendingIntent);
        assertTimeoutSet(single);
    }

    // GetDataPoints

    @Test
    public void Sensors_GetDataPoints() throws Exception {
        ArgumentCaptor<SensorsDataPointObservable> captor = ArgumentCaptor.forClass(SensorsDataPointObservable.class);

        final SensorRequest request = Mockito.mock(SensorRequest.class);
        rxFit.sensors().getDataPoints(request);
        rxFit.sensors().getDataPoints(request, TIMEOUT_TIME, TIMEOUT_TIMEUNIT);

        PowerMockito.verifyStatic(times(2));
        Observable.create(captor.capture());

        SensorsDataPointObservable observable = captor.getAllValues().get(0);
        assertEquals(request, observable.sensorRequest);
        assertNoTimeoutSet(observable);

        observable = captor.getAllValues().get(1);
        assertEquals(request, observable.sensorRequest);
        assertTimeoutSet(observable);
    }

    // Remove DataPoint Intent

    @Test
    public void Sensors_FindDataSources() throws Exception {
        ArgumentCaptor<SensorsFindDataSourcesSingle> captor = ArgumentCaptor.forClass(SensorsFindDataSourcesSingle.class);

        final DataSourcesRequest request = Mockito.mock(DataSourcesRequest.class);
        rxFit.sensors().findDataSources(request);
        rxFit.sensors().findDataSources(request, TIMEOUT_TIME, TIMEOUT_TIMEUNIT);


        PowerMockito.verifyStatic(atLeast(2));
        Single.create(captor.capture());

        SensorsFindDataSourcesSingle single = captor.getAllValues().get(0);
        assertEquals(request, single.dataSourcesRequest);
        assertNull(single.dataType);
        assertNoTimeoutSet(single);

        single = captor.getAllValues().get(1);
        assertEquals(request, single.dataSourcesRequest);
        assertNull(single.dataType);
        assertTimeoutSet(single);
    }

    @Test
    public void Sensors_FindDataSources_DataType() throws Exception {
        ArgumentCaptor<SensorsFindDataSourcesSingle> captor = ArgumentCaptor.forClass(SensorsFindDataSourcesSingle.class);

        final DataSourcesRequest request = Mockito.mock(DataSourcesRequest.class);
        final DataType dataType = Mockito.mock(DataType.class);
        rxFit.sensors().findDataSources(request, dataType);
        rxFit.sensors().findDataSources(request, dataType, TIMEOUT_TIME, TIMEOUT_TIMEUNIT);


        PowerMockito.verifyStatic(atLeast(2));
        Single.create(captor.capture());

        SensorsFindDataSourcesSingle single = captor.getAllValues().get(0);
        assertEquals(request, single.dataSourcesRequest);
        assertEquals(dataType, single.dataType);
        assertNoTimeoutSet(single);

        single = captor.getAllValues().get(1);
        assertEquals(request, single.dataSourcesRequest);
        assertEquals(dataType, single.dataType);
        assertTimeoutSet(single);
    }

}
