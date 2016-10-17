package com.patloew.rxfit;

import android.content.Context;
import android.support.annotation.CallSuper;

import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.fitness.Fitness;

import org.mockito.Mock;

import java.util.concurrent.TimeUnit;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static org.mockito.Mockito.when;

public abstract class BaseTest {

    protected static final long TIMEOUT_TIME = 1L;
    protected static final TimeUnit TIMEOUT_TIMEUNIT = TimeUnit.SECONDS;

    @Mock Context ctx;

    RxFit rxFit;

    @CallSuper
    public void setup() throws Exception {
        when(ctx.getApplicationContext()).thenReturn(ctx);

        rxFit = new RxFit(ctx, new Api[] {Fitness.BLE_API}, new Scope[]{});
    }

    protected static final void assertNoTimeoutSet(BaseRx baseRx) {
        assertNull(baseRx.timeoutTime);
        assertNull(baseRx.timeoutUnit);
    }

    protected static final void assertTimeoutSet(BaseRx baseRx) {
        assertEquals(TIMEOUT_TIME, (long) baseRx.timeoutTime);
        assertEquals(TIMEOUT_TIMEUNIT, baseRx.timeoutUnit);
    }
}
