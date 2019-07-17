package vn.lequan.wallpaperhtc;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import vn.lequan.wallpaperhtc.control.OnItemClickListener;

public class CustomDialogSetWallpaper extends BottomSheetDialog {
    private Button btn_set_lock_screen, btn_set_home, btn_exit;


    public CustomDialogSetWallpaper(Context context, final OnItemClickListener onClickListener) {
        super(context);
        View view = LayoutInflater.from(context).inflate(R.layout.custom_dialog_wallpaper, null);
        setContentView(view);
        setCancelable(true);
        getWindow()
                .findViewById(R.id.design_bottom_sheet)
                .setBackgroundResource(android.R.color.transparent);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        btn_exit = view.findViewById(R.id.btn_exit);
        btn_set_home = view.findViewById(R.id.btn_set_home);
        btn_set_lock_screen = view.findViewById(R.id.btn_set_lock_screen);
        btn_exit.setOnClickListener(v -> {
            dismiss();
        });
        btn_set_home.setOnClickListener(v -> {
            dismiss();
            onClickListener.onItemClicked(1);
        });
        btn_set_lock_screen.setOnClickListener(v -> {
            dismiss();
            onClickListener.onItemClicked(2);
        });
    }
}
