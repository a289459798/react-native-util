
package com.ichong.zzy.util;

import cc.shinichi.library.ImagePreview;
import cc.shinichi.library.bean.ImageInfo;
import com.facebook.react.bridge.*;

import java.util.ArrayList;
import java.util.List;

public class RNImageBrowserModule extends ReactContextBaseJavaModule {

    private final ReactApplicationContext reactContext;

    public RNImageBrowserModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    @Override
    public String getName() {
        return "RNImageBrowser";
    }

    @ReactMethod
    public void show(ReadableArray list) {
        show(list, 0);
    }

    @ReactMethod
    public void show(ReadableArray list, int index) {
        ArrayList<String> images = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            ReadableMap map = list.getMap(i);
            images.add(map.getString("image"));
        }
        ImagePreview
            .getInstance()
            .setContext(reactContext.getCurrentActivity())
            .setIndex(index)
            .setImageList(images)
            .setEnableDragClose(true)
            .setEnableUpDragClose(true)
            .start();
    }

}
