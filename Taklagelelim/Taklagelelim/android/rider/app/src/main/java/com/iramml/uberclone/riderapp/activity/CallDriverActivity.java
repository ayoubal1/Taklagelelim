package com.iramml.uberclone.riderapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.PermissionRequest;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesMissingManifestValueException;
import com.iramml.uberclone.riderapp.common.Common;
import com.iramml.uberclone.riderapp.interfaces.IFCMService;
import com.iramml.uberclone.riderapp.model.firebase.User;
import com.iramml.uberclone.riderapp.R;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.security.Permission;

import de.hdodenhof.circleimageview.CircleImageView;

public class CallDriverActivity extends AppCompatActivity {
    private static int PERMISSION_CODE =100;
    CircleImageView imgAvatar;
    TextView tvName, tvPhone;
    Button btnCallDriver;
    String phone;
    String driverID;
    LatLng lastLocation;
    EditText drphone;

    IFCMService mService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_driver);
        mService = Common.getFCMService();

        imgAvatar = (CircleImageView)findViewById(R.id.imgAvatar);
        tvName = findViewById(R.id.tvDriverName);
        tvPhone = findViewById(R.id.tvPhone);
        btnCallDriver = findViewById(R.id.btnCallDriver);
        drphone = findViewById(R.id.drphone);
        if(getIntent()!=null){
            driverID=getIntent().getStringExtra("driverID");
            double lat=getIntent().getDoubleExtra("lat", 0.0);
            double lng=getIntent().getDoubleExtra("lng", 0.0);
            lastLocation=new LatLng(lat, lng);
            loadDriverInfo(driverID);
        }else finish();
        if (ContextCompat.checkSelfPermission(CallDriverActivity.this, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(CallDriverActivity.this,new String[]{Manifest.permission.CALL_PHONE},PERMISSION_CODE);
        }
        btnCallDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                phone = drphone.getText().toString();
                Intent i = new Intent(Intent.ACTION_CALL);
                i.setData(Uri.parse("tel:"+phone));
                startActivity(i);
            }
        });
    }

    private void loadDriverInfo(String driverID) {
        FirebaseDatabase.getInstance().getReference(Common.user_driver_tbl)
                .child(driverID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user=dataSnapshot.getValue(User.class);

                if(user.getAvatarUrl()!=null &&
                        !TextUtils.isEmpty(user.getAvatarUrl()))
                    Picasso.get().load(user.getAvatarUrl()).into(imgAvatar);
                tvName.setText(user.getName());
                tvPhone.setText(user.getPhone());
                drphone.setText(user.getPhone());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
