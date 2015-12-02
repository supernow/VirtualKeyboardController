package io.github.erehmi.library.util;

import android.content.Context;
import android.os.IBinder;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * 控制软键盘弹出隐藏的工具类
 *
 * @author WhatsAndroid
 */
public class IMMUtils {
    public static InputMethodManager from(Context context) {
        return (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    public static boolean isActive(Context context) {
        InputMethodManager imm = from(context);
        return imm.isActive();
    }

    public static boolean isActive(Context context, View focusView) {
        InputMethodManager imm = from(context);
        return imm.isActive(focusView);
    }

    /** 切换软件盘弹出隐藏状态, 如果软键盘处于隐藏状态时, 调用该方法, 会弹出软键盘; 否则隐藏软键盘. */
    public static void toggleSoftInput(Context context, View focusView) {
        InputMethodManager imm = from(context);
        if (imm.isActive()) {
            IBinder token = focusView.getWindowToken();
            imm.toggleSoftInputFromWindow(token,
                    InputMethodManager.SHOW_IMPLICIT,
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /** 弹出软件盘 */
    public static void showSoftInput(Context context, View focusView) {
        InputMethodManager imm = from(context);
        if (imm.isActive()) {
            imm.showSoftInput(focusView, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    /** 隐藏软件盘 */
    public static void hideSoftInput(Context context, View focusView) {
        InputMethodManager imm = from(context);
        if (imm.isActive()) {
            IBinder token = focusView.getWindowToken();
            imm.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
