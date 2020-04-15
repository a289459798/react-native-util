
package com.ichong.zzy.util;

import com.facebook.react.bridge.*;
import com.qiniu.android.common.Zone;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.*;
import org.json.JSONObject;

public class RNQiNiuModule extends ReactContextBaseJavaModule {

    private final ReactApplicationContext reactContext;

    public RNQiNiuModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    @Override
    public String getName() {
        return "RNQiNiu";
    }

    private volatile boolean isCancelled = false;
    private volatile WritableMap imagePaths;
    private volatile int successCount = 0;
    private int mCount = 0;

    @ReactMethod
    public void upload(final ReadableMap files, String token, String dir, String zone,
                       final Promise promise) {

        imagePaths = Arguments.createMap();
        successCount = 0;
        isCancelled = false;
        mCount = 0;

        Zone uploadZone = Zone.zone0;

        if ("zone1".equals(zone)) {
            uploadZone = Zone.zone1;
        }

        Configuration configuration = new Configuration.Builder().zone(uploadZone).build();

        UploadManager uploadManager = new UploadManager(configuration);
        ReadableMapKeySetIterator iter = files.keySetIterator();
        while (iter.hasNextKey()) {
            if (files.getMap(iter.nextKey()) != null) {
                mCount++;
            }
        }
        if (mCount > 0) {
            iter = files.keySetIterator();
            while (iter.hasNextKey()) {
                final String k = iter.nextKey();
                ReadableMap fileObj = files.getMap(k);
                if (fileObj == null) {
                    continue;
                }
                String data = fileObj.getString("uri").replace("file://", "");
                String key = String.format("%s%s%s.jpg", dir, System.currentTimeMillis() + "", "34563");
                uploadManager.put(data, key, token,
                    new UpCompletionHandler() {
                        @Override
                        public void complete(String key, ResponseInfo info, JSONObject res) {
                            //res包含hash、key等信息，具体字段取决于上传策略的设置。
                            if (info.isOK()) {

                                successCount++;
                                imagePaths.putString(k, key);
                                if (successCount == mCount) {

                                    promise.resolve(imagePaths);
                                }

                            } else {

                                isCancelled = true;
                                promise.reject("999", new Exception(info.error));
                            }
                        }
                    }, new UploadOptions(null, null, false, null,
                        new UpCancellationSignal() {
                            public boolean isCancelled() {
                                return isCancelled;
                            }
                        }));
            }

        } else {

            promise.reject("999", new Exception("请选择上传文件"));
        }
    }
}