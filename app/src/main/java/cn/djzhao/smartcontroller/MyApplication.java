package cn.djzhao.smartcontroller;

import android.app.Application;

import com.lzy.okgo.OkGo;

import java.util.logging.Level;

/**
 * 我的App
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        OkGo.init(this);
        OkGo.getInstance()
                .debug("OKGO", Level.INFO, true)
                .setConnectTimeout(OkGo.DEFAULT_MILLISECONDS)  //全局的连接超时时间
                .setReadTimeOut(OkGo.DEFAULT_MILLISECONDS)     //全局的读取超时时间
                .setWriteTimeOut(OkGo.DEFAULT_MILLISECONDS)//全局的写入超时时间
                .setRetryCount(3);
    }
}
