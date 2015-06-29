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
 * FILE: TwitterSharedPrefsTest.java
 * AUTHOR: Dr. Isaac Ben-Akiva <isaac.ben-akiva@ubimobitech.com>
 * <p/>
 * CREATED ON: 28/06/15
 */

package com.ubimobitech.ubitwitter;

import android.content.Context;
import android.test.suitebuilder.annotation.SmallTest;

import com.ubimobitech.ubitwitter.auth.TwitterSession;
import com.ubimobitech.ubitwitter.prefs.TwitterSharedPrefs;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;


@SmallTest
@RunWith(MockitoJUnitRunner.class)
public class TwitterSharedPrefsTest {
    private static final String TEST_OAUTH_TOKEN = "oNwiugn948gh0oinv899rv48N08y73";
    private static final String TEST_OAUTH_SECRET = "oqirvbitybqiuhv899nqourh30948on0489hg";
    private TwitterSession mSession;

    private TwitterSharedPrefs mMockTwitterSharedPrefs;

    @Mock
    Context mMockContext;


    @Before
    public void initMocks() {
        mSession = new TwitterSession(TEST_OAUTH_TOKEN, TEST_OAUTH_SECRET, true);
    }

    @Test
    public void saveAndReadTwitterSession() {
        mMockTwitterSharedPrefs.saveTwitterSession(mMockContext, mSession);

        TwitterSession session = mMockTwitterSharedPrefs.getTwitterSession(mMockContext);

        assertEquals(mSession.getOAuthToken(), session.getOAuthToken());
        assertEquals(mSession.getOAuthSecret(), session.getOAuthSecret());
        assertEquals(mSession.isLoggedIn(), session.isLoggedIn());
    }
}
