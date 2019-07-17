package vn.lequan.wallpaperhtc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

import vn.lequan.wallpaperhtc.Model.ItemImage;
import vn.lequan.wallpaperhtc.control.OnItemClickListener;
import vn.lequan.wallpaperhtc.utils.Debug;
import vn.lequan.wallpaperhtc.utils.GlobalApp;
import vn.lequan.wallpaperhtc.utils.PrefManager;

import static android.app.Activity.RESULT_OK;


public class MyWallpaperFragment extends Fragment implements OnItemClickListener {


    private Context context;
    private RecyclerView recyclerView;
    private ItemImageAdapter adapter;
    private Uri imageUri;

    public static SharedPreferences sharedpreferences;
    private static final String MyPREFERENCES = "DANHGIA";
    int PRIVATE_MODE = 0;
    private ProgressBar marker_progress;
    private PrefManager pref;


    public MyWallpaperFragment() {
    }

    // TODO: Rename and change types and number of parameters
    public static MyWallpaperFragment newInstance() {
        MyWallpaperFragment fragment = new MyWallpaperFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        marker_progress = view.findViewById(R.id.marker_progress);
        sharedpreferences = context.getSharedPreferences(MyPREFERENCES, PRIVATE_MODE);
        recyclerView = view.findViewById(R.id.recyclerView);
        adapter = new ItemImageAdapter(context, new ArrayList<>(), 3, this, true);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 3));
        recyclerView.setAdapter(adapter);

        pref = new PrefManager(context);
        pref.setGalleryName("Wallpaper");
        return view;
    }


    public ArrayList<ItemImage> getFilePaths() {
        File myDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                pref.getGalleryName());
        String directory = myDir.getPath();
        Debug.e(directory);
        ArrayList<ItemImage> pathArray = new ArrayList<>();
        pathArray.add(new ItemImage("", ""));
        File file = new File(directory);
        File[] listfiles = file.listFiles();
        if (listfiles != null)
            Arrays.sort(listfiles, (f1, f2) -> Long.valueOf(f1.lastModified()).compareTo(f2.lastModified()));
        if (listfiles != null) {
            for (int i = 0; i < listfiles.length; i++) {
                if (listfiles[i].isFile()) {
                    pathArray.add(new ItemImage(listfiles[i].getAbsolutePath(), listfiles[i].getName().replace(".jpg", "")));
                }
            }
        }
        marker_progress.setVisibility(View.GONE);
        return pathArray;
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.clearData();
        adapter.addAll(getFilePaths());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }


    @Override
    public void onItemClicked(int position, Bitmap bitmap, String id) {
        if (position == 0) {
            intentSelectImage();
        } else {
            Intent intent = new Intent(context, ImageFullActivity.class);
            GlobalApp.imgAvatar = bitmap;
            intent.putExtra("id", id);
            intent.putExtra("isCrop", true);
            startActivity(intent);
        }
    }

    @Override
    public void onItemClicked(int position) {

    }

    private void intentSelectImage() {
        Intent pictureActionIntent = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        File path = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        final File file = new File(path, "image.jpg");
        imageUri = Uri.fromFile(file);

        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,
                imageUri);

        String pickTitle = "Chọn thư viện";
        Intent chooserIntent = Intent.createChooser(pictureActionIntent, pickTitle);
        chooserIntent.putExtra
                (
                        Intent.EXTRA_INITIAL_INTENTS,
                        new Intent[]{cameraIntent}
                );
        startActivityForResult(chooserIntent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitmap = null;
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                if (data == null) {
                    bitmap = getBitmapFromData();
                } else {
                    try {
                        InputStream inputStream = context.getContentResolver().openInputStream(data.getData());
                        bitmap = BitmapFactory.decodeStream(inputStream);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (bitmap == null)
                return;
            Intent in1 = new Intent(context, ImageFullActivity.class);
            GlobalApp.imgAvatar = bitmap;
            in1.putExtra("type", "avatar");
            startActivity(in1);
        }
    }

    public Bitmap getBitmapFromData() {
        Bitmap photo = null;
        try {
            photo = MediaStore.Images.Media.getBitmap(context.getContentResolver(), imageUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return photo;
    }
}
