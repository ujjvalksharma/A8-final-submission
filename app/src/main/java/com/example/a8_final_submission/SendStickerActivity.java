package com.example.a8_final_submission;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;

import java.sql.Timestamp;

public class SendStickerActivity extends AppCompatActivity {

    String stickerId;
    EditText stickerReciverUserNameEditText;
    UserInfo userInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_sticker);
        Intent intent = getIntent();
        this.stickerId=intent.getStringExtra("stickerId");
        stickerReciverUserNameEditText = (EditText) findViewById(R.id.stickerReciverUserNameEditText);
        userInfo = (UserInfo) getIntent().getSerializableExtra("UserInfo");
        Button sendStickerBtn= (Button) findViewById(R.id.sendStickerBtn);
        sendStickerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String enteredReciverUsername=stickerReciverUserNameEditText.getText().toString().toUpperCase();
                if(TextUtils.isEmpty(enteredReciverUsername)){
                    Toast.makeText(SendStickerActivity.this,"Enter reciver username",
                            Toast.LENGTH_SHORT)
                            .show();
                    return;
                }
                FirebaseDatabase database = FirebaseDatabase.getInstance("https://fir-example-e11f0-default-rtdb.firebaseio.com/");
                DatabaseReference ref = database.getReference("A8");
                DatabaseReference usersRef = ref.child("users");
                ValueEventListener valueEventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String receiverToken=null;
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            UserInfo value=ds.getValue(UserInfo.class);
                            if(value.getUserName().equals(enteredReciverUsername)){
                                receiverToken=value.getToken();
                            }

                        }
                        if(receiverToken!=null){

                            //create StickerUserInfoRelation object
                            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                            StickerUserInfoRelation stickerUserInfoRelation=new StickerUserInfoRelation(stickerId,
                                    userInfo.getUserName(),
                                    enteredReciverUsername,
                                    timestamp.toString());
                            //save StickerUserInfoRelation in firebase
                            DatabaseReference stickerUserRelationRef = ref.child("StickerUserRelation");
                            stickerUserRelationRef.push().setValue(stickerUserInfoRelation);
                            Toast.makeText(SendStickerActivity.this,"Sticker is send",
                                            Toast.LENGTH_SHORT)
                                    .show();
                            //notify reciver by entered username. Find the userName token
                            final String  finalReceiverToken = receiverToken;
                            Runnable runnable=()->{

                                SendNotificationService sendNotificationService=new SendNotificationService();
                                try {
                                    sendNotificationService.sendNotficationImpl(SendStickerActivity.this,
                                            "Username:"+enteredReciverUsername+" got a new sticker from "+userInfo.getUserName(),
                                            stickerId,
                                            finalReceiverToken);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            };

                            new Thread(runnable).start();


                        }else{
                            Toast.makeText(SendStickerActivity.this,"The username you entered is not registered",
                                            Toast.LENGTH_SHORT)
                                    .show();
                            return;
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                };
                usersRef.addListenerForSingleValueEvent(valueEventListener);

            }
        });
    }

}