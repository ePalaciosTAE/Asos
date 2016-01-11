package com.tae.asos.application;

import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.StandardExceptionParser;
import com.google.android.gms.analytics.Tracker;
import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.tae.asos.constants.Constants;

/**
 * MIX PANEL INTEGRATION
 * Initialize MixPanel here.
 * I have also created some utils classes to use MixPanel whethever we need in the app.
 * There is a dummmy example in Main Activity
 * And more tracking events within the service classes
 */

/**
 * Created by Eduardo on 05/01/2016.
 * TODO: STEPS TO INTEGRATE ANALYTICS
 * 1. In developers console enable Analytics API
 * 2. Create a new track-id in analytics (Admin-property)
 * 3.Create the xml directory and add the app-tracker.xml under res folder
 * 3.1 Add your track-id and set your properties
 * 3.2 Change the package in the xml for the activities
 * 3.3 Add logger: <string name="ga_logLevel">verbose</string>
       <bool name="ga_dryRun">true</bool>
 * 4. Create a Class, extend Application
 * 4.1 make this class a Singleton
 * 5.Create the AnalyticsTrackers class
 * 6.Init AnalyticsTrackers into onCreate in Application class
 * 6.1.Add the tracking methods in Application class
 * 7.Use the tracking methods arround the code whethever you need
 * 8.Add receivers from example if needed and:
 * <meta-data
   android:name="com.google.android.gms.analytics.globalConfigResource"
   android:resource="@xml/app_tracker" />
 */
public class AsosApplication extends Application {

    public static final String TAG = AsosApplication.class.getClass().getSimpleName();
    private static AsosApplication instance; // this singleton instance is used for the Analytics implementation

    public static synchronized AsosApplication getInstance() {
        return instance;
    }

    public synchronized Tracker getGoogleAnalyticsTracker() {
        AnalyticsTrackers analyticsTrackers = AnalyticsTrackers.getInstance();
        return analyticsTrackers.get(AnalyticsTrackers.Target.APP);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (instance == null) {
            instance = this;
        }
        AnalyticsTrackers.initialize(this);
        AnalyticsTrackers.getInstance().get(AnalyticsTrackers.Target.APP);

        initMixPanel();
    }

    /**
     * MixPanel Initialization
     */
    private void initMixPanel() {
        MixpanelAPI mixPanel = MixpanelAPI.getInstance(this, Constants.MIX_PANEL_PROJECT_TOKEN);
        mixPanel.identify(Constants.MIX_PANEL_DISTINCT_ID_FOR_THE_USER);
        mixPanel.getPeople().identify(Constants.MIX_PANEL_DISTINCT_ID_FOR_THE_USER);
        mixPanel.getPeople().initPushHandling(Constants.GCM_SENDER_ID);
        mixPanel.flush();
    }

    /*ANALYTICS TRACKERS*/

    /***
     * Tracking screen view
     * @param screenName screen name to be displayed on GA dashboard
     */
    public void trackScreenView(String screenName) {
        Tracker t = getGoogleAnalyticsTracker();
        // Set screen name.
        t.setScreenName(screenName);
        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());
        GoogleAnalytics.getInstance(this).dispatchLocalHits();
    }

    /***
     * Tracking exception
     * @param e exception to be tracked
     */
    public void trackException(Exception e) {
        if (e != null) {
            Tracker t = getGoogleAnalyticsTracker();
            t.send(new HitBuilders.ExceptionBuilder()
                            .setDescription(
                                    new StandardExceptionParser(this, null)
                                            .getDescription(Thread.currentThread().getName(), e))
                            .setFatal(false)
                            .build()
            );
        }
    }

    /***
     * Tracking event
     * @param category event category
     * @param action   action of the event
     * @param label    label
     */
    public void trackEvent(String category, String action, String label) {
        Tracker t = getGoogleAnalyticsTracker();
        // Build and send an Event.
        t.send(new HitBuilders.EventBuilder().setCategory(category).setAction(action).setLabel(label).build());
    }


}
