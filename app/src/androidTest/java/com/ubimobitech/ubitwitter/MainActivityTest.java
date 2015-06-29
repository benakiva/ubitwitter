/*
 * Copyright 2015, Isaac Ben-Akiva
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
/**
 * FILE: MainActivityTest.java
 * AUTHOR: Dr. Isaac Ben-Akiva <isaac.ben-akiva@ubimobitech.com>
 * <p/>
 * CREATED ON: 28/06/15
 */

package com.ubimobitech.ubitwitter;

import android.app.FragmentManager;
import android.database.DataSetObserver;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.MediumTest;
import android.widget.ListView;

import com.ubimobitech.ubitwitter.comm.CommUtils;
import com.ubimobitech.ubitwitter.usertimeline.UserTimelineFragment;

/**
 * Created by benakiva on 28/06/15.
 */
public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {
    private MainActivity mActivity;
    private ListView mListView;
    private FragmentManager mFragmentManager;

    public MainActivityTest() {
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        //injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        mActivity = getActivity();
        mFragmentManager = mActivity.getFragmentManager();
    }

    /**
     * Tests the preconditions of this test fixture.
     */
    @MediumTest
    public void testPreconditions() {
        assertNotNull("mActivity is null", mActivity);
    }

    public void testHomeTimeline_isLoaded() {
        assertTrue("OK. Network enabled", CommUtils.hasInternetConnection(mActivity));
    }

    public void testFragment() throws Exception {
        mFragmentManager.beginTransaction()
                .replace(R.id.container, new UserTimelineFragment())
                .commit();

        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                mFragmentManager.executePendingTransactions();
            }
        });

        UserTimelineFragment underTest = (UserTimelineFragment) mFragmentManager.findFragmentById(R.id.container);

        assertNotNull(underTest);

        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                getInstrumentation().callActivityOnStart(getActivity());
                getInstrumentation().callActivityOnResume(getActivity());
                mFragmentManager.executePendingTransactions();
            }
        });

        mListView = (ListView) mActivity.findViewById(R.id.listview_tweets);

        assertNotNull(mListView);
    }
}
