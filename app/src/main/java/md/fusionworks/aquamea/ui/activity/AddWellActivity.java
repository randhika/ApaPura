package md.fusionworks.aquamea.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import md.fusionworks.aquamea.R;
import md.fusionworks.aquamea.ui.view.EmptyImageView;
import md.fusionworks.aquamea.util.BitmapUtils;
import md.fusionworks.aquamea.util.GPSTracker;

public class AddWellActivity extends BaseActivity implements View.OnClickListener {

    private static final int OPTION_TAKE_PHOTO = 0;
    private static final int OPTION_PICK_PHOTO = 1;
    private static final int OPTION_REMOVE_PHOTO = 2;

    private static final int OPTION_DETERMINE_GPS_COORDINATES = 0;
    private static final int OPTION_ENTER_GPS_COORDINATES_MANUALLY = 1;

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.photoFab)
    FloatingActionButton photoFab;
    @Bind(R.id.coordinatesLayout)
    LinearLayout coordinatesLayout;
    @Bind(R.id.emptyImageView)
    EmptyImageView emptyImageView;
    @Bind(R.id.appearanceRatingBar)
    RatingBar appearanceRatingBar;
    @Bind(R.id.tasteRatingBar)
    RatingBar tasteRatingBar;
    @Bind(R.id.smellRatingBar)
    RatingBar smellRatingBar;

    private GPSTracker gpsTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_well);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        photoFab.setOnClickListener(this);
        coordinatesLayout.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_well, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void pickPhoto() {

        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, OPTION_PICK_PHOTO);
    }

    private void takePhoto() {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, OPTION_TAKE_PHOTO);
        }
    }

    private void determineGPSCoordinates() {

        gpsTracker = new GPSTracker(this);

        if (gpsTracker.canGetLocation()) {

            double latitude = gpsTracker.getLatitude();
            double longitude = gpsTracker.getLongitude();

            Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
        } else {

            gpsTracker.showSettingsAlert();
        }
    }

    private void enterGPSCoordinatesManually() {

        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title("Enter GPS coordinates")
                .customView(R.layout.dialog_enter_gps_coordinates, true)
                .positiveText("Save")
                .negativeText(android.R.string.cancel)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {

                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                    }
                }).build();

        final View positiveAction = dialog.getActionButton(DialogAction.POSITIVE);
        final EditText latitudeField = (EditText) dialog.findViewById(R.id.latitudeField);
        final EditText longitudeField = (EditText) dialog.findViewById(R.id.longitudeField);

        latitudeField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                positiveAction.setEnabled(s.toString().trim().length() > 0 && longitudeField.getText().toString().length() > 0);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        longitudeField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                positiveAction.setEnabled(s.toString().trim().length() > 0 && latitudeField.getText().toString().length() > 0);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        positiveAction.setEnabled(false);

        dialog.show();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.photoFab:

                int arrayResource = (emptyImageView.hasImage()) ? R.array.photo_options_plus_remove : R.array.photo_options;

                new MaterialDialog.Builder(AddWellActivity.this)
                        .items(arrayResource)
                        .itemsCallback(new MaterialDialog.ListCallback() {
                                           @Override
                                           public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {

                                               switch (which) {

                                                   case OPTION_TAKE_PHOTO:

                                                       takePhoto();
                                                       break;
                                                   case OPTION_PICK_PHOTO:

                                                       pickPhoto();
                                                       break;
                                                   case OPTION_REMOVE_PHOTO:

                                                       emptyImageView.removeImage();
                                                       break;
                                               }
                                           }
                                       }
                        ).show();

                break;

            case R.id.coordinatesLayout:

                new MaterialDialog.Builder(AddWellActivity.this)
                        .items(R.array.coordinates_options)
                        .itemsCallback(new MaterialDialog.ListCallback() {
                                           @Override
                                           public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {

                                               switch (which) {

                                                   case OPTION_DETERMINE_GPS_COORDINATES:

                                                       determineGPSCoordinates();
                                                       break;
                                                   case OPTION_ENTER_GPS_COORDINATES_MANUALLY:

                                                       enterGPSCoordinatesManually();
                                                       break;
                                               }
                                           }
                                       }
                        ).show();

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case OPTION_PICK_PHOTO:

                if (resultCode == RESULT_OK) {

                    Uri imageUri = data.getData();
                    try {

                        Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                        emptyImageView.setImageBitmap(BitmapUtils.scaleToActualAspectRatio(this, imageBitmap));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case OPTION_TAKE_PHOTO:

                if (requestCode == OPTION_TAKE_PHOTO && resultCode == RESULT_OK) {

                    Bundle extras = data.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    emptyImageView.setImageBitmap(imageBitmap);
                }
                break;
        }
    }

}
