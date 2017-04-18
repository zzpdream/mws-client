package com.ydd.conference.util;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hehelt on 16/3/19.
 */
public class ActivityManager {

    private static List<Activity> mActivities = new ArrayList<>();

    public static void add(Activity activity) {
        mActivities.add(activity);
    }

    public static void remove(Activity activity) {
        mActivities.remove(activity);
    }

    public static void clear() {
        for (int i = 0; i < mActivities.size(); i++) {
            mActivities.get(i).finish();
        }
    }
}
