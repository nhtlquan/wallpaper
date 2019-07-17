package vn.lequan.wallpaperhtc;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Build;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorListener;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.util.ArrayList;
import java.util.List;

import vn.lequan.wallpaperhtc.Model.ItemImage;
import vn.lequan.wallpaperhtc.control.OnItemClickListener;
import vn.lequan.wallpaperhtc.utils.Debug;
import vn.lequan.wallpaperhtc.utils.GlobalApp;


public class ItemImageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<ItemImage> lstData;
    private Point point;
    private int numberColmns = 0;
    private int size;
    private OnItemClickListener onItemClickListener;
    private boolean isAddItem;
    public static SharedPreferences sharedpreferences;
    private static final String MyPREFERENCES = "Wallpaper";
    int PRIVATE_MODE = 0;

    public ItemImageAdapter(Context context, ArrayList<ItemImage> lstData, int numberColmns, OnItemClickListener onItemClickListener, boolean isAddItem) {
        this.context = context;
        this.lstData = lstData;
        this.numberColmns = numberColmns;
        this.onItemClickListener = onItemClickListener;
        this.isAddItem = isAddItem;
        point = getScreenSize(context);
        sharedpreferences = context.getSharedPreferences(MyPREFERENCES, PRIVATE_MODE);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_item_image, parent, false);
        if (numberColmns != 0) {
            view.setLayoutParams(new ViewGroup.LayoutParams(point.x / 3, point.y / 3));
        }
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyViewHolder myViewHolder = (MyViewHolder) holder;
        myViewHolder.init(lstData.get(position), position);
    }

    public void addAll(List<ItemImage> data) {
        this.lstData.addAll(data);
        size = getItemCount();
        notifyDataSetChanged();
    }

    public void clearData() {
        this.lstData.clear();
        notifyDataSetChanged();
    }

    public void addItem(ItemImage data) {
        this.lstData.add(data);
        size = getItemCount();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return lstData.size();
    }


    private class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView, ic_plus, ic_check;
        private Bitmap bitmap;

        public MyViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.imageView);
            ic_plus = view.findViewById(R.id.ic_plus);
            ic_check = view.findViewById(R.id.ic_check);
        }

        public void init(final ItemImage item, int position) {

            if (position == 0 && isAddItem) {
                ic_plus.setVisibility(View.VISIBLE);
                Glide.with(context)
                        .load(item.getName())
                        .asBitmap()
                        .placeholder(R.color.item_add)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(imageView);
            } else {
                Glide.with(context)
                        .load(item.getName())
                        .asBitmap()
                        .placeholder(GlobalApp.getColor(position))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                bitmap = resource;
                                imageView.setImageBitmap(resource);
                            }
                        });
            }
            if (isAddItem && sharedpreferences.getString("isSetWallpaper", "").equals(item.getId())) {
                ic_check.setVisibility(View.VISIBLE);
            } else {
                ic_check.setVisibility(View.GONE);
            }

            itemView.setOnClickListener(view -> ViewCompat.animate(view)
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
                            if (position == 0 && isAddItem)
                                onItemClickListener.onItemClicked(position, null, item.getId());
                            else
                                onItemClickListener.onItemClicked(position, bitmap, item.getId());

                        }

                        @Override
                        public void onAnimationCancel(final View view) {

                        }
                    })
                    .withLayer()
                    .start());
        }

        public class CycleInterpolator implements android.view.animation.Interpolator {

            private final float mCycles = 0.5f;

            @Override
            public float getInterpolation(final float input) {
                return (float) Math.sin(2.0f * mCycles * Math.PI * input);
            }
        }


    }
    public static Point getScreenSize(Context context) {
        Point localPoint = new Point();
        Display display;
        try {
            display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
            if (Build.VERSION.SDK_INT >= 13) {
                display.getSize(localPoint);
            } else {
                localPoint.set(display.getWidth(), display.getHeight());
            }
        } catch (Exception e) {
            Debug.e(e.toString());
        }
        return localPoint;
    }
}

