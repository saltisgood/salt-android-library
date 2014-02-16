package com.nickstephen.lib.gui;

/**
 * Created by Nick on 26/11/13.
 */
public final class FragmentUtil {
    private FragmentUtil() {}

    /**
     * Get the id of the root view of an ActionBarActivity. Normally used in conjunction with
     * Fragments to replace the entire view of an Activity at once.
     * e.g. FragmentManager.beginTransaction().replace(getContentViewCompat(), new Fragment())
     * @return
     */
    /* public static int getContentViewCompat() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH ?
                android.R.id.content : R.id.action_bar_activity_content;
    } */
}
