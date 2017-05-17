package co.deonna.mosaic.models;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.util.HashMap;
import java.util.Map;

import co.deonna.mosaic.activities.MainActivity;

public class ImageGrid {

    private static final String TAG = ImageGrid.class.getSimpleName();

    private final Context context;
    private final String IMAGE_URL;

    private final int screenWidth;
    private final int screenHeight;

    private final Map<Point, Integer> pointToRgba;

    private final ImageView ivPlaceholder;

    public ImageGrid(final Context context, final String imageUrl, final int screenWidth, final int screenHeight) {

        this.context = context;

        IMAGE_URL = imageUrl;

        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;

        pointToRgba = new HashMap<>();

        ivPlaceholder = new ImageView(context);

        storeAverageColorValues();
    }

    public void storeAverageColorValues() {

        Glide
            .with(context)
            .load(IMAGE_URL)
            .into(new SimpleTarget<GlideDrawable>() {
                @Override
                public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                    int width = resource.getIntrinsicWidth();
                    int height = resource.getMinimumHeight();

                    Log.d(TAG, String.format("Width: %d, Height: %d", width, height));

                    ivPlaceholder.setImageDrawable(resource);

                    Bitmap bitmap = ((GlideBitmapDrawable) resource).getBitmap();

                    setAverageColorValuesForImage(bitmap);
                   }
            });
    }

    private void setAverageColorValuesForImage(final Bitmap bitmap) {

        for (int x = 0; x < screenWidth; x += MainActivity.WIDTH_DIVISOR) {

            for (int y = 0; y < screenHeight; y += MainActivity.HEIGHT_DIVISOR) {

                setAverageRgbaForRegion(bitmap, new Point(x, y));
            }
        }
    }

    private void setAverageRgbaForRegion(final Bitmap bitmap, final Point topLeft) {

        int redAverage, greenAverage, blueAverage, alphaAverage;
        redAverage = greenAverage = blueAverage = alphaAverage = 0;

        int numPixels = 0;

        for (int x = topLeft.x; x < MainActivity.WIDTH_DIVISOR; x++) {

            for (int y = topLeft.y; y < MainActivity.HEIGHT_DIVISOR; y++) {

                int pixel = bitmap.getPixel(topLeft.x + x, topLeft.y + y);

                int red = Color.red(pixel);
                redAverage += red;

                int green = Color.green(pixel);
                greenAverage += green;

                int blue = Color.blue(pixel);
                blueAverage += blue;

                int alpha = Color.alpha(pixel);
                alphaAverage += alpha;

                int color = Color.argb(alpha, red, green, blue);

                Log.d(TAG, String.format("Did it work? Color: %d, Red: %d Green: %d Blue: %d Alpha: %d", color, red, green, blue, alpha));
                numPixels++;
            }
        }

        if (numPixels == 0) {
            Log.d(TAG, String.format("Here's the value of Top Left: (%d, %d)", topLeft.x, topLeft.y));
        } else {
            redAverage /= numPixels;
            greenAverage /= numPixels;
            blueAverage /= numPixels;
            alphaAverage /= numPixels;
        }

        Log.d(TAG, String.format("Averages - Red: %d, Green: %d, Blue: %d, Alpha: %d", redAverage, greenAverage, blueAverage, alphaAverage));
    }
}
