
/*
 * Copyright 2010, 2011, 2012, 2013 mapsforge.org
 *
 * This program is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package lbs.erasmus.touristanbul.maps;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Utility functions that can be used across different mapsforge based activities
 */
public final class Utils {

    public static boolean checkWifiConection(Context context) {
        ConnectivityManager conectManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conectManager != null) {
            NetworkInfo wifiConection = conectManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (wifiConection != null
                    && wifiConection.isAvailable()
                    && wifiConection.getDetailedState() == NetworkInfo.DetailedState.CONNECTED) {
                return true;
            }
        }
        return false;
    }

}
