package com.zzti.fengongge.imagepickerdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;



/**
 * @author fengyongge
 * @Description
 */
public class MainActivity extends Activity {

    TextView tvAddPic,tvPreView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvAddPic= (TextView) findViewById(R.id.tvAddPic);
        tvPreView= (TextView) findViewById(R.id.tvPreView);

        tvAddPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              startActivity(new Intent(MainActivity.this,AddPicActivity.class));

            }
        });

        tvPreView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainActivity.this,PreViewActivity.class));
            }
        });

    }


}
