package com.patloew.rxfitsample;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

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
public class FitnessSessionAdapter extends RecyclerView.Adapter<FitnessSessionViewHolder> {

    private final List<FitnessSessionData> fitnessSessionDataList;

    public FitnessSessionAdapter(List<FitnessSessionData> fitnessSessionDataList) {
        this.fitnessSessionDataList = fitnessSessionDataList;
    }

    @Override
    public FitnessSessionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_fitness_session, parent, false);

        return new FitnessSessionViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(FitnessSessionViewHolder holder, int position) {
        holder.bind(fitnessSessionDataList.get(position));
    }

    @Override
    public int getItemCount() {
        return fitnessSessionDataList.size();
    }
}
