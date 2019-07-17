
package vn.lequan.wallpaperhtc.Model;

import java.util.ArrayList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import vn.lequan.wallpaperhtc.Model.GetImage;

public class HinhAnh {

    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("mesage")
    @Expose
    private String mesage;
    @SerializedName("getImage")
    @Expose
    private ArrayList<GetImage> getImage = null;

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public String getMesage() {
        return mesage;
    }

    public void setMesage(String mesage) {
        this.mesage = mesage;
    }

    public ArrayList<GetImage> getGetImage() {
        return getImage;
    }

    public void setGetImage(ArrayList<GetImage> getImage) {
        this.getImage = getImage;
    }

}
