package com.example.registrazioneapp;


import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class ElencoAdmin extends AppCompatActivity {
    private ListView listView;
    DatabaseReference databaseReference;
    List<Users> usersList;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    final ArrayList<String> keyList = new ArrayList<>();
    FirebaseStorage storage;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        setContentView(R.layout.activity_elenco);

        storage = FirebaseStorage.getInstance();
        //storageReference = storage.getReference();
        listView = findViewById(R.id.listView);
        databaseReference = FirebaseDatabase.getInstance().getReference("Segnalazioni");

        usersList = new ArrayList<>();


    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usersList.clear();
                for(DataSnapshot usersSnapshot:dataSnapshot.getChildren()){
                    Users users = usersSnapshot.getValue(Users.class);
                    usersList.add(users);
                    keyList.add(usersSnapshot.getKey());
                }
                UserAdapter userAdapter = new UserAdapter(ElencoAdmin.this,usersList);
                listView.setAdapter(userAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            final CharSequence[] items = {"SI","NO"};
            AlertDialog.Builder builder = new AlertDialog.Builder(ElencoAdmin.this);
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                builder.setTitle("Vuoi eliminare la segnalazione?");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        if(items[i]=="SI"){
                            StorageReference photoRef = storage.getReferenceFromUrl("gs://tesi-779f2.appspot.com/"+keyList.get(position));
                            photoRef.delete();
                            usersList.remove(position);
                            databaseReference.getRoot().child("Users").child(keyList.get(position)).removeValue();
                            keyList.remove(position);
                            //startActivity(new Intent(Elenco.this,Profilo.class));
                        }else if(items[i]=="NO"){
                            dialog.dismiss();
                        }

                    }
                });
                builder.show();

            }
        });
    }
}