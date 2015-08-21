package com.android.flowlayout;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * @作者          jingqiang
 * @项目名称    FlowLayout
 * @包名      com.android.flowlayout
 * @创建时间 15/8/20 下午7:19
 */
public class FlowLayout extends ViewGroup {
    private List<Line> mLine = new ArrayList<>();//一共有多少行
    private Line mCurrentLine;
    private int viewSpace = 10;
    private int VerticlaSpace = 10;


    public FlowLayout(Context context) {
        super(context);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //因为过程中会多次测量，所以需要清空。
        mLine.clear();
        mCurrentLine = null;

        //做测量
        //自己的宽高。MeasureSpec 父容器期望孩子的方法 MeasureSpec.makeMeasureSpec 父容器期望孩子的宽高。
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int MaxWidth = width - getPaddingLeft() - getPaddingRight();
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View view = getChildAt(i);
            measureChild(view, widthMeasureSpec, heightMeasureSpec);

            if (mCurrentLine == null) {
                //当前行是空的,可以将测量后的view加入到这行中。
                mCurrentLine = new Line(MaxWidth, viewSpace);
                mCurrentLine.addView(view);
                mLine.add(mCurrentLine);
            } else {
                if (mCurrentLine.canAdd(view)) {
                    mCurrentLine.addView(view);
                } else {
                    mCurrentLine = new Line(MaxWidth, viewSpace);
                    mCurrentLine.addView(view);
                    mLine.add(mCurrentLine);
                }
            }

        }
        //计算出自己(整个)的高度
        int TotalHeight = 0;
        TotalHeight += getPaddingBottom() + getPaddingTop();
        for (int i = 0; i < mLine.size(); i++) {
            TotalHeight += mLine.get(i).Height;
        }
        TotalHeight += (mLine.size() - 1) * VerticlaSpace;

        setMeasuredDimension(width, TotalHeight);
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        int top = getPaddingTop();
        for (int i = 0; i < mLine.size(); i++) {
            //给每行设置view控件
            Line line = mLine.get(i);
            line.Layout(top, getPaddingLeft());
            top += line.Height + VerticlaSpace;
            // Log.d("aaa",""+line.Height);
        }
    }

    private class Line {
        private List<View> mView = new ArrayList<>();
        private int MaxWidth;//每行的最大长度
        private int UsedWidth;//当前行已经使用的长度
        private int Height;//高度
        private int space;//控件之间的间隔

        public Line(int MaxWidth, int space)//传入每行的最大长度，和控件之间的距离。
        {
            this.MaxWidth = MaxWidth;
            this.space = space;
        }

        public void Layout(int top, int left) {
            //每行剩余的宽度.
            int FreeWidth = MaxWidth - UsedWidth;

            //多出来的宽度均分给每个控件
            int avgWidth = (int) (FreeWidth / mView.size() + 0.5f);


            for (int i = 0; i < mView.size(); i++) {
                View view = mView.get(i);
                int vMeasuredWidth = view.getMeasuredWidth();
                int vMeasuredHeight = view.getMeasuredHeight();
                int mHeight = (Height - vMeasuredHeight) / 2;//有大有小时，让小控件居中显示

                if (avgWidth > 0) {
                    //重新测量
                    view.measure(MeasureSpec.makeMeasureSpec(vMeasuredWidth + avgWidth, MeasureSpec.EXACTLY),
                            MeasureSpec.makeMeasureSpec(vMeasuredHeight, MeasureSpec.EXACTLY)
                    );
                }

                vMeasuredWidth = view.getMeasuredWidth();
                vMeasuredHeight = view.getMeasuredHeight();
                int vTop = top + mHeight;
                int vBottom = top + vMeasuredHeight + mHeight;
                int vLeft = left;
                int vRight = left + vMeasuredWidth;


                left += vMeasuredWidth + space;
                view.layout(vLeft, vTop, vRight, vBottom);
            }
        }

        public void addView(View view) {
            int width = view.getMeasuredWidth();
            int height = view.getMeasuredHeight();

            if (mView.size() == 0) {
                //说明没有控件
                if (width > MaxWidth) {
                    UsedWidth = MaxWidth;
                    Height = height;
                } else {
                    UsedWidth += width;
                    Height = height;
                }
            } else {
                //之前已经存在控件.走到这里不用判断新增控件后width会超过长度(canAdd()已经判断)
                UsedWidth += width + space;
                Height = Height > height ? Height : height;//取最大的高度.
            }

            mView.add(view);
        }

        public boolean canAdd(View view) {
            int width = view.getMeasuredWidth();
            if (mView.size() == 0) {
                return true;
            } else {
                //说明当前行中已经有控件了.
                if (UsedWidth + width + space > MaxWidth) {
                    //当前控件加入当前行中会超过最大长度，所以不能加在当前行.
                    return false;
                } else {
                    return true;
                }
            }
        }

    }
}
