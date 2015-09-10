package com.test.toon.test;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import fi.harism.curl.CurlPage;
import fi.harism.curl.CurlView;

/**
 * Created by abasile on 07/09/2015.
 */
public class ThirdActivity extends AppCompatActivity {
    private CurlView page;

    @Bind(R.id.bn_textview) TextView butterknife;
    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        // recuperation de l'image
        byte[] byteArray = getIntent().getByteArrayExtra("img");
        final Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        final View main  = getWindow().getDecorView().findViewById(android.R.id.content);
        final FrameLayout layout  = (FrameLayout) getWindow().getDecorView().findViewById(android.R.id.content).getRootView();

        // Creation de la view
//        ImageView img = new ImageView(this);
//        img.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        page = new CurlView(this);
        page.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        PageProvider pp = new PageProvider();
        page.setPageProvider(pp);
        page.setSizeChangedObserver(new SizeChangedObserver());
        page.setCurrentIndex(0);
        page.setBackgroundColor(0xFF202830);

        ButterKnife.bind(this);
        butterknife.setText("test2");
        final Bitmap v = getScreenViewBitmap(layout,bmp.getWidth(),bmp.getHeight(),getStatusBarHeight());
        layout.addView(page, 0);
        pp.setBitmaps(bmp,v);

        page.bringToFront();


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

    private class PageProvider implements CurlView.PageProvider {

        // Bitmap resources.
        Bitmap front;
        Bitmap back;

        public void setBitmaps(Bitmap front, Bitmap back){
            this.front = front;
            this.back = back;
        }

        @Override
        public int getPageCount() {
            return 2;
        }


        @Override
        public void updatePage(CurlPage page, int width, int height, int index) {

            switch (index) {
                // First case is image on front side, solid colored back.
                case 0: {
                    page.setTexture(front, CurlPage.SIDE_FRONT);
                    page.setColor(Color.rgb(180, 180, 180), CurlPage.SIDE_BACK);
                    break;
                }
                // Second case is image on back side, solid colored front.
                case 1: {

                    page.setTexture(back, CurlPage.SIDE_FRONT);
                    page.setColor(Color.rgb(127, 140, 180), CurlPage.SIDE_BACK);
                    break;
                }

            }
        }

    }

    /**
     * CurlView size changed observer.
     */
    private class SizeChangedObserver implements CurlView.SizeChangedObserver {
        @Override
        public void onSizeChanged(int w, int h) {
            if (w > h) {
                page.setViewMode(CurlView.SHOW_TWO_PAGES);
                page.setMargins(.1f, .05f, .1f, .05f);
            } else {
                page.setViewMode(CurlView.SHOW_ONE_PAGE);
                page.setMargins(.1f, .1f, .1f, .1f);
            }
        }
    }


}
