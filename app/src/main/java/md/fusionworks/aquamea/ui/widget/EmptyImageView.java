package md.fusionworks.aquamea.ui.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

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

    Bitmap bitmapReference;

    public EmptyImageView(Context context, AttributeSet attrs) {
        super(context, attrs);

        inflate(context, R.layout.photo_view_layout, this);
        ButterKnife.bind(this);
        setTag(null);
    }

    public void setImageBitmap(Bitmap bitmap) {

        emptyLayout.setVisibility(GONE);
        imageView.setImageBitmap(bitmap);
        this.bitmapReference = bitmap;
    }

    public void setServerImage(Context context, String imageUrl) {

        Picasso.with(context).load(imageUrl).into(imageView);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        clearImageView();
    }

    public boolean hasImage() {

        return bitmapReference != null;
    }

    public void removeImage() {

        emptyLayout.setVisibility(VISIBLE);
        clearImageView();
        setTag(null);
    }

    private void clearImageView() {

        imageView.setImageBitmap(null);
        if (bitmapReference != null) {

            bitmapReference.recycle();
            bitmapReference = null;
        }
    }
}
