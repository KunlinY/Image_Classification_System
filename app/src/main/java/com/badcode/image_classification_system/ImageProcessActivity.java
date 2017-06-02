package com.badcode.image_classification_system;


import com.dodowaterfall.widget.ScaleImageView;
import com.example.android.bitmapfun.util.ImageFetcher;
import com.soundcloud.android.crop.Crop;
import com.soundcloud.android.crop.CropImageActivity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.File;

public class ImageProcessActivity extends AppCompatActivity {

    private static final String pic = "Picture";
    private String data;
    private ScaleImageView resultView;
    private ImageFetcher mImageFetcher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_process);
        data = getIntent().getStringExtra(pic);//这里需要判断如果没有找到对应图片的解决方案
        resultView = (ScaleImageView) findViewById(R.id.result_image);
        mImageFetcher = new ImageFetcher(this, 240);
        mImageFetcher.setLoadingImage(R.drawable.ic_launcher);
        resultView.setImageWidth(10);
        resultView.setImageHeight(10);
        mImageFetcher.loadImage(data, resultView);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.image_process, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.crop) {
            Intent i = new Intent(ImageProcessActivity.this, CropImageActivity.class);
            i.setData(Uri.parse(data));
            startActivity(i);
            Crop.pickImage(this);
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent result) {
        if (requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK) {
            beginCrop(result.getData());
        } else if (requestCode == Crop.REQUEST_CROP) {
            handleCrop(resultCode, result);
        }
    }

    private void beginCrop(Uri source) {
        Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
        Crop.of(source, destination).asSquare().start(this);
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {
            resultView.setImageURI(Crop.getOutput(result));
        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

}