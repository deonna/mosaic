package co.deonna.mosaic.models;

public class ImageGrid {

    public static final int WIDTH_DIVISOR = 40; // 45 total images
    public static final int HEIGHT_DIVISOR = 32; // 77 total images

    private final int screenWidth;
    private final int screenHeight;

    public ImageGrid( int screenWidth, int screenHeight) {

        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
    }


}
