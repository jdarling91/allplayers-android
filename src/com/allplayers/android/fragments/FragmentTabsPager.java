/*
 * Copyright (C) 2011 The Android Open Source Project
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
package com.allplayers.android.fragments;

import com.allplayers.android.app.ActionBarTabsActivity;

import android.os.Bundle;

/**
 * Demonstrates combining a TabHost with a ViewPager to implement a tab UI
 * that switches between tabs and also allows the user to perform horizontal
 * flicks to move between the tabs.
 */
public class FragmentTabsPager extends ActionBarTabsActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mTabsAdapter = new TabsAdapter(this, mTabHost, mViewPager);

        mTabsAdapter.addTab(mTabHost.newTabSpec("simple").setIndicator("Simple"),
                FragmentStackSupport.CountingFragment.class, null);
        mTabsAdapter.addTab(mTabHost.newTabSpec("contacts").setIndicator("Contacts"),
                LoaderCursorSupport.CursorLoaderListFragment.class, null);
        mTabsAdapter.addTab(mTabHost.newTabSpec("custom").setIndicator("Custom"),
                LoaderCustomSupport.AppListFragment.class, null);
        mTabsAdapter.addTab(mTabHost.newTabSpec("throttle").setIndicator("Throttle"),
                LoaderThrottleSupport.ThrottledLoaderListFragment.class, null);

    }

}
