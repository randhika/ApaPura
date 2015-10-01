package md.fusionworks.apapura.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Checkable;
import android.widget.FrameLayout;
import android.widget.ImageView;

import butterknife.Bind;
import butterknife.ButterKnife;
import md.fusionworks.apapura.R;

/**
 * Created by ungvas on 10/1/15.
 */
public class MapTypeSatelliteSwitcher extends FrameLayout implements Checkable, View.OnClickListener {

    @Bind(R.id.satelliteLayout)
    View satelliteLayout;
    @Bind(R.id.satelliteIcon)
    ImageView satelliteIcon;

    private OnCheckedChangeListener onCheckedChangeListener;
    private boolean isChecked = false;

    public MapTypeSatelliteSwitcher(Context context, AttributeSet attrs) {
        super(context, attrs);

        inflate(context, R.layout.map_type_satellite_switcher_layout, this);
        ButterKnife.bind(this);

        setChecked(false);

        satelliteLayout.setOnClickListener(this);
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener onCheckedChangeListener) {

        this.onCheckedChangeListener = onCheckedChangeListener;
    }

    @Override
    public void setChecked(boolean isChecked) {

        this.isChecked = isChecked;
        satelliteIcon.setColorFilter(isChecked ? getResources().getColor(R.color.primary) : getResources().getColor(R.color.secondary_text));
        if (onCheckedChangeListener != null)
            onCheckedChangeListener.onCheckedChanged(isChecked);
    }

    @Override
    public boolean isChecked() {

        return isChecked;
    }

    @Override
    public void toggle() {

        setChecked(!isChecked);
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.satelliteLayout) {

            toggle();
        }
    }

    public interface OnCheckedChangeListener {

        void onCheckedChanged(boolean isChecked);
    }
}
