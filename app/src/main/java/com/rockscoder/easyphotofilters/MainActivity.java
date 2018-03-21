package com.rockscoder.easyphotofilters;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.graphics.BitmapCompat;
import android.support.v4.internal.view.SupportMenu;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

import com.rockscoder.easyphotofilters.model.Param;
import com.rockscoder.easyphotofilters.utils.HttpClient;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import jp.co.cyberagent.android.gpuimage.GPUImageView;

public class MainActivity extends AppCompatActivity {
    Bitmap bitmap;
    String encoded = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageView imageView = findViewById(R.id.image_preview);
        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        Bitmap bitmap = drawable.getBitmap();

        this.compressImage(bitmap);

        new CallApi(this.encoded,imageView,"filter-27-th-hd").execute();
    }

    private class CallApi extends AsyncTask<Object, Object, String> {
        String encoded;
        String file_link;
        ImageView imageView;
        String type;

        public CallApi(String encoded, ImageView imageView, String type) {
            this.encoded = encoded;
            this.imageView = imageView;
            this.type = type;
        }

        @Override
        protected String doInBackground(Object... objects) {
            ArrayList<Param> params = new ArrayList<>();
            params.add(new Param("fileToUpload",this.encoded));
            try {
                this.file_link = new HttpClient().makeHttpRequestPost("http://color.photofuneditor.com/" + this.type, params).optString("file_link");
                return this.file_link;
            }catch (Exception e){
                e.printStackTrace();
                return "error";
            }
        }

        @Override
        protected void onPostExecute(String res) {
            super.onPostExecute(res);
            Log.d("Result",res);

            //Picasso.with(MainActivity.this).load("http://color.photofuneditor.com/output/" + res).into(imageView);
            Picasso.with(MainActivity.this).load("http://color.photofuneditor.com/output/" + res).into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    Bitmap newBitmap = bitmap.copy(bitmap.getConfig(), true);
                    Bitmap n1 = Bitmap.createBitmap(newBitmap, 0, 20, newBitmap.getWidth(), newBitmap.getHeight() - 30);
                    imageView.destroyDrawingCache();
                    imageView.setImageBitmap(n1);
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            });

        }
    }


    private void compressImage(Bitmap imageViewBitmap) {
        this.bitmap = imageViewBitmap;
        if (BitmapCompat.getAllocationByteCount(this.bitmap) < 512000) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            scaleBitmapAndKeepRation(this.bitmap, 1024, 1024).compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            this.encoded = Base64.encodeToString(byteArrayOutputStream.toByteArray(), 0);
            return;
        }
        int division = 25600000 / BitmapCompat.getAllocationByteCount(this.bitmap);
        if (division == 0) {
            division = 1;
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        scaleBitmapAndKeepRation(this.bitmap, 512, 512).compress(Bitmap.CompressFormat.PNG, division, byteArrayOutputStream);
        this.encoded = Base64.encodeToString(byteArrayOutputStream.toByteArray(), 0);
    }

    public static Bitmap scaleBitmapAndKeepRation(Bitmap TargetBmp, int reqHeightInPixels, int reqWidthInPixels) {
        Matrix m = new Matrix();
        m.setRectToRect(new RectF(0.0f, 0.0f, (float) TargetBmp.getWidth(), (float) TargetBmp.getHeight()), new RectF(0.0f, 0.0f, (float) reqWidthInPixels, (float) reqHeightInPixels), Matrix.ScaleToFit.CENTER);
        return Bitmap.createBitmap(TargetBmp, 0, 0, TargetBmp.getWidth(), TargetBmp.getHeight(), m, true);

    }

}
