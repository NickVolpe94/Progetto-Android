package com.example.registrazioneapp;

import android.location.Location;
import android.location.LocationListener;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener,GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;

    private ChildEventListener mChildEventListener;
    Marker marker;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    DatabaseReference databaseReference;
    FirebaseStorage storage;
    StorageReference storageReference;
    boolean[] a= new boolean[4];



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        ChildEventListener mChildEventListener;

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Segnalazioni");
        databaseReference.push().setValue(marker);

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

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.moveCamera( CameraUpdateFactory.newLatLngZoom(new LatLng(45.6140747,8.8427703) , 14.0f) );
        googleMap.setOnMarkerClickListener(this);
        googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot s : dataSnapshot.getChildren() ){
                    Users users = s.getValue(Users.class);
                    if(a[0]==true && users.getMotivo().equals("ESCREMENTI")){
                        LatLng location = new LatLng(users.getLat(),users.getLon());
                        mMap.addMarker(new MarkerOptions().position(location).title(users.getMotivo()))
                                .setIcon(BitmapDescriptorFactory
                                        .defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
                    }
                    if(a[1]==true && users.getMotivo().equals("PERICOLOSI")){
                        LatLng location = new LatLng(users.getLat(),users.getLon());
                        mMap.addMarker(new MarkerOptions().position(location).title(users.getMotivo()))
                                .setIcon(BitmapDescriptorFactory
                                        .defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                    }
                    if(a[2]==true && users.getMotivo().equals("MALTRATTATI")){
                        LatLng location = new LatLng(users.getLat(),users.getLon());
                        mMap.addMarker(new MarkerOptions().position(location).title(users.getMotivo()))
                                .setIcon(BitmapDescriptorFactory
                                        .defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                    }
                    if(a[3]==true && users.getMotivo().equals("RANDAGIO")){
                        LatLng location = new LatLng(users.getLat(),users.getLon());
                        mMap.addMarker(new MarkerOptions().position(location).title(users.getMotivo()))
                                .setIcon(BitmapDescriptorFactory
                                        .defaultMarker(BitmapDescriptorFactory.HUE_RED));
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }
}
