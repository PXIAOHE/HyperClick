package com.xiaohe.hyperclick;

import android.util.Log;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class MainHook implements IXposedHookLoadPackage {
    private static final String TAG = "HyperClick";
    private static final String TARGET_CLASS = "com.android.systemui.qs.tiles.ScreenLockTile";

    @Override
    public void handleLoadPackage(LoadPackageParam lpparam) throws Throwable {
        if (!"com.android.systemui".equals(lpparam.packageName)) return;

        try {
            // Hook 基类确保方法一定存在
            XposedHelpers.findAndHookMethod(
                    "com.android.systemui.qs.tileimpl.QSTileImpl",
                    lpparam.classLoader,
                    "handleLongClick",
                    "com.android.systemui.animation.Expandable",
                    new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            // 判定当前长按的是否为锁屏按钮
                            if (param.thisObject.getClass().getName().equals(TARGET_CLASS)) {
                                Log.d(TAG, "ScreenLockTile long click detected!");

                                try {
                                    // 获取 WindowManager 并弹出电源菜单
                                    Class<?> wmGlobal = XposedHelpers.findClass("android.view.WindowManagerGlobal", lpparam.classLoader);
                                    Object wmService = XposedHelpers.callStaticMethod(wmGlobal, "getWindowManagerService");
                                    if (wmService != null) {
                                        XposedHelpers.callMethod(wmService, "showGlobalActions");
                                        // 拦截原有逻辑
                                        param.setResult(null);
                                    }
                                } catch (Exception e) {
                                    Log.e(TAG, "Failed to show global actions: " + e.getMessage());
                                }
                            }
                        }
                    }
            );
        } catch (Throwable t) {
            Log.e(TAG, "Hooking error: " + t.getMessage());
        }
    }
}
