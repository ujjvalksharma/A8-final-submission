package com.example.a8_final_submission;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {


    EditText usernameEditText;
    String token="dummy-token";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        usernameEditText= (EditText) findViewById(R.id.userNameEditText);
        findInstanceToken();
    }

    public void LoginBtnClickHandler(View view){

        String userName=usernameEditText.getText().toString();
        if(TextUtils.isEmpty(userName)){
            Toast.makeText(this,"Enter username!",Toast.LENGTH_SHORT);
            return;
        }
        if(TextUtils.isEmpty(token)){
            Toast.makeText(this,"Database server are down!",Toast.LENGTH_SHORT);
            return;
        }

        UserInfo userInfo=new UserInfo(userName,token);
        VerifyUserIsPresent(userInfo);

    }

    public void findInstanceToken(){

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            return;
                        }
                        token = task.getResult();
                    }
                });

    }


    private boolean VerifyUserIsPresent(UserInfo userInfo) {
        final FirebaseDatabase database = FirebaseDatabase.getInstance("https://fir-example-e11f0-default-rtdb.firebaseio.com/");
        DatabaseReference ref = database.getReference("A8");
        DatabaseReference usersRef = ref.child("users");

            final boolean[] isPresent = {false};
            try {
                ValueEventListener valueEventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            UserInfo value=ds.getValue(UserInfo.class);
                           if(value.getUserName().equalsIgnoreCase(userInfo.getUserName())
                             &&value.getToken().equals(userInfo.getToken())){
                              isPresent[0]=true;
                           }
                        }
                        userInfo.setUserName(userInfo.getUserName().toUpperCase());
                        if(!isPresent[0]){
                            usersRef.push().setValue(userInfo);
                            Toast toast = Toast.makeText(MainActivity.this,
                                    "You have registered as a user!",
                                    Toast.LENGTH_SHORT);
                            toast.show();
                        }
                            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                        intent.putExtra("UserInfo", userInfo);
                            startActivity(intent);




                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                };
                usersRef.addListenerForSingleValueEvent(valueEventListener);
            } catch(Exception e){
                System.out.println(e);
            }

            return isPresent[0];


    }

}