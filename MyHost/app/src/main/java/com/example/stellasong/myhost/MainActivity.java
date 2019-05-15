package com.example.stellasong.myhost;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.qihoo360.replugin.RePlugin;
import com.qihoo360.replugin.model.PluginInfo;
import com.qihoo360.replugin.utils.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_install_test_plugin).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final ProgressDialog pd = ProgressDialog.show(MainActivity.this, "Installing...", "Please wait...", true, true);
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        simulateInstallMyTestMPlugin();
                        pd.dismiss();
                    }
                }, 1000);
            }
        });

        findViewById(R.id.btn_start_test_plugin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pluginName = "com.example.stellasong.testplugin";
                // 若没有安装，则直接提示“错误”
                if (RePlugin.isPluginInstalled(pluginName)) {
                    RePlugin.startActivity(MainActivity.this, RePlugin.createIntent(pluginName, "com.example.stellasong.testplugin.MainActivity"));
                } else {
                    Toast.makeText(MainActivity.this, "You must install TestPlugin first!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        findViewById(R.id.btn_open_new_activity).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent it = new Intent(MainActivity.this, NewActivity.class);
                startActivity(it);
            }
        });
    }

    private void simulateInstallMyTestMPlugin() {
        String testPluginApk= "TestPlugin.apk";
        String testPluginapkPath = "external" + File.separator + testPluginApk;

        // 文件是否已经存在？直接删除重来
        String pluginFilePath = getFilesDir().getAbsolutePath() + File.separator + testPluginApk;
        File pluginFile = new File(pluginFilePath);
        if (pluginFile.exists()) {
            FileUtils.deleteQuietly(pluginFile);
        }

        // 开始复制
        copyAssetsFileToAppFiles(testPluginapkPath, testPluginApk);
        PluginInfo info = null;
        if (pluginFile.exists()) {
            info = RePlugin.install(pluginFilePath);
        }

        if (info != null) {
            RePlugin.startActivity(MainActivity.this, RePlugin.createIntent(info.getName(), "com.example.stellasong.testplugin.MainActivity"));
        } else {
            Toast.makeText(MainActivity.this, "install my test plugin failed", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 从assets目录中复制某文件内容
     *  @param  assetFileName assets目录下的Apk源文件路径
     *  @param  newFileName 复制到/data/data/package_name/files/目录下文件名
     */
    private void copyAssetsFileToAppFiles(String assetFileName, String newFileName) {
        InputStream is = null;
        FileOutputStream fos = null;
        int buffsize = 1024;

        try {
            is = this.getAssets().open(assetFileName);
            fos = this.openFileOutput(newFileName, Context.MODE_PRIVATE);
            int byteCount = 0;
            byte[] buffer = new byte[buffsize];
            while((byteCount = is.read(buffer)) != -1) {
                fos.write(buffer, 0, byteCount);
            }
            fos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
