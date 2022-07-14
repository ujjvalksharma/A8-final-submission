package com.example.a8_final_submission;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements MyStickerRecycleViewAdaptor.NameClickListener {

    MyStickerRecycleViewAdaptor adapter;
    RecyclerView recyclerView;
    List<String> stickerIds=new ArrayList<String>();
    UserInfo userInfo;
    TextView myStickerTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        userInfo = (UserInfo) getIntent().getSerializableExtra("UserInfo");
        myStickerTextView=(TextView) findViewById(R.id.myStickerTextView);
       Button stickerHistoryBtn= (Button) findViewById(R.id.stickerHistoryBtn);
       //findTotalSendStickerCount();
       stickerHistoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(HomeActivity.this, StickerHistoryActivity.class);
                intent.putExtra("UserInfo", userInfo);
                startActivity(intent);
            }
        });


        stickerIds.add("https://d6ce0no7ktiq.cloudfront.net/images/preview/2020/03/24/design-62241/template-sticker-300x300.png");
        stickerIds.add("https://d6ce0no7ktiq.cloudfront.net/images/preview/2020/03/24/design-62242/template-sticker-300x300.png");
        stickerIds.add("https://d6ce0no7ktiq.cloudfront.net/images/preview/2020/03/24/design-62243/template-sticker-300x300.png");
        stickerIds.add("https://d6ce0no7ktiq.cloudfront.net/images/stickers/1611t.png");
        stickerIds.add("https://d6ce0no7ktiq.cloudfront.net/images/stickers/1505t.png");
        stickerIds.add("https://d6ce0no7ktiq.cloudfront.net/images/preview/2018/12/18/design-32745/template-sticker-300x300.png");
        stickerIds.add("https://d6ce0no7ktiq.cloudfront.net/images/preview/2019/05/21/design-41303/template-sticker-300x300.png");
        stickerIds.add("https://d6ce0no7ktiq.cloudfront.net/images/preview/2019/05/21/design-41308/template-sticker-300x300.png");
        stickerIds.add("https://d6ce0no7ktiq.cloudfront.net/images/preview/2019/06/04/design-41980/template-sticker-300x300.png");
        stickerIds.add("https://d6ce0no7ktiq.cloudfront.net/images/preview/2019/10/30/design-52269/template-sticker-300x300.png");

    recyclerView = findViewById(R.id.myStickerRecycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyStickerRecycleViewAdaptor(this, stickerIds,userInfo);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        findTotalSendStickerCount();
    }

    private void findTotalSendStickerCount() {

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://fir-example-e11f0-default-rtdb.firebaseio.com/");
        DatabaseReference ref = database.getReference("A8");
        DatabaseReference stickerUserRelationRef = ref.child("StickerUserRelation");
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               int totalStickerSent=0;
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    StickerUserInfoRelation value=ds.getValue(StickerUserInfoRelation.class);
         if(value.getSenderUsername().equals(userInfo.getUserName())){
             totalStickerSent++;
         }
                }

                myStickerTextView.setText("I have sent:"+totalStickerSent+" stickers and below are my stickers");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        stickerUserRelationRef.addListenerForSingleValueEvent(valueEventListener);

    }

    @Override
    public void onClickName(View view, int position) {

    }

}