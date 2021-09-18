package cn.bsd.learn.hotfix.sample.acts;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import cn.bsd.learn.hotfix.library.FixDexUtils;
import cn.bsd.learn.hotfix.library.utils.Constants;
import cn.bsd.learn.hotfix.library.utils.FileUtils;
import cn.bsd.learn.hotfix.sample.BaseActivity;
import cn.bsd.learn.hotfix.sample.R;
import cn.bsd.learn.hotfix.sample.err.ParamsSort;

public class SecondActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
    }

    //展示
    public void show(View view) {
        ParamsSort sort = new ParamsSort();
        sort.math(this);
    }

    //修复
    public void fix(View view) {
        //直接将修复包放在SDCard里面，不模拟网络下载环节
        File sourceFile = new File(Environment.getExternalStorageDirectory(), Constants.DEX_NAME);

        //目标路径,私有目录
        File targetFile = new File(getDir(Constants.DEX_DIR, Context.MODE_PRIVATE).getAbsolutePath()+
                File.separator + Constants.DEX_NAME);

        //如果存在之前修复过的dex
        if(targetFile.exists()){
            targetFile.delete();
            Toast.makeText(this,"删除dex文件完成",Toast.LENGTH_SHORT).show();
        }


        try {
            //将下载的修复包，复制到私有目录，然后再做解压工作
            FileUtils.copyFile(sourceFile, targetFile);
            Toast.makeText(this,"复制dex文件完成",Toast.LENGTH_SHORT).show();
            //开始修复dex
            FixDexUtils.loadFixedDex(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
