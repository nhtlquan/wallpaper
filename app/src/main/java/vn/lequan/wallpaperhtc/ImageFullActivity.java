package vn.lequan.wallpaperhtc;

import android.annotation.SuppressLint;
import android.app.WallpaperManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;

import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorListener;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;


import java.io.File;
import java.io.FileOutputStream;

import vn.lequan.wallpaperhtc.control.OnItemClickListener;
import vn.lequan.wallpaperhtc.utils.GlobalApp;
import vn.lequan.wallpaperhtc.utils.PrefManager;


public class ImageFullActivity extends AppCompatActivity {
    private ImageView imageView;
    private ImageView llBack, llEdit;
    private PrefManager pref;
    private Button llSetWallpaper;
    private String id;
    public static SharedPreferences sharedpreferences;
    private static final String MyPREFERENCES = "Wallpaper";
    int PRIVATE_MODE = 0;
    SharedPreferences.Editor edit;
    private ProgressBar marker_progress;

    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_fullscreen_image);
        sharedpreferences = getApplicationContext().getSharedPreferences(MyPREFERENCES, PRIVATE_MODE);
        edit = sharedpreferences.edit();
        marker_progress = findViewById(R.id.marker_progress);
        marker_progress.setVisibility(View.GONE);
        imageView = findViewById(R.id.imgFullscreen);
        llBack = findViewById(R.id.llBack);
        llSetWallpaper = findViewById(R.id.llSetWallpaper);
        llEdit = findViewById(R.id.llEdit);
        pref = new PrefManager(this);
        pref.setGalleryName("Wallpaper");
        id = getIntent().getStringExtra("id");
        if (getIntent().getBooleanExtra("isCrop", true)) {
            llEdit.setVisibility(View.VISIBLE);
        } else {
            llEdit.setVisibility(View.GONE);
        }
        imageView.setOnClickListener(v -> {
            if (llSetWallpaper.getVisibility() == View.VISIBLE) {
                llSetWallpaper.setVisibility(View.GONE);
                llBack.setVisibility(View.GONE);
                llEdit.setVisibility(View.GONE);
            } else {
                llSetWallpaper.setVisibility(View.VISIBLE);
                llBack.setVisibility(View.VISIBLE);
                if (getIntent().getBooleanExtra("isCrop", true))
                    llEdit.setVisibility(View.VISIBLE);
            }
        });
        llSetWallpaper.setOnClickListener(view -> ViewCompat.animate(view)
                .setDuration(200)
                .scaleX(0.9f)
                .scaleY(0.9f)
                .setInterpolator(new CycleInterpolator())
                .setListener(new ViewPropertyAnimatorListener() {
                    @Override
                    public void onAnimationStart(final View view) {

                    }

                    @Override
                    public void onAnimationEnd(final View view) {
                        OnItemClickListener onItemClickListener = new OnItemClickListener() {
                            @Override
                            public void onItemClicked(int position, Bitmap bitmap, String id) {

                            }

                            @Override
                            public void onItemClicked(int position) {
                                switch (position) {
                                    case 1:
                                        setAsWallpaper(GlobalApp.imgAvatar);
                                        setAsWallpaperLockScreen(GlobalApp.imgAvatar);
                                        break;
                                    case 2:
                                        setAsWallpaperLockScreen(GlobalApp.imgAvatar);
                                        break;
                                }
                            }
                        };
                        CustomDialogSetWallpaper customDialogSetWallpaper = new CustomDialogSetWallpaper(ImageFullActivity.this, onItemClickListener);
                        customDialogSetWallpaper.show();

                    }

                    @Override
                    public void onAnimationCancel(final View view) {

                    }
                })
                .

                        withLayer()
                .

                        start());
        llEdit.setOnClickListener(view -> ViewCompat.animate(view)
                .

                        setDuration(200)
                .

                        scaleX(0.9f)
                .

                        scaleY(0.9f)
                .

                        setInterpolator(new CycleInterpolator())
                .

                        setListener(new ViewPropertyAnimatorListener() {
                            @Override
                            public void onAnimationStart(final View view) {

                            }

                            @Override
                            public void onAnimationEnd(final View view) {
                                Intent intent = new Intent(ImageFullActivity.this, CropActivity.class);
                                startActivityForResult(intent, 1);

                            }

                            @Override
                            public void onAnimationCancel(final View view) {

                            }
                        })
                .

                        withLayer()
                .

                        start());

        llBack.setOnClickListener(v -> finish());
//        saveImageToSDCard(GlobalApp.imgAvatar);
    }

    @Override
    protected void onResume() {
        super.onResume();
        imageView.setImageBitmap(GlobalApp.imgAvatar);
    }

    public class CycleInterpolator implements android.view.animation.Interpolator {

        private final float mCycles = 0.5f;

        @Override
        public float getInterpolation(final float input) {
            return (float) Math.sin(2.0f * mCycles * Math.PI * input);
        }
    }


    public void setAsWallpaper(Bitmap bitmap) {
        marker_progress.setVisibility(View.VISIBLE);
        try {
            WallpaperManager wm = WallpaperManager.getInstance(this);
            wm.setBitmap(bitmap);
            Toast.makeText(this,
                    "Thành Công",
                    Toast.LENGTH_SHORT).show();
            creatNewFile();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this,
                    "Không thành công",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void creatNewFile() {
        String fname = id + ".jpg";// tên file mới
        File myDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                pref.getGalleryName());
        myDir.mkdirs();
        File filenew = new File(myDir, fname);
        edit.putString("isSetWallpaper", id);
        edit.apply();
        if (filenew.exists())
            filenew.delete();// xóa file cũ
        try {
            FileOutputStream out = new FileOutputStream(filenew);// tạo file mới
            GlobalApp.imgAvatar.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            marker_progress.setVisibility(View.GONE);
        } catch (Exception e) {
            marker_progress.setVisibility(View.GONE);
            e.printStackTrace();
            Toast.makeText(this,
                    "Lỗi",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void setAsWallpaperLockScreen(Bitmap bitmap) {
        marker_progress.setVisibility(View.VISIBLE);
        try {
            WallpaperManager wm = WallpaperManager.getInstance(this);
            wm.setBitmap(bitmap, null, true, WallpaperManager.FLAG_LOCK);
            Toast.makeText(this,
                    getResources().getString(R.string.succes),
                    Toast.LENGTH_SHORT).show();
            creatNewFile();
        } catch (Exception e) {
            marker_progress.setVisibility(View.GONE);
            e.printStackTrace();
            Toast.makeText(this,
                    getResources().getString(R.string.not_succes),
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 2) {
            id = "edit" + id;
            imageView.setImageBitmap(GlobalApp.imgAvatar);
        }
    }
}
