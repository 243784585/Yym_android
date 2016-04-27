package com.shengtao.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * @Description
 * @Created by YCH on 2015/11/9.
 */
public class CropImageView extends ImageView {


    /**
     * 画布的宽度
     */
    private int width;

    /**
     * 画布的高度
     */
    private int height;


    float scaleWidth = 1.0f;
    float scaleHeight = 1.0f;

    public CropImageView(Context context) {
        super(context);
    }

    public CropImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        width = canvas.getWidth();
        height = canvas.getHeight();

        Drawable drawable = getDrawable();

        if (drawable != null) {
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            if(bitmap != null){
                int bmWidth = bitmap.getWidth();
                int bmHeight = bitmap.getHeight();
                float scale = 1.0f;

                float scaleBitmap = (float)bmWidth/(float)bmHeight;//图片宽高比

                float scaleContainer = (float)width/(float)height;//容器宽高比

                //如果容器的比例大于图片的比例
                if(scaleContainer > scaleBitmap){
                    //根据宽度进行计算
                    scale = (float) width / (float) bmWidth;
                }else {
                    //根据高度进行计算
                    scale = (float) height / (float) bmHeight;
                }

                Matrix matrix = new Matrix();
                matrix.postScale(scaleWidth * scale, scaleHeight * scale);

                Rect rect = new Rect();
                rect.left = 0;
                rect.top = 0;
                rect.right = width;
                rect.bottom = height;
                Bitmap bm = Bitmap.createBitmap(bitmap, 0, 0, bmWidth, bmHeight, matrix, true);
                canvas.drawBitmap(bm, rect, rect, null);
            }
        }
    }
}