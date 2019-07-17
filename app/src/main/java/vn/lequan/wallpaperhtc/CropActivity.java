package vn.lequan.wallpaperhtc;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.WindowManager;
import android.widget.ImageView;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;

import vn.lequan.wallpaperhtc.utils.Debug;
import vn.lequan.wallpaperhtc.utils.GlobalApp;


public class CropActivity extends AppCompatActivity implements
        CropImageView.OnCropImageCompleteListener {
    private Bitmap bitmap;
    private CropImageView cropImageView;
    private ImageView img_crop, img_xoay, img_rota;
    private File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        cropImageView = findViewById(R.id.cropImageView);
        img_crop = findViewById(R.id.img_crop);
        img_xoay = findViewById(R.id.img_xoay);
        img_rota = findViewById(R.id.img_rota);
        cropImageView.setOnCropImageCompleteListener(this);
        bitmap = GlobalApp.imgAvatar;
        cropImageView.setImageBitmap(bitmap);
        img_xoay.setOnClickListener(v -> cropImageView.rotateImage(90));
        img_rota.setOnClickListener(v -> cropImageView.flipImageHorizontally());
        cropImageView.setAspectRatio(9, 16);
        File path = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        file = new File(path, "avatar.jpg");
        img_crop.setOnClickListener(v -> cropImageView.saveCroppedImageAsync(Uri.fromFile(file)));
    }


    @Override
    public void onCropImageComplete(CropImageView view, CropImageView.CropResult result) {
        if (result.getError() != null)
            Debug.e(result.getError().getMessage());
        if (result.getUri() != null)
            shoDialog(view.getCroppedImage());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        cropImageView.setOnCropImageCompleteListener(null);
        finish();
    }

    private void shoDialog(final Bitmap bitmap) {
        new AlertDialog.Builder(CropActivity.this, R.style.MyDialogTheme)
                .setTitle(getResources().getString(R.string.upload))
                .setCancelable(true)
                .setMessage(getResources().getString(R.string.cut_image))
                .setPositiveButton(getResources().getString(R.string.yes), (dialog, which) -> {
                    GlobalApp.imgAvatar = bitmap;
                    setResult(2);
                    finish();
                }).setNegativeButton(getResources().getString(R.string.no), (dialog, which) -> dialog.dismiss()).show();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        cropImageView.rotateImage(90);
    }
}