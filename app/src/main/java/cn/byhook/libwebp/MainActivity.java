package cn.byhook.libwebp;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import java.io.InputStream;

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
                R.raw.image);

        byte[] data = WebpUtils.streamToBytes(rawImageStream);
        final Bitmap webpBitmap = WebpUtils.webpToBitmap(data);

        iv_logo.setImageBitmap(webpBitmap);
    }
}
