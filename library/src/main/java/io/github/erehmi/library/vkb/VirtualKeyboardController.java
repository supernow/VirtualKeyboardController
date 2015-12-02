package io.github.erehmi.library.vkb;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;

import io.github.erehmi.library.util.IMMUtils;

/**
 * @author WhatsAndroid
 */
public class VirtualKeyboardController {
    private static final String TAG = "VirtualKeyboardController";

    private Context mContext;
    private LayoutManager mLayoutManager;

    public VirtualKeyboardController(Context context) {
        mContext = context;
    }

    public void setLayoutManager(LayoutManager layoutManager) {
        mLayoutManager = layoutManager;
        mLayoutManager.mVirtualKeyboardView.setVisibility(View.GONE);
        mLayoutManager.addOnGlobalLayoutListener();
    }

    public void showVirtualKeyboard() {
        if (mLayoutManager.mFocusView != null) {
            IMMUtils.hideSoftInput(mContext, mLayoutManager.mFocusView);
        }

        mLayoutManager.showVirtualKeyboardView();
    }

    public void hideVirtualKeyboard() {
        hideVirtualKeyboard(true);
    }

    public void hideVirtualKeyboard(boolean shouldShowSoftInput) {
        if (mLayoutManager.mFocusView != null && shouldShowSoftInput) {
            IMMUtils.showSoftInput(mContext, mLayoutManager.mFocusView);
        }

        mLayoutManager.hideVirtualKeyboardView(!shouldShowSoftInput);
    }

    public boolean isVirtualKeyboardShown() {
        return mLayoutManager.isVirtualKeyboardViewShown();
    }

    public void toggleVirtualKeyboard() {
        if (isVirtualKeyboardShown()) {
            hideVirtualKeyboard();
        } else {
            showVirtualKeyboard();
        }
    }

    public static abstract class LayoutManager {
        private Context mContext;
        private View mContentView;
        private View mFocusView;
        private View mVirtualKeyboardView;

        public LayoutManager(Context context) {
            mContext = context;
        }

        public Context getContext() {
            return mContext;
        }

        public void setContentView(View contentView) {
            mContentView = contentView;
        }

        protected View getContentView() {
            return mContentView;
        }

        public void setSoftInputFocusView(View focusView) {
            mFocusView = focusView;
        }

        protected View getFocusView() {
            return mFocusView;
        }

        public void setVirtualKeyboardView(View view) {
            mVirtualKeyboardView = view;
        }

        protected View getVirtualKeyboardView() {
            return mVirtualKeyboardView;
        }

        public void addOnGlobalLayoutListener() {
            mVirtualKeyboardView.getViewTreeObserver()
                    .addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                            if (mVirtualKeyboardView != null && !isVirtualKeyboardViewShown()) {
                                onVirtualKeyboardViewHide();
                            }
                        }
                    });
        }

        void showVirtualKeyboardView() {
            onVirtualKeyboardViewShow();

            LayoutParams layoutParams = mVirtualKeyboardView.getLayoutParams();
            layoutParams.height = getVirtualKeyboardHeight();
            mVirtualKeyboardView.setLayoutParams(layoutParams);
            mVirtualKeyboardView.setVisibility(View.VISIBLE);
        }

        protected abstract int getVirtualKeyboardHeight();

        protected abstract void onVirtualKeyboardViewShow();

        void hideVirtualKeyboardView(boolean restoreContentLayoutParams) {
            mVirtualKeyboardView.getLayoutParams().height = 0;
            if (restoreContentLayoutParams) {
                onVirtualKeyboardViewHide();
                mContentView.requestLayout();
            }
        }

        protected abstract void onVirtualKeyboardViewHide();

        boolean isVirtualKeyboardViewShown() {
            //return mVirtualKeyboardView.getLayoutParams().height > 0;
            return mVirtualKeyboardView.isShown() && mVirtualKeyboardView.getHeight() > 0;
        }
    }
}
