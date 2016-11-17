/*
 * Copyright (C) 2016 Yaroslav Mytkalyk
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.doctoror.fuckoffmusicplayer.util;

import android.databinding.BindingAdapter;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

public final class BindingAdapters {

    private BindingAdapters() {
        throw new UnsupportedOperationException();
    }

    @BindingAdapter("recyclerAdapter")
    public static void setRecyclerAdapter(@NonNull final RecyclerView recyclerView,
            @Nullable final RecyclerView.Adapter<?> adapter) {
        recyclerView.setAdapter(adapter);
    }
}
