package vn.lequan.wallpaperhtc.control;

import android.graphics.Bitmap;

public interface OnItemClickListener {
    void onItemClicked(int position, Bitmap bitmap, String id);
    void onItemClicked(int position);
}
