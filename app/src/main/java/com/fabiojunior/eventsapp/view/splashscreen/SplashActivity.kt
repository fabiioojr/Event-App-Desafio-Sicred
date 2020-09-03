package com.fabiojunior.eventsapp.view.splashscreen

import android.content.Intent
import android.os.Handler
import android.os.Looper
import com.fabiojunior.eventsapp.R
import com.fabiojunior.eventsapp.view.base.BaseActivity
import com.fabiojunior.eventsapp.view.register.RegisterActivity

class SplashActivity : BaseActivity() {

    override fun getLayout(): Int {
        return R.layout.activity_splash
    }

    override fun init() {
        setupStatusBar()

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }, 2000)
    }
}