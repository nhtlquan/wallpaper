package vn.lequan.wallpaperhtc.utils;

import android.app.Application;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import vn.lequan.wallpaperhtc.R;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/JosefinSans-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }


}