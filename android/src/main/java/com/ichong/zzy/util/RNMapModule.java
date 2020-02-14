package com.ichong.zzy.util;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

/**
 * Created by zzy on 16/9/8.
 * Date : 16/9/8 13:21
 */
public class RNMapModule extends ReactContextBaseJavaModule {

    private ReactApplicationContext mContent;

    public RNMapModule(ReactApplicationContext reactContext) {
        super(reactContext);
        mContent = reactContext;

    }

    @Override
    public String getName() {
        return "RNMap";
    }

    @ReactMethod
    public void checkInstall(final String packageName, Callback callback) {
        if (packageName == null || packageName.isEmpty()) {
            callback.invoke(false);
        }
        PackageInfo packageInfo;
        try {
            packageInfo = mContent.getPackageManager().getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
            e.printStackTrace();
        }
        if (packageInfo == null) {
            callback.invoke(false);
        } else {
            callback.invoke(true);
        }
    }

    @ReactMethod
    public void open(final String type, final String address) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        switch (type) {
            case "gaode":
                intent.setPackage("com.autonavi.minimap");
                intent.addCategory("android.intent.category.DEFAULT");
                intent.setData(Uri.parse("androidamap://route?sourceApplication=" + R.string.app_name
                    + "&sname=我的位置"
                    + "&dname=" + address
                    + "&dev=0&m=0&t=1"));
                mContent.startActivity(intent);
                break;
            case "baidu":
                intent.setData(Uri.parse("baidumap://map/direction?origin=我的位置&destination=name:"
                    + address
                    + "&mode=transit&sy=3&index=0&target=1"));
                mContent.startActivity(intent);
                break;
            default:
                break;
        }
    }

}
