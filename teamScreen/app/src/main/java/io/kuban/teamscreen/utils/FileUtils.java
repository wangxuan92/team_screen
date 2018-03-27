package io.kuban.teamscreen.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by wangxuan on 17/11/6.
 */

public class FileUtils {
    //    public static final String ONE = "file:///android_asset/badge_1.html";
    public static final String ONE = "badge_1.html";
    public static final String SECOND = "badge_2.html";
    public static final String THIRD = "badge_3.html";

    public static String TAG = "FileUtils";

    public static File FileSave(Context context, byte[] data) {
        File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                RandomUtil.getRandomFileName() + ".jpg");
        OutputStream os = null;
        try {
            os = new FileOutputStream(file);
            os.write(data);
            os.close();
        } catch (IOException e) {
            Log.w(TAG, "Cannot write to " + file, e);
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    // Ignore
                }
            }
        }
        return file;
    }

    /**
     * 拷贝assets文件
     */
    public static File copyAddrDB(Context context, String fileName) {
        File file = new File(context.getFilesDir(), fileName);
        if (file.exists()) {
            Log.e(TAG, "数据库已经存在，不需要拷贝。。。");
        } else {
            Log.e(TAG, "开始拷贝了。。。。");
            copyAddrFile(context, fileName);
        }
        return file;
    }

    public static void copyAddrFile(Context context, String fileName) {
        File file = new File(context.getFilesDir(), fileName);
        // 拷贝
        try {
            InputStream is = context.getAssets().open(fileName);    // 获取数据库库文件输入流
            FileOutputStream fos = new FileOutputStream(file);  // 定义输出流
            byte[] bt = new byte[8192];
            int len = -1;
            while ((len = is.read(bt)) != -1) {
                fos.write(bt, 0, len);
            }
            is.close();
            fos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 镜像水平翻转
     *
     * @param a
     * @return
     */
    public static Bitmap convert(Bitmap a) {
        int w = a.getWidth();
        int h = a.getHeight();
        Bitmap newb = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);// 创建一个新的和SRC长度宽度一样的位图
        Canvas cv = new Canvas(newb);
        Matrix m = new Matrix();
//        m.postScale(1, -1);   //镜像垂直翻转
        m.postScale(1, -1);   //镜像水平翻转
        m.postRotate(180);  //旋转-90度
        Bitmap new2 = Bitmap.createBitmap(a, 0, 0, w, h, m, true);
        cv.drawBitmap(new2, new Rect(0, 0, new2.getWidth(), new2.getHeight()), new Rect(0, 0, w, h), null);
        return newb;
    }

    /**
     * 根据路径获取文件名
     *
     * @param pathandname
     * @return
     */
    public static String getFileName(String pathandname) {

        int start = pathandname.lastIndexOf("/");
        int end = pathandname.lastIndexOf(".");
        if (start != -1 && end != -1) {
            return pathandname.substring(start + 1, end);
        } else {
            return null;
        }

    }
}
