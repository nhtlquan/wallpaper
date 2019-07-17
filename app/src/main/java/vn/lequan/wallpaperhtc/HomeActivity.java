package vn.lequan.wallpaperhtc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Process;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.LinkedHashMap;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import vn.lequan.wallpaperhtc.utils.Debug;

public class HomeActivity extends AppCompatActivity {
    private SmartTabLayout viewPagerTab;
    LinearLayout headerTab;
    ViewPager pager;
    FrameLayout tab;
    private LinkedHashMap<String, Fragment> mapFragments;


    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        headerTab = findViewById(R.id.header_tab);
        pager = findViewById(R.id.viewpager1);
        tab = findViewById(R.id.tab);
        tab.addView(LayoutInflater.from(this).inflate(R.layout.tab_indicator, tab, false));
        viewPagerTab = findViewById(R.id.viewpagertab);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mapFragments = new LinkedHashMap<>();
        mapFragments.put(getResources().getString(R.string.tab_wallpaper), WallpaperFragment.newInstance());
        mapFragments.put(getResources().getString(R.string.tab_my_wallpaper), MyWallpaperFragment.newInstance());
        TabFragmentAdapter tabFragmentAdapter = new TabFragmentAdapter(this, getSupportFragmentManager(), mapFragments);
        pager.setAdapter(tabFragmentAdapter);
        viewPagerTab.setViewPager(pager);
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions
                .request(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(granted -> {
                    if (granted) {
                    } else {
                        // Oups permission denied
                    }
                });
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


}
