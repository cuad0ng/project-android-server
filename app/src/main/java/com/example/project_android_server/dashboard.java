package com.example.project_android_server;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.project_android_server.Common.Common;
import com.example.project_android_server.Model.Token;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

public class dashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        CardView btnManageProduct = findViewById(R.id.btnManageProduct);
        CardView btnManageOrder = findViewById(R.id.btnManageOrder);
        Button btnLogout = findViewById(R.id.btnLogOut);
        TextView txtName = findViewById(R.id.txtNameDash);
        txtName.setText(Common.currentUser.getName());
        btnManageProduct.setOnClickListener(view -> {
            Intent product = new Intent(dashboard.this, Home.class);
            startActivity(product);

        });
        btnManageOrder.setOnClickListener(view -> {
            Intent orderIntent = new Intent(dashboard.this, OrderStatus.class);
            startActivity(orderIntent);
        });
        btnLogout.setOnClickListener(view -> {
            Intent SignIn = new Intent(dashboard.this, MainActivity.class);
            SignIn.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(SignIn);
        });
        // send token
        updateToken(FirebaseMessaging.getInstance().getToken());
    }
    private void updateToken(Task<String> task) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference tokens = db.getReference("Tokens");
        String token = task.getResult();
        Token data = new Token(token, true);
        tokens.child(Common.currentUser.getPhone()).setValue(data);
    }
}