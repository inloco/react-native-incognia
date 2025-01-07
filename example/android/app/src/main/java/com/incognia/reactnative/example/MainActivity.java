package com.incognia.reactnative.example;

import com.facebook.react.ReactActivity;
import com.facebook.react.ReactActivityDelegate;
import com.facebook.react.defaults.DefaultReactActivityDelegate;

import java.util.Objects;

public class MainActivity extends ReactActivity {

  /**
   * Returns the name of the main component registered from JavaScript. This is used to schedule
   * rendering of the component.
   */
  @Override
  protected String getMainComponentName() {
    return "IncogniaExample";
  }

  @Override
  public ReactActivityDelegate createReactActivityDelegate() {
    return new DefaultReactActivityDelegate(this, Objects.requireNonNull(getMainComponentName()), true);
  }
}
