package md.fusionworks.aquamea.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

import md.fusionworks.aquamea.R;
import md.fusionworks.aquamea.util.TypefaceCache;

/**
 * Created by ungvas on 9/14/15.
 */
public class TypefaceEditText extends EditText {

    public TypefaceEditText(Context context) {
        super(context);
    }

    public TypefaceEditText(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.TypefaceTextView, 0, 0);
        try {
            String font = ta.getString(R.styleable.TypefaceTextView_typeface);
            Typeface typeface = TypefaceCache.getTypeface(context, font);
            if (null != typeface) {
                setTypeface(typeface);
            }

        } finally {
            ta.recycle();
        }
    }

    public TypefaceEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.TypefaceTextView, 0, 0);
        try {
            String font = ta.getString(R.styleable.TypefaceTextView_typeface);
            Typeface typeface = TypefaceCache.getTypeface(context, font);
            if (null != typeface) {
                setTypeface(typeface);
            }

        } finally {
            ta.recycle();
        }
    }
}
