package com.appnetwork.androidmatrialactivity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class ImageviewActivity extends AppCompatActivity {
    ImageView imageView;
    Button btnshare,btnsave,btnedt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imageview);
        imageView  = findViewById(R.id.imgview);
        btnshare = findViewById(R.id.btnshare);
        btnsave = findViewById(R.id.btnsave);
        btnedt = findViewById(R.id.btnedt);
        Intent intent=getIntent();
        String imgs=intent.getStringExtra("id");

        imageView.setImageURI(Uri.parse(imgs));

        btnshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                Uri phototUri = Uri.parse(imgs);
                shareIntent.setData(phototUri);
                shareIntent.setType("image/*");
                shareIntent.putExtra(Intent.EXTRA_STREAM, phototUri);
                startActivity(Intent.createChooser(shareIntent, "Share"));
            }
        });

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"save",Toast.LENGTH_SHORT).show();
            }
        });

        btnedt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(ImageviewActivity.this,EditImageActivity.class);
                intent1.setType("image/*");
                intent1.putExtra("id",imgs);
                startActivity(intent1);
                finish();
            }
        });
    }
}
