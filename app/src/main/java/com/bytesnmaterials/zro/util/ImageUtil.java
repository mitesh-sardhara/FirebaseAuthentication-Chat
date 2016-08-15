package com.bytesnmaterials.zro.util;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ThumbnailUtils;
import android.util.Base64;
import android.widget.ImageView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.bytesnmaterials.zro.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.io.ByteArrayOutputStream;

public class ImageUtil {

    public static Bitmap getCircleBitmap(Bitmap bm) {

        int sice = Math.min((bm.getWidth()), (bm.getHeight()));

        Bitmap bitmap = ThumbnailUtils.extractThumbnail(bm, sice, sice);

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);

        Canvas canvas = new Canvas(output);

        final int color = 0xffff0000;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setFilterBitmap(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawOval(rectF, paint);

        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth((float) 4);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    public static Bitmap generateQR(String content) {
        QRCodeWriter writer = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, 512, 512);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            Bitmap bmp = Bitmap.createBitmap(width, height, Config.RGB_565);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
            //((ImageView) findViewById(R.id.img_result_qr)).setImageBitmap(bmp);

            return bmp;

        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }

    //region Base64
    public static String getBase64(Bitmap bitmap) {
        if (bitmap == null) return "";

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        bitmap.recycle();
        byte[] byteArray = stream.toByteArray();
        String imageFile = Base64.encodeToString(byteArray, Base64.DEFAULT);
        return imageFile;
    }

    public static Bitmap getBitmap(String base64Image) {
        if (base64Image == null || base64Image.equals("")) {
            return null;
        }

        byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
        Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        return decodedBitmap;
    }
    //endregion

    //region Drawable Text
    public static TextDrawable getDrawable(String text) {
        if (text != null && !text.equals("")) {

            ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
            int color = generator.getColor(text);
            String ImageLetter = text.substring(0, 2).toUpperCase();

            return TextDrawable.builder()
                    .buildRound(ImageLetter, color);
        }

        return null;
    }
    //endregion


    public static void displayImage(ImageView view, String path, ImageLoadingListener listener) {
        ImageLoader loader = ImageLoader.getInstance();
        try {
            loader.displayImage(path, view, DEFAULT_DISPLAY_IMAGE_OPTIONS, listener);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            loader.clearMemoryCache();
        }
    }

    public static void displayRoundImage(ImageView view, String path, ImageLoadingListener listener) {
        ImageLoader loader = ImageLoader.getInstance();
        try {
            loader.displayImage(path, view, ROUND_DISPLAY_IMAGE_OPTIONS, listener);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            loader.clearMemoryCache();
        }
    }

    public static void loadImage(String path, ImageLoadingListener listener) {
        ImageLoader loader = ImageLoader.getInstance();
        try {
            loader.loadImage(path, DEFAULT_DISPLAY_IMAGE_OPTIONS, listener);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
    }

    //TODO Change default image
    private static final DisplayImageOptions.Builder DEFAULT_DISPLAY_IMAGE_OPTIONS_BUIDLER = new DisplayImageOptions.Builder()
            .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
            .displayer(new FadeInBitmapDisplayer(300, true, false, false))
            .showImageForEmptyUri(R.drawable.default_image)
            .showImageOnLoading(R.drawable.default_image)
            .showImageOnFail(R.drawable.default_image).cacheOnDisk(true)
            .cacheInMemory(true).bitmapConfig(Config.ARGB_8888);

    private static final DisplayImageOptions DEFAULT_DISPLAY_IMAGE_OPTIONS = DEFAULT_DISPLAY_IMAGE_OPTIONS_BUIDLER
            .build();
    private static final DisplayImageOptions ROUND_DISPLAY_IMAGE_OPTIONS = DEFAULT_DISPLAY_IMAGE_OPTIONS_BUIDLER
            .displayer(new RoundedBitmapDisplayer(500)).build();
}
