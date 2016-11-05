package com.patloew.rxfitsample;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;

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
public class FitnessSessionViewHolder extends RecyclerView.ViewHolder {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private final Context ctx;

    private TextView name;
    private TextView dateTimeStart;
    private TextView dateTimeStop;
    private TextView activity;
    private TextView calories;
    private TextView steps;
    private ImageView icon;

    public FitnessSessionViewHolder(View itemView) {
        super(itemView);
        ctx = itemView.getContext().getApplicationContext();

        name = (TextView) itemView.findViewById(R.id.tv_name);
        dateTimeStart = (TextView) itemView.findViewById(R.id.tv_datetime_start);
        dateTimeStop = (TextView) itemView.findViewById(R.id.tv_datetime_stop);
        activity = (TextView) itemView.findViewById(R.id.tv_activity);
        calories = (TextView) itemView.findViewById(R.id.tv_calories);
        steps = (TextView) itemView.findViewById(R.id.tv_steps);
        icon = (ImageView) itemView.findViewById(R.id.iv_icon);
    }

    public void bind(FitnessSessionData fitnessSessionData) {
        dateTimeStart.setText("Start: " + DATE_FORMAT.format(fitnessSessionData.start));
        dateTimeStop.setText("Stop: " + DATE_FORMAT.format(fitnessSessionData.end));
        calories.setText(fitnessSessionData.calories + "kcal");
        steps.setText(fitnessSessionData.steps + " steps");
        activity.setText("Activity: " + fitnessSessionData.activity);
        name.setText("Session" + (!TextUtils.isEmpty(fitnessSessionData.name) ? ": " + fitnessSessionData.name : ""));

        try {
            icon.setVisibility(View.VISIBLE);
            icon.setImageDrawable(ctx.getPackageManager().getApplicationIcon(fitnessSessionData.appName));
        } catch(Exception ignore) {
            icon.setVisibility(View.GONE);
        }
    }
}
