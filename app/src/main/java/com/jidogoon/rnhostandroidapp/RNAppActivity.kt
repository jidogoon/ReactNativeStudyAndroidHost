package com.jidogoon.rnhostandroidapp

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity
import com.facebook.react.modules.core.DefaultHardwareBackBtnHandler
import com.facebook.react.ReactInstanceManager
import com.facebook.react.ReactRootView
import com.facebook.react.common.LifecycleState
import com.facebook.react.shell.MainReactPackage
import android.view.KeyEvent.KEYCODE_MENU
import com.facebook.react.ReactPackage
import com.jidogoon.rnhostandroidapp.rn.RNPackage


class RNAppActivity : AppCompatActivity(), DefaultHardwareBackBtnHandler {
    private lateinit var reactRootView: ReactRootView
    private lateinit var reactInstanceManager: ReactInstanceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!canStartRNApp())
            finish()

        reactRootView = ReactRootView(this)
        reactInstanceManager = ReactInstanceManager.builder().apply {
            setApplication(application)
            setCurrentActivity(this@RNAppActivity)
            setBundleAssetName("index.android.bundle")
            setJSMainModulePath("rn_src/index")
            addPackages(mutableListOf<ReactPackage>().apply{
                add(MainReactPackage())
                add(RNPackage())
            })
            setUseDeveloperSupport(BuildConfig.DEBUG)
            setInitialLifecycleState(LifecycleState.RESUMED)
        }.build()

        val initialProps = Bundle().apply {
            putStringArray("names", arrayOf("name_1", "name_2", "name_3"))
        }

        reactRootView.startReactApplication(reactInstanceManager, "DorunDorun", initialProps)
        setContentView(reactRootView)
    }

    override fun onPause() {
        super.onPause()
        if (::reactInstanceManager.isInitialized)
            reactInstanceManager.onHostPause(this)
    }

    override fun onResume() {
        super.onResume()
        if (::reactInstanceManager.isInitialized)
            reactInstanceManager.onHostResume(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::reactInstanceManager.isInitialized)
            reactInstanceManager.onHostDestroy(this)
        if (::reactRootView.isInitialized)
            reactRootView.unmountReactApplication()
    }

    override fun onBackPressed() {
        if (::reactInstanceManager.isInitialized)
            reactInstanceManager.onBackPressed()
        else
            super.onBackPressed()
    }

    override fun invokeDefaultOnBackPressed() {
        super.onBackPressed()
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KEYCODE_MENU && ::reactInstanceManager.isInitialized) {
            reactInstanceManager.showDevOptionsDialog()
            return true
        }
        return super.onKeyUp(keyCode, event)
    }

    private fun canStartRNApp(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                val intent = Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName")
                )
                startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE)
                return false
            }
        }
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == OVERLAY_PERMISSION_REQ_CODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Settings.canDrawOverlays(this)) {
                    // SYSTEM_ALERT_WINDOW permission not granted
                }
            }
        }
    }

    companion object {
        private const val OVERLAY_PERMISSION_REQ_CODE = 1
    }
}