package md.fusionworks.aquamea.util;

import md.fusionworks.aquamea.model.Well;

/**
 * Created by ungvas on 9/25/15.
 */
public class Convertor {

    public static Well wellRealmObjectToSimple(md.fusionworks.aquamea.model.realm.Well wellRealm) {

        Well well = new Well();
        well.setPhotoPath(wellRealm.getPhotoPath());
        well.setAppearanceRating(wellRealm.getAppearanceRating());
        well.setTasteRating(wellRealm.getTasteRating());
        well.setSmellRating(wellRealm.getSmellRating());
        well.setNote(wellRealm.getNote());
        well.setLatitude(wellRealm.getLatitude());
        well.setLongitude(wellRealm.getLongitude());

        return well;
    }
}
