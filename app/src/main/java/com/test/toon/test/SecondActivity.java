package com.test.toon.test;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import android.view.View.MeasureSpec;

import butterknife.Bind;

/**
 * Created by abasile on 07/09/2015.
 */
public class SecondActivity extends AppCompatActivity {
    PageCurlView page;

    @Bind(R.id.bn_textview) TextView butterknife;
    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        // recuperation de l'image
        byte[] byteArray = getIntent().getByteArrayExtra("img");
        Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        final View main  = getWindow().getDecorView().findViewById(android.R.id.content);
        final FrameLayout layout  = (FrameLayout) getWindow().getDecorView().findViewById(android.R.id.content).getRootView();

        // Creation de la view
//        ImageView img = new ImageView(this);
//        img.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        page = new PageCurlView(this){
            @Override
            public void onPageTurned() {
                super.onPageTurned();
                layout.removeView(this);
            }
        };
        page.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        //Ajout au layout

//        layout.addView(img, 0);
//        img.setImageBitmap(bmp);
//        img.bringToFront();
        ButterKnife.bind(this);
        butterknife.setText("test2");
        Bitmap v = getScreenViewBitmap(layout,bmp.getWidth(),bmp.getHeight(),getStatusBarHeight());
        layout.addView(page, 0);
        page.addBitmap(bmp, v);
        page.bringToFront();

        page.turnPage();
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public Bitmap takeScreenShot(View view) {
        // configuramos para que la view almacene la cache en una imagen
        view.setDrawingCacheEnabled(true);
        view.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_LOW);
        view.buildDrawingCache();

        if(view.getDrawingCache() == null) return null; // Verificamos antes de que no sea null

        // utilizamos esa cache, para crear el bitmap que tendra la imagen de la view actual
        Bitmap snapshot = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);
        view.destroyDrawingCache();

        return snapshot;
    }

    public static Bitmap getScreenViewBitmap(final View view,int width, int height,int statusbarHeight) {
        view.measure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
        //Assign a size and position to the view and all of its descendants
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

        //Create the bitmap
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(),
                view.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        //Create a canvas with the specified bitmap to draw into
        Canvas c = new Canvas(bitmap);
        c.translate(0, statusbarHeight);

        //Render this view (and all of its children) to the given Canvas
        view.draw(c);
        return bitmap;
    }


}
