package android.jhyuk.com.sensor;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Pair;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maxx on 2017. 9. 1..
 */

public class Graph extends View {

    private int realWidth, realHeight;
    private final int xDensity = 200, yDensity = 500;
    private float cellWidth, cellHeight;
    private static final int DEFAULT_GRAPH_COLOR = Color.CYAN;
    private Paint graphPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int lineWidth = 10;

    private List<Float> points = new ArrayList<>();
    private float[] xLines = new float[xDensity * 2];
    private Pair<Float,Float>[] linePoints = new Pair[xDensity];
    private Point zeroByzero = new Point();

    public Graph(Context context) {
        this(context, null);
    }

    public Graph(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.Graph);
        int graphColor = ta.getInt(R.styleable.Graph_graphColor, DEFAULT_GRAPH_COLOR);
        graphPaint.setColor(graphColor);
        graphPaint.setStrokeWidth(lineWidth);
        ta.recycle();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        realWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
        realHeight = getMeasuredHeight() - getPaddingTop() - getPaddingBottom();
        cellHeight = realHeight / yDensity;
        Log.i("cellHeight", "==========" + cellHeight);
        cellWidth = realWidth / xDensity;
        Log.i("cellWidth", "============" + cellWidth);
        zeroByzero.set(getPaddingLeft(), getPaddingTop() + realHeight / 2);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        realWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
        realHeight = getMeasuredHeight() - getPaddingTop() - getPaddingBottom();
        cellHeight = realHeight / yDensity;
        Log.i("cellHeight", "==========" + cellHeight);
        cellWidth = realWidth / xDensity;
        Log.i("cellWidth", "============" + cellWidth);
        zeroByzero.set(getPaddingLeft(), getPaddingTop() + realHeight / 2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        canvas.drawLines(xLines, graphPaint);
        for (int i1 = 0; i1 < linePoints.length; i1++) {
            if (i1 > 1 && linePoints[i1] != null) {
                canvas.drawLine(linePoints[i1 - 1].first, linePoints[i1 - 1].second, linePoints[i1].first, linePoints[i1].second, graphPaint);
            }
        }

    }


    public void setPoints(List<Float> points) {
        if (points.size() < 2) {
            throw new IllegalArgumentException("Required at least two points");
        } else if (points.size() > xDensity) {
            int deletable = points.size() - xDensity;
            for (int i = 0; i < deletable; i++) {
                points.remove(i);
            }
        }
        this.points = points;
        invalidate();
    }

    public void setPoint(Float point) {
        if (points.size() == xDensity) {
            points.remove(0);
        } else if (points.size() == 0) {
            points.add(0.0f);
        }
        points.add(point);
        populateLinePoints();
        invalidate();
    }

    private void populateLinePoints() {

        for (int i = 0; i < points.size(); i++) {
            float y = zeroByzero.y - (points.get(i) * cellHeight);
            xLines[i * 2 + 1] = y;
            float x = zeroByzero.x + (i) * cellWidth;
            xLines[i * 2] = x;
            linePoints[i] = new Pair<>(x, y);
        }
    }
}
