
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

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Chronometer;
import android.widget.TextView;

import com.graphhopper.GHResponse;
import com.graphhopper.util.PointList;

import org.mapsforge.android.maps.overlay.Marker;
import org.mapsforge.android.maps.overlay.PolygonalChain;
import org.mapsforge.android.maps.overlay.Polyline;
import org.mapsforge.core.model.GeoPoint;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import lbs.erasmus.touristanbul.R;

/**
 * Utility functions that can be used across different mapsforge based activities
 */
public final class Utils {

    public static void showSimpleDialog(Context context, int title_id, int message_id){
        AlertDialog.Builder alertDialog;
        alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle(context.getString(title_id));
        alertDialog.setMessage(context.getString(message_id));
        alertDialog.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.show();
    }

    public static Marker createTextMarker(Context context,String title, GeoPoint point, int resource)
    {
        Drawable drawable = context.getResources().getDrawable(resource);

        return new TextMarker(title, point, Marker.boundCenterBottom(drawable), context.getResources().getDisplayMetrics().density);
    }

    public static Marker createMarker(Context context, GeoPoint point, int resource)
    {
        Drawable drawable = context.getResources().getDrawable(resource);

        return new Marker(point, Marker.boundCenterBottom(drawable));
    }

    public static Polyline createPolyline( GHResponse response )
    {
        int points = response.getPoints().getSize();
        List<GeoPoint> geoPoints = new ArrayList<GeoPoint>(points);
        PointList tmp = response.getPoints();
        for (int i = 0; i < response.getPoints().getSize(); i++)
        {
            geoPoints.add(new GeoPoint(tmp.getLatitude(i), tmp.getLongitude(i)));
        }
        PolygonalChain polygonalChain = new PolygonalChain(geoPoints);
        Paint paintStroke = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintStroke.setStyle(Paint.Style.STROKE);
        paintStroke.setColor(Color.BLUE);
        paintStroke.setAlpha(128);
        paintStroke.setStrokeWidth(8);
        paintStroke.setPathEffect(new DashPathEffect(new float[] {25, 15}, 0));

        return new Polyline(polygonalChain, paintStroke);
    }

    public static void showRouteInfo(Activity activity, GHResponse response, float time) {
        LayoutInflater inflater = activity.getLayoutInflater();

        AlertDialog.Builder alertDialog;
        alertDialog = new AlertDialog.Builder(activity);
        alertDialog.setTitle(activity.getResources().getString(R.string.mapfragment_route_info_title));
        View view = inflater.inflate(R.layout.fragment_map_dialog_info, null);
        alertDialog.setView(view);

        TextView distanceText = (TextView) view.findViewById(R.id.mapfragment_info_distance);
        Chronometer timeChrono = (Chronometer) view.findViewById(R.id.mapfragment_info_time);
        TextView calculatedText = (TextView) view.findViewById(R.id.mapfragment_info_calculated);

        distanceText.setText(((int) (response.getDistance() / 100) / 10f) + " Km");
        timeChrono.setBase(response.getMillis()/1000);
        calculatedText.setText(time + " sec");

        alertDialog.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.show();
    }

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

    public static boolean unzipFile(String path, String zipname) {
        InputStream is;
        ZipInputStream zis;

        try {
            String filename;
            is = new FileInputStream(path + "/" + zipname);
            zis = new ZipInputStream(new BufferedInputStream(is));
            ZipEntry ze;
            byte[] buffer = new byte[1024];
            int count;

            while ((ze = zis.getNextEntry()) != null) {

                filename = ze.getName();

                // Need to create directories if not exists, or
                // it will generate an Exception...
                if (ze.isDirectory()) {
                    File fmd = new File(path + "/" + filename);
                    fmd.mkdirs();
                    continue;
                }

                FileOutputStream fout = new FileOutputStream(path + "/" + filename);

                while ((count = zis.read(buffer)) != -1) {
                    fout.write(buffer, 0, count);
                }

                fout.close();
                zis.closeEntry();
            }

            zis.close();
        }
        catch(IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static int getCategoryDrawableId(String category) {
        if (category.equals("Airport"))
            return R.drawable.ic_airport;
        else if (category.equals("Hospital"))
            return R.drawable.ic_hospital;
        else if (category.equals("Hotel"))
            return R.drawable.ic_hotel;
        else if (category.equals("Mall"))
            return R.drawable.ic_mall;
        else if (category.equals("Mosques"))
            return R.drawable.ic_mosque;
        else if (category.equals("Museums"))
            return R.drawable.ic_museum;
        else if (category.equals("Restaurant"))
            return R.drawable.ic_restaurant;
        else if (category.equals("Wifi"))
            return R.drawable.ic_wifi;
        else return R.drawable.flag_green;
    }

    public static File createFileFromInputStream(InputStream inputStream, String filename) {
        try{
            File file = new File(filename);
            OutputStream outputStream = new FileOutputStream(file);
            byte buffer[] = new byte[1024];
            int length = 0;

            while((length=inputStream.read(buffer)) > 0) {
                outputStream.write(buffer,0,length);
            }

            outputStream.close();
            inputStream.close();

            return file;
        }catch (IOException e) {
            //Logging exception
        }

        return null;
    }

}
