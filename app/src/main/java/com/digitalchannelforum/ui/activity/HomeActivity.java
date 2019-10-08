package com.digitalchannelforum.ui.activity;


import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.digitalchannelforum.drupalcon.R;
import com.digitalchannelforum.drupalcon.app.App;
import com.digitalchannelforum.drupalcon.model.Model;
import com.digitalchannelforum.drupalcon.model.UpdateRequest;
import com.digitalchannelforum.drupalcon.model.UpdatesManager;
import com.digitalchannelforum.drupalcon.model.data.Level;
import com.digitalchannelforum.drupalcon.model.data.Track;
import com.digitalchannelforum.drupalcon.model.managers.SharedScheduleManager;
import com.digitalchannelforum.drupalcon.model.managers.TracksManager;
import com.digitalchannelforum.network.BackendMethods;
import com.digitalchannelforum.ui.adapter.item.EventListItem;
import com.digitalchannelforum.ui.dialog.FilterDialog;
import com.digitalchannelforum.ui.dialog.IrrelevantTimezoneDialogFragment;
import com.digitalchannelforum.ui.drawer.DrawerAdapter;
import com.digitalchannelforum.ui.drawer.DrawerManager;
import com.digitalchannelforum.ui.drawer.DrawerMenu;
import com.digitalchannelforum.ui.drawer.DrawerMenuItem;
import com.digitalchannelforum.utils.AnalyticsManager;
import com.digitalchannelforum.utils.L;
import com.digitalchannelforum.utils.ScheduleManager;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.app.NotificationCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HomeActivity extends StateActivity implements FilterDialog.OnFilterApplied {
    private static final String NAVIGATE_TO_SCHEDULE_EXTRA_KEY = "navigate_to_schedule_extra_key";
    private DrawerManager mFrManager;
    private DrawerAdapter mAdapter;
    private int mPresentTitle;
    private int mSelectedItem = 0;
    private boolean isIntentHandled = false;

    private DrawerLayout mDrawerLayout;

    public FilterDialog mFilterDialog;
    public boolean mIsDrawerItemClicked;

    private UpdatesManager.DataUpdatedListener updateReceiver = new UpdatesManager.DataUpdatedListener() {
        @Override
        public void onDataUpdated(List<UpdateRequest> requests) {
            initFilterDialog();
        }
    };

    public static void startThisActivity(Activity activity, long scheduleCode) {
        Intent intent = new Intent(activity, HomeActivity.class);
        intent.putExtra(NAVIGATE_TO_SCHEDULE_EXTRA_KEY, scheduleCode);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null){
            SplashActivity.startThisActivity(this);
        }
        setContentView(R.layout.ac_main);
        Model.instance().getUpdatesManager().registerUpdateListener(updateReceiver);

        long code = getIntent().getLongExtra(NAVIGATE_TO_SCHEDULE_EXTRA_KEY, SharedScheduleManager.MY_DEFAULT_SCHEDULE_CODE);
        L.e("Navigate code = " + code);
        initToolbar();

        initNavigationDrawerList();
        initFilterDialog();

        initFragmentManager(code);
        if (getIntent().getExtras() != null) {
            isIntentHandled = true;
        }
        handleIntent(getIntent());
        //showIrrelevantTimezoneDialogIfNeeded();

        checkNewPush();
    }
    public void checkNewPush(){
        BackendMethods.getInfo(new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("info");
                    if(jsonArray.length() > 0){
                        JSONObject jsonObject_first = jsonArray.getJSONObject(0);
                        if(App.myPreferences.getLastGetInfo() == null){
                            App.myPreferences.setLastGetInfo(jsonObject_first.toString());
                        }else{
                            JSONObject jsonObject_last_saved = new JSONObject(App.myPreferences.getLastGetInfo());
                            long infoId = jsonObject_last_saved.getLong("infoId");
                            long infoId_first = jsonObject_first.getLong("infoId");
                            App.myPreferences.setLastGetInfo(jsonObject_first.toString());
                            if(infoId != infoId_first){
                                showNotification(HomeActivity.this,infoId_first,getString(R.string.scopri_il_nuovo_articolo));
                            }
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //ERROR API
            }
        });
    }
    private void showNotification(Context context, long id, String text) {
        String title = context.getString(R.string.app_name);
        int icon = android.R.drawable.ic_dialog_info;


        Intent intent = new Intent(context, HomeActivity.class);
        intent.putExtra(EventDetailsActivity.EXTRA_EVENT_ID, id);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setSmallIcon(icon);
        builder.setContentTitle(title);
        builder.setContentText(text);
        builder.setAutoCancel(true);
        builder.setChannelId(getString(R.string.channel));

        builder.setContentIntent(contentIntent);
        builder.setDefaults(Notification.DEFAULT_ALL);

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        setUpNotificationChannel(context,manager);
        manager.notify(0, builder.build());
    }
    public void setUpNotificationChannel(Context context,NotificationManager notificationManager){
        String id = context.getString(R.string.channel);
        // The user-visible name of the channel.
        CharSequence name = context.getString(R.string.channel);
        // The user-visible description of the channel.
        String description = "Notifiche push "+context.getString(R.string.app_name);
        int importance = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = null;

            mChannel = new NotificationChannel(id, name,importance);
            // Configure the notification channel.
            mChannel.setDescription(description);
            mChannel.enableLights(true);
            // Sets the notification light color for notifications posted to this
            // channel, if the device supports this feature.
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);

            mChannel.setVibrationPattern(new long[]{100,50,100,50,100});

            notificationManager.createNotificationChannel(mChannel);

        }

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(null);
        if (!isIntentHandled) {
            handleIntent(intent);
        }
    }

    @Override
    protected void onDestroy() {
        Model.instance().getUpdatesManager().unregisterUpdateListener(updateReceiver);
        super.onDestroy();
    }

    @Override
    public void onNewFilterApplied() {
        mFrManager.reloadPrograms(DrawerMenu.getNavigationDrawerItems().get(mSelectedItem).getEventMode());
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        initNavigationDrawer(toolbar);
    }

    private void initNavigationDrawer(Toolbar toolbar) {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.app_name, R.string.app_name);
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        mDrawerLayout.closeDrawers();
        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                //TODO HIDE KEYBOARD
            }

            @Override
            public void onDrawerOpened(View drawerView) {
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                if (mIsDrawerItemClicked) {
                    mIsDrawerItemClicked = false;
                    changeFragment();
                }
            }

            @Override
            public void onDrawerStateChanged(int newState) {
            }
        });

        drawerToggle.syncState();
    }

    private void initNavigationDrawerList() {
        List<DrawerMenuItem> menu = DrawerMenu.getNavigationDrawerItems();
        mAdapter = new DrawerAdapter(this, menu);

        final ListView listView = (ListView) findViewById(R.id.leftDrawer);
        if (listView != null) {
            listView.addHeaderView(
                    getLayoutInflater().inflate(R.layout.nav_drawer_header, listView, false),
                    null,
                    false);
            listView.addFooterView(
                    getLayoutInflater().inflate(R.layout.nav_drawer_footer, listView, false),
                    null,
                    false);
        }

        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                onDrawerItemClick(position - listView.getHeaderViewsCount());
            }
        });
    }

    public void initFilterDialog() {
        new AsyncTask<Void, Void, List<EventListItem>>() {
            @Override
            protected List<EventListItem> doInBackground(Void... params) {
                TracksManager tracksManager = Model.instance().getTracksManager();
                List<Track> trackList = tracksManager.getTracks();
                List<Level> levelList = tracksManager.getLevels();

                Collections.sort(trackList, new Comparator<Track>() {
                    @Override
                    public int compare(Track track1, Track track2) {
                        String name1 = track1.getName();
                        String name2 = track2.getName();
                        return name1.compareToIgnoreCase(name2);
                    }
                });

                String[] tracks = new String[trackList.size()];
                String[] levels = new String[levelList.size()];

                for (int i = 0; i < trackList.size(); i++) {
                    tracks[i] = trackList.get(i).getName();
                }

                for (int i = 0; i < levelList.size(); i++) {
                    levels[i] = levelList.get(i).getName();
                }
                mFilterDialog = FilterDialog.newInstance(tracks, levels);
                mFilterDialog.setData(levelList, trackList);
                return null;
            }

            @Override
            protected void onPostExecute(List<EventListItem> eventListItems) {
            }
        }.execute();
    }

    public void closeFilterDialog() {
        if (mFilterDialog != null) {
            if (mFilterDialog.isAdded()) {
                mFilterDialog.dismissAllowingStateLoss();
            }
            mFilterDialog.clearFilter();
        }
    }

    private void handleIntent(Intent intent) {
        if (intent.getExtras() != null) {
            long eventId = intent.getLongExtra(EventDetailsActivity.EXTRA_EVENT_ID, -1);
            long day = intent.getLongExtra(EventDetailsActivity.EXTRA_DAY, -1);
            if (eventId != -1 && day != -1) {
                redirectToDetails(eventId, day);
                isIntentHandled = false;
                new ScheduleManager(this).cancelAlarm(eventId);
            }

        }
    }

    private void redirectToDetails(long id, long day) {
        Intent intent = new Intent(this, EventDetailsActivity.class);
        intent.putExtra(EventDetailsActivity.EXTRA_EVENT_ID, id);
        intent.putExtra(EventDetailsActivity.EXTRA_DAY, day);
        startActivity(intent);
    }

    private void onDrawerItemClick(int position) {
        mDrawerLayout.closeDrawers();
        if (mSelectedItem == position) {
            return;
        }
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        mSelectedItem = position;
        mIsDrawerItemClicked = true;
    }

    private void changeFragment() {
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

        {
            if (mFrManager != null) {
                mFrManager.setFragment(DrawerMenu.getNavigationDrawerItems().get(mSelectedItem).getEventMode(), SharedScheduleManager.MY_DEFAULT_SCHEDULE_CODE);
                mPresentTitle = DrawerMenu.getNavigationDrawerItems().get(mSelectedItem).getName();

                setToolBarTitle(mPresentTitle);

                mAdapter.setSelectedPos(mSelectedItem);
                mAdapter.notifyDataSetChanged();

                AnalyticsManager.drawerFragmentTracker(this, mPresentTitle);
            }
        }
    }

    private void initFragmentManager(long code) {
        mFrManager = DrawerManager.getInstance(getSupportFragmentManager(), R.id.mainFragment);

        setDefaultFragment(code);
    }

    private void showIrrelevantTimezoneDialogIfNeeded() {
        if (!IrrelevantTimezoneDialogFragment.isCurrentTimezoneRelevant()
                && IrrelevantTimezoneDialogFragment.canPresentMessage(this)
                && !isFinishing()) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(new IrrelevantTimezoneDialogFragment(), IrrelevantTimezoneDialogFragment.TAG);
            ft.commitAllowingStateLoss();
        }
    }

    private void setDefaultFragment(long code) {
        DrawerMenuItem drawerMenuItem;
        if (code == SharedScheduleManager.MY_DEFAULT_SCHEDULE_CODE) {
            drawerMenuItem = DrawerMenu.getNavigationDrawerItems().get(mSelectedItem);
            mFrManager.setFragment(drawerMenuItem.getEventMode(), SharedScheduleManager.MY_DEFAULT_SCHEDULE_CODE);
        } else {
            mSelectedItem = DrawerMenu.MY_SCHEDULE_POSITION;
            drawerMenuItem = DrawerMenu.getMyScheduleDrawerMenuItem();
            mAdapter.setSelectedPos(mSelectedItem);
            mFrManager.setFragment(drawerMenuItem.getEventMode(), code);
            AnalyticsManager.drawerFragmentTracker(this, drawerMenuItem.getName());
        }
        AnalyticsManager.drawerFragmentTracker(this, drawerMenuItem.getName());
        mPresentTitle = drawerMenuItem.getName();
        setToolBarTitle(mPresentTitle);
    }

    private void setToolBarTitle(int title){
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setTitle(title);
        }
    }


}