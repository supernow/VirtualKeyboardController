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
        mLayoutManager.mEmojiKeyboardView.setVisibility(View.GONE);
        mLayoutManager.addOnGlobalLayoutListener();
    }

    public void showEmojiKeyboard() {
        if (mLayoutManager.mFocusView != null) {
            IMMUtils.hideSoftInput(mContext, mLayoutManager.mFocusView);
        }

        mLayoutManager.showEmojiKeyboardView();
    }

    public void hideEmojiKeyboard() {
        hideEmojiKeyboard(true);
    }

    public void hideEmojiKeyboard(boolean shouldShowSoftInput) {
        if (mLayoutManager.mFocusView != null && shouldShowSoftInput) {
            IMMUtils.showSoftInput(mContext, mLayoutManager.mFocusView);
        }

        mLayoutManager.hideEmojiKeyboardView(!shouldShowSoftInput);
    }

    public boolean isEmojiKeyboardShown() {
        return mLayoutManager.isEmojiKeyboardViewShown();
    }

    public void toggleEmojiKeyboard() {
        if (isEmojiKeyboardShown()) {
            hideEmojiKeyboard();
        } else {
            showEmojiKeyboard();
        }
    }

    public static abstract class LayoutManager {
        private Context mContext;
        private View mContentView;
        private View mFocusView;
        private View mEmojiKeyboardView;

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

        public void setEmojiKeyboardView(View view) {
            mEmojiKeyboardView = view;
        }

        protected View getEmojiKeyboardView() {
            return mEmojiKeyboardView;
        }

        public void addOnGlobalLayoutListener() {
            mEmojiKeyboardView.getViewTreeObserver()
                    .addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                            if (mEmojiKeyboardView != null && !isEmojiKeyboardViewShown()) {
                                onEmojiKeyboardViewHide();
                            }
                        }
                    });
        }

        void showEmojiKeyboardView() {
            onEmojiKeyboardViewShow();

            LayoutParams layoutParams = mEmojiKeyboardView.getLayoutParams();
            layoutParams.height = getEmojiKeyboardHeight();
            mEmojiKeyboardView.setLayoutParams(layoutParams);
            mEmojiKeyboardView.setVisibility(View.VISIBLE);
        }

        protected abstract int getEmojiKeyboardHeight();

        protected abstract void onEmojiKeyboardViewShow();

        void hideEmojiKeyboardView(boolean restoreContentLayoutParams) {
            mEmojiKeyboardView.getLayoutParams().height = 0;
            if (restoreContentLayoutParams) {
                onEmojiKeyboardViewHide();
                mContentView.requestLayout();
            }
        }

        protected abstract void onEmojiKeyboardViewHide();

        boolean isEmojiKeyboardViewShown() {
            //return mEmojiKeyboardView.getLayoutParams().height > 0;
            return mEmojiKeyboardView.isShown() && mEmojiKeyboardView.getHeight() > 0;
        }
    }
}
