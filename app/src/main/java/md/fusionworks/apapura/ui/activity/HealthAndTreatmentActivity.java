package md.fusionworks.apapura.ui.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import md.fusionworks.apapura.R;

public class HealthAndTreatmentActivity extends BaseNavigationDrawerActivity {

    @Bind(R.id.healthField)
    TextView healthField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_treatment);

        ButterKnife.bind(this);

       // healthField.setText(Html.fromHtml(getString(R.string.health_text)));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_health_treatment, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }

    @Override
    protected int getSelfDrawerItem() {

        return DRAWER_ITEM_HEALTH_TREATMENT;
    }
}
