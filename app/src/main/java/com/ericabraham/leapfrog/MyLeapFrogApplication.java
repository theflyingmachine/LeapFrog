package com.ericabraham.leapfrog;

import android.app.Application;
import android.support.multidex.MultiDex;

public class MyLeapFrogApplication extends Application {

  @Override public void onCreate() {
    super.onCreate();
    MultiDex.install(this);
  }
}
