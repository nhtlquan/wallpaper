package vn.lequan.wallpaperhtc.utils;

import android.graphics.Bitmap;

import java.util.List;

import retrofit2.Retrofit;
import vn.lequan.wallpaperhtc.Model.Photo;
import vn.lequan.wallpaperhtc.R;

/**
 * Created by Le Quan on 27/06/2017.
 */

public class GlobalApp {
    public static Bitmap imgAvatar;
    public Retrofit retrofit;
    public String URL = "https://api.flickr.com/";
    public String METHOD = "flickr.photosets.getPhotos";
    public String PHOTOSET_ID = "72157685465042826";
    public String API_KEY = "b1442a61dd1da76dc8b04ba10f7ed2ae";
    public String FOMAT = "json";
    public String USER_ID = "116078178@N08";
    public String NOJSONCALLBACK = "1";
    public String PAGE = "1";
    public String PER_PAGE = "500";
    public List<Photo> photos;
    private static GlobalApp ourInstance = new GlobalApp();

    public static GlobalApp getInstance() {
        return ourInstance;
    }

    public static int getColor(int pos) {
        switch (pos % 4) {
            case 1:
                return R.color.place_color_1;
            case 2:
                return R.color.place_color_2;
            case 3:
                return R.color.place_color_3;
            case 0:
                return R.color.place_color_4;
            default:
                return R.color.place_color_1;
        }
    }


}

