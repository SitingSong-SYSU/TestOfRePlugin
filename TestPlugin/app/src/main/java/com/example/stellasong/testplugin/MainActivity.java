package com.example.stellasong.testplugin;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Context context = getBaseContext();

        setContentView(R.layout.activity_main);
        findViewById(R.id.open_host_activity).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent();
                    intent.setComponent(new ComponentName("com.example.stellasong.myhost", "com.example.stellasong.myhost.NewActivity"));
                    getBaseContext().startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(context, "Can not find host activity !", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Toast.makeText(context, "Hello My Test Plugin !", Toast.LENGTH_SHORT).show();
    }
}
