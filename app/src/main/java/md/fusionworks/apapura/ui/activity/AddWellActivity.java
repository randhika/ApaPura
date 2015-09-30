package md.fusionworks.apapura.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.Realm;
import md.fusionworks.apapura.R;
import md.fusionworks.apapura.model.realm.Well;
import md.fusionworks.apapura.ui.view.EmptyImageView;
import md.fusionworks.apapura.util.BitmapUtils;
import md.fusionworks.apapura.util.Constants;
import md.fusionworks.apapura.util.Convertor;
import md.fusionworks.apapura.util.DialogUtils;

public class AddWellActivity extends BaseLocationActivity implements View.OnClickListener {

    private int[] photoOptions = {Constants.OPTION_TAKE_PHOTO, Constants.OPTION_PICK_PHOTO, Constants.OPTION_REMOVE_PHOTO};
    private int[] coordinatesOptions = {Constants.OPTION_MARK_A_POINT_ON_MAP, Constants.OPTION_DETERMINE_GPS_COORDINATES, Constants.OPTION_ENTER_GPS_COORDINATES_MANUALLY};

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.collapsedToolbarLayout)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @Bind(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;
    @Bind(R.id.photoFab)
    FloatingActionButton photoFab;
    @Bind(R.id.coordinatesCardView)
    View coordinatesCardView;
    @Bind(R.id.emptyImageView)
    EmptyImageView emptyImageView;
    @Bind(R.id.appearanceRatingBar)
    RatingBar appearanceRatingBar;
    @Bind(R.id.tasteRatingBar)
    RatingBar tasteRatingBar;
    @Bind(R.id.smellRatingBar)
    RatingBar smellRatingBar;
    @Bind(R.id.noteField)
    EditText noteField;
    @Bind(R.id.latitudeField)
    TextView latitudeField;
    @Bind(R.id.longitudeField)
    TextView longitudeField;

    private String currentPhotoPath;
    private boolean isActivityResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_well);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        collapsingToolbarLayout.setTitle(getString(R.string.module_title_add_well));
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                setResult(RESULT_CANCELED, intent);
                finish();
            }
        });

        isActivityResult = false;

        photoFab.setOnClickListener(this);
        coordinatesCardView.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_well, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();


        if (id == R.id.action_add_well) {

            saveWell();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void saveWell() {

        if (validateInput()) {

            final String photoPath = (String) emptyImageView.getTag();
            final int appearanceRating = (int) appearanceRatingBar.getRating();
            final int tasteRating = (int) tasteRatingBar.getRating();
            final int smellRating = (int) smellRatingBar.getRating();
            final String note = noteField.getText().toString();
            final double latitude = Double.valueOf(latitudeField.getText().toString());
            final double longitude = Double.valueOf(longitudeField.getText().toString());

            final Well wellRealm = new Well();
            wellRealm.setPhotoPath(photoPath);
            wellRealm.setAppearanceRating(appearanceRating);
            wellRealm.setSmellRating(smellRating);
            wellRealm.setTasteRating(tasteRating);
            wellRealm.setNote(note);
            wellRealm.setLatitude(latitude);
            wellRealm.setLongitude(longitude);

            Realm realm = Realm.getInstance(this);
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {

                    realm.copyToRealm(wellRealm);
                }
            });

            md.fusionworks.apapura.model.Well well = Convertor.wellRealmObjectToSimple(wellRealm);

            Intent intent = new Intent();
            intent.putExtra(Constants.EXTRA_PARAM_WELL, well);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    private boolean validateInput() {

        boolean isValid = true;

        if (TextUtils.isEmpty(latitudeField.getText().toString()) || TextUtils.isEmpty(longitudeField.getText().toString())) {

            isValid = false;
            DialogUtils.showAlertDialog(this, getString(R.string.dialog_title_validation_error), getString(R.string.validation_error_message_gps_coordinates_required));
        }

        return isValid;
    }

    private File getPhotoAlbumDir() {

        File storageDir = null;

        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {

            storageDir = new File(Environment.getExternalStorageDirectory() + Constants.CAMERA_DIR + Constants.PHOTO_ALBUM_NAME);
            if (storageDir != null) {
                if (!storageDir.mkdirs()) {
                    if (!storageDir.exists()) {
                        return null;
                    }
                }
            }

        } else {
            Log.v(getString(R.string.app_name), "External storage is not mounted READ/WRITE.");
        }

        return storageDir;
    }

    private void pickPhoto() {

        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, Constants.OPTION_PICK_PHOTO);
    }

    private void takePhoto() {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

            File f = null;

            try {
                f = setUpPhotoFile();
                currentPhotoPath = f.getAbsolutePath();
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
            } catch (IOException e) {
                e.printStackTrace();
                f = null;
                currentPhotoPath = null;
            }

            startActivityForResult(takePictureIntent, Constants.OPTION_TAKE_PHOTO);
        }
    }

    private File createImageFile() throws IOException {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = Constants.JPEG_FILE_PREFIX + timeStamp + "_";
        File albumF = getPhotoAlbumDir();
        File imageF = File.createTempFile(imageFileName, Constants.JPEG_FILE_SUFFIX, albumF);
        return imageF;
    }

    private File setUpPhotoFile() throws IOException {

        File f = createImageFile();
        currentPhotoPath = f.getAbsolutePath();

        return f;
    }

    private void galleryAddPic() {

        Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        File f = new File(currentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    private void markAPointOnMap() {

        Intent intent = new Intent(this, MarkAPointOnMapActivity.class);

        String latitude = latitudeField.getText().toString();
        String longitude = longitudeField.getText().toString();
        if (!TextUtils.isEmpty(latitude) && !TextUtils.isEmpty(longitude)) {

            intent.putExtra(Constants.EXTRA_PARAM_LATITUDE, latitude);
            intent.putExtra(Constants.EXTRA_PARAM_LONGITUDE, longitude);
        }
        startActivityForResult(intent, Constants.ACTIVITY_RESULT_MARK_A_POINT_ON_MAP);
    }


    private void determineGPSCoordinates() {

        buildGoogleApiClient();
        connectGoogleApiClient();
    }

    private void enterGPSCoordinatesManually() {

        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title(R.string.dialog_title_enter_gps_coordinates)
                .customView(R.layout.dialog_enter_gps_coordinates, true)
                .positiveText(R.string.action_save)
                .negativeText(android.R.string.cancel)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {

                        EditText dialogLatitudeField = (EditText) dialog.findViewById(R.id.latitudeField);
                        EditText dialogLongitudeField = (EditText) dialog.findViewById(R.id.longitudeField);

                        latitudeField.setText(dialogLatitudeField.getText().toString());
                        longitudeField.setText(dialogLongitudeField.getText().toString());

                        coordinatorLayout.postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                Snackbar.make(coordinatorLayout, getString(R.string.info_gps_coordonates_was_set), Snackbar.LENGTH_SHORT).show();
                            }
                        }, 500);
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                    }
                }).build();

        final View positiveAction = dialog.getActionButton(DialogAction.POSITIVE);
        final EditText dialogLatitudeField = (EditText) dialog.findViewById(R.id.latitudeField);
        final EditText dialogLongitudeField = (EditText) dialog.findViewById(R.id.longitudeField);

        dialogLatitudeField.setText(latitudeField.getText().toString());
        dialogLongitudeField.setText(longitudeField.getText().toString());

        dialogLatitudeField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                positiveAction.setEnabled(s.toString().trim().length() > 0 && dialogLongitudeField.getText().toString().length() > 0);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        dialogLongitudeField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                positiveAction.setEnabled(s.toString().trim().length() > 0 && dialogLatitudeField.getText().toString().length() > 0);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        positiveAction.setEnabled(latitudeField.getText().toString().length() > 0);

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

                                               int whichOption = photoOptions[which];

                                               switch (whichOption) {

                                                   case Constants.OPTION_TAKE_PHOTO:

                                                       takePhoto();
                                                       break;
                                                   case Constants.OPTION_PICK_PHOTO:

                                                       pickPhoto();
                                                       break;
                                                   case Constants.OPTION_REMOVE_PHOTO:

                                                       emptyImageView.removeImage();
                                                       break;
                                               }
                                           }
                                       }
                        ).show();

                break;

            case R.id.coordinatesCardView:

                new MaterialDialog.Builder(AddWellActivity.this)
                        .items(R.array.coordinates_options)
                        .itemsCallback(new MaterialDialog.ListCallback() {
                                           @Override
                                           public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {

                                               int whichOption = coordinatesOptions[which];

                                               switch (whichOption) {

                                                   case Constants.OPTION_MARK_A_POINT_ON_MAP:

                                                       markAPointOnMap();
                                                       break;
                                                   case Constants.OPTION_DETERMINE_GPS_COORDINATES:

                                                       determineGPSCoordinates();
                                                       break;
                                                   case Constants.OPTION_ENTER_GPS_COORDINATES_MANUALLY:

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

        isActivityResult = true;

        switch (requestCode) {

            case Constants.OPTION_PICK_PHOTO:

                if (resultCode == RESULT_OK) {

                    Uri imageUri = data.getData();
                    String selectedPhotoPath = getRealPathFromURI(this, imageUri);
                    emptyImageView.setTag(selectedPhotoPath);
                    try {

                        Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                        emptyImageView.setImageBitmap(BitmapUtils.scaleToActualAspectRatio(this, imageBitmap));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case Constants.OPTION_TAKE_PHOTO:

                if (requestCode == Constants.OPTION_TAKE_PHOTO && resultCode == RESULT_OK) {

                    if (currentPhotoPath != null) {

                        try {
                            Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.fromFile(new File(currentPhotoPath)));
                            emptyImageView.setImageBitmap(BitmapUtils.scaleToActualAspectRatio(this, imageBitmap));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        galleryAddPic();
                        emptyImageView.setTag(currentPhotoPath);
                        currentPhotoPath = null;
                    }

                }
                break;
            case Constants.ACTIVITY_RESULT_MARK_A_POINT_ON_MAP:

                if (requestCode == Constants.ACTIVITY_RESULT_MARK_A_POINT_ON_MAP && resultCode == RESULT_OK) {

                    String latitude = data.getStringExtra(Constants.EXTRA_PARAM_LATITUDE);
                    String longitude = data.getStringExtra(Constants.EXTRA_PARAM_LONGITUDE);

                    latitudeField.setText(latitude);
                    longitudeField.setText(longitude);

                    coordinatorLayout.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            Snackbar.make(coordinatorLayout, getString(R.string.info_gps_coordonates_was_set), Snackbar.LENGTH_SHORT).show();
                        }
                    }, 500);
                }
                break;
        }
    }

    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        outState.putString(Constants.KEY_PHOTO_PATH, (String) emptyImageView.getTag());
        outState.putInt(Constants.KEY_APPEREANCE, (int) appearanceRatingBar.getRating());
        outState.putInt(Constants.KEY_TASTE, (int) tasteRatingBar.getRating());
        outState.putInt(Constants.KEY_SMELL, (int) smellRatingBar.getRating());
        outState.putString(Constants.KEY_NOTE, noteField.getText().toString());
        outState.putString(Constants.KEY_LATITUDE, latitudeField.getText().toString());
        outState.putString(Constants.KEY_LONGITUDE, longitudeField.getText().toString());

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        String photoPath = savedInstanceState.getString(Constants.KEY_PHOTO_PATH);
        int appearanceRating = savedInstanceState.getInt(Constants.KEY_APPEREANCE);
        int smellRating = savedInstanceState.getInt(Constants.KEY_SMELL);
        int tasteRating = savedInstanceState.getInt(Constants.KEY_TASTE);
        String note = savedInstanceState.getString(Constants.KEY_NOTE);
        String latitude = savedInstanceState.getString(Constants.KEY_LATITUDE);
        String longitude = savedInstanceState.getString(Constants.KEY_LONGITUDE);

        appearanceRatingBar.setRating(appearanceRating);
        smellRatingBar.setRating(smellRating);
        tasteRatingBar.setRating(tasteRating);
        noteField.setText(note);
        latitudeField.setText(latitude);
        longitudeField.setText(longitude);

        if (photoPath != null) {

            try {
                Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.fromFile(new File(photoPath)));
                emptyImageView.setImageBitmap(BitmapUtils.scaleToActualAspectRatio(this, imageBitmap));
            } catch (IOException e) {
                e.printStackTrace();
            }
            emptyImageView.setTag(photoPath);
        }
    }

    @Override
    public void onGetLastLocation(Location location) {

        if (!isActivityResult) {

            if (location != null) {

                latitudeField.setText(String.valueOf(location.getLatitude()));
                longitudeField.setText(String.valueOf(location.getLongitude()));

                coordinatorLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        Snackbar.make(coordinatorLayout, getString(R.string.info_gps_coordonates_was_set), Snackbar.LENGTH_SHORT).show();
                    }
                }, 500);
            } else {

                DialogUtils.showAlertDialog(this, getString(R.string.dialog_title_gps_location_error), getString(R.string.info_cannot_get_gps_coordiates));
            }
        } else {

            isActivityResult = false;
        }
        disconnectGoogleApiClient();
    }
}
