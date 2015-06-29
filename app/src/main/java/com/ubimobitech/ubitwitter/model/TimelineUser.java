/**
 * FILE: TimelineUser.java
 * AUTHOR: Dr. Isaac Ben-Akiva <isaac.ben-akiva@ubimobitech.com>
 * <p/>
 * CREATED ON: 27/06/15
 */

package com.ubimobitech.ubitwitter.model;

/**
 * Created by benakiva on 27/06/15.
 */
public class TimelineUser {
    private long id;
    private String name;
    private String screen_name;
    private String profile_image_url;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getScreenName() {
        return screen_name;
    }

    public String getProfileImageUrl() {
        return profile_image_url;
    }
}
