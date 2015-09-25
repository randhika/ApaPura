package md.fusionworks.apapura.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;

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
}
