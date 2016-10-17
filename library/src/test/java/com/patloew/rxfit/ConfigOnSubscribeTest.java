package com.patloew.rxfit;

import android.support.v4.content.ContextCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.request.DataTypeCreateRequest;
import com.google.android.gms.fitness.result.DataTypeResult;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareOnlyThisForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;

import rx.Single;
import rx.observers.TestSubscriber;

import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@SuppressStaticInitializationFor("com.google.android.gms.fitness.Fitness")
@PrepareOnlyThisForTest({ ContextCompat.class, Fitness.class, Status.class, ConnectionResult.class, DataType.class, BaseRx.class })
public class ConfigOnSubscribeTest extends BaseOnSubscribeTest {

    @Mock DataType dataType;

    @Override
    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        super.setup();
    }


    // ConfigCreateCustomDataTypeObservable

    @Test
    public void ConfigCreateCustomDataTypeObservable_Success() {
        TestSubscriber<DataType> sub = new TestSubscriber<>();
        DataTypeCreateRequest dataTypeCreateRequest = Mockito.mock(DataTypeCreateRequest.class);
        DataTypeResult dataTypeResult = Mockito.mock(DataTypeResult.class);
        ConfigCreateCustomDataTypeSingle single = PowerMockito.spy(new ConfigCreateCustomDataTypeSingle(rxFit, dataTypeCreateRequest, null, null));

        setPendingResultValue(dataTypeResult);
        when(dataTypeResult.getStatus()).thenReturn(status);
        when(dataTypeResult.getDataType()).thenReturn(dataType);
        when(status.isSuccess()).thenReturn(true);
        when(configApi.createCustomDataType(apiClient, dataTypeCreateRequest)).thenReturn(pendingResult);

        setupBaseSingleSuccess(single);
        Single.create(single).subscribe(sub);

        assertSingleValue(sub, dataType);
    }

    @Test
    public void ConfigCreateCustomDataTypeObservable_StatusException() {
        TestSubscriber<DataType> sub = new TestSubscriber<>();
        DataTypeCreateRequest dataTypeCreateRequest = Mockito.mock(DataTypeCreateRequest.class);
        DataTypeResult dataTypeResult = Mockito.mock(DataTypeResult.class);
        ConfigCreateCustomDataTypeSingle single = PowerMockito.spy(new ConfigCreateCustomDataTypeSingle(rxFit, dataTypeCreateRequest, null, null));

        setPendingResultValue(dataTypeResult);
        when(dataTypeResult.getStatus()).thenReturn(status);
        when(dataTypeResult.getDataType()).thenReturn(dataType);
        when(status.isSuccess()).thenReturn(false);
        when(configApi.createCustomDataType(apiClient, dataTypeCreateRequest)).thenReturn(pendingResult);

        setupBaseSingleSuccess(single);
        Single.create(single).subscribe(sub);

        assertError(sub, StatusException.class);
    }

    // ConfigDisableFitObservable

    @Test
    public void ConfigDisableFitObservable_Success() {
        TestSubscriber<Status> sub = new TestSubscriber<>();
        ConfigDisableFitSingle single = PowerMockito.spy(new ConfigDisableFitSingle(rxFit, null, null));

        setPendingResultValue(status);
        when(status.isSuccess()).thenReturn(true);
        when(configApi.disableFit(apiClient)).thenReturn(pendingResult);

        setupBaseSingleSuccess(single);
        Single.create(single).subscribe(sub);

        assertSingleValue(sub, status);
    }

    @Test
    public void ConfigDisableFitObservable_StatusException() {
        TestSubscriber<Status> sub = new TestSubscriber<>();
        ConfigDisableFitSingle single = PowerMockito.spy(new ConfigDisableFitSingle(rxFit, null, null));

        setPendingResultValue(status);
        when(status.isSuccess()).thenReturn(false);
        when(configApi.disableFit(apiClient)).thenReturn(pendingResult);

        setupBaseSingleSuccess(single);
        Single.create(single).subscribe(sub);

        assertError(sub, StatusException.class);
    }

    // ConfigReadDataTypeObservable

    @Test
    public void ConfigReadDataTypeObservable_Success() {
        TestSubscriber<DataType> sub = new TestSubscriber<>();
        String dataTypeName = "dataTypeName";
        DataTypeResult dataTypeResult = Mockito.mock(DataTypeResult.class);
        ConfigReadDataTypeSingle single = PowerMockito.spy(new ConfigReadDataTypeSingle(rxFit, dataTypeName, null, null));

        setPendingResultValue(dataTypeResult);
        when(dataTypeResult.getStatus()).thenReturn(status);
        when(dataTypeResult.getDataType()).thenReturn(dataType);
        when(status.isSuccess()).thenReturn(true);
        when(configApi.readDataType(apiClient, dataTypeName)).thenReturn(pendingResult);

        setupBaseSingleSuccess(single);
        Single.create(single).subscribe(sub);

        assertSingleValue(sub, dataType);
    }

    @Test
    public void ConfigReadDataTypeObservable_StatusException() {
        TestSubscriber<DataType> sub = new TestSubscriber<>();
        String dataTypeName = "dataTypeName";
        DataTypeResult dataTypeResult = Mockito.mock(DataTypeResult.class);
        ConfigReadDataTypeSingle single = PowerMockito.spy(new ConfigReadDataTypeSingle(rxFit, dataTypeName, null, null));

        setPendingResultValue(dataTypeResult);
        when(dataTypeResult.getStatus()).thenReturn(status);
        when(dataTypeResult.getDataType()).thenReturn(dataType);
        when(status.isSuccess()).thenReturn(false);
        when(configApi.readDataType(apiClient, dataTypeName)).thenReturn(pendingResult);

        setupBaseSingleSuccess(single);
        Single.create(single).subscribe(sub);

        assertError(sub, StatusException.class);
    }

}
