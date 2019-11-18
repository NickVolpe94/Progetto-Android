package com.example.registrazioneapp;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Preferenze extends AppCompatActivity {

    Switch escrementi,randagio,guasto,buca;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    DatabaseReference databaseReference;
    FirebaseStorage storage;
    StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferenze);
        escrementi = findViewById(R.id.swEscrementi);
        randagio = findViewById(R.id.swCaniRandagi);
        guasto = findViewById(R.id.swGuasto);
        buca = findViewById(R.id.swBuca);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Impostazioni");


        databaseReference.child(firebaseAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Impostazioni imp = dataSnapshot.getValue(Impostazioni.class);
                if(imp.getEscrementi()==false){
                    escrementi.setChecked(false);
                }
                if(imp.getBuca()==false){
                    buca.setChecked(false);
                }
                if(imp.getGuasto()==false){
                    guasto.setChecked(false);
                }
                if(imp.getRandagio()==false){
                    randagio.setChecked(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });






        escrementi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    databaseReference.child(firebaseAuth.getUid()).child("escrementi").setValue(true);
                }else {
                    databaseReference.child(firebaseAuth.getUid()).child("escrementi").setValue(false);
                }
            }
        });
        randagio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    databaseReference.child(firebaseAuth.getUid()).child("randagio").setValue(true);
                }else {
                    databaseReference.child(firebaseAuth.getUid()).child("randagio").setValue(false);
                }
            }
        });
        guasto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    databaseReference.child(firebaseAuth.getUid()).child("guasto").setValue(true);
                }else {
                    databaseReference.child(firebaseAuth.getUid()).child("guasto").setValue(false);
                }
            }
        });
        buca.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    databaseReference.child(firebaseAuth.getUid()).child("buca").setValue(true);
                }else {
                    databaseReference.child(firebaseAuth.getUid()).child("buca").setValue(false);;
                }
            }
        });



    }
}
