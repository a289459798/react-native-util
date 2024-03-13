package com.ichong.zzy.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Base64;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.facebook.react.bridge.*;
import com.luck.picture.lib.basic.PictureSelectionModel;
import com.luck.picture.lib.basic.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.config.SelectMimeType;
import com.luck.picture.lib.config.SelectModeConfig;
import com.luck.picture.lib.engine.CompressFileEngine;
import com.luck.picture.lib.engine.CropEngine;
import com.luck.picture.lib.engine.CropFileEngine;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.interfaces.OnKeyValueResultCallbackListener;
import com.luck.picture.lib.interfaces.OnResultCallbackListener;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropImageEngine;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import top.zibin.luban.Luban;
import top.zibin.luban.OnNewCompressListener;

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

        PictureSelectionModel model = PictureSelector.create(getCurrentActivity())
                .openGallery(SelectMimeType.ofImage())
                .setMaxSelectNum(max)
                .setSelectedData(select)
                .setSelectionMode(max == 1 ? SelectModeConfig.SINGLE : SelectModeConfig.MULTIPLE)
                .isDirectReturnSingle(true)
//                .enableCrop(crop)
                .isGif(false)
                .setCompressEngine(new CompressFileEngine() {
                    @Override
                    public void onStartCompress(Context context, ArrayList<Uri> source, OnKeyValueResultCallbackListener call) {
                        Luban.with(context).load(source).ignoreBy(100)
                                .setCompressListener(new OnNewCompressListener() {
                                    @Override
                                    public void onStart() {

                                    }

                                    @Override
                                    public void onSuccess(String source, File compressFile) {
                                        if (call != null) {
                                            call.onCallback(source, compressFile.getAbsolutePath());
                                        }
                                    }

                                    @Override
                                    public void onError(String source, Throwable e) {
                                        if (call != null) {
                                            call.onCallback(source, null);
                                        }
                                    }
                                }).launch();
                    }
                }).setImageEngine(GlideEngine.createGlideEngine());

        if (crop) {
            model.setCropEngine(new CropFileEngine() {

                @Override
                public void onStartCrop(Fragment fragment, Uri srcUri, Uri destinationUri, ArrayList<String> dataSource, int requestCode) {
// 注意* 如果你实现自己的裁剪库，需要在Activity的.setResult();
                    // Intent中需要给MediaStore.EXTRA_OUTPUT，塞入裁剪后的路径；如果有额外数据也可以通过CustomIntentKey.EXTRA_CUSTOM_EXTRA_DATA字段存入；

                    UCrop uCrop = UCrop.of(srcUri, destinationUri, dataSource);
                    uCrop.setImageEngine(new UCropImageEngine() {
                        @Override
                        public void loadImage(Context context, String url, ImageView imageView) {
                            Glide.with(context).load(url).into(imageView);
                        }

                        @Override
                        public void loadImage(Context context, Uri url, int maxWidth, int maxHeight, OnCallbackListener<Bitmap> call) {

                        }
                    });
//                    uCrop.withOptions(buildOptions());
                    uCrop.start(fragment.getActivity(), fragment, requestCode);
                }
            });

        }

        model.forResult(new OnResultCallbackListener<LocalMedia>() {
            @Override
            public void onResult(ArrayList<LocalMedia> result) {
                selectImages = result;
                WritableArray list = Arguments.createArray();
                for (int i = 0; i < result.size(); i++) {
                    WritableMap map = Arguments.createMap();
                    String path = result.get(i).getRealPath();
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

            @Override
            public void onCancel() {

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
