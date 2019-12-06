
package com.ichong.zzy.util;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;

public class RNUtilModule extends ReactContextBaseJavaModule {

  private final ReactApplicationContext reactContext;

  public RNUtilModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;
  }

  @Override
  public String getName() {
    return "RNUtil";
  }
}