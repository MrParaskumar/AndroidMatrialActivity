package com.appnetwork.androidmatrialactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;

import java.io.ByteArrayOutputStream;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    Button btncaptureimage,btnselectimage,btngalleryopen;
    private static final int IMAGE_PICK_CODE = 1000;
    static final int CAPTURE_IMAGE_REQUEST = 1;
    private static final int PERMISSION_CODE = 1001;
    private static final int CAMERAPERMISSIONCODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btncaptureimage=findViewById(R.id.btncaptureimage);
        btnselectimage=findViewById(R.id.btnselectimage);
        btngalleryopen=findViewById(R.id.btngalleryopen);

        btncaptureimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERAPERMISSIONCODE);
                    } else {
                        captureImage();
                    }
                } else {
                    captureImage();
                }
            }
        });

        btnselectimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                        requestPermissions(permissions, PERMISSION_CODE);
                    } else {
                        picimagefromgallery();
                    }
                } else {
                    picimagefromgallery();
                }
            }
        });
        btngalleryopen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,GalleryActivity.class);
                startActivity(intent);
            }
        });
    }
    private void picimagefromgallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_CODE);
    }

    private void captureImage() {
        Intent intent=new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,CAPTURE_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_REQUEST && resultCode == RESULT_OK) {

            Bitmap bitmap = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
            assert bitmap != null;
            Uri tempUri = getImageUri(MainActivity.this, bitmap);
            Intent intent=new Intent(MainActivity.this,EditImageActivity.class);
            intent.putExtra("imgcapture",tempUri.toString());
            startActivity(intent);

        } else if (requestCode == IMAGE_PICK_CODE && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            Intent intent=new Intent(MainActivity.this,EditImageActivity.class);
            assert uri != null;
            intent.putExtra("imgpick",uri.toString());
            startActivity(intent);
        } else {
//            Toast.makeText(MainActivity.this, "Request cancelled", Toast.LENGTH_SHORT).show();
        }
    }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                captureImage();
                picimagefromgallery();
            }
        }
    }
}
