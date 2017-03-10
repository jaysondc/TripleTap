package com.shakeup.setofthree.CustomView;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.shakeup.setofthree.R;

/**
 * Created by Jayson on 3/6/2017.
 * <p>
 * This view is able to display cards to be used in Set.
 * It's editable from the layout editor should be editable during runtime
 * <p>
 * The card will always be 2:3 aspect ratio. Orientation can be set but defaults to vertical.
 */

public class SetGameCard extends CardView {

    // Width and height aspect ratio. Defaults to 2:3
    private int mAspectRatioWidth;
    private int mAspectRatioHeight;

    /**
     * Set Card attributes. Defaults to 0:0:0:0
     * which is Oval:Red:One:Solid
     */
    private int mShape;
    private int mColor;
    private int mCount;
    private int mFill;

    /**
     * Keep an reference to the view's context
     */
    private Context mContext;

    public SetGameCard(Context context) {
        super(context);
    }

    /**
     * Constructor used most often by layout editor. Attributes are passed through attrs,
     * otherwise the SetCard is initialized with defaults.
     *
     * @param context Context containing our view
     * @param attrs   Attributes passed from constructing code
     */
    public SetGameCard(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init(context, attrs);
    }

    public SetGameCard(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        init(context, attrs);
    }

    /**
     * Initialize our card with passed in attributes.
     *
     * @param context Context our view is in
     * @param attrs   Attributes passed into the constructor from code or the layout xml
     */
    private void init(Context context, AttributeSet attrs) {

        /**
         * Get custom attributes
         */
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.SetGameCard);

        try {
            mAspectRatioWidth = attributes.getInt(R.styleable.SetGameCard_aspectWidth, 2);
            mAspectRatioHeight = attributes.getInt(R.styleable.SetGameCard_aspectHeight, 3);

            // Defaults to Oval:Red:One:Solid if not set
            mShape = attributes.getInt(R.styleable.SetGameCard_symbol_shape, 0);
            mColor = attributes.getInt(R.styleable.SetGameCard_symbol_color, 0);
            mCount = attributes.getInt(R.styleable.SetGameCard_symbol_count, 0);
            mFill = attributes.getInt(R.styleable.SetGameCard_symbol_fill, 0);
        } finally {
            attributes.recycle();
        }

        /**
         * Registers a listener to fire once the initial layout pass is done so we can
         * add a child LinearLayout and ImageView and have access to their measure height and width
         */
        final FrameLayout view = this; // Create a final reference to this view
        final Context myContext = context; // Create a final reference to this context
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // Immediately unregister the listener so it doesn't fire repeatedly
                view.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                /**
                 * Add a LinearLayout to the card's frame layout
                 */
                LinearLayout linearLayout = new LinearLayout(myContext);
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                linearLayout.setGravity(Gravity.CENTER);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                );
                linearLayout.setLayoutParams(layoutParams);

                // Add the blank LinearLayout to our card
                view.addView(linearLayout);

                // Add an image to the LinearLayout in the card
                addImage(myContext, linearLayout);
                addImage(myContext, linearLayout);
                addImage(myContext, linearLayout);
            }
        });
    }

    /**
     * Adds the appropriate symbol image to the center of the card. This will takes the
     * class attributes and displays the image with the correct shape, color, count, and fill.
     *
     * @param context Context of our view
     */
    private void addImage(Context context, LinearLayout linearLayout) {

        ImageView symbolView = new ImageView(context);

        // Set the height of the image to 1/2 the card height
        int imageHeight = getHeight() / 2;
        int imageWidth = imageHeight / 2;
        LayoutParams params = new LayoutParams(
                imageWidth,
                imageHeight
        );
        params.setMarginEnd(5);
        params.setMarginStart(5);
        symbolView.setLayoutParams(params);

        // Set placeholder image
        symbolView.setImageDrawable(context.getDrawable(R.drawable.ic_set_icons_squiggle_open));

        linearLayout.addView(symbolView, 0);
    }


    /**
     * Custom aspect ratios have proven difficult to work right. Implement feature later.
     */
//    /**
//     * When the view is measured modify it's height to match the aspect ratio
//     * specified in the attributes.
//     * @param widthMeasureSpec The horizontal space requirements imposed by the parent
//     * @param heightMeasureSpec The vertical space requirements imposed by the parent
//     */
//    @Override protected void onMeasure (int widthMeasureSpec, int heightMeasureSpec)
//    {
//        int finalWidth, finalHeight;
//
//        // Get original height and width
//        int originalWidth = MeasureSpec.getSize(widthMeasureSpec);
//        int originalHeight = MeasureSpec.getSize(heightMeasureSpec);
//
//        if ( mAspectRatioWidth > mAspectRatioHeight ){
//            // Scale height based on width
//            finalHeight = originalWidth * mAspectRatioHeight / mAspectRatioWidth;
//            finalWidth = originalWidth;
//        } else if ( mAspectRatioWidth < mAspectRatioHeight ){
//            // Scale width based on height
//            finalWidth = originalHeight * mAspectRatioHeight / mAspectRatioWidth;
//            finalHeight = originalHeight;
//        } else {
//            finalWidth = originalWidth;
//            finalHeight = originalHeight;
//        }
//
//        super.onMeasure(
//                MeasureSpec.makeMeasureSpec(finalWidth, MeasureSpec.EXACTLY),
//                MeasureSpec.makeMeasureSpec(finalHeight, MeasureSpec.EXACTLY));
//    }

    /**
     * Setters and getters for custom attributes
     */
//    public int getAspectRatioWidth() {
//        return mAspectRatioWidth;
//    }
//
//    public void setAspectRatioWidth(int mAspectRatioWidth) {
//        this.mAspectRatioWidth = mAspectRatioWidth;
//        invalidate();
//        requestLayout();
//    }
//
//    public int getAspectRatioHeight() {
//        return mAspectRatioHeight;
//    }
//
//    public void setAspectRatioHeight(int mAspectRatioHeight) {
//        this.mAspectRatioHeight = mAspectRatioHeight;
//        invalidate();
//        requestLayout();    }
    public int getShape() {
        return mShape;
    }

    public void setShape(int mShape) {
        this.mShape = mShape;
        invalidate();
        requestLayout();
    }

    public int getColor() {
        return mColor;
    }

    public void setColor(int mColor) {
        this.mColor = mColor;
        invalidate();
        requestLayout();
    }

    public int getCount() {
        return mCount;
    }

    public void setCount(int mCount) {
        this.mCount = mCount;
        invalidate();
        requestLayout();
    }

    public int getFill() {
        return mFill;
    }

    public void setFill(int mFill) {
        this.mFill = mFill;
        invalidate();
        requestLayout();
    }
}
