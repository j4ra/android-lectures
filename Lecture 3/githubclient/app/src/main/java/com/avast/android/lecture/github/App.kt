package com.avast.android.lecture.github

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationManagerCompat
import com.facebook.flipper.android.AndroidFlipperClient
import com.facebook.flipper.android.utils.FlipperUtils
import com.facebook.flipper.plugins.inspector.DescriptorMapping
import com.facebook.flipper.plugins.inspector.InspectorFlipperPlugin
import com.facebook.flipper.plugins.sharedpreferences.SharedPreferencesFlipperPlugin
import com.facebook.soloader.SoLoader

/**
 * Application singleton. It's started before any [android.app.Activity], [android.app.Service] or
 * [android.content.BroadcastReceiver]. Ideal place to initialize 3rd party libraries or build
 * dependency injection graph.
 *
 * Don't forget to register your custom [Application] in AndroidManifest.xml
 */
class App: Application() {

    val settings: Settings by lazy {
        Settings.getInstance(this)
    }

    /**
     * This is called first when the application is starting. Avoid long running operations here.
     * No UI is displayed until this finishes.
     */
    override fun onCreate() {
        super.onCreate()

        createNotificationChannel()
        initFlipper()
    }

    /**
     * Initializes stetho library for inspecting share preferences, views etc.
     *
     * http://facebook.github.io/stetho/
     */
    private fun initFlipper() {
        SoLoader.init(this, false);

        if (BuildConfig.DEBUG && FlipperUtils.shouldEnableFlipper(this)) {
            val client = AndroidFlipperClient.getInstance(this)
            client.addPlugin(InspectorFlipperPlugin(this, DescriptorMapping.withDefaults()))
            client.addPlugin(SharedPreferencesFlipperPlugin(this, Settings.APP_PREFERENCES))
            client.start();
        }
    }

    private fun createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= 26) {
            Log.d(null,"creating notification channel")
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_MAIN_ID,
                NOTIFICATION_CHANNEL_MAIN_DESCRIPTION,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = NOTIFICATION_CHANNEL_MAIN_DESCRIPTION
            }
            NotificationManagerCompat.from(this).createNotificationChannel(channel)
        }
    }

    companion object {
        val NOTIFICATION_CHANNEL_MAIN_ID = "channel_main"
        val NOTIFICATION_CHANNEL_MAIN_DESCRIPTION = "Login channel"

        val NOTIFICATION_HELLO_WORLD_ID = 42
    }
}