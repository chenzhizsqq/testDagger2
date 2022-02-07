package com.example.testdagger2

import android.graphics.ColorSpace
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Inject
import javax.inject.Singleton


class MainActivity : AppCompatActivity() {

    /**
     * Inject 创建直接创建对象的接口
     */
    @Inject
    lateinit var user:User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /**
         *  这里的DaggerMainComponent
         *  用MainComponent 生成 Dagger对象
         */
        DaggerMainComponent.builder().build().inject(this)

        /**
         *  然后这里就可以直接使用了，而不需要再去新建成对象了。
         *  目的就是防止多次创建对象，而让程序简洁。
         */
        user.name="Dagger hello world"
        Toast.makeText(this, user.name, Toast.LENGTH_SHORT).show()
    }

}


/**
 * 在User @Inject constructor(),就说明User类，是被Inject调用。
 */
class User @Inject constructor()
{
    var name:String?=null
    override fun toString(): String {
        return "Name:$name"
    }
}

/**
 * 在MainComponent的接口中，就说明了在MainActivity中被调用
 */
@Component
public interface MainComponent{
    fun inject(activity: MainActivity)
}