package com.incognia.reactnative.example;

import static com.facebook.react.defaults.DefaultNewArchitectureEntryPoint.load;
import static com.facebook.react.defaults.DefaultReactHost.getDefaultReactHost;

import android.app.Application;

import androidx.annotation.NonNull;

import com.facebook.react.PackageList;
import com.facebook.react.ReactApplication;
import com.facebook.react.ReactHost;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactPackage;
import com.facebook.react.defaults.DefaultReactNativeHost;
import com.facebook.react.soloader.OpenSourceMergedSoMapping;
import com.facebook.soloader.SoLoader;

import java.io.IOException;
import java.util.List;

import com.incognia.Incognia;
import com.incognia.reactnative.IncogniaPackage;
import com.zoontek.rnpermissions.RNPermissionsPackage;
public class MainApplication extends Application implements ReactApplication {

  private final ReactNativeHost reactNativeHost =
      new DefaultReactNativeHost(this) {
        @Override
        public boolean getUseDeveloperSupport() {
          return BuildConfig.DEBUG;
        }

        @Override
        protected List<ReactPackage> getPackages() {
          List<ReactPackage> packages = new PackageList(this).getPackages();
          // Packages that cannot be autolinked yet can be added manually here, for example:
          // packages.add(new MyReactNativePackage());
          packages.add(new RNPermissionsPackage());
          packages.add(new IncogniaPackage());
          return packages;
        }

        @Override
        protected String getJSMainModuleName() {
          return "index";
        }

        @Override
        public boolean isNewArchEnabled() {
          return BuildConfig.IS_NEW_ARCHITECTURE_ENABLED;
        }

        @Override
        public Boolean isHermesEnabled() {
          return BuildConfig.IS_HERMES_ENABLED;
        }
      };

  @NonNull
  @Override
  public ReactNativeHost getReactNativeHost() {
    return reactNativeHost;
  }

  @NonNull
  @Override
  public ReactHost getReactHost() {
    return getDefaultReactHost(getApplicationContext(), reactNativeHost);
  }

  @Override
  public void onCreate() {
    super.onCreate();
    try {
      SoLoader.init(this, OpenSourceMergedSoMapping.INSTANCE);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    if (BuildConfig.IS_NEW_ARCHITECTURE_ENABLED) {
      // If you opted-in for the New Architecture, we load the native entry point for this app.
      load();
    }
    Incognia.init(this);
  }
}
