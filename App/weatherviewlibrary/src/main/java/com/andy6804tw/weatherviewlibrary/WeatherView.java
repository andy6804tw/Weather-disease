package com.andy6804tw.weatherviewlibrary;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import java.util.Calendar;

/**
 * Created by DoubleCC on 2017/1/18 0018.
 */

public class WeatherView extends View {
    // windspeed
    private String windSpeed = "11";
    // winddir
    private String windDir = "東風";
    // sunrise sunset：07:00 17:50
    private String sunrise = "07:50";
    private String sunset = "17:50";
    // pressure（hpa）
    private String pressure = "1024";
    // windspeed relation
    private boolean openRelation = true;

    private int viewColor;
    private int sunPathColor;

    private int width, height;
    private final float density;
    private final DashPathEffect dashPathEffect;
    private Path sunPath = new Path();
    private RectF sunRectF = new RectF();
    private Path fanPath = new Path();
    private Path fanPillarPath = new Path();
    private float fanPillerHeight;
    private final TextPaint paint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
    private float paintTextOffset;
    final float offsetDegree = 15f;
    private float curRotate;
    private float sunArcHeight, sunArcRadius;
    private Rect visibleRect = new Rect();

    public WeatherView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.WeatherAstroView);
        viewColor = ta.getColor(R.styleable.WeatherAstroView_viewcolor, ContextCompat.getColor(context,R.color.WeatherAstroViewColor));
        sunPathColor = ta.getColor(R.styleable.WeatherAstroView_sunpathcolor, ContextCompat.getColor(context,R.color.WeatherAstroSunpathColor));
        openRelation = ta.getBoolean(R.styleable.WeatherAstroView_windrelation,true);

        density = context.getResources().getDisplayMetrics().density;
        dashPathEffect = new DashPathEffect(new float[] { density * 3, density * 3 }, 1);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(density);
        paint.setTextAlign(Paint.Align.CENTER);
        if(isInEditMode()){
            return;
        }
        paint.setTypeface(Typeface.DEFAULT);
    }

    public void initData(String windSpeed,String windDir,String sunrise,String sunset,String pressure,boolean openRelation){
        this.windSpeed = windSpeed;
        this.windDir = windDir;
        this.sunrise = sunrise;
        this.sunset = sunset;
        this.pressure = pressure;
        this.openRelation = openRelation;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float textSize = paint.getTextSize();
        try {
            paint.setStrokeWidth(density);
            paint.setStyle(Paint.Style.STROKE);
            // sunpath
            paint.setColor(sunPathColor);

            paint.setPathEffect(dashPathEffect);
            canvas.drawPath(sunPath, paint);
            paint.setPathEffect(null);
            paint.setColor(viewColor);
            int saveCount = canvas.save();
            canvas.translate(width / 2f - fanPillerHeight * 1f, textSize + sunArcHeight - fanPillerHeight);
            // windtext
            paint.setStyle(Paint.Style.FILL);
            paint.setTextAlign(Paint.Align.LEFT);
            final float fanHeight = textSize * 2f;
            canvas.drawText(getResources().getString(R.string.windSpeed), fanHeight + textSize, -textSize, paint);
            canvas.drawText(windSpeed + " mph " + windDir, fanHeight + textSize, 0, paint);
            // windmill
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawPath(fanPillarPath, paint);
            canvas.rotate(curRotate * 360f);
            float speed = 0f;
            try {
                speed = Float.valueOf(windSpeed);
            } catch (Exception e) {
                e.printStackTrace();
            }
            // relation
            if (openRelation){
                speed = Math.max(speed, 0.75f);
            }else{
                speed = 11.0f;
            }
            curRotate += 0.001f * speed;
            if (curRotate > 1f) {
                curRotate = 0f;
            }
            paint.setStyle(Paint.Style.FILL);
            canvas.drawPath(fanPath, paint);
            canvas.rotate(120f);
            canvas.drawPath(fanPath, paint);
            canvas.rotate(120f);
            canvas.drawPath(fanPath, paint);
            canvas.restoreToCount(saveCount);

            paint.setStyle(Paint.Style.STROKE);
            final float lineLeft = width / 2f - sunArcRadius;
            canvas.drawLine(lineLeft, sunArcHeight + textSize, width - lineLeft, sunArcHeight + textSize, paint);

            paint.setStyle(Paint.Style.FILL);
            paint.setTextAlign(Paint.Align.RIGHT);
            final float pressureTextRight = width / 2f + sunArcRadius - textSize * 2.5f;
            canvas.drawText(getResources().getString(R.string.pressure)+" " + pressure + " millibars", pressureTextRight, sunArcHeight + paintTextOffset, paint);
            // draw astor info
            final float textLeft = width / 2f - sunArcRadius+11;// sunArcSize;
            paint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText(getResources().getString(R.string.sunrise)+" "+ sunrise, textLeft, textSize * 10.5f + paintTextOffset, paint);
            canvas.drawText(sunset +" "+ getResources().getString(R.string.sunset), width - textLeft, textSize * 10.5f + paintTextOffset, paint);
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        try {
            // sun
            String[] sr = sunrise.split(":");
            int srTime = Integer.valueOf(sr[0]) * 60 * 60 + Integer.valueOf(sr[1]) * 60 + 0;
            String[] ss = sunset.split(":");
            int ssTime = Integer.valueOf(ss[0]) * 60 * 60 + Integer.valueOf(ss[1]) * 60 + 0;
            Calendar c = Calendar.getInstance();
            int curTime = c.get(Calendar.HOUR_OF_DAY) * 60 * 60 + c.get(Calendar.MINUTE) * 60 + c.get(Calendar.MINUTE);
            if (curTime >= srTime && curTime <= ssTime) {
                canvas.save();
                canvas.translate(width / 2f, sunArcRadius + textSize);
                float percent = (curTime - srTime) / ((float) (ssTime - srTime));
                float degree = 15f + 150f * percent;
                final float sunRadius = density * 4f;
                canvas.rotate(degree);

                paint.setStyle(Paint.Style.FILL);
                paint.setStrokeWidth(density * 1.333f);
                canvas.translate(-sunArcRadius, 0);
                canvas.rotate(-degree);
                canvas.drawCircle(0, 0, sunRadius, paint);
                paint.setStyle(Paint.Style.STROKE);
                final int light_count = 8;
                for (int i = 0; i < light_count; i++) {
                    double radians = Math.toRadians(i * (360 / light_count));
                    float x1 = (float) (Math.cos(radians) * sunRadius * 1.6f);
                    float y1 = (float) (Math.sin(radians) * sunRadius * 1.6f);
                    float x2 = x1 * (1f + 0.4f * 1f);
                    float y2 = y1 * (1f + 0.4f * 1f);
                    canvas.drawLine(0 + x1, y1, 0 + x2, y2, paint);
                }
                canvas.restore();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        getGlobalVisibleRect(visibleRect);
        if (!visibleRect.isEmpty()) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }
    private float getTextPaintOffset(Paint paint) {
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        return -(fontMetrics.bottom - fontMetrics.top) / 2f - fontMetrics.top;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        this.width = w;
        this.height = h;

        try {
            final float textSize = height / 12f;
            paint.setTextSize(textSize);
            paintTextOffset = getTextPaintOffset(paint);

            sunPath.reset();

            sunArcHeight = textSize * 8.5f;
            sunArcRadius = (float) (sunArcHeight / (1f - Math.sin(Math.toRadians(offsetDegree))));
            final float sunArcLeft = width / 2f - sunArcRadius;
            sunRectF.left = sunArcLeft;
            sunRectF.top = textSize;
            sunRectF.right = width - sunArcLeft;
            sunRectF.bottom = sunArcRadius * 2f + textSize;
            sunPath.addArc(sunRectF, -165, +150);


            fanPath.reset();
            final float fanSize = textSize * 0.2f;
            final float fanHeight = textSize * 2f;
            final float fanCenterOffsetY = fanSize * 1.6f;

            fanPath.addArc(new RectF(-fanSize, -fanSize - fanCenterOffsetY, fanSize, fanSize - fanCenterOffsetY), 0,
                    180);
            fanPath.quadTo(-fanSize * 1f, -fanHeight * 0.5f - fanCenterOffsetY, 0, -fanHeight - fanCenterOffsetY);
            fanPath.quadTo(fanSize * 1f, -fanHeight * 0.5f - fanCenterOffsetY, fanSize, -fanCenterOffsetY);
            fanPath.close();

            fanPillarPath.reset();
            final float fanPillarSize = textSize * 0.25f;
            fanPillarPath.moveTo(0, 0);
            fanPillerHeight = textSize * 4f;
            fanPillarPath.lineTo(fanPillarSize, fanPillerHeight);
            fanPillarPath.lineTo(-fanPillarSize, fanPillerHeight);
            fanPillarPath.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec,
                             int heightMeasureSpec) {

        setMeasuredDimension(
                measureDimension(dp2px(350),widthMeasureSpec),
                measureDimension(dp2px(144),heightMeasureSpec));

    }

    private int measureDimension(int defaultDimension,int measureSpec) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = defaultDimension;
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    private int dp2px(int dp){
        return (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dp,getResources().getDisplayMetrics());
    }
}
