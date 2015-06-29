/**
 * FILE: Hashtag.java
 * AUTHOR: Dr. Isaac Ben-Akiva <isaac.ben-akiva@ubimobitech.com>
 * <p/>
 * CREATED ON: 27/06/15
 */

package com.ubimobitech.ubitwitter.model;

import java.util.List;

/**
 * Created by benakiva on 27/06/15.
 */
public class Hashtag {
    private String text;
    private List<Integer> indices;

    public String getText() {
        return text;
    }

    public int getStartIndex() {
        return (indices != null & indices.size() == 2) ? indices.get(0) : 0;
    }

    public int getEndIndex() {
        return (indices != null & indices.size() == 2) ? indices.get(1) : 0;
    }
}
