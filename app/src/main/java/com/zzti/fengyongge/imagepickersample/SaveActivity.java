package com.zzti.fengyongge.imagepickersample;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.zzti.fengyongge.imagepicker.ImagePickerInstance;
import com.zzti.fengyongge.imagepicker.model.PhotoModel;

import java.util.ArrayList;
import java.util.List;


public class SaveActivity extends AppCompatActivity {

    String url= "http://gank.io/images/f0c192e3e335400db8a709a07a891b2e";

    private ImageView ivDownload;
    private TextView tvSave;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save);

        ivDownload = findViewById(R.id.ivDownload);

        Glide.with(this).load(url).into(ivDownload);

        findViewById(R.id.tvSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<PhotoModel> tempList = new ArrayList<>();
                PhotoModel photoModel = new PhotoModel();
                photoModel.setOriginalPath(url);
                tempList.add(photoModel);
                ImagePickerInstance.getInstance().photoPreview(SaveActivity.this, tempList, 0, true);
            }
        });


    }


}