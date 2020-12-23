package cn.byhook.libwebp;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.InputStream;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private ImageView iv_logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initData();
    }

    private void initView() {
        iv_logo = (ImageView) findViewById(R.id.iv_logo);
    }

    private void initData() {
        InputStream rawImageStream = getResources().openRawResource(
                R.raw.background);

        byte[] data = WebpUtils.streamToBytes(rawImageStream);
        System.out.println(Arrays.toString(data));
        final Bitmap webpBitmap = WebpUtils.webpToBitmap(data);

        iv_logo.setImageBitmap(webpBitmap);
    }
}
