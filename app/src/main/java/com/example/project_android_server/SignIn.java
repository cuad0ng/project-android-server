package com.example.project_android_server;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project_android_server.Common.Common;
import com.example.project_android_server.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

public class SignIn extends AppCompatActivity {
    EditText editPhone,editPassword;
    Button btnSignIn;

    FirebaseDatabase db;
    DatabaseReference users;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        editPassword = (MaterialEditText)findViewById(R.id.editPassword);
        editPhone   = (MaterialEditText)findViewById(R.id.editPhone);

        btnSignIn    = (Button)findViewById(R.id.btnSignIn1);

        //Init Firebase

        db = FirebaseDatabase.getInstance();
        users = db.getReference("User");

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sigInUser(editPhone.getText().toString(),editPassword.getText().toString());
            }
        });
    }

    private void sigInUser(final String userid, String password) {
        final ProgressDialog md = new ProgressDialog(SignIn.this);
        md.setMessage("Please Wait...");

        final String localphone = userid;
        final String localpassword = password;
        users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(localphone).exists())
                {
                    md.dismiss();
                    //Get User Information
                    User user = dataSnapshot.child(localphone).getValue(User.class);
                    user.setPhone(localphone);
                    if(user.getIsStaff())
                    {
                        if (user.getPassword().equals(localpassword))
                        {
                            Intent homeIntent = new Intent(SignIn.this,Home.class);
                            Common.currentUser=user;
                            startActivity(homeIntent);
                            finish();
                        }
                        else
                        {
                            Toast.makeText(SignIn.this, "Wrong Password!!!", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(SignIn.this, "Use admin account!!!", Toast.LENGTH_SHORT).show();

                    }
                }
                else
                {
                    md.dismiss();
                    Toast.makeText(SignIn.this, "You Doesnt Exist On Our Databse,Want To Become our Partner Call Our GrubHub Executive", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
     }

}
