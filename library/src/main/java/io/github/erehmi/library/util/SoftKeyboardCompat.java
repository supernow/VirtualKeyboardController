package io.github.erehmi.library.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.view.Display;
import android.view.View;

/**
 * @author WhatsAndroid
 */
public class SoftKeyboardCompat {
    private static final String TAG = "SoftKeyboardCompat";

    private static final String PREFS_NAME = "last_saved_states_prefs";
    private static final String PREF_KEY_HEIGHT = "soft_keyboard_height";
    private static final int DEFAULT_HEIGHT = 780;

    private Activity mActivity;
    private SharedPreferences mPreferences;

    public SoftKeyboardCompat(Activity activity) {
        mActivity = activity;
        mPreferences = activity.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public int getVisibleHeight(View focusView) {
        Rect r = new Rect();
        focusView.getWindowVisibleDisplayFrame(r);
        int height = getDisplayHeight() - r.height() - r.top;
        if (height > 0) {
            applyPref(mPreferences.edit().putInt(PREF_KEY_HEIGHT, height));
        }
        return height;
    }

    private void applyPref(Editor editor) {
        if (VERSION.SDK_INT >= VERSION_CODES.GINGERBREAD) {
            editor.apply();
        } else {
            editor.commit();
        }
    }

    public int getSuggestedHeight(View focusView) {
        int height = getVisibleHeight(focusView);
        return (height > 0) ? height : mPreferences.getInt(PREF_KEY_HEIGHT, DEFAULT_HEIGHT);
    }

    private int getDisplayHeight() {
        Display display = mActivity.getWindowManager().getDefaultDisplay();

        if (VERSION.SDK_INT >= VERSION_CODES.HONEYCOMB_MR2) {
            Point size = new Point();
            display.getSize(size);
            return size.y;
        } else {
            return display.getHeight();
        }
    }

    public boolean isShown(View focusView) {
        int height = getVisibleHeight(focusView);
        return (height > 0);
    }
}
