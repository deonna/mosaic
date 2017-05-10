package co.deonna.mosaic.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.deonna.mosaic.R;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.ivTargetPhoto)
    ImageView ivTargetPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(MainActivity.this);

        loadInitialImage();
    }

    private void loadInitialImage() {

        Glide.with(MainActivity.this)
            .load("http://static.pexels.com/photos/39517/rose-flower-blossom-bloom-39517.jpeg")
            .into(ivTargetPhoto);
    }
}
