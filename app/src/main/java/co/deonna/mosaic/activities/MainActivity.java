package co.deonna.mosaic.activities;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.BitmapDecoder;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.deonna.mosaic.R;
import co.deonna.mosaic.models.ImageGrid;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

//    public static final int WIDTH_DIVISOR = 40; // 45 total images
//    public static final int HEIGHT_DIVISOR = 32; // 77 total images
    public static final int WIDTH_DIVISOR = 200; // 45 total images
    public static final int HEIGHT_DIVISOR = 154; // 16 total images
    private static final String TEST_IMAGE_URL = "http://static.pexels.com/photos/39517/rose-flower-blossom-bloom-39517.jpeg";

    public static int screenWidth;
    public static int screenHeight;

    @BindView(R.id.rlMain)
    RelativeLayout rlMain;

    @BindView(R.id.ivTargetPhoto)
    ImageView ivTargetPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(MainActivity.this);

        setDeviceDimensions();
//        loadInitialImage();
        createImageViews();

        ImageGrid imageGrid = new ImageGrid(MainActivity.this, TEST_IMAGE_URL, screenWidth, screenHeight);
    }

    private void setDeviceDimensions() {

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        screenWidth = metrics.widthPixels;
        screenHeight = metrics.heightPixels;

        Log.d(TAG, String.format("Dimensions: Width: %d, Height: %d", screenWidth, screenHeight));
    }

    private void loadInitialImage() {

        Glide.with(MainActivity.this)
            .load(TEST_IMAGE_URL)
            .into(new SimpleTarget<GlideDrawable>() {
                @Override
                public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                    int width = resource.getIntrinsicWidth();
                    int height = resource.getMinimumHeight();

                    Log.d(TAG, String.format("Width: %d, Height: %d", width, height));

                    ivTargetPhoto.setImageDrawable(resource);

                    Bitmap bitmap = ((GlideBitmapDrawable) resource).getBitmap();

                    int pixel = bitmap.getPixel(1000, 1000);

                    int red = Color.red(pixel);
                    int green = Color.green(pixel);
                    int blue = Color.blue(pixel);
                    int alpha = Color.alpha(pixel);

                    int color = Color.argb(alpha, red, green, blue);

                    Log.d(TAG, String.format("Did it work? Color: %d, Red: %d Green: %d Blue: %d Alpha: %d", color, red, green, blue, alpha));
                }
            });
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void createImageViews() {

        boolean alternateColor = false;

        for (int x = 0; x < screenWidth; x += WIDTH_DIVISOR) {

            alternateColor = !alternateColor;

            for (int y = 0; y < screenHeight; y += HEIGHT_DIVISOR) {

                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(WIDTH_DIVISOR, HEIGHT_DIVISOR);

                ImageView imageView = new ImageView(MainActivity.this);

                imageView.setLayoutParams(layoutParams);

                imageView.setX(x);
                imageView.setY(y);
                imageView.setMaxWidth(WIDTH_DIVISOR);
                imageView.setMaxHeight(HEIGHT_DIVISOR);

                Glide
                    .with(this)
                    .load(TEST_IMAGE_URL)
                    .centerCrop()
                    .into(imageView);

                if (!alternateColor) {
                    imageView.setBackgroundColor(getResources().getColor(R.color.colorPrimary, getTheme()));
                } else {
                    imageView.setBackgroundColor(getResources().getColor(R.color.colorAccent, getTheme()));
                }

                alternateColor = !alternateColor;

                rlMain.addView(imageView);
            }
        }
    }
}
