package vn.lequan.wallpaperhtc.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class PrefManager {
    private static final String TAG = PrefManager.class.getSimpleName();

    SharedPreferences pref;

    Editor editor;

    Context _context;

    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "hinhnen";


    private static final String KEY_GALLERY_NAME = "gallery_name";

    public void setGalleryName(String galleryName) {
        editor = pref.edit();

        editor.putString(KEY_GALLERY_NAME, galleryName);

        // commit changes
        editor.commit();
    }

    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
    }


    public String getGalleryName() {
        return pref.getString(KEY_GALLERY_NAME, "hinhnen");
    }

}