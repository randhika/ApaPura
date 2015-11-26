package md.fusionworks.aquamea.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by ungvas on 9/24/15.
 */
public class Well implements Serializable {

    @SerializedName("photo")
    private String serverPhoto;
    private String localPhoto;
    @SerializedName("appearance")
    private int appearanceRating;
    @SerializedName("taste")
    private int tasteRating;
    @SerializedName("smell")
    private int smellRating;
    private String note;
    private double latitude;
    private double longitude;


    public Well() {
    }

    public String getServerPhoto() {
        return serverPhoto;
    }

    public void setServerPhoto(String serverPhoto) {
        this.serverPhoto = serverPhoto;
    }

    public String getLocalPhoto() {
        return localPhoto;
    }

    public void setLocalPhoto(String localPhoto) {
        this.localPhoto = localPhoto;
    }

    public int getAppearanceRating() {
        return appearanceRating;
    }

    public void setAppearanceRating(int appearanceRating) {
        this.appearanceRating = appearanceRating;
    }

    public int getTasteRating() {
        return tasteRating;
    }

    public void setTasteRating(int tasteRating) {
        this.tasteRating = tasteRating;
    }

    public int getSmellRating() {
        return smellRating;
    }

    public void setSmellRating(int smellRating) {
        this.smellRating = smellRating;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
