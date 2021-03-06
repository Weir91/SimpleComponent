package cn.weir.simplecomponent.test

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cn.weir.simplecomponent.R
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import kotlinx.android.synthetic.main.activity_main.*

@Route(path = "/test/Test1Activity")
class Test1Activity : AppCompatActivity() {

    val mRepo = ApiRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tvText.text = localClassName

        tvText.setOnClickListener {
            ARouter.getInstance().build("/example_a/ExampleAActivity").navigation()
        }
    }
}
