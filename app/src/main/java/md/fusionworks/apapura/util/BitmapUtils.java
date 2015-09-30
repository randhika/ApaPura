package md.fusionworks.apapura.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by ungvas on 9/16/15.
 */
public class BitmapUtils {

    public static Bitmap scaleToActualAspectRatio(Context context, Bitmap bitmap) {
        if (bitmap != null) {
            boolean flag = true;

            int deviceWidth = ((Activity) context).getWindowManager().getDefaultDisplay()
                    .getWidth();
            int deviceHeight = ((Activity) context).getWindowManager().getDefaultDisplay()
                    .getHeight();

            int bitmapHeight = bitmap.getHeight(); // 563
            int bitmapWidth = bitmap.getWidth(); // 900

// aSCPECT rATIO IS Always WIDTH x HEIGHT rEMEMMBER 1024 x 768

            if (bitmapWidth > deviceWidth) {
                flag = false;

// scale According to WIDTH
                int scaledWidth = deviceWidth;
                int scaledHeight = (scaledWidth * bitmapHeight) / bitmapWidth;

                try {
                    if (scaledHeight > deviceHeight)
                        scaledHeight = deviceHeight;

                    bitmap = Bitmap.createScaledBitmap(bitmap, scaledWidth,
                            scaledHeight, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (flag) {
                if (bitmapHeight > deviceHeight) {
                    // scale According to HEIGHT
                    int scaledHeight = deviceHeight;
                    int scaledWidth = (scaledHeight * bitmapWidth)
                            / bitmapHeight;

                    try {
                        if (scaledWidth > deviceWidth)
                            scaledWidth = deviceWidth;

                        bitmap = Bitmap.createScaledBitmap(bitmap, scaledWidth,
                                scaledHeight, true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return bitmap;
    }

    public static Bitmap scaleToActualAspectRatio(View parentView, String path) {

        // Get the dimensions of the View
        int targetW = parentView.getWidth();
        int targetH = parentView.getHeight();

        if (targetH == 0 || targetW == 0) {

            targetW = parentView.getMinimumWidth();
            targetH = parentView.getMinimumHeight();
        }

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(path, bmOptions);

        return bitmap;
    }
}
