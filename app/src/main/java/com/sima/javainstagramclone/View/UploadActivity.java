package com.sima.javainstagramclone.View;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sima.javainstagramclone.databinding.ActivityUploadBinding;

import java.util.HashMap;
import java.util.UUID;

public class UploadActivity extends AppCompatActivity {

    private ActivityUploadBinding binding;
    Uri uri;
    public ActivityResultLauncher<Intent> activityResultLauncher;
    public ActivityResultLauncher<String> permissionresultLauncher;
    private FirebaseAuth auth;
    private FirebaseStorage storage;
    private StorageReference reference;
    private FirebaseFirestore firestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityUploadBinding.inflate(getLayoutInflater());
        View view=binding.getRoot();
        setContentView(view);

        registerLaunch();
        auth=FirebaseAuth.getInstance();
        storage=FirebaseStorage.getInstance();
        firestore=FirebaseFirestore.getInstance();
        reference=storage.getReference();
    }
    public void Uploadİmage(View view){

        UUID uuid=UUID.randomUUID();
        String uriname="/images"+uuid+"/.jpg";

        if(uri!=null){
            reference.child(uriname).putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    //download url
                    StorageReference newRefrence=reference.child(uriname);  //referans oluşturma referansttan ama
                    newRefrence.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String downloadUrl=uri.toString();
                            String comment=binding.editTextTextMultiLine.getText().toString();

                            FirebaseUser user=auth.getCurrentUser();
                            String Email= user.getEmail();

                            HashMap<String,Object>myHashMap=new HashMap<>();
                            myHashMap.put("usermail",Email);
                            myHashMap.put("comment",comment);
                            myHashMap.put("downloadUrl",downloadUrl);
                            myHashMap.put("date", FieldValue.serverTimestamp());//güncel zamanı alma mathotudur.

                            firestore.collection("posts").add(myHashMap).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Intent intent=new Intent(UploadActivity.this, FeedActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                     Toast.makeText(UploadActivity.this,e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                                }
                            });

                        }
                    });



                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(UploadActivity.this,e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                }
            });

        }
    }
    public void Selectimage(View view){



        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.TIRAMISU){
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                if(ActivityCompat.shouldShowRequestPermissionRationale(UploadActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)){
                    Snackbar.make(view,"permission needed for gallery",Snackbar.LENGTH_INDEFINITE).setAction("give permission", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //izin aldık mantığı göstermeyip
                            permissionresultLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
                        }
                    });
                }else{
                    //izin alacağız yine ama mantığı demeden
                    permissionresultLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
                }


            }else{
                Intent ıntent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI); //izin verildi zaten
                activityResultLauncher.launch(ıntent);
            }
        }else{
            Intent intent=new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

            activityResultLauncher.launch(intent);
        }

    }


    private void registerLaunch() {
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK) {
                            Intent resultimage = result.getData();
                            if (resultimage != null) {
                                uri = resultimage.getData();
                                binding.imageView.setImageURI(uri);

                 /*   try {
                        if(resultimage!=null){
                            uri=resultimage.getData();
                            if(Build.VERSION.SDK_INT>=28){
                                ImageDecoder.Source source=ImageDecoder.createSource(getContentResolver(),uri);
                                bitmap=ImageDecoder.decodeBitmap(source);
                                binding.imageView.setImageBitmap(bitmap);

                            }
                            bitmap=MediaStore.Images.Media.getBitmap(getContentResolver(),uri);


                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }*/


                            }
                        }
                    }


                }
        );


        permissionresultLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
            @Override
            public void onActivityResult(Boolean result) {
                if (result) {
                    // İzin verildi, galeriye erişim mümkün.
                    Intent Intentgallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    activityResultLauncher.launch(Intentgallery);
                } else {
                    // İzin verilmedi, Toast mesajı göster.
                    Toast.makeText(UploadActivity.this, "Permission needed for gallery", Toast.LENGTH_LONG).show();
                }
            }
        });

    }


}