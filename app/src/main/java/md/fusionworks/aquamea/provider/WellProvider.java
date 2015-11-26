package md.fusionworks.aquamea.provider;

import android.content.Context;

import io.realm.Realm;
import io.realm.RealmResults;
import md.fusionworks.aquamea.model.realm.Well;

/**
 * Created by ungvas on 9/24/15.
 */
public class WellProvider {

    Realm realm;

    public static WellProvider newInstance(Context context) {

        return new WellProvider(context);
    }

    public WellProvider(Context context) {

        this.realm = Realm.getInstance(context);
    }

    public RealmResults<Well> getAll() {

        return realm.where(Well.class).equalTo("sync", false).findAll();
    }
}
