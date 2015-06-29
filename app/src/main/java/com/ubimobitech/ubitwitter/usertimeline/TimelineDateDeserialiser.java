/**
 * FILE: TimelineDateDeserialiser.java
 * AUTHOR: Dr. Isaac Ben-Akiva <isaac.ben-akiva@ubimobitech.com>
 * <p/>
 * CREATED ON: 27/06/15
 */

package com.ubimobitech.ubitwitter.usertimeline;

import android.util.Log;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by benakiva on 27/06/15.
 */
public class TimelineDateDeserialiser implements JsonDeserializer<Date> {
    private static final String TAG = TimelineDateDeserialiser.class
            .getSimpleName();

    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
            "EEE MMM dd HH:mm:ss Z yyyy");

    public Date deserialize(JsonElement json, Type typeOfT,
                            JsonDeserializationContext context) throws JsonParseException {

        try {
            return simpleDateFormat.parse(json.getAsJsonPrimitive()
                    .getAsString());
        } catch (ParseException e) {
            Log.e(TAG, "JSON date Parsing ERROR.");
            return null;
        }
    }
}
