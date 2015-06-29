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
 * FILE: TweetUpdateActivityTest.java
 * AUTHOR: Dr. Isaac Ben-Akiva <isaac.ben-akiva@ubimobitech.com>
 * <p/>
 * CREATED ON: 28/06/15
 */

package com.ubimobitech.ubitwitter;

import android.test.ActivityInstrumentationTestCase2;
import android.test.ViewAsserts;
import android.test.suitebuilder.annotation.MediumTest;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by benakiva on 28/06/15.
 */
public class TweetUpdateActivityTest extends ActivityInstrumentationTestCase2<TweetUpdateActivity> {
    private TweetUpdateActivity mActivity;
    private EditText mUserTweet;
    private Button mSendTweet;
    private TextView mCharCount;

    private static final String TWEET_TEST_TEXT = "Testing Tweet send Button";
    public TweetUpdateActivityTest() {
        super(TweetUpdateActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        setActivityInitialTouchMode(true);

        mActivity = getActivity();

        mUserTweet = (EditText) mActivity.findViewById(R.id.tweet_text);
        mSendTweet = (Button) mActivity.findViewById(R.id.send_tweet);
        mCharCount = (TextView) mActivity.findViewById(R.id.num_character);
    }

    /**
     * Tests the preconditions of this test fixture.
     */
    @MediumTest
    public void testPreconditions() {
        assertNotNull("mActivity is null", mActivity);
        assertNotNull("mSendTweet is null", mSendTweet);
        assertNotNull("mUserTweet is null", mUserTweet);
        assertNotNull("mCharCount is null", mCharCount);
    }

    @MediumTest
    public void testEditText_Text() {
        final View decorView = mActivity.getWindow().getDecorView();

        ViewAsserts.assertOnScreen(decorView, mUserTweet);
        assertEquals("", mUserTweet.getText().toString());

        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                mUserTweet.setText(TWEET_TEST_TEXT);
            }
        });

        assertEquals(TWEET_TEST_TEXT, mUserTweet.getText().toString());

        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                mUserTweet.setText("");
            }
        });

        assertEquals("", mUserTweet.getText().toString());
    }

    @MediumTest
    public void testClickSendButton() {
        final View decorView = mActivity.getWindow().getDecorView();

        ViewAsserts.assertOnScreen(decorView, mSendTweet);

        assertEquals("", mUserTweet.getText().toString());
        assertFalse("OK. Button disabled while editext is empty", mSendTweet.isEnabled());

        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                mUserTweet.setText(TWEET_TEST_TEXT);
            }
        });

        assertTrue("OK. EditText not empty", !TextUtils.isEmpty(mUserTweet.getText().toString()));
        assertTrue("OK. Button enabled when there's text in edittext", mSendTweet.isEnabled());
    }

    @MediumTest
    public void testCharCountTextView_labelText() {
        final View decorView = mActivity.getWindow().getDecorView();
        ViewAsserts.assertOnScreen(decorView, mCharCount);

        assertEquals("140", mCharCount.getText());

        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                mUserTweet.setText(TWEET_TEST_TEXT);
            }
        });

        assertEquals("115", mCharCount.getText());

        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                mUserTweet.setText("");
            }
        });

        assertEquals("140", mCharCount.getText());
    }
}
