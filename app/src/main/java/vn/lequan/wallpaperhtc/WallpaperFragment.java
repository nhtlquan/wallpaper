package vn.lequan.wallpaperhtc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import java.io.File;
import java.util.ArrayList;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import vn.lequan.wallpaperhtc.Model.ItemImage;
import vn.lequan.wallpaperhtc.Model.Photo;
import vn.lequan.wallpaperhtc.Model.PhotoFlickr;
import vn.lequan.wallpaperhtc.control.OnItemClickListener;
import vn.lequan.wallpaperhtc.control.getImageAPI;
import vn.lequan.wallpaperhtc.utils.Debug;
import vn.lequan.wallpaperhtc.utils.GlobalApp;


public class WallpaperFragment extends Fragment implements OnItemClickListener {


    private Context context;
    private RecyclerView recyclerView;
    private ItemImageAdapter adapter;

    public static SharedPreferences sharedpreferences;
    private static final String MyPREFERENCES = "DANHGIA";
    int PRIVATE_MODE = 0;
    private ProgressBar marker_progress;


    public WallpaperFragment() {
    }

    // TODO: Rename and change types and number of parameters
    public static WallpaperFragment newInstance() {
        WallpaperFragment fragment = new WallpaperFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        marker_progress = view.findViewById(R.id.marker_progress);
        sharedpreferences = context.getSharedPreferences(MyPREFERENCES, PRIVATE_MODE);
        recyclerView = view.findViewById(R.id.recyclerView);
        adapter = new ItemImageAdapter(context, new ArrayList<>(), 3, this, false);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 3));
        recyclerView.setAdapter(adapter);
        File httpCacheDirectory = new File(context.getCacheDir(), "http-cache");
        int cacheSize = 10 * 1024 * 1024; // 10 MiB
        Cache cache = new Cache(httpCacheDirectory, cacheSize);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addNetworkInterceptor(REWRITE_CACHE_CONTROL_INTERCEPTOR)
                .cache(cache)
                .build();
        GlobalApp.getInstance().retrofit = new Retrofit.Builder().baseUrl(GlobalApp.getInstance().URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
        getImage();
        return view;
    }

    private final Interceptor REWRITE_CACHE_CONTROL_INTERCEPTOR = chain -> {
        okhttp3.Response originalResponse = chain.proceed(chain.request());
        if (isOnline()) {
            int maxAge = 600000; // read from cache for 1 minute
            return originalResponse.newBuilder()
                    .header("Cache-Control", "public, max-age=" + maxAge)
                    .build();
        } else {
            int maxStale = 60 * 60 * 24 * 28; // tolerate 4-weeks stale
            return originalResponse.newBuilder()
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                    .build();
        }
    };

    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
    private void getImage() {
        Call<PhotoFlickr> call = GlobalApp.getInstance().retrofit.create(getImageAPI.class).getAllImage(GlobalApp.getInstance().METHOD, GlobalApp.getInstance().PHOTOSET_ID, GlobalApp.getInstance().API_KEY, GlobalApp.getInstance().FOMAT, GlobalApp.getInstance().USER_ID, GlobalApp.getInstance().PAGE, GlobalApp.getInstance().PER_PAGE, GlobalApp.getInstance().NOJSONCALLBACK);
        call.enqueue(new Callback<PhotoFlickr>() {
            @Override
            public void onResponse(Call<PhotoFlickr> call, Response<PhotoFlickr> response) {
                try {
                    PhotoFlickr value = response.body();
                    for (Photo item : value.getPhotoset().getPhoto()) {
                        final String url = "https://farm" + item.getFarm() + ".staticflickr.com/" + item.getServer() + "/" + item.getId() + "_" + item.getSecret() + "_b.jpg";
                       Debug.e(url);
                        adapter.addItem(new ItemImage(url, item.getId()));
                    }
                    marker_progress.setVisibility(View.GONE);
                } catch (Exception e) {
                    Debug.e("Lỗi: " + e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<PhotoFlickr> call, Throwable t) {
                Debug.e("Lỗi: " + t.getMessage());
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onItemClicked(int position, Bitmap bitmap, String id) {
        Intent intent = new Intent(context, ImageFullActivity.class);
        GlobalApp.imgAvatar = bitmap;
        intent.putExtra("isCrop", false);
        intent.putExtra("id", id);
        startActivity(intent);
    }

    @Override
    public void onItemClicked(int position) {

    }

}
