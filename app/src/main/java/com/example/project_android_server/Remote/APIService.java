package com.example.project_android_server.Remote;

import com.example.project_android_server.Model.MyResponse;
import com.example.project_android_server.Model.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAZjSpphE:APA91bGmtXUrsJ8RsnrFxnTtx7M5PdEfh81lK-Otkoyksvt_5eIe6VQnyKHtGU9ZfkYTTD6grzNEtK21XKTTI16HWIb5LrG-LQcIswcNTHxLlNQ_l8kN-MlUW3SD_T2L9WnS_SU_fYsT"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
