package com.zzti.fengyongge.imagepickersample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;




public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btAddPic,btPreView,btSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initOnclick();
    }
    void initView(){
        btAddPic = findViewById(R.id.btAddPic);
        btPreView= findViewById(R.id.btPreView);
        btPreView= findViewById(R.id.btPreView);
        btSave= findViewById(R.id.btSave);
    }

    void initOnclick(){
        btAddPic.setOnClickListener(this);
        btPreView.setOnClickListener(this);
        btSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btAddPic){
            startActivity(new Intent(MainActivity.this,AddPicActivity.class));
        }else if(v.getId() == R.id.btPreView){
            startActivity(new Intent(MainActivity.this,PreViewActivity.class));
        }else if(v.getId() == R.id.btSave){
            startActivity(new Intent(MainActivity.this,SaveActivity.class));
        }

    }
}
