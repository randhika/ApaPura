package md.fusionworks.aquamea.util;

/**
 * Created by ungvas on 9/21/15.
 */
public class Utils {

    public static double calculateWaterRating(int appearance, int taste, int smell) {

        return (appearance + taste + smell) / 3;
    }
}
