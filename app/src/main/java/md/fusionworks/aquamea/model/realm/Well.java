package md.fusionworks.aquamea.model.realm;

import io.realm.RealmObject;

/**
 * Created by ungvas on 9/24/15.
 */
public class Well extends RealmObject {

    private String serverPhoto;
    private String localPhoto;
    private int appearanceRating;
    private int tasteRating;
    private int smellRating;
    private String note;
    private double latitude;
    private double longitude;
    private boolean sync;

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

    public boolean isSync() {
        return sync;
    }

    public void setSync(boolean sync) {
        this.sync = sync;
    }
}
