package cn.jiguang.imui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.graphics.drawable.shapes.Shape;
import android.util.AttributeSet;
import android.graphics.drawable.BitmapDrawable;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import java.util.Arrays;

import cn.jiguang.imui.R;

import static android.view.View.LAYER_TYPE_HARDWARE;


public class ShapeImageView extends AppCompatImageView {

    private Paint mPaint;
    private Shape mShape;

    private float mRadius;

    public int theWidth=0;

    public ShapeImageView(Context context) {
        super(context);
    }

    public ShapeImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ShapeImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        setLayerType(LAYER_TYPE_HARDWARE, null);
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.MessageList);
            mRadius = a.getDimensionPixelSize(R.styleable.MessageList_photoMessageRadius,
                    context.getResources().getDimensionPixelSize(R.dimen.aurora_radius_photo_message));
            a.recycle();
        }
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setFilterBitmap(true);
        mPaint.setColor(Color.BLACK);

        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
    }


    public  void setTheWidth(int x){
        theWidth=x;
    }

    public int dp2px(float value) {
        float scale = getResources().getDisplayMetrics().density;
        return (int) (value * scale + 0.5f);
    }

    public int px2dp(float px) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        System.out.println("xxxxxxxxxxxxxxxxxxxx");
//        System.out.println(px2dp(1080));

        Drawable drawable = getDrawable();
        if(drawable!=null ){
            //收缩高度，是图片不会在画布里上下移动，只会左右移动，使ScaleType.FIT_END同一靠右上
            if( drawable.getIntrinsicWidth()>drawable.getIntrinsicHeight()){
                //比例关系（前提，宽高相同相同）
                try{
                    int picHeight=drawable.getIntrinsicHeight()*dp2px(theWidth)/drawable.getIntrinsicWidth();
                    setMeasuredDimension(widthMeasureSpec,picHeight+0);
                    return;
                }catch (Exception E){
                    setMeasuredDimension(widthMeasureSpec,heightMeasureSpec);

                }
            }
            else{
                try{
                    int picWidth=drawable.getIntrinsicWidth()*dp2px(theWidth)/drawable.getIntrinsicHeight();
                    setMeasuredDimension(picWidth+0,heightMeasureSpec);
                    return;
                }catch (Exception E){
                    setMeasuredDimension(widthMeasureSpec,heightMeasureSpec);

                }
            }
        }

        setMeasuredDimension(widthMeasureSpec,heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (mShape == null) {
            float[] radius = new float[8];
            Arrays.fill(radius, mRadius);
            mShape = new RoundRectShape(radius, null, null);
        }

        mShape.resize(getWidth(), getHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Drawable drawable = getDrawable();



//        if( drawable.getIntrinsicWidth()>drawable.getIntrinsicHeight()){
//            setScaleType(ScaleType.FIT_START);
//        }else{
//            setScaleType(ScaleType.FIT_END);
//        }
        setScaleType(ScaleType.FIT_START);

        int saveCount = canvas.getSaveCount();
        canvas.save();
        super.onDraw(canvas);
//        if (mShape != null  ) {
//            if(mPaint==null){
//                mPaint = new Paint();
//                mPaint.setAntiAlias(true);
//                mPaint.setFilterBitmap(true);
//                mPaint.setColor(Color.parseColor("#FFAAAAAA"));
//                mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
//            }
//            mShape.draw(canvas, mPaint);
//        }
        canvas.restoreToCount(saveCount);

    }

    public void setBorderRadius(int radius) {
        this.mRadius = radius;
        invalidate();
    }
}
