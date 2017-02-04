package com.example.admin.simpleblogfirebaseapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AddPost extends AppCompatActivity {

    ImageButton selectImage;
    Button SubmitPost;
    private static int reCode=101;
    EditText title,desc;
    Uri imageUri=null;
    StorageReference mstorage;
    DatabaseReference databaseReference;
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        mstorage= FirebaseStorage.getInstance().getReference();
        databaseReference= FirebaseDatabase.getInstance().getReference();
        selectImage= (ImageButton) findViewById(R.id.btnselectImageForPost);
        SubmitPost= (Button) findViewById(R.id.btnSubmitPost);
        title= (EditText) findViewById(R.id.tvPostName);
        desc= (EditText) findViewById(R.id.tvPostDescription);
        dialog=new ProgressDialog(this);
        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryType=new Intent(Intent.ACTION_GET_CONTENT);
                galleryType.setType("image/*");
                startActivityForResult(galleryType,reCode);
            }
        });
        SubmitPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPosting();
            }
        });
    }

    private void startPosting() {

      dialog.setTitle("Posting To Blog");
      dialog.show();
     final String postTitle=title.getText().toString();
     final String postDesc=desc.getText().toString();
    if(!TextUtils.isEmpty(postTitle)&&!TextUtils.isEmpty(postDesc)&&imageUri!=null)
    {
        StorageReference filePath=mstorage.child("Blog_Images").child(imageUri.getLastPathSegment());
        filePath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
            Uri downLoadUrl=taskSnapshot.getDownloadUrl();
            DatabaseReference newPost=databaseReference.child("Blog").push();
            newPost.child("title").setValue(postTitle);
            newPost.child("desc").setValue(postDesc);
            newPost.child("imageUrl").setValue(downLoadUrl.toString());

             dialog.dismiss();
                Toast.makeText(AddPost.this, "Post Submitted Successfully", Toast.LENGTH_SHORT).show();
             Intent i=new Intent(AddPost.this,MainActivity.class);
             startActivity(i);

            }
        });
    }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==reCode && resultCode==RESULT_OK)
        {
            imageUri=data.getData();
            selectImage.setImageURI(imageUri);
            Log.e("onActivityResult: ",imageUri+"");

        }
}
}
