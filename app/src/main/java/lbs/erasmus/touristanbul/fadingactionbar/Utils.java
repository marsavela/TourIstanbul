package lbs.erasmus.touristanbul.fadingactionbar;

/**
 * Created by sergiu on 24/03/14.
 */

import android.content.Context;
import android.view.WindowManager;

public class Utils {
    public static int getDisplayHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        @SuppressWarnings("deprecation")
        int displayHeight = wm.getDefaultDisplay().getHeight();
        return displayHeight;
    }
}
