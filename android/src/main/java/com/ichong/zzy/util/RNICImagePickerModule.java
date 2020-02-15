package com.ichong.zzy.util;

import android.util.Base64;
import com.facebook.react.bridge.*;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnResultCallbackListener;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zzy on 16/9/8.
 * Date : 16/9/8 13:21
 */
public class RNICImagePickerModule extends ReactContextBaseJavaModule {

    private List<LocalMedia> selectImages;

    public RNICImagePickerModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return "RNICImagePicker";
    }

    @ReactMethod
    public void open(final ReadableMap params,
                     ReadableArray selectedAssets,
                     final Callback callback) {

        int max = 1;
        if (params.getInt("max") > 0) {
            max = params.getInt("max");
        }
        boolean crop = false;
        if (params.hasKey("crop")) {
            crop = params.getBoolean("crop");
        }
        final boolean finalCrop = crop;

        List<LocalMedia> select = new ArrayList<>();
        if (selectedAssets != null && selectImages != null) {

            for (int i = 0; i < selectedAssets.size(); i++) {
                if (selectedAssets.getType(i) != ReadableType.Null) {
                    select.add(selectImages.get(i));
                }
            }
        }

        PictureSelector.create(getCurrentActivity())
            .openGallery(PictureMimeType.ofImage())
            .maxSelectNum(max)
            .selectionMedia(select)
            .selectionMode(max == 1 ? PictureConfig.SINGLE : PictureConfig.MULTIPLE)
            .enableCrop(crop)
            .isGif(false)
            .loadImageEngine(GlideEngine.createGlideEngine())
            .forResult(new OnResultCallbackListener() {
                @Override
                public void onResult(List<LocalMedia> result) {
                    selectImages = result;
                    WritableArray list = Arguments.createArray();
                    for (int i = 0; i < result.size(); i++) {
                        WritableMap map = Arguments.createMap();
                        String path = result.get(i).getPath();
                        if (finalCrop) {
                            path = result.get(i).getCutPath();
                        }
                        map.putString("uri", "file://" + path);
                        map.putInt("width", result.get(i).getWidth());
                        map.putInt("height", result.get(i).getHeight());
                        map.putDouble("size", result.get(i).getSize());
                        map.putString("mediaType", result.get(i).getMimeType());
                        boolean base64 = false;
                        if (params.hasKey("base64")) {
                            base64 = params.getBoolean("base64");
                        }
                        if (base64) {
                            String encodeString = getBase64StringFromFile(path);
                            map.putString("base64", encodeString);

                        }
                        list.pushMap(map);
                    }
                    if (callback != null) {
                        callback.invoke(list);
                    }
                }
            });

    }

    private String getBase64StringFromFile(String absoluteFilePath) {
        InputStream inputStream;
        try {
            inputStream = new FileInputStream(new File(absoluteFilePath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }

        byte[] bytes;
        byte[] buffer = new byte[8192];
        int bytesRead;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        bytes = output.toByteArray();
        return "data:image/jpeg;base64," + Base64.encodeToString(bytes, Base64.NO_WRAP);
    }

}
