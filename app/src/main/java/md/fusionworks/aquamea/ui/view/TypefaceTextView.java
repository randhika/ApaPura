package md.fusionworks.aquamea.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import md.fusionworks.aquamea.R;
import md.fusionworks.aquamea.util.TypefaceCache;

public class TypefaceTextView extends TextView
{

    public TypefaceTextView(Context context) {
        this(context, null);
    }

    public TypefaceTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TypefaceTextView(Context context, AttributeSet attrs, int defStyle) {
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