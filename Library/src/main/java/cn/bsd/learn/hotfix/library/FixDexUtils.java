package cn.bsd.learn.hotfix.library;

import android.content.Context;

import java.io.File;
import java.util.HashSet;

import cn.bsd.learn.hotfix.library.utils.ArrayUtils;
import cn.bsd.learn.hotfix.library.utils.Constants;
import cn.bsd.learn.hotfix.library.utils.ReflectUtils;
import dalvik.system.DexClassLoader;
import dalvik.system.PathClassLoader;

public class FixDexUtils {

    //classes2.dex 和 classes3.dex同时修复
    private static HashSet<File> loadedDex = new HashSet<>();

    static {
        //修复之前清理集合
        loadedDex.clear();
    }

    //加载热修复的Dex文件
    public static void loadFixedDex(Context context) {
        File fileDir = context.getDir(Constants.DEX_DIR, Context.MODE_PRIVATE);
        //循环私有目录中的所有文件
        File[] listFiles = fileDir.listFiles();
        for (File file : listFiles) {
            if (file.getName().endsWith(Constants.DEX_SUFFIX) && !"classes.dex".equals(file.getName())) {
                //找到了修复包dex文件，加入到集合
                loadedDex.add(file);
            }
        }

        //模拟类加载器
        createDexClassLoader(context, fileDir);
    }

    //创建加载补丁的DexClassLoader类加载器
    private static void createDexClassLoader(Context context, File fileDir) {
        //创建解压的目录
        String optimizedDir = fileDir.getAbsolutePath() + File.separator + "opt_dex";
        //创建目录
        File fopt = new File(optimizedDir);
        if (!fopt.exists()) {
            //创建这个多级目录
            fopt.mkdirs();
        }
        for (File dex : loadedDex) {
            //自有的类加载器
            DexClassLoader classLoader = new DexClassLoader(dex.getAbsolutePath(),
                    optimizedDir, null, context.getClassLoader());
            //每循环一次，修复一次
            hotfix(classLoader, context);
        }

    }

    private static void hotfix(DexClassLoader classLoader, Context context) {
        //获取系统的PathClassLoader
        PathClassLoader pathLoader = (PathClassLoader) context.getClassLoader();
        try {
            //获取自有的dexElements数组
            Object myElements = ReflectUtils.getDexElements(ReflectUtils.getPathList(classLoader));
            //获取系统的dexElements数组
            Object systemElements = ReflectUtils.getDexElements(ReflectUtils.getPathList(pathLoader));
            //合并并且生成新的dexElements数组
            Object dexElements = ArrayUtils.combineArray(myElements, systemElements);
            //获取系统的pathList
            Object systemPathList = ReflectUtils.getPathList(pathLoader);
            //通过反射技术，将新的dexElements数组赋值给系统的pathList
            ReflectUtils.setField(systemPathList, systemPathList.getClass(), dexElements);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
