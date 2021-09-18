package cn.bsd.learn.hotfix.sample.err;

import android.content.Context;
import android.widget.Toast;

public class ParamsSort {
    public void math(Context context){
        int a = 10;
        int b = 1;
        Toast.makeText(context,"math >>> " + a/b,Toast.LENGTH_SHORT).show();
    }
}
