package com.example.kktext.Utills;

import android.app.Activity;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;


import com.example.kktext.LocationClient;
import com.example.kktext.Model.ArchitectBean;
import com.example.kktext.Model.ClientBean;
import com.example.kktext.Model.CustProdCatBean;
import com.example.kktext.Model.DealerBean;
import com.example.kktext.Model.GlassColorBean;
import com.example.kktext.Model.GlassThicknessBean;
import com.example.kktext.Model.InstallerBean;
import com.example.kktext.Model.MEPBean;
import com.example.kktext.Model.ProductCategoryBean;
import com.example.kktext.Model.ProfileColorBean;
import com.example.kktext.Model.ProfileNameBean;
import com.example.kktext.Model.PropertyStageBean;
import com.example.kktext.Model.SourceBean;
import com.example.kktext.Model.StateBean;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Locale;

public class SalesApp extends Application implements Application.ActivityLifecycleCallbacks {

    public static Context appContext;
    public static Location userCurrentlocation;
    public static String deviceId;
    public static boolean isAddAccessToken;
    public static boolean isUpComingSession = true;
    public static ArrayList<StateBean.Data> stateList = new ArrayList<>();
    public static ArrayList<PropertyStageBean.Data> propertyStageList = new ArrayList<>();
    public static ArrayList<ProductCategoryBean.Data>productCatList = new ArrayList<>();
    public static ArrayList<CustProdCatBean.Data>prodCustomCatList = new ArrayList<>();
    public static ArrayList<DealerBean.Data>dealerList = new ArrayList<>();
    public static ArrayList<GlassColorBean.Data>glassColorList = new ArrayList<>();
    public static ArrayList<ProfileColorBean.Data>profileColorList = new ArrayList<>();
    public static ArrayList<GlassThicknessBean.Data>glassThicknessList = new ArrayList<>();
    public static ArrayList<ProfileNameBean.Data>profileNameList = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = this;

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel =new  NotificationChannel(
                    "location",
                    "Location",
                    NotificationManager.IMPORTANCE_LOW
            );
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);


        }
     //   deviceId = Settings.Secure.getString(getContentResolver(),Settings.Secure.ANDROID_ID);
       // Locale locale = new Locale("en", "US");
       // setLocale(locale);
        registerActivityLifecycleCallbacks(this);
      //  HashKeyGenerator();


    }
    public static void setConnectivityListener(ConnectivityListener.ConnectivityReceiverListener listener) {
        ConnectivityListener.connectivityReceiverListener = listener;
    }
    private  boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                        this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
        ) {

            return true;
        }
        return false;
    }
    private boolean isLocationEnabled() {
        LocationManager locationManager =
                (LocationManager) getSystemService(LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }
    @SuppressWarnings("deprecation")
    private void setLocale(Locale locale) {
        Resources resources = getResources();
        Configuration configuration = resources.getConfiguration();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(locale);
        } else {
            configuration.locale = locale;
        }
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
            getApplicationContext().createConfigurationContext(configuration);
        } else {
            resources.updateConfiguration(configuration, displayMetrics);
        }
    }

    public Context getAppContext() {
        return appContext;
    }

    public void setAppContext(Context appContext) {
        this.appContext = appContext;
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
        Utility.Companion.changeStatusBarColor(activity);

        new GPSTrackerFus(activity, new GPSTrackerFus.LocationInterface() {
            @Override
            public void locationReceived(Location location) {
                userCurrentlocation = location;
                Log.e("onActivityCreated: ", userCurrentlocation.getLongitude() + "");
            }
        });
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {

    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {

    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {

    }

    public void HashKeyGenerator() {
        // SHA1: BB:69:5C:1B:FB:0F:2B:DC:85:6B:A9:DF:64:2E:E9:79:54:72:73:35
        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.example.salesapp", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = null;
                try {
                    md = MessageDigest.getInstance("SHA");
                } catch (NoSuchAlgorithmException ex) {
                    ex.printStackTrace();
                }
                md.update(signature.toByteArray());
                Log.e("KeyHashhhhh:", "++++++++++++++++++++++++++++++++++++++" + Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("KeyHash:", "++++++++++++++++++++++++++++++++++++++" + e.toString());

        }
    }

}
