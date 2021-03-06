/*
 * Copyright 2014 Google Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package md.fusionworks.aquamea.util;

import android.os.Build;
import android.view.View;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;

/**
 * An assortment of UI helpers.
 */
public class UIUtils {

    public static void setAccessibilityIgnore(View view) {

        view.setClickable(false);
        view.setFocusable(false);
        view.setContentDescription("");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setImportantForAccessibility(View.IMPORTANT_FOR_ACCESSIBILITY_NO);
        }
    }

    public static float getMarkerColorByWaterRating(double rating) {

        if (rating >= 0 && rating < 3)
            return BitmapDescriptorFactory.HUE_RED;

        if (rating >= 3 && rating < 4)
            return BitmapDescriptorFactory.HUE_GREEN;

        return BitmapDescriptorFactory.HUE_BLUE;
    }


}
