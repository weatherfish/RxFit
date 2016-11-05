package com.patloew.rxfitsample;

import com.google.android.gms.fitness.data.Bucket;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.patloew.rxfit.History;
import com.patloew.rxfit.RxFit;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.ArgumentCaptor;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import rx.Observable;

import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/* Copyright 2016 Patrick Löwenstein
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. */
@RunWith(JUnit4.class)
public class MainPresenterTest {

    @Rule public RxSchedulersOverrideRule rxSchedulersOverrideRule = new RxSchedulersOverrideRule();

    @Mock RxFit rxFit;
    @Mock History history;

    @Mock Bucket bucket;

    @Mock MainView mainView;

    MainPresenter mainPresenter;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        doReturn(history).when(rxFit).history();

        mainPresenter = new MainPresenter(rxFit);
        mainPresenter.attachView(mainView);
    }

    @Test
    public void testNoResults() {
        doReturn(Observable.empty()).when(history).readBuckets(Matchers.any(DataReadRequest.class));

        mainPresenter.getFitnessData();

        ArgumentCaptor<List> fitnessSessionDataCaptor = ArgumentCaptor.forClass(List.class);

        verify(mainView, times(1)).onFitnessSessionDataLoaded(fitnessSessionDataCaptor.capture());
        assertTrue(fitnessSessionDataCaptor.getValue().isEmpty());
    }
}
