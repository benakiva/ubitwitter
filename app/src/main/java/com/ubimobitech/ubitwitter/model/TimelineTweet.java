/**
 * FILE: TimelineTweet.java
 * AUTHOR: Dr. Isaac Ben-Akiva <isaac.ben-akiva@ubimobitech.com>
 * <p/>
 * CREATED ON: 27/06/15
 */

package com.ubimobitech.ubitwitter.model;

import java.util.Date;

/**
 * Created by benakiva on 27/06/15.
 */
public class TimelineTweet {
    private long id;
    private Date created_at;
    private Entities entities;
    private String text;
    private TimelineUser user;

    public long getId() {
        return id;
    }

    public Date getCreatedAt() {
        return created_at;
    }

    public Entities getEntities() {
        return entities;
    }

    public String getText() {
        return text;
    }

    public TimelineUser getUser() {
        return user;
    }
}
