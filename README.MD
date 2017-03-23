**目录**
- [1.简介](#简介)
- [2.源码配置](#2源码配置)
- [3.编译生成](#3编译生成)
- [4.示例源码地址](#4示例源码地址)
- [5.参考](#5参考)

###1.简介
Webp图片格式是google官方推出的一种图片格式，相比传统的jpeg格式，可以节约1/3的流量，因此在做大量使用图片的App时，益处多多。
而此前项目来来回回换了几个图片框架，都不太理想
最后决定选用Glide+libwebp的模式
由于4.0以下Android对Webp的支持并不好，因此需要我们手动支持。
###2.源码配置
[libwebp的最新源码](https://storage.googleapis.com/downloads.webmproject.org/releases/webp/index.html)
建议开个代理
将源码文件夹改名为jni
编辑Android.mk文件，开头加上

```
#开启webp 共享库编译
ENABLE_SHARED := 1
```

![](http://upload-images.jianshu.io/upload_images/2006464-cb17f911647bdcc3?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

添加swig/libwebp_java_wrap.c文件到Android.mk中

![](http://upload-images.jianshu.io/upload_images/2006464-11abb3e47393e55f?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

添加Application.mk文件

```
APP_ABI := armeabi armeabi-v7a
APP_PLATFORM := android-8
```
###3.编译生成
此时可以使用ndk-build命令编译

![](http://upload-images.jianshu.io/upload_images/2006464-fb0cd6f3684df7fe?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

项目中使用
建立Sample工程
将swig目录下的libwebp.jar文件和编译生成的库文件添加到项目中去
整理的对应的工具类

```java
package com.webp.lib;

import android.graphics.Bitmap;

import com.google.webp.libwebp;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;

/**
 * 作者: andy
 * 时间: 16-4-8
 * 描述:
 * 取自互联网
 * 修订: 暂无
 */
public class WebpUtils {

    static {
        System.loadLibrary("webp");
    }

    /**
     * 字节数组转换为bitmap对象
     *
     * @param encoded
     * @return
     */
    public static Bitmap webpToBitmap(byte[] encoded) {
        int[] width = new int[]{0};
        int[] height = new int[]{0};
        byte[] decoded = libwebp.WebPDecodeARGB(encoded, encoded.length, width,
                height);

        int[] pixels = new int[decoded.length / 4];
        ByteBuffer.wrap(decoded).asIntBuffer().get(pixels);

        return Bitmap.createBitmap(pixels, width[0], height[0],
                Bitmap.Config.ARGB_8888);
    }

    /**
     * 流转换为字节数组
     * @param in
     * @return
     */
    public static byte[] streamToBytes(InputStream in) {
        ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
        byte[] buffer = new byte[1024];
        int len = -1;
        try {
            while ((len = in.read(buffer)) >= 0) {
                out.write(buffer, 0, len);
                out.flush();
            }
        } catch (java.io.IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return out.toByteArray();
    }

    public static boolean isWebp(byte[] data) {
        return data != null && data.length > 12 && data[0] == 'R'
                && data[1] == 'I' && data[2] == 'F' && data[3] == 'F'
                && data[8] == 'W' && data[9] == 'E' && data[10] == 'B'
                && data[11] == 'P';
    }

    /**
     * 获取webp版本
     * 用来测试JNI的调用
     * @return
     */
    public static int getWebpVersion() {
        return libwebp.WebPGetDecoderVersion();
    }

}

```
效果如图所示

![](http://upload-images.jianshu.io/upload_images/2006464-d92e98f32d7cddbd.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

使用Geeymotion的模拟器2.3.7成功加载webp图片

###4.示例源码地址
https://github.com/byhook/WebpSample
###5.参考
[官方使用FAQ](https://developers.google.com/speed/webp/faq)
