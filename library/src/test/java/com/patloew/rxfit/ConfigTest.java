package com.patloew.rxfit;

import android.support.v4.content.ContextCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.request.DataTypeCreateRequest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareOnlyThisForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;

import rx.Single;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.times;

@RunWith(PowerMockRunner.class)
@SuppressStaticInitializationFor("com.google.android.gms.fitness.Fitness")
@PrepareOnlyThisForTest({ Single.class, ContextCompat.class, Fitness.class, Status.class, ConnectionResult.class, DataType.class })
public class ConfigTest extends BaseTest {

    @Mock DataTypeCreateRequest dataTypeCreateRequest;

    @Override
    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        PowerMockito.spy(Single.class);
        super.setup();
    }


    // Create Custom Data Type

    @Test
    public void Config_CreateCustomDataType() throws Exception {
        ArgumentCaptor<ConfigCreateCustomDataTypeSingle> captor = ArgumentCaptor.forClass(ConfigCreateCustomDataTypeSingle.class);

        rxFit.config().createCustomDataType(dataTypeCreateRequest);
        rxFit.config().createCustomDataType(dataTypeCreateRequest, TIMEOUT_TIME, TIMEOUT_TIMEUNIT);

        PowerMockito.verifyStatic(times(2));
        Single.create(captor.capture());

        ConfigCreateCustomDataTypeSingle single = captor.getAllValues().get(0);
        assertEquals(dataTypeCreateRequest, single.dataTypeCreateRequest);
        assertNoTimeoutSet(single);

        single = captor.getAllValues().get(1);
        assertEquals(dataTypeCreateRequest, single.dataTypeCreateRequest);
        assertTimeoutSet(single);
    }

    // Disable Fit


    @Test
    public void Config_DisableFit() throws Exception {
        ArgumentCaptor<ConfigDisableFitSingle> captor = ArgumentCaptor.forClass(ConfigDisableFitSingle.class);

        rxFit.config().disableFit();
        rxFit.config().disableFit(TIMEOUT_TIME, TIMEOUT_TIMEUNIT);

        PowerMockito.verifyStatic(times(2));
        Single.create(captor.capture());

        ConfigDisableFitSingle single = captor.getAllValues().get(0);
        assertNoTimeoutSet(single);

        single = captor.getAllValues().get(1);
        assertTimeoutSet(single);
    }


    // Read Data Type

    @Test
    public void Config_ReadDataType() throws Exception {
        ArgumentCaptor<ConfigReadDataTypeSingle> captor = ArgumentCaptor.forClass(ConfigReadDataTypeSingle.class);

        final String dataTypeName = "name";
        rxFit.config().readDataType(dataTypeName);
        rxFit.config().readDataType(dataTypeName, TIMEOUT_TIME, TIMEOUT_TIMEUNIT);

        PowerMockito.verifyStatic(times(2));
        Single.create(captor.capture());

        ConfigReadDataTypeSingle single = captor.getAllValues().get(0);
        assertEquals(dataTypeName, single.dataTypeName);
        assertNoTimeoutSet(single);

        single = captor.getAllValues().get(1);
        assertEquals(dataTypeName, single.dataTypeName);
        assertTimeoutSet(single);
    }

}
