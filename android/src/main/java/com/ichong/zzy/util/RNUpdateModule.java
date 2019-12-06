package com.ichong.zzy.util;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.beta.UpgradeInfo;

/**
 * Created by zzy on 16/9/8.
 * Date : 16/9/8 13:21
 */
public class RNUpdateModule extends ReactContextBaseJavaModule {


    public RNUpdateModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return "RNUpdate";
    }

    @ReactMethod
    public void check(final boolean show,
                      final Promise promise) {

        UpgradeInfo info = Beta.getUpgradeInfo();
        if (info != null) {

            if (show) {
                Beta.checkUpgrade();
            }
            promise.resolve(null);
        } else {
            promise.reject("999", "没有更新");
        }

    }


}
