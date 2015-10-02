package md.fusionworks.aquamea.util;

/**
 * Created by ungvas on 9/21/15.
 */
public class Utils {

    public static int calculateWaterRating(int appearance, int taste, int smell) {

        int total = appearance + taste + smell;
        return (total > 0) ? total / 3 : 0;
    }
}
