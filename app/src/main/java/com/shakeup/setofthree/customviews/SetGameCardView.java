package com.shakeup.setofthree.customviews;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.shakeup.setgamelibrary.enums.CardColor;
import com.shakeup.setgamelibrary.enums.CardCount;
import com.shakeup.setgamelibrary.enums.CardFill;
import com.shakeup.setgamelibrary.enums.CardShape;
import com.shakeup.setofthree.R;

/**
 * Created by Jayson on 3/6/2017.
 * <p>
 * This view is able to display cards to be used in Set.
 * It's editable from the layout editor should be editable during runtime
 * <p>
 */

public class SetGameCardView extends CardView {

    /*
     * Constants to map to Enum attributes
     */
    // SHAPE
    private static final int OVAL = 0;
    private static final int DIAMOND = 1;
    private static final int SQUIGGLE = 2;
    // COLOR
    private static final int RED = 0;
    private static final int GREEN = 1;
    private static final int PURPLE = 2;
    // COUNT
    private static final int ONE = 0;
    private static final int TWO = 1;
    private static final int THREE = 2;
    // FILL
    private static final int SOLID = 0;
    private static final int OPEN = 1;
    private static final int STRIPE = 2;
    // Width and height aspect ratio. Defaults to 2:3
    private int mAspectRatioWidth;
    private int mAspectRatioHeight;
    /*
     * Set Card attributes. Defaults to 0:0:0:0
     * which is Oval:Red:One:Solid
     */
    private int mShape;
    private int mColor;
    private int mCount;
    private int mFill;
    private int[][] mShapeFill = new int[3][3];

    /*
     * Keep an reference to the view's and attributes
     */
    private Context mContext;
    private AttributeSet mAttrs;
    private LinearLayout mLinearLayout;

    /*
     * Member variables for other state info
     */
    private boolean mIsChecked;
    private boolean mIsHighlighted;


    public SetGameCardView(Context context) {
        super(context);
    }

    /**
     * Constructor used most often by layout editor. Attributes are passed through attrs,
     * otherwise the SetCard is initialized with defaults.
     *
     * @param context Context containing our view
     * @param attrs   Attributes passed from constructing code
     */
    public SetGameCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mAttrs = attrs;
        init(context, attrs);
    }

    public SetGameCardView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        mAttrs = attrs;
        init(context, attrs);
    }


    /**
     * Initialize our card with passed in attributes.
     *
     * @param context Context our view is in
     * @param attrs   Attributes passed into the constructor from code or the layout xml
     */
    private void init(Context context, AttributeSet attrs) {

        // Get custom attributes
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.SetGameCardView);

        // Set some defaults
        mIsChecked = false;
        mIsHighlighted = false;

        try {
            mAspectRatioWidth = attributes.getInt(R.styleable.SetGameCardView_aspectWidth, 2);
            mAspectRatioHeight = attributes.getInt(R.styleable.SetGameCardView_aspectHeight, 3);

            // Defaults to Oval:Red:One:Solid if not set
            mShape = attributes.getInt(R.styleable.SetGameCardView_symbol_shape, 0);
            mColor = attributes.getInt(R.styleable.SetGameCardView_symbol_color, 0);
            mCount = attributes.getInt(R.styleable.SetGameCardView_symbol_count, 0);
            mFill = attributes.getInt(R.styleable.SetGameCardView_symbol_fill, 0);
        } finally {
            attributes.recycle();
        }

        /*
         * Set up 2D array to get the correct drawable based on shape and fill
         * +--------------------------------------------------+
         * | oval_solid     | oval_open     | oval_stripe     |
         * | diamond_solid  | diamond_open  | diamond_stripe  |
         * | squiggle_solid | squiggle_open | squiggle_stripe |
         * +--------------------------------------------------+
         */
        mShapeFill[0][0] = R.drawable.ic_set_icons_oval_solid;
        mShapeFill[0][1] = R.drawable.ic_set_icons_oval_open;
        mShapeFill[0][2] = R.drawable.ic_set_icons_oval_stripe;
        mShapeFill[1][0] = R.drawable.ic_set_icons_diamond_solid;
        mShapeFill[1][1] = R.drawable.ic_set_icons_diamond_open;
        mShapeFill[1][2] = R.drawable.ic_set_icons_diamond_stripe;
        mShapeFill[2][0] = R.drawable.ic_set_icons_squiggle_solid;
        mShapeFill[2][1] = R.drawable.ic_set_icons_squiggle_open;
        mShapeFill[2][2] = R.drawable.ic_set_icons_squiggle_stripe;

        /*
         * Add a LinearLayout to the card's frame layout to hold the symbols
         */
        mLinearLayout = new LinearLayout(mContext);
        mLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        mLinearLayout.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        mLinearLayout.setLayoutParams(layoutParams);
        this.setCardBackgroundColor(
                ContextCompat.getColor(getContext(), R.color.card_background_normal));
        this.addView(mLinearLayout);

        drawSymbols();
    }

    /**
     * Draws the appropriate symbols on the card. This should be called whenever
     * the symbol attributes change and need to be redrawn
     */
    private void drawSymbols() {

        /*
         * Registers a listener to fire once the initial layout pass is done so we can
         * add a child LinearLayout and ImageView and have access to their measure height and width
         */
        final FrameLayout view = this; // Create a final reference to this view
        final Context myContext = mContext; // Create a final reference to this context
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // Immediately unregister the listener so it doesn't fire repeatedly
                view.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                // Remove any existing views from the LinearLayout we're replacing them
                mLinearLayout.removeAllViews();

                // Add an image to the LinearLayout in the card
                switch (mCount) {
                    case ONE:
                        addImage(myContext, mLinearLayout);
                        break;
                    case TWO:
                        addImage(myContext, mLinearLayout);
                        addImage(myContext, mLinearLayout);
                        break;
                    case THREE:
                        addImage(myContext, mLinearLayout);
                        addImage(myContext, mLinearLayout);
                        addImage(myContext, mLinearLayout);
                        break;
                }
            }
        });

        // Set content descriptions for each card
        this.setContentDescription(this.toString());

        invalidate();
    }

    /**
     * Adds the appropriate symbol to the center of the card. This takes the
     * class attributes and displays the image with the correct shape, color, and fill
     * and can be called multiple times to display the correct count of symbols.
     *
     * @param context Context of our view
     */
    private void addImage(Context context, LinearLayout linearLayout) {

        ImageView symbolView = new ImageView(context);

        /*
         * Turn off hardware acceleration for this View otherwise
         * the colors don't get drawn correctly
         */
        symbolView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);


        // Set the height of the image to 1/2 the card height
        int imageHeight = (int) Math.floor(getHeight() * 0.7);
        int imageWidth = imageHeight / 2;
        LayoutParams params = new LayoutParams(
                imageWidth,
                imageHeight
        );
        params.setMarginEnd(10);
        params.setMarginStart(10);
        symbolView.setLayoutParams(params);

        // Get drawable resource from ShapeFill array
        Drawable symbol = context.getDrawable(mShapeFill[mShape][mFill]);

        // Set symbol color based on defaults or user preferences
        setSymbolColor(symbol);

        // Set the drawable to the imageview
        symbolView.setImageDrawable(symbol);

        // Add the completed imageview to the layout
        linearLayout.addView(symbolView, 0);
    }


    /**
     * Tints the symbol drawable based on default colors or user preferences if set.
     * Set default colors for now. Eventually this will be the users preference colors
     *
     * @param symbol The drawable to be tinted
     */
    private void setSymbolColor(Drawable symbol) {
        switch (mColor) {
            case RED:
                symbol.setTint(ContextCompat.getColor(mContext, R.color.set_red));
                break;
            case GREEN:
                symbol.setTint(ContextCompat.getColor(mContext, R.color.set_green));
                break;
            case PURPLE:
                symbol.setTint(ContextCompat.getColor(mContext, R.color.set_purple));
                break;
        }
    }

    public boolean isChecked() {
        return mIsChecked;
    }

    // IMPLEMENTATION FOR CHECKABLE INTERFACE //

    /**
     * Sets the card to a checked state. The view will be marked as activated and the background will
     * animate to the checked color if specified.
     * @param checked is the value to set the 'checked' and 'activated' flags
     * @param animate is whether or not to animate the background color change
     */
    public void setChecked(boolean checked, boolean animate) {
        mIsChecked = checked;
        if (mIsChecked) { // Check
            if (animate) animateColorChange(
                    R.color.card_background_normal,
                    R.color.card_background_selected);
            this.setActivated(true);
        } else { // Uncheck, highlight again if highlighted
            if (!isHighlighted()) {
                if (animate) animateColorChange(
                        R.color.card_background_selected,
                        R.color.card_background_normal);
            } else {
                if (animate) animateColorChange(
                        R.color.card_background_selected,
                        R.color.card_background_highlighted);
            }
            this.setActivated(false);
        }
        invalidate();
    }

    public void toggleChecked() {
        if (isChecked()) {
            setChecked(false, true);
        } else {
            setChecked(true, true);
        }
    }

    public boolean isHighlighted() {
        return mIsHighlighted;
    }

    // Implement Highlighted tag to highlight cards as hints//
    public void setHighlighted(boolean highlighted) {
        if (!mIsHighlighted && highlighted) {
            animateColorChange(R.color.card_background_normal, R.color.card_background_highlighted);
        } else if (mIsHighlighted && !highlighted){
            animateColorChange(R.color.card_background_highlighted, R.color.card_background_normal);
        }
        mIsHighlighted = highlighted;
        setSelected(highlighted);
        invalidate();
    }

    public void toggleHighlighted() {
        if (isHighlighted()) {
            setHighlighted(false);
        } else {
            setHighlighted(true);
        }
    }

    /**
     * Setters and getters for custom attributes
     */
    public int getShape() {
        return mShape;
    }

    public void setShape(int mShape) {
        this.mShape = mShape;
        drawSymbols();
    }

    public int getColor() {
        return mColor;
    }

    public void setColor(int mColor) {
        this.mColor = mColor;
        drawSymbols();
    }

    public int getCount() {
        return mCount;
    }

    public void setCount(int mCount) {
        this.mCount = mCount;
        drawSymbols();
    }

    public int getFill() {
        return mFill;
    }

    public void setFill(int mFill) {
        this.mFill = mFill;
        drawSymbols();
    }

    /**
     * Public method to set all SetCard attributes at once
     *
     * @param shape
     * @param color
     * @param count
     * @param fill
     */
    public void setAll(int shape, int color, int count, int fill) {
        this.mShape = shape;
        this.mColor = color;
        this.mCount = count;
        this.mFill = fill;
        drawSymbols();
    }

    /**
     * Returns the string representation of the card
     *
     * @return String representation of the current card
     */
    public String toString() {

        String shape = CardShape.values()[this.getShape()].toString();
        String color = CardColor.values()[this.getColor()].toString();
        String count = CardCount.values()[this.getCount()].toString();
        String fill = CardFill.values()[this.getFill()].toString();

        return "[" + shape + "," + color + "," + count + "," + fill + "]";
    }

    /**
     * Change card color. This method wraps the Property Animation API mentioned here
     * https://stackoverflow.com/a/14467625/7009268
     */
    public void animateColorChange(int colorFromId, int colorToId) {
        int colorFrom = ContextCompat.getColor(getContext(), colorFromId);
        int colorTo = ContextCompat.getColor(getContext(), colorToId);
        final SetGameCardView card = this;

        int duration = getContext().getResources().getInteger(R.integer.card_animation_duration);

        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimation.setDuration(duration); // milliseconds
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                card.setCardBackgroundColor((int) animator.getAnimatedValue());
            }
        });
        colorAnimation.start();
    }

    /**
     * Change card color. This method wraps the Property Animation API mentioned here
     * https://stackoverflow.com/a/14467625/7009268
     */
    public void animateFailedSet() {
        int colorFrom = ContextCompat.getColor(getContext(), R.color.card_background_normal);
        int colorTo = ContextCompat.getColor(getContext(), R.color.fbutton_color_carrot);
        final SetGameCardView card = this;

        int duration = getContext().getResources().getInteger(R.integer.card_fail_animation_duration_flash);

        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimation.setDuration(duration); // milliseconds
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                card.setCardBackgroundColor((int) animator.getAnimatedValue());
            }
        });
        colorAnimation.setRepeatMode(ValueAnimator.REVERSE);
        colorAnimation.setRepeatCount(2);
        colorAnimation.start();

        colorAnimation.addListener(new AnimatorListenerAdapter()
        {
            @Override
            public void onAnimationEnd(Animator animation)
            {
                // Once animation is over, animate back to selected or highlighted or normal
                card.setChecked(false, false);
                if (isHighlighted()) {
                    animateColorChange(R.color.fbutton_color_alizarin, R.color.card_background_highlighted);
                } else {
                    animateColorChange(R.color.fbutton_color_alizarin, R.color.card_background_normal);
                }
            }
        });
    }

    /**
     * Change card color. This method wraps the Property Animation API mentioned here
     * https://stackoverflow.com/a/14467625/7009268
     */
    public void animateSuccessfulSet(final AnimationEndCallback callback) {
        int colorFrom = ContextCompat.getColor(getContext(), R.color.card_background_normal);
        int colorTo = ContextCompat.getColor(getContext(), R.color.fbutton_color_nephritis);
        final SetGameCardView card = this;

        int duration = getContext().getResources().getInteger(R.integer.card_success_animation_duration);

        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimation.setDuration(duration); // milliseconds
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                card.setCardBackgroundColor((int) animator.getAnimatedValue());
            }
        });

        colorAnimation.start();

        colorAnimation.addListener(new AnimatorListenerAdapter()
        {
            @Override
            public void onAnimationEnd(Animator animation)
            {
                card.setChecked(false, true);
                // Notify our calling method that the animation is over
                if (callback != null) {
                    callback.onAnimationFinish();
                }
            }
        });
    }

    /**
     * Interface to be notified when an animation is finished
     */
    public interface AnimationEndCallback {
        void onAnimationFinish();
    }
}
