package deadline.expandabletextview;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.Layout;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.TextView;

/**
 * @author deadline
 * @time 2016/9/23
 *
 * 问题： 动画快速执行高度不准确
 */
public class ExpandableTextView extends TextView{

    private final String TAG = ExpandableTextView.class.getSimpleName();

    private static final int DEFAULT_ANIM_DURATION = 100;

    private static final int DEFAULT_MAX_LINES = 2;

    private Drawable mExpandDrawable = null;

    private Drawable mCollapseDrawable = null;

    private int mAnimDuration = DEFAULT_ANIM_DURATION;

    private OnExpandStateChangeListener mListener;

    //is animation running
    private boolean isInAnimation = false;

    //if true this TextView is expanded
    private boolean mState = false;

    private int mCollapseHeight = 0;

    private int mMaxLines = DEFAULT_MAX_LINES;

    ExpandCollapseAnimation animation;

    private int mDrawableGravity;

    public ExpandableTextView(Context context) {
        this(context, null);
    }

    public ExpandableTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ExpandableTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setup();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ExpandableTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    private void setup() {

        super.setMaxLines(mMaxLines);
        mDrawableGravity = Gravity.LEFT;
        setTag(-1, false);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!isInAnimation){
                    executeAnimation(!mState);
                }
            }
        });

    }

    @Override
    public void setMaxLines(int maxlines) {
        super.setMaxLines(maxlines);
        mMaxLines = maxlines;
    }

    @Override
    public void setCompoundDrawables(Drawable left, Drawable top, Drawable right, Drawable bottom) {
        super.setCompoundDrawables(left, top, right, null);
    }

    private void executeAnimation(final boolean b) {

        isInAnimation = true;

        if(animation != null){
            animation.cancel();
            animation = null;
        }

        if(b) {
            mCollapseHeight = getHeight();
            getLayoutParams().height = mCollapseHeight;
            requestLayout();
            ExpandableTextView.super.setMaxLines(Integer.MAX_VALUE);
        }

        int height = getTextViewHeight();

        animation = b
                    ? new ExpandCollapseAnimation(this, mCollapseHeight, height)
                    : new ExpandCollapseAnimation(this, height, mCollapseHeight);

        //Log.e(TAG, "height: " + height + "\n" + "mCollapseHeight: " + mCollapseHeight);

        animation.setFillAfter(true);
        animation.setDuration(mAnimDuration);
        animation.setInterpolator(new AccelerateDecelerateInterpolator());
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mState = b;
                setDrawable();
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if(mListener != null)
                    mListener.onExpandStateChanged(ExpandableTextView.this, mState);

                isInAnimation = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        startAnimation(animation);
    }

    /**
     *
     * Get the height after the expansion
     * @return
     */
    private int getTextViewHeight() {
        Layout layout = getLayout();
        if(layout != null) {
            int desired = layout.getLineTop(getLineCount()) + layout.getBottomPadding();
            int padding = getCompoundPaddingTop()+ getCompoundPaddingBottom();
            return desired + padding;
        }
        return 0;
    }

    private void setDrawable() {

        Drawable temp = mState ? mExpandDrawable : mCollapseDrawable;
        setBounds(mExpandDrawable);
        setBounds(mCollapseDrawable);
        if(temp != null) {
            final Drawable[] CompoundDrawables = getCompoundDrawables();
            super.setCompoundDrawables(CompoundDrawables[0],
                    CompoundDrawables[1], CompoundDrawables[2], temp);
        }
    }

    private void setBounds(Drawable temp) {

        if(temp != null) {
            if (mDrawableGravity == Gravity.LEFT) {
                temp.setBounds(
                        -(getMeasuredWidth() - temp.getIntrinsicWidth()) / 2,
                        0,
                        temp.getIntrinsicWidth() - (getMeasuredWidth() - temp.getIntrinsicWidth()) / 2,
                        temp.getIntrinsicHeight());

            } else if (mDrawableGravity == Gravity.CENTER) {

                temp.setBounds(0, 0, temp.getIntrinsicWidth(), temp.getIntrinsicHeight());

            } else {
                temp.setBounds(
                        (getMeasuredWidth() - temp.getIntrinsicWidth()) / 2,
                        0,
                        (getMeasuredWidth() - temp.getIntrinsicWidth()) / 2 + temp.getIntrinsicWidth(),
                        temp.getIntrinsicHeight());
            }
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        //Here execute setDrawable method,
        // because now we can get a nonzero width

        //you may have a question that why use a tag.
        //actually, i want the setDrawable method execute only once at first
        if(!(Boolean) getTag(-1)) {
            setDrawable();
            setTag(-1, true);
        }
    }

    public boolean isExpanded(){
        return mState;
    }

    public int getAnimDuration() {
        return mAnimDuration;
    }

    public void setAnimDuration(int mAnimDuration) {
        this.mAnimDuration = mAnimDuration;
    }

    /**
     * Gravity.LEFT left
     * Gravity.CENTER center
     * Gravity.RIGHT right
     * @param drawableGravity
     */
    public void setDrawableGravity(int drawableGravity){
        mDrawableGravity = drawableGravity;
    }

    public Drawable getExpandDrawable() {
        return mExpandDrawable;
    }

    public void setExpandDrawable(@Nullable Drawable mExpandDrawable) {
        this.mExpandDrawable = mExpandDrawable;
    }

    public void setExpandDrawable(int resId) {
        this.mExpandDrawable = ContextCompat.getDrawable(getContext(), resId);
    }

    public Drawable getCollapseDrawable() {
        return mCollapseDrawable;
    }

    public void setCollapseDrawable(@Nullable Drawable mCollapseDrawable) {
        this.mCollapseDrawable = mCollapseDrawable;
}

    public void setCollapseDrawable(int resId) {
        this.mCollapseDrawable = ContextCompat.getDrawable(getContext(), resId);
    }

    public void setOnExpandStateChangeListener(@Nullable OnExpandStateChangeListener mListener) {
        this.mListener = mListener;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        //do release
        if(mExpandDrawable != null){
            mExpandDrawable = null;
        }

        if(mCollapseDrawable != null){
            mCollapseDrawable = null;
        }

        if(animation != null){
            animation.cancel();
            animation = null;
        }
    }

    public interface OnExpandStateChangeListener {

        void onExpandStateChanged(ExpandableTextView textView, boolean isExpanded);
    }


    /**
     * Create an inner class to perform animation
     */
    class ExpandCollapseAnimation extends Animation {
        private final View mTargetView;
        private final int mStartHeight;
        private final int mEndHeight;

        public ExpandCollapseAnimation(View view, int startHeight, int endHeight) {
            mTargetView = view;
            mStartHeight = startHeight;
            mEndHeight = endHeight;
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {

            final int newHeight = (int)((mEndHeight - mStartHeight) * interpolatedTime + mStartHeight);
            mTargetView.getLayoutParams().height = newHeight;
            mTargetView.requestLayout();
           /// Log.e(TAG, "animation height : " + newHeight);
        }

        @Override
        public boolean willChangeBounds( ) {
            return true;
        }
    };

}
