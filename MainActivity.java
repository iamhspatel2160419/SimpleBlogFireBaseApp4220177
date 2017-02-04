package com.example.admin.simpleblogfirebaseapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {


    RecyclerView mBlogList;
    private DatabaseReference mDatabse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDatabse= FirebaseDatabase.getInstance().getReference().child("Blog");
        mBlogList= (RecyclerView) findViewById(R.id.BlogList);
        mBlogList.setHasFixedSize(true);
        mBlogList.setLayoutManager(new LinearLayoutManager(this));



    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.game_menu, menu);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Blog,BlogViewHolder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<Blog, BlogViewHolder>(
                Blog.class,
                R.layout.blog_raw,
                BlogViewHolder.class,
                mDatabse

        ) {
            @Override
            protected void populateViewHolder(BlogViewHolder viewHolder, Blog model, int position) {
               viewHolder.setTitle(model.getTitle());
               viewHolder.setDesc(model.getDesc());
               viewHolder.setImage(getApplicationContext(),model.getImageUrl());
            }
        };
        mBlogList.setAdapter(firebaseRecyclerAdapter);
    }

    public static class BlogViewHolder extends RecyclerView.ViewHolder{


        View view;
        public BlogViewHolder(View itemView) {
            super(itemView);
            view=itemView;
        }

        public void setTitle(String title)
        {
            TextView post_title= (TextView) view.findViewById(R.id.tvblogTitle);
            post_title.setText(title);
        }
        public void setDesc(String desc)
        {
            TextView post_title= (TextView) view.findViewById(R.id.tvblogDescription);
            post_title.setText(desc);
        }
        public void setImage(Context context,String url)
        {
            ImageView imge= (ImageView) view.findViewById(R.id.blogImage);
            Picasso.with(context).load(url).into(imge);
        }


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addPost:
                Intent intent=new Intent(MainActivity.this,AddPost.class);
                startActivity(intent);

            case R.id.Settings:

                break;
            default:
                break;

        }
        return true;
    }
}

