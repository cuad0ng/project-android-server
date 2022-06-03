package com.example.project_android_server.Common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.project_android_server.Model.Request;
import com.example.project_android_server.Model.User;
import com.example.project_android_server.Remote.APIService;
import com.example.project_android_server.Remote.RetrofitClient;

public class Common {
    public static User currentUser;
    public static Request currentRequest;
    public static final String BASE_URL = "https://fcm.googleapis.com/";
    public static final String UPDATE = "Update";
    public static final String DELETE = "Delete";
    public static final int PICK_IMAGE_REQUEST = 71;
    public static String convertCodeToStatus(String status) {
        if (status.equals("0"))
            return "Placed";
        else if(status.equals("1"))
            return "On My Way";
        else return "Shipped";
    }

    public static boolean isConnectedToInternet(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager != null) {
            NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
            if (info != null) {
                for (int i=0; i<info.length; i++){
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                        return true;
                }
            }
        }
        return false;
    }
    public static APIService getFCMClient() {
        return RetrofitClient.getClient(BASE_URL).create(APIService.class);
    }
}
