package vn.lequan.wallpaperhtc.control;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import vn.lequan.wallpaperhtc.Model.HinhAnh;
import vn.lequan.wallpaperhtc.Model.PhotoFlickr;

public interface getImageAPI {
    @GET("getImage")
    Call<HinhAnh> getImage();

    @GET("services/rest/")
    Call<PhotoFlickr> getAllImage(@Query("method") String method, @Query("photoset_id") String photoset_id, @Query("api_key") String api_key, @Query("format") String format, @Query("user_id") String user_id, @Query("page") String page, @Query("per_page") String per_page, @Query("nojsoncallback") String nojsoncallback);

}
