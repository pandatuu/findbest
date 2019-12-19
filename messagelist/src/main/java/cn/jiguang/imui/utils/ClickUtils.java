package cn.jiguang.imui.utils;

public class ClickUtils {
    private static final int MIN_DELAY_TIME = 1000;  // 两次点击间隔不能少于1000ms
    private static long lastClickTime;
    public static boolean clickEnable() {
        boolean flag = false;
        long currentClickTime = System.currentTimeMillis();
        if ((currentClickTime - lastClickTime) >= MIN_DELAY_TIME) {
            flag = true;
        }
        lastClickTime = currentClickTime;
        return flag;
    }
}
