package md.fusionworks.aquamea.model;

import java.io.Serializable;

import io.realm.RealmObject;

/**
 * Created by ungvas on 9/24/15.
 */
public class Well extends RealmObject implements Serializable {

    private String photoPath;
    private int appearanceRating;
    private int tasteRating;
    private int smellRating;
    private String note;
    private double latitude;
    private double longitude;

    public Well() {
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
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
