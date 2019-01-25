package com.example.yd.xposet;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dalvik.system.DexFile;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class HookToast implements IXposedHookLoadPackage  {

    private XC_LoadPackage.LoadPackageParam m_loadPackageParam;

    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable{

        if (loadPackageParam.packageName.equals("android"))
            return ;

        XposedBridge.log("packageName="+loadPackageParam.packageName);
        /*
        if (loadPackageParam.packageName.equals("com.example.yd.xposet")){

            XposedBridge.log("找到自己的app数据");


            final Class clazz = loadPackageParam.classLoader.loadClass("com.example.yd.xposet.MainActivity");
            this.dumpClass(clazz);
            XposedHelpers.findAndHookMethod(clazz, "toastMessage", new XC_MethodHook(){
            final Class bb = clazz;
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable{
                    XposedBridge.log("33333");
                    super.beforeHookedMethod(param);
                    XposedBridge.log("找到自己的app数据222");
                }

                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable{
                    XposedBridge.log("44444");
                    param.setResult("你已被劫持");
                    XposedBridge.log("找到自己的app数据111");
                    Log.v("1111","22222");

                    Field field=bb.getDeclaredField("view");
                    field.setAccessible(true);
                    TextView textView= (TextView) field.get(param.thisObject);
                    textView.setText("Hello Xposed");
                }

            });

            XposedHelpers.findAndHookMethod("com.example.yd.xposet.MainActivity", loadPackageParam.classLoader,
                    "isModuleActive", XC_MethodReplacement.returnConstant(true));
        }*/


        String strPackageName = "com.smile.gifmaker";
        if (loadPackageParam.packageName.equals(strPackageName)){

            XposedBridge.log("找到快手app数据");

            ArrayList<String> listClassName = new ArrayList<>();
            listClassName.add("a");


            XposedBridge.log("----------------------------------------------------------------------------------");

            try
            {
                //XposedHelpers.findAndHookMethod(Application.class, name, Context.class, new XC_MethodHook() {
                XposedHelpers.findAndHookMethod(Application.class, "attach", Context.class, new XC_MethodHook() {

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        //Class z = XposedHelpers.findClass("com.yxcorp.gifshow.util.CPU", loadPackageParam.classLoader);
                        XposedHelpers.findAndHookMethod("com.yxcorp.gifshow.util.CPU", loadPackageParam.classLoader, "a", Context.class, byte[].class , int.class, new XC_MethodHook() {

                            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                                XposedBridge.log( "调用前1");
                                XposedBridge.log(param.toString());
                                JSONObject json = (JSONObject)JSONObject.wrap(param.args[0]);

                                Map<String,Application> a = new HashMap<String,Application>();
                                a.put("data",(Application)param.args[0]);
                                String str = (new JSONObject(a)).toString();
                                XposedBridge.log(str);
                                XposedBridge.log(param.args[0] + "," + param.args[1].toString() + "," + param.args[2].toString());
                                //XposedBridge.log( "" + XposedHelpers.getObjectField(param.args[0], "b"));
                                XposedBridge.log( "调用前2");
                                Application gg = null;
                                XposedBridge.log(""+ gg);
                                super.beforeHookedMethod(param);
                            }

                            @Override
                            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                                XposedBridge.log( "调用后");
                                XposedBridge.log(param.toString());
                                XposedBridge.log(param.getResult().toString());
                                super.afterHookedMethod(param);
                            }
                        });
                    }
                });
            }
            catch(Throwable e)
            {
                XposedBridge.log("捕获异常" + e.toString());
            }


            XposedBridge.log("----------------------------------------------------------------------------------");
        }
    }

    //打印类所有
    private static Method[] dumpClass(Class actions) {
        XposedBridge.log("Dump class " + actions.getName());

        XposedBridge.log("Methods");
        Method[] m = actions.getDeclaredMethods();
        for (int i = 0; i < m.length; i++) {
            XposedBridge.log(m[i].toString());
        }
        XposedBridge.log("Fields");
        Field[] f = actions.getDeclaredFields();
        for (int j = 0; j < f.length; j++) {
            XposedBridge.log(f[j].toString());
        }
        XposedBridge.log("Classes");
        Class[] c = actions.getDeclaredClasses();
        for (int k = 0; k < c.length; k++) {
            XposedBridge.log(c[k].toString());
        }

        return m;
    }

    public void hook() throws IOException, ClassNotFoundException {
        DexFile dexFile = new DexFile(m_loadPackageParam.appInfo.sourceDir);
        Enumeration<String> classNames = dexFile.entries();
        while (classNames.hasMoreElements()) {
            String className = classNames.nextElement();


            if (isClassNameValid(className)) {
                final Class clazz = Class.forName(className, false, m_loadPackageParam.classLoader);


                for (Method method: clazz.getDeclaredMethods()) {
                    if (!Modifier.isAbstract(method.getModifiers())) {
                        XposedBridge.hookMethod(method, new XC_MethodHook() {
                            @Override
                            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                                log("HOOKED: " + clazz.getName() + "\\" + param.method.getName());
                            }
                        });
                    }
                }
            }
        }
    }


    public void log(Object str) {
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        XposedBridge.log("[" + df.format(new Date()) + "]:  "
                + str.toString());
    }

    public boolean isClassNameValid(String className) {
        return className.startsWith(m_loadPackageParam.packageName)
                && !className.contains("$")
                && !className.contains("BuildConfig")
                && !className.equals(m_loadPackageParam.packageName + ".R");
    }

    public boolean gogogo(){
        String strPackageName = "com.smile.gifmaker";
        if (m_loadPackageParam.packageName.equals(strPackageName)){

            XposedBridge.log("找到快手app数据");

            ArrayList<String> listClassName = new ArrayList<>();
            //listClassName.add(strPackageName + ".CoreEventBusIndex");
            listClassName.add(strPackageName + ".a");
            listClassName.add("com.smile.gifshow.a");
            listClassName.add("com.yxcorp.gifshow.retrofit.c");
            listClassName.add("com.yxcorp.retrofit.d.d");
            listClassName.add("com.yxcorp.gifshow.util.CPU");
            listClassName.add("com.yxcorp.retrofit.g");
            listClassName.add("com.yxcorp.gifshow.retrofit.service.KwaiApiService");
            listClassName.add("com.yxcorp.gifshow.retrofit.service.KwaiHttpsService");

            XposedBridge.log("----------------------------------------------------------------------------------");

            for (String name : listClassName) {

                try
                {
                    XposedBridge.log("要查找的类:" + name);
                    final Class clazz = m_loadPackageParam.classLoader.loadClass(name);
                    Method[] arrayMethod= this.dumpClass(clazz);

                        for (Method m : arrayMethod){


                            XposedHelpers.findAndHookMethod(clazz, m.getName(), new XC_MethodHook() {

                            final Class bb = clazz;

                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                                super.beforeHookedMethod(param);


                                String a = param.thisObject.toString();

                                XposedBridge.log("gotoBefore" + a);

                                Log.v("33333", a);
                            }

                            @Override
                            protected void afterHookedMethod(MethodHookParam param) throws Throwable {

                                Log.v("555555", "666666");
                                String a = param.thisObject.toString();
                                XposedBridge.log("gotoafter" + a);

                                Field field = bb.getDeclaredField("view");
                                field.setAccessible(true);
                                TextView textView = (TextView) field.get(param.thisObject);
                                textView.setText("Hello Xposed");
                            }

                        });
                    }
                }
                catch(Throwable e)
                {
                    XposedBridge.log("捕获异常" + e.toString());
                }

            }

            XposedBridge.log("----------------------------------------------------------------------------------");
        }

        return true;
    }
}
