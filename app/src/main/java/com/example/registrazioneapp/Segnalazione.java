package com.example.registrazioneapp;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Segnalazione extends AppCompatActivity {
    Button inviaSegnalazione,motivazioneb;
    Button foto;
    ImageView immagine;
    Integer REQUEST_CAMERA =1,SELECT_FILE=0;
    Uri filePath;
    Uri photoURI;
    String codice;
    int controllo=0,controllo2,controllo3;
    TextView motivazione;
    String motivaziones;
    Location l;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    DatabaseReference databaseReference;
    FirebaseStorage storage;
    StorageReference storageReference;
    boolean[] a= new boolean[4];




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_segnalazione);

        foto = findViewById(R.id.btnFoto);
        immagine =findViewById(R.id.imageView);
        motivazione = findViewById(R.id.tvMotivazione);
        motivazioneb = findViewById(R.id.btnMotivazione);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Segnalazioni");


        inviaSegnalazione = findViewById(R.id.btnInviaSegnalazione);
        ActivityCompat.requestPermissions(Segnalazione.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},123);

        FirebaseDatabase.getInstance().getReference("Impostazioni").child(firebaseAuth.getUid())
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Impostazioni imp = dataSnapshot.getValue(Impostazioni.class);
                if(imp.getEscrementi()==false){
                   a[0]=false;
                }else a[0]=true;
                if(imp.getBuca()==false){
                    a[1]=false;
                }else a[1]=true;
                if(imp.getGuasto()==false){
                    a[2]=false;
                }else a[2]=true;
                if(imp.getRandagio()==false){
                    a[3]=false;
                }else a[3]=true;
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        motivazioneb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] itemse = {"","","","","ESCI"};
                int b=1;
                if(a[0]==true){
                    itemse[0]="ESCREMENTI";
                    b++;
                }
                if(a[1]==true){
                    itemse[1]="CANI PERICOLOSI";
                    b++;
                }
                if(a[2]==true){
                    itemse[2]="CANI MALTRATTATI";
                    b++;
                }
                if(a[3]==true){
                    itemse[3]="CANI RANDAGI";
                    b++;
                }
                final CharSequence[] motivi = new CharSequence[b];
                for(int i=0,z=0;i<itemse.length;i++){
                    if(itemse[i]!=""){
                        motivi[z]=itemse[i];
                        z++;
                    }
                }
                AlertDialog.Builder builder= new AlertDialog.Builder(Segnalazione.this);
                builder.setTitle("Inserisci una motivazione");
                builder.setItems(motivi, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                            if(motivi[i]=="ESCI"){
                                dialog.dismiss();
                            }else {
                                if(motivi[i]=="ESCREMENTI"){
                                    motivaziones="ESCREMENTI";
                                    motivazione.setText(motivi[i]);
                                    controllo2=1;
                                }
                                if(motivi[i]=="CANI RANDAGI"){
                                    motivaziones="RANDAGIO";
                                    motivazione.setText(motivi[i]);
                                    controllo2=1;
                                }
                                if(motivi[i]=="CANI MALTRATTATI"){
                                    motivaziones="MALTRATTATI";
                                    motivazione.setText(motivi[i]);
                                    controllo2=1;
                                }
                                if(motivi[i]=="CANI PERICOLOSI"){
                                    motivaziones="PERICOLOSI";
                                    motivazione.setText(motivi[i]);
                                    controllo2=1;
                                }
                            }
                    }
                });
                builder.show();
            }
        });

        foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        inviaSegnalazione.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if(controllo2==1){
                            addSegnalazione();
                    }else{
                        Toast.makeText(Segnalazione.this,"Inserisci il motivo oppure seleziona qualche preferenza",Toast.LENGTH_LONG ).show();
                    }

            }
        });

    }


    private void selectImage() {
        final CharSequence[] items = {"Fotocamera","Galleria","Esci"};
        AlertDialog.Builder builder= new AlertDialog.Builder(Segnalazione.this);
        builder.setTitle("Inserisci un'immagine");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                if(items[i]== "Fotocamera"){
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    // Ensure that there's a camera activity to handle the intent
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                        // Create the File where the photo should go
                        File photoFile = null;
                        try {
                            photoFile = createImageFile();
                        } catch (IOException ex) {
                            // Error occurred while creating the File
                        }
                        // Continue only if the File was successfully created
                        if (photoFile != null) {
                            photoURI = FileProvider.getUriForFile(Segnalazione.this,
                                    "com.example.android.fileprovider",
                                    photoFile);
                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                            startActivityForResult(takePictureIntent, REQUEST_CAMERA);
                        }
                    }
                }else if(items[i] =="Galleria"){
                    Intent intent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(intent.createChooser(intent,"Seleziona l'immagine"),SELECT_FILE);
                }else if(items[i]== "Esci"){
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    String currentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    public void onActivityResult(int requestcode,int resultcode,Intent data){
        super.onActivityResult(requestcode,resultcode,data);
        if(requestcode==REQUEST_CAMERA){ ;
            immagine.setImageURI(photoURI);
            filePath = photoURI;
            controllo=1;
        }else if(requestcode==SELECT_FILE){
                Uri selectedImageUri = data.getData();
                immagine.setImageURI(selectedImageUri);
                filePath = selectedImageUri;
                controllo=1;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        GPStracker g = new GPStracker(getApplicationContext());
        l = g.getLocation();

    }

    public void addSegnalazione(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        String data = dateFormat.format(date);
        String motivosegnalazione = motivaziones;
        String id = firebaseUser.getUid();


        double lat,lon;

        if (l != null){
            lat =l.getLatitude();
            lon =l.getLongitude();
            controllo3=1;
            //Toast.makeText(getApplicationContext(),"Lat: "+lat+"\n Lon:"+lon,Toast.LENGTH_LONG ).show();
            if(controllo2==1){
                String ud = databaseReference.push().getKey();
                codice=ud;
                Users users = new Users(id,motivosegnalazione,data,lat,lon,date);

                databaseReference.child(ud).setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            if(filePath!=null){
                                StorageReference ref =storageReference.child(codice);
                                ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                    String UrlImm = uri.toString();
                                                    databaseReference.child(ud).child("urlimmagine").setValue(UrlImm);
                                            }
                                        });

                                    }
                                });

                            }else{
                                databaseReference.child(ud).child("urlimmagine").setValue("https://firebasestorage.googleapis.com/v0/b/tesi-779f2.appspot.com/o/no-image-icon.png?alt=media&token=93414144-a521-4c96-9cea-a3ab1bf383d6");
                            }
                            Toast.makeText(Segnalazione.this,"Segnalazione caricata con successo!", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(Segnalazione.this,task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }else{
            Toast.makeText(Segnalazione.this,"Accendi il GPS!",Toast.LENGTH_LONG ).show();
        }
    }

}
