package md.fusionworks.aquamea.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.afollestad.materialdialogs.MaterialDialog;

import java.io.FileNotFoundException;

import butterknife.Bind;
import butterknife.ButterKnife;
import md.fusionworks.aquamea.R;
import md.fusionworks.aquamea.ui.view.EmptyImageView;

public class AddWellActivity extends BaseActivity implements View.OnClickListener {

    private static final int OPTION_TAKE_PHOTO = 0;
    private static final int OPTION_PICK_PHOTO = 1;

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.photoFab)
    FloatingActionButton photoFab;
    @Bind(R.id.location)
    LinearLayout location;
    @Bind(R.id.emptyImageView)
    EmptyImageView emptyImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_well);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new MaterialDialog.Builder(AddWellActivity.this)
                        .items(R.array.coordinates_options)
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {

                            }
                        })
                        .show();
            }
        });

        photoFab.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.photoFab:

                new MaterialDialog.Builder(AddWellActivity.this)
                        .items(R.array.photo_options)
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
                    emptyImageView.setImage(imageUri);
                }
                break;
            case OPTION_TAKE_PHOTO:
                if (requestCode == OPTION_TAKE_PHOTO && resultCode == RESULT_OK) {

                    Bundle extras = data.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    emptyImageView.setImage(imageBitmap);
                }
                break;
        }
    }

}
