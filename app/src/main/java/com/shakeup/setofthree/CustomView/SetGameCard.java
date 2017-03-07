package com.shakeup.setofthree.CustomView;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;

import com.shakeup.setofthree.R;

/**
 * Created by Jayson on 3/6/2017.
 *
 * This view is able to display cards to be used in Set.
 * It's editable from the layout editor should be editable during runtime
 *
 * The card will always be 2:3 aspect ratio. Orientation can be set but defaults to vertical.
 */

public class SetGameCard extends CardView{

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

    public SetGameCard(Context context){
        super(context);
    }

    /**
     * Constructor used most often by layout editor. Attributes are passed through attrs,
     * otherwise the SetCard is initialized with defaults.
     * @param context Context containing our view
     * @param attrs Attributes passed from constructing code
     */
    public SetGameCard(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context, attrs);
    }

    public SetGameCard(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);

        init(context, attrs);
    }

    /**
     * Initialize our card with passed in attributes.
     * @param context
     * @param attrs
     */
    private void init(Context context, AttributeSet attrs)
    {
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.SetGameCard);

        mAspectRatioWidth = attributes.getInt(R.styleable.SetGameCard_aspectWidth, 2);
        mAspectRatioHeight = attributes.getInt(R.styleable.SetGameCard_aspectHeight, 3);

        // Defaults to Oval:Red:One:Solid if not set
        mShape = attributes.getInt(R.styleable.SetGameCard_symbol_shape, 0);
        mColor = attributes.getInt(R.styleable.SetGameCard_symbol_color, 0);
        mCount = attributes.getInt(R.styleable.SetGameCard_symbol_count, 0);
        mFill = attributes.getInt(R.styleable.SetGameCard_symbol_fill, 0);

        attributes.recycle();
    }



    /**
     * When the view is measured modify it's width and height to match the aspect ratio
     * specified in the attributes
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override protected void onMeasure (int widthMeasureSpec, int heightMeasureSpec)
    {
        int originalWidth = MeasureSpec.getSize(widthMeasureSpec);

        int originalHeight = MeasureSpec.getSize(heightMeasureSpec);

        int calculatedHeight = originalWidth * mAspectRatioHeight / mAspectRatioWidth;

        int finalWidth, finalHeight;

        if (calculatedHeight > originalHeight)
        {
            finalWidth = originalHeight * mAspectRatioWidth / mAspectRatioHeight;
            finalHeight = originalHeight;
        }
        else
        {
            finalWidth = originalWidth;
            finalHeight = calculatedHeight;
        }

        // Pass back the modified width and height.
        super.onMeasure(
                MeasureSpec.makeMeasureSpec(finalWidth, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(finalHeight, MeasureSpec.EXACTLY));
    }

}
