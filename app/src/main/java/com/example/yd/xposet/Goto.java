package com.example.yd.xposet;

import android.util.Log;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class Goto implements IXposedHookLoadPackage {

    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam loadPackageParam)
            throws Throwable {

        if (!loadPackageParam.packageName.contains("com.tencent.mm")) {
            return;
        }
        XposedHelpers.findAndHookMethod("com.tencent.mm.ui.LauncherUI", loadPackageParam.classLoader,
                "bUn", new XC_MethodHook() {

                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        super.beforeHookedMethod(param);
                        Log.e("Xposed", "开始hook>>>>启动的应用包名" + loadPackageParam.packageName);

                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);
                        XposedBridge.log("成功hook>>>>启动的应用包名" + loadPackageParam.packageName);

                        Log.e("Xposed", "成功hook>>>>启动的应用包名" + loadPackageParam.packageName);
                    }
                });


    }
}
