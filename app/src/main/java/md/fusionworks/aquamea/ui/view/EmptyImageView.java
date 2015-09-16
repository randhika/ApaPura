package md.fusionworks.aquamea.ui.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import md.fusionworks.aquamea.R;

/**
 * Created by ungvas on 9/15/15.
 */
public class EmptyImageView extends FrameLayout {

    @Bind(R.id.imageView)
    ImageView imageView;
    @Bind(R.id.emptyLayout)
    View emptyLayout;

    public EmptyImageView(Context context, AttributeSet attrs) {
        super(context, attrs);

        inflate(context, R.layout.photo_view_layout, this);
        ButterKnife.bind(this);
    }

    public void setImage(Uri uri) {

        emptyLayout.setVisibility(GONE);

        imageView.setImageURI(uri);
    }

    public void setImage(Bitmap bitmap) {

        emptyLayout.setVisibility(GONE);

        imageView.setImageBitmap(bitmap);
    }
}
