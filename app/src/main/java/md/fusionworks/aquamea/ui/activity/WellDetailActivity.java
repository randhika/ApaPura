package md.fusionworks.aquamea.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import md.fusionworks.aquamea.R;
import md.fusionworks.aquamea.ui.widget.EmptyImageView;
import md.fusionworks.aquamea.util.BitmapUtils;
import md.fusionworks.aquamea.util.Constants;

public class WellDetailActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.collapsedToolbarLayout)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @Bind(R.id.emptyImageView)
    EmptyImageView emptyImageView;
    @Bind(R.id.coordinatesCardView)
    View coordinatesCardView;
    @Bind(R.id.appearanceRatingBar)
    RatingBar appearanceRatingBar;
    @Bind(R.id.tasteRatingBar)
    RatingBar tasteRatingBar;
    @Bind(R.id.smellRatingBar)
    RatingBar smellRatingBar;
    @Bind(R.id.noteField)
    TextView noteField;
    @Bind(R.id.latitudeField)
    TextView latitudeField;
    @Bind(R.id.longitudeField)
    TextView longitudeField;

    private md.fusionworks.aquamea.model.Well well;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_well_detail);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        collapsingToolbarLayout.setTitle(getString(R.string.module_title_well_detail));
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
        toolbar.setNavigationOnClickListener(v -> {

            Intent intent = new Intent();
            setResult(RESULT_CANCELED, intent);
            finish();
        });
        Intent intent = getIntent();
        well = (md.fusionworks.aquamea.model.Well) intent.getSerializableExtra(Constants.EXTRA_PARAM_WELL);

        fillUI();

    }

    private void fillUI() {

        if (well != null) {

            String localPhoto = well.getLocalPhoto();
            String serverPhoto = well.getServerPhoto();

            if (!TextUtils.isEmpty(localPhoto)) {

                try {
                    Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.fromFile(new File(localPhoto)));
                    emptyImageView.setImageBitmap(BitmapUtils.scaleToActualAspectRatio(this, imageBitmap));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (!TextUtils.isEmpty(serverPhoto)) {

                String photoUrl = Constants.SERVER_URL + "/photo/" + serverPhoto;
                emptyImageView.setServerImage(WellDetailActivity.this, photoUrl);
            }

            appearanceRatingBar.setRating(well.getAppearanceRating());
            tasteRatingBar.setRating(well.getTasteRating());
            smellRatingBar.setRating(well.getSmellRating());
            noteField.setText(well.getNote());
            latitudeField.setText(String.valueOf(well.getLatitude()));
            longitudeField.setText(String.valueOf(well.getLongitude()));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_well_detail, menu);
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
}
