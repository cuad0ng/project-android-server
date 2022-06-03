package com.example.project_android_server.Service;

import android.util.Log;

import com.example.project_android_server.Common.Common;
import com.example.project_android_server.Model.Token;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;

import java.util.Objects;

public class MyFirebaseService extends FirebaseMessagingService {
    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.d("NEW_TOKEN", s);
        if (Common.currentRequest != null) {
            updateToServer();
        }

    }


    private void updateToServer() {
        if (Common.currentUser != null) {
            FirebaseMessaging.getInstance().getToken()
                    .addOnCompleteListener(task -> {
                        if (!task.isSuccessful()) {
                            //Could not get FirebaseMessagingToken
                            return;
                        }
                        if (null != task.getResult()) {
                            //Got FirebaseMessagingToken
                            String firebaseMessagingToken = Objects.requireNonNull(task.getResult());
                            //Use firebaseMessagingToken further
                            FirebaseDatabase db = FirebaseDatabase.getInstance();
                            DatabaseReference tokens = db.getReference("Tokens");
                            Token data = new Token(firebaseMessagingToken, true);
                            tokens.child(Common.currentUser.getPhone()).setValue(data);
                        }
                    });
        }
    }
}
