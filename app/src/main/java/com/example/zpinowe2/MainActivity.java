package com.example.zpinowe2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.example.zpinowe2.Cards.arrayAdapter;
import com.example.zpinowe2.Cards.cards;
import com.example.zpinowe2.Matches.MatchesActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private cards cards_data[];
    private com.example.zpinowe2.Cards.arrayAdapter arrayAdapter;
    private int i;

    private FirebaseAuth mAuth;

    private String currentUId;
    private DatabaseReference usersDb;

    ListView listView;
    List<cards> rowItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        usersDb = FirebaseDatabase.getInstance("https://zpinowe-default-rtdb.europe-west1.firebasedatabase.app/").getReference().child("Users");
        mAuth = FirebaseAuth.getInstance();

        currentUId = mAuth.getCurrentUser().getUid();

        checkUserPref();

      //  Toast.makeText(MainActivity.this,"a" , Toast.LENGTH_SHORT).show();

        rowItems = new ArrayList<>();

        arrayAdapter = new arrayAdapter(this, R.layout.item, rowItems);


        SwipeFlingAdapterView flingContainer = (SwipeFlingAdapterView) findViewById(R.id.frame);

        flingContainer.setAdapter(arrayAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {

                rowItems.remove(0);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                cards obj = (cards) dataObject;
                String userId = obj.getUserId();



                usersDb.child(userId).child("connections").child("left").child(currentUId).setValue(true);
                Toast.makeText(MainActivity.this, "lewo", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                cards obj = (cards) dataObject;
                String userId = obj.getUserId();
                usersDb.child(userId).child("connections").child("right").child(currentUId).setValue(true);
                Toast.makeText(MainActivity.this, userF, Toast.LENGTH_SHORT).show();
                isConnectionMatch(userId);
                Toast.makeText(MainActivity.this, "prawo", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {


                arrayAdapter.notifyDataSetChanged();

                i++;
            }

            @Override
            public void onScroll(float scrollProgressPercent) {

            }
        });


        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                Toast.makeText(MainActivity.this, "Click!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void isConnectionMatch(String userId) {
        DatabaseReference currentUserConnectionDb = usersDb.child(currentUId).child("connections").child("right").child(userId);
        currentUserConnectionDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    Toast.makeText(MainActivity.this, "Nowa para!", Toast.LENGTH_SHORT).show();
                    String key = FirebaseDatabase.getInstance("https://zpinowe-default-rtdb.europe-west1.firebasedatabase.app/").getReference().child("Chat").push().getKey();


                 //   usersDb.child(snapshot.getKey()).child("connections").child("matches").child(currentUId).setValue(true);
                    usersDb.child(snapshot.getKey()).child("connections").child("matches").child(currentUId).child("ChatId").setValue(key);

                 //   usersDb.child(currentUId).child("connections").child("matches").child(snapshot.getKey()).setValue(true);
                    usersDb.child(currentUId).child("connections").child("matches").child(snapshot.getKey()).child("ChatId").setValue(key);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



    private String userT;
    private String userF;
    private String userN;


    public void checkUserPref(){
    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference userDb = usersDb.child(user.getUid());

    userDb.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()){
                if (dataSnapshot.child("sex").getValue() != null){
                    userT = dataSnapshot.child("sex").getValue().toString();
                    userN = dataSnapshot.child("name").getValue().toString();
                    switch (userT){
                        case "Male":
                            userF = "Female";
                            break;
                        case "Female":
                            userF = "Male";
                            break;
                    }
                    usersDb.child(currentUId).child("connections").child("left").child(currentUId).setValue(true);
                    //  Toast.makeText(MainActivity.this, userDb.child(currentUId).child("connections").toString() , Toast.LENGTH_SHORT).show();
                    otherUsers();
                }
            }
        }
        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    });
}

    public void otherUsers(){

        usersDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.exists() && userN!=snapshot.child("name").getValue() &&  !snapshot.child("connections").child("left").hasChild(currentUId) && !snapshot.child("connections").child("right").hasChild(currentUId) && snapshot.child("sex").getValue().toString().equals(userT) ){

                        String test123=""+userN.toString()+ snapshot.child("name").getValue().toString();




                    String profileImageUrl;
                    if(snapshot.child("profileImageUrl").getValue()==null)
                    {profileImageUrl = "deafult";}
                    else{profileImageUrl  = snapshot.child("profileImageUrl").getValue().toString() ;
                    }


                    cards item = new cards(snapshot.getKey(), snapshot.child("name").getValue().toString(), profileImageUrl);
                    rowItems.add(item);
                    arrayAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
            }
            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void logoutUser(View view) {
        mAuth.signOut();
        Intent intent = new Intent(MainActivity.this, LoginRegisterActivity.class);
        startActivity(intent);
        finish();
        return;
    }
    public void goToSettings(View view){
        Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(intent);
        return;
    }


    public void goToMatches(View view) {
        Intent intent = new Intent(MainActivity.this, MatchesActivity.class);
        startActivity(intent);
        return;
    }
}