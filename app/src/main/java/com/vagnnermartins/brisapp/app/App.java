package com.vagnnermartins.brisapp.app;

import android.app.Activity;
import android.app.Application;

import com.gc.materialdesign.widgets.SnackBar;
import com.vagnnermartins.brisapp.R;
import com.vagnnermartins.brisapp.util.ConnectionDetectorUtils;

/**
 * Created by vagnnermartins on 19/03/15.
 */
public class App extends Application {

    public String message;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public boolean isInternetConnection(Activity activity){
        ConnectionDetectorUtils cd = new ConnectionDetectorUtils(this);
        if (!cd.isConnectingToInternet()) {
            new SnackBar(activity, activity.getString(R.string.exception_erro_err_internet_disconnected)).show();
            return false;
        }
        return true;
    }
}
