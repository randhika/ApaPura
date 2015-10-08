package md.fusionworks.aquamea.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import md.fusionworks.aquamea.R;
import md.fusionworks.aquamea.util.Constants;

public class AboutAppActivity extends BaseNavigationDrawerActivity implements View.OnClickListener {

    @Bind(R.id.partnerLogoField)
    View partnerLogoField;
    @Bind(R.id.emailField)
    TextView emailField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_app);

        ButterKnife.bind(this);

        emailField.setMovementMethod(LinkMovementMethod.getInstance());
        emailField.setOnClickListener(this);
        partnerLogoField.setOnClickListener(this);
    }

    @Override
    protected int getSelfDrawerItem() {

        return DRAWER_ITEM_ABOUT_APP;
    }

    @Override
    public void onClick(View v) {

        Intent intent;

        switch (v.getId()) {

            case R.id.partnerLogoField:

                intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.partener_web_address)));
                startActivity(intent);
                break;
            case R.id.emailField:

                intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(Constants.MAIL_TO, getString(R.string.field_contact_email), null));
                startActivity(Intent.createChooser(intent, null));
                break;
        }
    }
}
