package cn.bsd.learn.hotfix.sample;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

import cn.bsd.learn.hotfix.library.FixDexUtils;

//主包
public class BaseApplication extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
//        MultiDex.install(this);
        FixDexUtils.loadFixedDex(this);
    }
}
