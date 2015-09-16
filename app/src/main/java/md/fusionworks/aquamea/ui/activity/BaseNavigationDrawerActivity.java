package md.fusionworks.aquamea.ui.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import md.fusionworks.aquamea.R;
import md.fusionworks.aquamea.util.UIUtils;

/**
 * Created by admin on 03.09.2015.
 */
public class BaseNavigationDrawerActivity extends BaseActivity {

    protected static final int DRAWER_ITEM_MAP = 0;
    protected static final int DRAWER_ITEM_HEALTH = 1;
    protected static final int DRAWER_ITEM_TREATMENT = 2;
    protected static final int DRAWER_ITEM_SETTINGS = 3;
    protected static final int DRAWER_ITEM_INVALID = -1;
    protected static final int DRAWER_ITEM_SEPARATOR = -2;

    private static final int DRAWER_LAUNCH_DELAY = 250;

    private static final int[] DRAWER_TITLE_RES_ID = new int[]{

            R.string.drawer_item_map,
            R.string.drawer_item_health,
            R.string.drawer_item_treatment
    };

    private static final int[] DRAWER_ICON_RES_ID = new int[]{

            R.drawable.ic_water_black_24dp,
            R.drawable.ic_water_black_24dp,
            R.drawable.ic_water_black_24dp
    };

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.drawerLayout)
    DrawerLayout drawerLayout;
    @Bind(R.id.drawer_items_list)
    ViewGroup drawerItemsListContainer;

    private ArrayList<Integer> drawerItems = new ArrayList<Integer>();
    private View[] drawerItemViews = null;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        handler = new Handler();
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setupDrawerLayout();
    }

    private void setupDrawerLayout() {

        int selfItem = getSelfDrawerItem();

        if (drawerLayout == null) {
            return;
        }

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.app_name, R.string.app_name);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        actionBarDrawerToggle.syncState();

        drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        populateDrawerItems();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    protected void setTitle(String title) {

        getSupportActionBar().setTitle(title);
    }

    private void populateDrawerItems() {

        drawerItems.add(DRAWER_ITEM_MAP);
        drawerItems.add(DRAWER_ITEM_HEALTH);
        drawerItems.add(DRAWER_ITEM_TREATMENT);

        createDrawerItems();
    }

    private void createDrawerItems() {

        if (drawerItemsListContainer == null) {
            return;
        }

        drawerItemViews = new View[drawerItems.size()];
        drawerItemsListContainer.removeAllViews();
        int i = 0;
        for (int itemId : drawerItems) {
            drawerItemViews[i] = makeDrawerItem(itemId, drawerItemsListContainer);
            drawerItemsListContainer.addView(drawerItemViews[i]);
            ++i;
        }
    }

    private View makeDrawerItem(final int itemId, ViewGroup container) {
        boolean selected = getSelfDrawerItem() == itemId;
        int layoutToInflate = 0;
        if (itemId == DRAWER_ITEM_SEPARATOR) {
            layoutToInflate = R.layout.navdrawer_separator;
        } else {
            layoutToInflate = R.layout.navdrawer_item;
        }
        View view = getLayoutInflater().inflate(layoutToInflate, container, false);

        if (isSeparator(itemId)) {

            UIUtils.setAccessibilityIgnore(view);
            return view;
        }

        ImageView iconView = (ImageView) view.findViewById(R.id.icon);
        TextView titleView = (TextView) view.findViewById(R.id.title);
        int iconId = itemId >= 0 && itemId < DRAWER_ICON_RES_ID.length ?
                DRAWER_ICON_RES_ID[itemId] : 0;
        int titleId = itemId >= 0 && itemId < DRAWER_TITLE_RES_ID.length ?
                DRAWER_TITLE_RES_ID[itemId] : 0;

        iconView.setVisibility(iconId > 0 ? View.VISIBLE : View.GONE);
        if (iconId > 0) {
            iconView.setImageResource(iconId);
        }
        titleView.setText(getString(titleId));

        formatDrawerItem(view, itemId, selected);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDrawerItemClicked(itemId);
            }
        });

        return view;
    }

    protected int getSelfDrawerItem() {
        return DRAWER_ITEM_INVALID;
    }

    private boolean isSeparator(int itemId) {
        return itemId == DRAWER_ITEM_SEPARATOR;
    }

    private boolean isSimpleActivity(int itemId) {
        return itemId == DRAWER_ITEM_SETTINGS;
    }

    private void formatDrawerItem(View view, int itemId, boolean selected) {
        if (isSeparator(itemId)) {
            // not applicable
            return;
        }

        ImageView iconView = (ImageView) view.findViewById(R.id.icon);
        TextView titleView = (TextView) view.findViewById(R.id.title);

        if (selected) {
            view.setBackgroundResource(R.drawable.selected_navdrawer_item_background);
        }

        titleView.setTextColor(selected ?
                getResources().getColor(R.color.navdrawer_text_color_selected) :
                getResources().getColor(R.color.navdrawer_text_color));
        iconView.setColorFilter(selected ?
                getResources().getColor(R.color.navdrawer_icon_tint_selected) :
                getResources().getColor(R.color.navdrawer_icon_tint));
    }

    private void onDrawerItemClicked(final int itemId) {
        if (itemId == getSelfDrawerItem()) {
            drawerLayout.closeDrawer(GravityCompat.START);
            return;
        }

        if (isSimpleActivity(itemId)) {
            goToDrawerItem(itemId);
        } else {

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    goToDrawerItem(itemId);
                }
            }, DRAWER_LAUNCH_DELAY);

            setSelectedDrawerItem(itemId);
        }

        drawerLayout.closeDrawer(GravityCompat.START);
    }

    private void goToDrawerItem(int item) {
        Intent intent;
        switch (item) {
            case DRAWER_ITEM_MAP:
                intent = new Intent(this, MapActivity.class);
                startActivity(intent);
                finish();
                break;
            case DRAWER_ITEM_HEALTH:
                intent = new Intent(this, HealthActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    private void setSelectedDrawerItem(int itemId) {

        if (drawerItemViews != null) {
            for (int i = 0; i < drawerItemViews.length; i++) {
                if (i < drawerItems.size()) {
                    int thisItemId = drawerItems.get(i);
                    formatDrawerItem(drawerItemViews[i], thisItemId, itemId == thisItemId);
                }
            }
        }
    }

}
