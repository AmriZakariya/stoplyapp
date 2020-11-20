package com.luissabias.stoply;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.IntentService;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.util.Log;

import com.scottyab.rootbeer.RootBeer;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.SortedMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;

import androidx.annotation.Nullable;

public class StoplyService extends IntentService {

    public static final long NOTIFY_INTERVAL = 1000;
    public static String str_receiver = "com.luissabias.stoply.receiver";
    public String str_testing;
    Calendar calendar;
    SimpleDateFormat simpleDateFormat;
    Long strDate;
    Long date_current, date_diff;
    //Root Detector
    RootBeer rootbeer;
    //Con Manager
    WifiManager wifiManager;
    //DH Helper
    DB_Helper db;
    Intent intent;
    Intent lockIntent;
    private Handler mHandler = new Handler();
    private Timer mTimer = null;


    public StoplyService() {
        super("StoplyService");
        lockIntent = new Intent(this, locked.class);
        lockIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        lockIntent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);

        db = new DB_Helper(this);
    }

    public StoplyService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if ((int) db.getAppsCount() != 0) {
            int count = (int) db.getAppsCount();
            for (int i = 1; i <= count; ++i) {
                Log.e("STOPLYY() STOPLYY For ", "db.get_app_PKG(i)"+ db.get_app_PKG(i));

                if (printForegroundTask().equalsIgnoreCase(db.get_app_PKG(i))) {
                    Log.e("STOPLYY() For Kill ", "db.get_app_PKG(i)"+ db.get_app_PKG(i));
//                    ActivityManager manager = (ActivityManager)getSystemService(Activity.ACTIVITY_SERVICE);
//                    manager.killBackgroundProcesses(db.get_app_PKG(i));

//                    ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
//                    Intent startMain = new Intent(Intent.ACTION_MAIN);
//                    startMain.addCategory(Intent.CATEGORY_HOME);
//                    startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    this.startActivity(startMain);
//                    am.killBackgroundProcesses(printForegroundTask());
                    startActivity(lockIntent);
                    startActivity(lockIntent);

                }
            }
        }
    }

    private String printForegroundTask() {
        String currentApp = "";
        @SuppressLint("WrongConstant") UsageStatsManager usm = (UsageStatsManager) this.getSystemService("usagestats");
        long time = System.currentTimeMillis();
        assert usm != null;
        List<UsageStats> appList = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000 * 1000, time);
        if (appList != null && appList.size() > 0) {
            SortedMap<Long, UsageStats> mySortedMap = new TreeMap<>();
            for (UsageStats usageStats : appList) {
                mySortedMap.put(usageStats.getLastTimeUsed(), usageStats);
            }
            if (mySortedMap != null && !mySortedMap.isEmpty()) {
                currentApp = mySortedMap.get(mySortedMap.lastKey()).getPackageName();
            }
        } else {
            ActivityManager am = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
            assert am != null;
            List<ActivityManager.RunningAppProcessInfo> tasks = am.getRunningAppProcesses();
            currentApp = tasks.get(0).processName;
        }

        Log.e("Foreground App", "Current App in foreground is: " + currentApp);
        return currentApp;

//        @SuppressLint("WrongConstant") UsageStatsManager usageStatsManager = (UsageStatsManager) this.getSystemService("usagestats");
//        long ts = System.currentTimeMillis();
//        List<UsageStats> queryUsageStats = usageStatsManager
//                .queryUsageStats(UsageStatsManager.INTERVAL_BEST,
//                        ts - 2000, ts);/*from  w  w  w.  j  a  v a  2  s  . co m*/
//        if (queryUsageStats == null || queryUsageStats.isEmpty()) {
//            return null;
//        }
//        UsageStats recentStats = null;
//        for (UsageStats usageStats : queryUsageStats) {
//            if (recentStats == null
//                    || recentStats.getLastTimeUsed() < usageStats
//                    .getLastTimeUsed()) {
//                recentStats = usageStats;
//            }
//        }
//        if( recentStats.getPackageName() != null){
//            return recentStats.getPackageName();
//        }else{
//            return "test" ;
//        }
//        return recentStats.getPackageName();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Du code
        return super.onStartCommand(intent, flags, startId);
    }


}
