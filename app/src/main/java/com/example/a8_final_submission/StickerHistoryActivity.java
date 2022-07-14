package com.example.a8_final_submission;

import android.os.Bundle;
import android.view.View;

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

public class StickerHistoryActivity extends AppCompatActivity implements StickerHistoryRecycleViewAdaptor.NameClickListener {

    RecyclerView recyclerView;
    StickerHistoryRecycleViewAdaptor adapter;
    UserInfo userInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sticker_history);
        userInfo = (UserInfo) getIntent().getSerializableExtra("UserInfo");
        findAllRecivedStickers();
    }

    private void findAllRecivedStickers() {

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://fir-example-e11f0-default-rtdb.firebaseio.com/");
        DatabaseReference ref = database.getReference("A8");
        DatabaseReference stickerUserRelationRef = ref.child("StickerUserRelation");
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<StickerUserInfoRelation> stickerUserInfoRelations =new ArrayList<StickerUserInfoRelation>();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    StickerUserInfoRelation value=ds.getValue(StickerUserInfoRelation.class);
                    if(value.getReceiverUserName().equals(userInfo.getUserName())){
                        stickerUserInfoRelations.add(value);
                    }
                }
                recyclerView = findViewById(R.id.stickerHistoryRecycleView);
                recyclerView.setLayoutManager(new LinearLayoutManager(StickerHistoryActivity.this));
                adapter = new StickerHistoryRecycleViewAdaptor(StickerHistoryActivity.this, stickerUserInfoRelations);
                //adapter.setClickListener(this);
                recyclerView.setAdapter(adapter);


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