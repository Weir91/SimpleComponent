package cn.figo.example_a;

import android.app.Application;

/**
 * @author Weir.
 * @date 2019/2/22.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ExampleAInterface.init(this);
    }
}
