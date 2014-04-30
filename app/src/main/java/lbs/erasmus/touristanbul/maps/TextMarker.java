package lbs.erasmus.touristanbul.maps;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;

import org.mapsforge.android.maps.overlay.Marker;
import org.mapsforge.android.maps.overlay.OverlayItem;
import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.GeoPoint;
import org.mapsforge.core.model.Point;
import org.mapsforge.core.util.MercatorProjection;

/**
 * Created by nacho on 16/04/14.
 */
public class TextMarker extends Marker implements OverlayItem {

    private static final int MIN_OBJECT_ZOOM = 13;
    private final static int MIN_ZOOM_LEVEL = 16;

    private String mText;
    private float mDensity;

    /**
     *
     * @param txt the title
     * @param geoPoint the initial geographical coordinates of this marker (may be null).
     * @param drawable the initial {@code Drawable} of this marker (may be null).
     */
    public TextMarker(String txt, GeoPoint geoPoint, Drawable drawable,float density) {
        super(geoPoint, drawable);
        mText = txt;
        mDensity = density;
    }

    @Override
    public synchronized boolean draw(BoundingBox boundingBox, byte zoomLevel, Canvas canvas, Point canvasPosition) {
        if (getGeoPoint() == null || getDrawable() == null) {
            return false;
        }

        double latitude = getGeoPoint().latitude;
        double longitude = getGeoPoint().longitude;
        int pixelX = (int) (MercatorProjection.longitudeToPixelX(longitude, zoomLevel) - canvasPosition.x);
        int pixelY = (int) (MercatorProjection.latitudeToPixelY(latitude, zoomLevel) - canvasPosition.y);

        Rect drawableBounds = getDrawable().copyBounds();
        int left = pixelX + drawableBounds.left;
        int top = pixelY + drawableBounds.top;
        int right = pixelX + drawableBounds.right;
        int bottom = pixelY + drawableBounds.bottom;

        if (!intersect(canvas, left, top, right, bottom)) {
            return false;
        }

        if (zoomLevel >= MIN_OBJECT_ZOOM) {
            getDrawable().setBounds(left, top, right, bottom);
            getDrawable().draw(canvas);
            getDrawable().setBounds(drawableBounds);

            if (zoomLevel >= MIN_ZOOM_LEVEL) {
                //DisplayMetrics metrics = getDisplayMetrics();
                float textSize = 20f;
                Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
                paint.setTextAlign(Paint.Align.CENTER);
                paint.setTextSize(textSize);
                canvas.drawText(mText, pixelX, pixelY - 15, paint);
                //canvas.drawText(mText, pixelX, bottom + textSize + (2 * mDensity), paint);
            }
        }
        return true;
    }

    private static boolean intersect(Canvas canvas, float left, float top, float right, float bottom) {
        return right >= 0 && left <= canvas.getWidth() && bottom >= 0 && top <= canvas.getHeight();
    }
}