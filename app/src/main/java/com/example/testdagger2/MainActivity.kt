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


    /**
     * Inject 再创建直接创建对象的接口
     */
    @Inject lateinit var dog:Dog

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


        /**
         * 这里就说明了，不用初始化，也可以直接调用
         */
        dog.name="it is dog name"

        /**
         * Dog @Inject constructor(var owner:User)
         * 的owner被赋值了
         */
        dog.owner.name="主人"
        Log.e("TAG", "dog.name: "+dog.name )

        /**
         * dog.toString(),在这里就说明了会调用 class Dog 的 fun toString()
         */
        Log.e("TAG", "dog: ${dog.toString()}")
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

class Dog @Inject constructor(var owner:User)
{
    lateinit var name: String
    override fun toString(): String {
        return "Dog class:$name, Owner:${owner.name}"
    }
}

/**
 * 在MainComponent的接口中，就说明了在MainActivity中被调用
 */
@Component
public interface MainComponent{
    fun inject(activity: MainActivity)
}