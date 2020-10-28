package com.appnetwork.androidmatrialactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import java.io.File;
import java.util.ArrayList;

public class GalleryActivity extends AppCompatActivity {

    private ImageAdapter imageAdapter;
    ArrayList<String> f = new ArrayList<String>();
    File[] listFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        getFromSdcard();
        GridView imagegrid = (GridView) findViewById(R.id.grid_view);
        imageAdapter = new ImageAdapter();
        imagegrid.setAdapter(imageAdapter);
        imagegrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(GalleryActivity.this, "Select  " + f.get(position), Toast.LENGTH_LONG).show();
                Intent intent=new Intent(GalleryActivity.this,ImageviewActivity.class);
                intent.putExtra("id", f.get(position));
                startActivity(intent);
            }
        });
    }
    public void getFromSdcard() {
        File file = new File(android.os.Environment.getExternalStorageDirectory(), "ImageEditor");

        if (file.isDirectory()) {
            listFile = file.listFiles();

            for (int i = 0; i < listFile.length; i++) {
                f.add(listFile[i].getAbsolutePath());
            }
        }
    }

    public class ImageAdapter extends BaseAdapter {
        private LayoutInflater mInflater;

        public ImageAdapter() {
            mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public int getCount() {
            return f.size();
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.gridviewcard, null);
                holder.imageview = (ImageView) convertView.findViewById(R.id.imgs);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            Bitmap myBitmap = BitmapFactory.decodeFile(f.get(position));
            holder.imageview.setImageBitmap(myBitmap);
            return convertView;
        }
    }
    class ViewHolder {
        ImageView imageview;
    }
}
