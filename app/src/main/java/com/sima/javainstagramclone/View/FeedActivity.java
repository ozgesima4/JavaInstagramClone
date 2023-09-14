package com.sima.javainstagramclone.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.sima.javainstagramclone.Adapter.RecyclerAdapter;
import com.sima.javainstagramclone.Model.Post;
import com.sima.javainstagramclone.R;
import com.sima.javainstagramclone.databinding.ActivityFeedBinding;

import java.util.ArrayList;
import java.util.Map;

public class FeedActivity extends AppCompatActivity {

    private  FirebaseAuth auth;
    private FirebaseFirestore firestore;
    ArrayList<Post>postArrayList;
    private ActivityFeedBinding binding;
    RecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityFeedBinding.inflate(getLayoutInflater());
        View view=binding.getRoot();
        setContentView(view);

        postArrayList=new ArrayList<>();

        auth=FirebaseAuth.getInstance();
        firestore=FirebaseFirestore.getInstance();

        getData();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter=new RecyclerAdapter(postArrayList);
        binding.recyclerView.setAdapter(adapter);


    }
    public void getData(){  //firebaseden verileir çekmek
                                                                      //tarihi ayarlamak sıralamaya göre -->orderby
        firestore.collection("posts").orderBy("date", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {  //firestoreda verileri sürekli snapshot ile dinledik collection belirtip
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {  //bize value ve error verebilir
                if(error!=null){   //error verirse kullanıcıya haber et
                    Toast.makeText(FeedActivity.this,error.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                }
                if(value!=null){  //değer alındıysa bunu dökümen halinde al sırayla mape at ordan da obje isimlerini veri tabanından yaz ki doğru olsun çekmiş olduk

                    for(DocumentSnapshot snapshot: value.getDocuments()){

                        Map<String,Object>data=snapshot.getData();

                        String comment=(String) data.get("comment");
                        String Kmail=(String) data.get("usermail");
                        String downloadUrl=(String) data.get("downloadUrl");

                        Post post=new Post(Kmail,downloadUrl,comment);
                        postArrayList.add(post);
                   }
                    adapter.notifyDataSetChanged();



                    //yeni veri geldikçe haber et ki recycler güncellensin
                }else {
                    // Veri yoksa Toast mesajı gösterin
                    Toast.makeText(FeedActivity.this, "Veri yok", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflate=getMenuInflater();
        inflate.inflate(R.menu.options_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()==R.id.addpost){
            Intent intent=new Intent(FeedActivity.this, UploadActivity.class);
            startActivity(intent);
        }
        else if(item.getItemId()==R.id.logout){
            auth.signOut();
            Intent intent=new Intent(FeedActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}