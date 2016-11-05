package com.patloew.rxfitsample;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.fitness.Fitness;
import com.patloew.rxfit.RxFit;

import java.util.List;
import java.util.concurrent.TimeUnit;

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
public class MainActivity extends AppCompatActivity implements MainView {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    private RxFit rxFit;

    private MainPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.rv_main);
        progressBar = (ProgressBar) findViewById(R.id.pb_main);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        rxFit = new RxFit(this, new Api[] { Fitness.SESSIONS_API, Fitness.HISTORY_API }, new Scope[] { new Scope(Scopes.FITNESS_ACTIVITY_READ) });
        rxFit.setDefaultTimeout(15, TimeUnit.SECONDS);

        presenter = new MainPresenter(rxFit);
        presenter.attachView(this);

        presenter.getFitnessData();
    }

    @Override
    protected void onDestroy() {
        presenter.detachView();
        super.onDestroy();
    }

    // View Interface

    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    @Override
    public void showRetrySnackbar() {
        progressBar.setVisibility(View.GONE);
        Snackbar.make(recyclerView, "Error getting Fit data", Snackbar.LENGTH_INDEFINITE)
                .setAction("Retry", v -> presenter.getFitnessData())
                .show();
    }

    @Override
    public void onFitnessSessionDataLoaded(List<FitnessSessionData> fitnessSessionDataList) {
        recyclerView.setAdapter(new FitnessSessionAdapter(fitnessSessionDataList));
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);

        if(fitnessSessionDataList.isEmpty()) {
            Snackbar.make(recyclerView, "No sessions found", Snackbar.LENGTH_INDEFINITE).show();
        }
    }
}
