package remotex.com.remotewebview;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import androidx.multidex.MultiDexApplication;

import com.onesignal.OSNotificationOpenedResult;
import com.onesignal.OneSignal;

import remotex.com.remotewebview.additionalSettings.CrashReportDB.CrashHandler;
import remotex.com.remotewebview.additionalSettings.myService.SyncInterval;
import remotex.com.remotewebview.additionalSettings.myService.OnChnageService;


public class MyApplication extends MultiDexApplication {

    private static Application instance;

    private static int numberOfRunningActivities = 0;



    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Thread.setDefaultUncaughtExceptionHandler(new CrashHandler(instance));

                if (!(constants.OnesigID ==null)){
                    OneSignal.initWithContext(MyApplication.this);
                    OneSignal.setAppId(constants.OnesigID);
                }

            }
        }, 3000);


        OneSignal.setNotificationOpenedHandler(new OneSignal.OSNotificationOpenedHandler() {
            @Override
            public void notificationOpened(OSNotificationOpenedResult result) {
                String launchURL = result.getNotification().getLaunchURL();

                if (launchURL != null) {
//                    Log.d(Const.DEBUG, "Launch URL: " + launchURL);
                    Intent intent = new Intent(getApplicationContext(), WebActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("url", launchURL);
                    startActivity(intent);
                }
            }
        });
    }


    public static Context getContext() {
        return instance.getApplicationContext();
    }


    public static void incrementRunningActivities() {
        numberOfRunningActivities++;
    }

    public static void decrementRunningActivities() {
        numberOfRunningActivities--;
        // Check if all activities are stopped
        if (numberOfRunningActivities == 0) {
            // Stop the service
            instance.stopService(new Intent(instance.getApplicationContext(), SyncInterval.class));
            instance.stopService(new Intent(instance.getApplicationContext(), OnChnageService.class));
        }
    }


}