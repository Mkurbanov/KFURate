package com.mkurbanov.kfurate.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.mkurbanov.kfurate.R
import com.mkurbanov.kfurate.data.config
import com.mkurbanov.kfurate.data.local.UserPreferencesRepository
import com.mkurbanov.kfurate.dataStore
import com.mkurbanov.kfurate.ui.MainActivity
import com.mkurbanov.kfurate.ui.login.LoginActivity
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    private val prefRepository: UserPreferencesRepository by lazy { UserPreferencesRepository(dataStore, this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        lifecycleScope.launch {
            prefRepository.userPreferencesFlow.collect(){

                if (it.phone.isEmpty())
                    startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                else {
                    config.TOKEN = it.token
                    startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                }

                this@SplashActivity.finish()
            }
        }




    }
}