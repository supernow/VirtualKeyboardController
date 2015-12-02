package io.github.erehmi.library.vkb;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout.LayoutParams;

import io.github.erehmi.library.util.SoftKeyboardCompat;

/**
 * @author WhatsAndroid
 */
public class LinearLayoutManager extends VirtualKeyboardController.LayoutManager {
    private static final String TAG = "LinearLayoutManager";

    private SoftKeyboardCompat mSoftKeyboardCompat;

    public LinearLayoutManager(Context context, SoftKeyboardCompat softKeyboardCompat) {
        super(context);
        mSoftKeyboardCompat = softKeyboardCompat;
    }

    @Override
    protected int getVirtualKeyboardHeight() {
        return mSoftKeyboardCompat.getSuggestedHeight(getFocusView());
    }

    @Override
    protected void onVirtualKeyboardViewShow() {
        final View contentView = getContentView();
        final View focusView = getFocusView();
        LayoutParams layoutParams = (LayoutParams) contentView.getLayoutParams();
        layoutParams.height = contentView.getHeight();
        if (!mSoftKeyboardCompat.isShown(focusView)) {
            layoutParams.height -= mSoftKeyboardCompat.getSuggestedHeight(focusView);
        }
        layoutParams.weight = 0f;

        // DON'T call setLayoutParams(...) immediately!!!
        // VirtualKeyboardController.LayoutManager will call setLayoutParams(...) for you.
    }

    @Override
    protected void onVirtualKeyboardViewHide() {
        final View contentView = getContentView();
        LayoutParams layoutParams = (LayoutParams) contentView.getLayoutParams();
        layoutParams.weight = 1.0f;
        layoutParams.height = 0;

        // DON'T call setLayoutParams(...) immediately!!!
        // VirtualKeyboardController.LayoutManager will call setLayoutParams(...) for you.
    }
}
