package md.fusionworks.apapura.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import md.fusionworks.apapura.R;

/**
 * Created by ungvas on 9/23/15.
 */
public class MapLegendView extends LinearLayout implements View.OnClickListener {

    @Bind(R.id.legendInfoLayout)
    View legendInfoLayout;
    @Bind(R.id.hideShowLegendInfoLayout)
    View hideShowLegendInfoLayout;
    @Bind(R.id.hideShowLegendInfoField)
    TextView hideShowLegendInfoField;

    public MapLegendView(Context context, AttributeSet attrs) {
        super(context, attrs);

        inflate(context, R.layout.map_legend_layout, this);
        ButterKnife.bind(this);

        hideLegend();

        hideShowLegendInfoLayout.setOnClickListener(this);
    }

    public void showLegend() {

        legendInfoLayout.setVisibility(VISIBLE);
        hideShowLegendInfoField.setText(R.string.action_hide);
    }

    public void hideLegend() {

        legendInfoLayout.setVisibility(GONE);
        hideShowLegendInfoField.setText(R.string.action_show_legend);
    }

    public boolean isLegendShowed() {

        return legendInfoLayout.getVisibility() == VISIBLE;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.hideShowLegendInfoLayout:

                if (isLegendShowed())
                    hideLegend();
                else
                    showLegend();
                break;
        }
    }
}
