package com.example.testdagger2

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Inject
import javax.inject.Qualifier
import javax.inject.Singleton


class MainActivity : AppCompatActivity() {

    /**
     * Inject 创建直接创建对象的接口
     */
    /*@Inject
    lateinit var user:User*/

    /**
     * 这里写上@Named("age28")，就说明调用UserModule中的 @Named("age28")
     */
    @Inject
    @YoungUser
    lateinit var user: User


    /**
     * Inject 再创建直接创建对象的接口
     */
    @Inject
    lateinit var dog: Dog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /**
         *  这里的DaggerMainComponent
         *  生成 interface MainComponent 的 Dagger对象
         */
        DaggerMainComponent.builder().build().inject(this)

        /**
         *  然后这里就可以直接使用了，而不需要再去新建成对象了。
         *  目的就是防止多次创建对象，而让程序简洁。
         */
        user.name = "Dagger hello world"
        Toast.makeText(this, user.name, Toast.LENGTH_SHORT).show()


        /**
         * 这里就说明了，不用初始化，也可以直接调用
         */
        dog.name = "it is dog name"

        /**
         * Dog @Inject constructor(var owner:User)
         * 的owner被赋值了
         */
        dog.owner.name = "主人"
        Log.e("TAG", "dog.name: " + dog.name)

        /**
         * dog.toString(),在这里就说明了会调用 class Dog 的 fun toString()
         */
        Log.e("TAG", "dog: $dog")


        /**
         * dog.toString(),在这里就说明了会调用 class Dog 的 fun toString()
         */
        Log.e("TAG", "user 调用override fun toString(): : $user")
    }

}

/**
 * @Module，修饰的类中所定义的有@Provides修饰的方法提供
 */
/*@Module
class UserModule{
    @Singleton
    @Provides
    fun provideUser():User
    {
        */
/**
 * 说明了，调用provideUser()后，就表示初始化的构造值为User(28)
 *//*
        return User(28)
    }
}*/

/**
 * 申明注解 YoungUser
 */
@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class YoungUser


/**
 * 申明注解 OldUser
 */
@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class OldUser

@Module
class UserModule {
    @Provides
    fun provideUserCommon(): User {
        return User(18)
    }

    /**
     * 定义注解 YoungUser 的作用，就是使用provideUserYoung()
     */
    @Provides
    @YoungUser
    fun provideUserYoung(): User {
        return User(28)
    }

    /**
     * 定义注解 OldUser 的作用，就是使用provideUserOld()
     */
    @Provides
    @OldUser
    fun provideUserOld(): User {
        return User(38)
    }

}


/**
 * 在User @Inject constructor(),就说明User类，是被Inject调用。
 */
/*class User @Inject constructor()
{
    var name:String?=null
    override fun toString(): String {
        return "Name:$name"
    }
}*/

/**
 * 在User @Inject constructor()中，加上构造值(var age:Int)，
 * 需要在interface MainComponent中，加上@Component(modules = [UserModule::class])
 */
class User @Inject constructor(var age: Int) {
    lateinit var name: String
    override fun toString(): String {
        return "Name:$name, Age:$age"
    }
}

/*class Dog @Inject constructor(var owner:User)
{
    lateinit var name: String
    override fun toString(): String {
        return "Dog class:$name, Owner:${owner.name}"
    }
}*/

/**
 * 因为这里constructor(@Named("age38") var owner: User)是调用了@Named("age38")
 * 所以当前的owner: User被调用时，会用上@Named("age38")对应的UserModule中fun provideUserOld()
 */
class Dog @Inject constructor(@OldUser var owner: User) {
    lateinit var name: String
    override fun toString(): String {
        return "Dog:$name, User:${owner.name} User Age:${owner.age}"
    }
}

/**
 * 在MainComponent的接口中，就说明了在MainActivity中被调用
 */
/*@Component
public interface MainComponent{
    fun inject(activity: MainActivity)
}*/


/**
 * 在User @Inject constructor()中，加上构造值，
 * 需要在interface MainComponent中，加上@Component(modules = [UserModule::class])
 *
 * @Component(modules = [UserModule::class])，
 * 调了provideUser()，所以初始化的构造值为User(28)
 */
@Singleton
@Component(modules = [UserModule::class])
interface MainComponent {
    fun inject(activity: MainActivity)
}