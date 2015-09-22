package md.fusionworks.aquamea.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import md.fusionworks.aquamea.R;
import md.fusionworks.aquamea.ui.view.EmptyImageView;
import md.fusionworks.aquamea.util.BitmapUtils;
import md.fusionworks.aquamea.util.CommonConstants;
import md.fusionworks.aquamea.util.GPSTracker;

public class AddWellActivity extends BaseActivity implements View.OnClickListener {

    private static final int OPTION_TAKE_PHOTO = 0;
    private static final int OPTION_PICK_PHOTO = 1;
    private static final int OPTION_REMOVE_PHOTO = 2;
    private static final int OPTION_DETERMINE_GPS_COORDINATES = 0;
    private static final int OPTION_ENTER_GPS_COORDINATES_MANUALLY = 1;

    private static final String JPEG_FILE_PREFIX = "IMG_";
    private static final String JPEG_FILE_SUFFIX = ".jpg";
    private static final String CAMERA_DIR = "/DCIM/";
    private static final String PHOTO_ALBUM_NAME = "AquaMeaPhotos";

    private static final String KEY_PHOTO_PATH = "KEY_PHOTO_PATH";
    private static final String KEY_APPEREANCE = "KEY_APPEREANCE";
    private static final String KEY_SMELL = "KEY_SMELL";
    private static final String KEY_TASTE = "KEY_TASTE";
    private static final String KEY_NOTE = "KEY_NOTE";

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.collapsedToolbarLayout)
    CollapsingToolbarLayout collapsingToolbarLayout;
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
    @Bind(R.id.noteField)
    EditText noteField;

    private GPSTracker gpsTracker;
    private Double latitude;
    private Double longitude;
    private String currentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_well);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            if (extras.containsKey(CommonConstants.EXTRA_PARAM_LATITUDE)) {

                latitude = extras.getDouble(CommonConstants.EXTRA_PARAM_LATITUDE);
                longitude = extras.getDouble(CommonConstants.EXTRA_PARAM_LONGITUDE);
            }
        }

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        collapsingToolbarLayout.setTitle("Add well");
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));

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

        if (id == R.id.action_add_well) {

            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private File getPhotoAlbumDir() {

        File storageDir = null;

        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {

            storageDir = new File(Environment.getExternalStorageDirectory() + CAMERA_DIR + PHOTO_ALBUM_NAME);
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
        startActivityForResult(photoPickerIntent, OPTION_PICK_PHOTO);
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

            startActivityForResult(takePictureIntent, OPTION_TAKE_PHOTO);
        }
    }

    private File createImageFile() throws IOException {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = JPEG_FILE_PREFIX + timeStamp + "_";
        File albumF = getPhotoAlbumDir();
        File imageF = File.createTempFile(imageFileName, JPEG_FILE_SUFFIX, albumF);
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
            case OPTION_TAKE_PHOTO:

                if (requestCode == OPTION_TAKE_PHOTO && resultCode == RESULT_OK) {

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

        outState.putString(KEY_PHOTO_PATH, (String) emptyImageView.getTag());
        outState.putInt(KEY_APPEREANCE, (int) appearanceRatingBar.getRating());
        outState.putInt(KEY_TASTE, (int) tasteRatingBar.getRating());
        outState.putInt(KEY_SMELL, (int) smellRatingBar.getRating());
        outState.putString(KEY_NOTE, noteField.getText().toString());

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        String photoPath = savedInstanceState.getString(KEY_PHOTO_PATH);
        int appearanceRating = savedInstanceState.getInt(KEY_APPEREANCE);
        int smellRating = savedInstanceState.getInt(KEY_SMELL);
        int tasteRating = savedInstanceState.getInt(KEY_TASTE);
        String note = savedInstanceState.getString(KEY_NOTE);

        appearanceRatingBar.setRating(appearanceRating);
        smellRatingBar.setRating(smellRating);
        tasteRatingBar.setRating(tasteRating);
        noteField.setText(note);

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
}
