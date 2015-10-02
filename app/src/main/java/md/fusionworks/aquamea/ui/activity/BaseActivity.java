package md.fusionworks.aquamea.ui.activity;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by admin on 14.08.2015.
 */
public class BaseActivity extends AppCompatActivity {

    public void showFragment(int containerViewId, Fragment fragment) {
        showFragment(containerViewId, fragment, false);
    }

    public void showFragment(int containerViewId, Fragment fragment, boolean omitFromBackStack) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(containerViewId, fragment);
        if (omitFromBackStack) {
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commit();
    }
}
