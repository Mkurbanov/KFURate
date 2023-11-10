package com.mkurbanov.kfurate.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import com.google.android.material.snackbar.Snackbar
import com.mkurbanov.kfurate.R
import com.mkurbanov.kfurate.data.local.UserPreferencesRepository
import com.mkurbanov.kfurate.data.models.Genders
import com.mkurbanov.kfurate.data.network.ApiHelper
import com.mkurbanov.kfurate.data.network.RetrofitBuilder
import com.mkurbanov.kfurate.data.repository.LoginRepository
import com.mkurbanov.kfurate.dataStore
import com.mkurbanov.kfurate.databinding.ActivityLoginBinding
import com.mkurbanov.kfurate.ui.splash.SplashActivity

class LoginActivity : AppCompatActivity() {

    private val binding: ActivityLoginBinding by lazy { ActivityLoginBinding.inflate(layoutInflater) }
    private val repository: LoginRepository by lazy { LoginRepository(ApiHelper(RetrofitBuilder.apiService)) }
    private val prefRepository: UserPreferencesRepository by lazy {
        UserPreferencesRepository(
            dataStore,
            this
        )
    }
    private val viewmodel: LoginViewModel by lazy {
        LoginViewModelFactory(repository, prefRepository).create(
            LoginViewModel::class.java
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        viewmodel.messageRes.observe(this) {
            binding.button.isEnabled = true
            Snackbar.make(binding.root, getString(it), Snackbar.LENGTH_SHORT).show()
        }

        viewmodel.error.observe(this) {
            binding.button.isEnabled = true
            Snackbar.make(binding.root, it.message, Snackbar.LENGTH_SHORT).show()
        }

        viewmodel.smsSend.observe(this) {
            binding.button.isEnabled = true
            binding.editTextSMScode.visibility = View.VISIBLE
        }

        viewmodel.smsConfirm.observe(this) {
            binding.button.isEnabled = true
            binding.smsVerifContainer.visibility = View.INVISIBLE
            binding.infoVerifContainer.visibility = View.VISIBLE
            binding.button.updateLayoutParams<ConstraintLayout.LayoutParams> {
                topToBottom = binding.infoVerifContainer.id
            }
        }

        viewmodel.user.observe(this) {
            startActivity(Intent(this, SplashActivity::class.java))
            finish()
        }


        binding.button.setOnClickListener {

            if (binding.infoVerifContainer.visibility == View.VISIBLE) {

                if (binding.editTextName.text.isEmpty()) {
                    binding.editTextName.error = getString(R.string.field_is_empty)
                    return@setOnClickListener
                }

                val gender =
                    if (binding.radioGroup.checkedRadioButtonId == R.id.radio_boy) Genders.man else Genders.woman
                viewmodel.registration(binding.editTextName.text.toString(), gender)

            } else {

                if (binding.editTextSMScode.visibility == View.VISIBLE) {

                    if (binding.editTextSMScode.text.isEmpty()) {
                        binding.editTextSMScode.error = getString(R.string.field_is_empty)
                        return@setOnClickListener
                    }

                    viewmodel.confirmSMSCode(binding.editTextSMScode.text.toString())
                } else {

                    if (binding.editTextPhone.text.isEmpty()) {
                        binding.editTextPhone.error = getString(R.string.field_is_empty)
                        return@setOnClickListener
                    } else viewmodel.sendSMSCode(binding.editTextPhone.text.toString())

                }

            }

            binding.button.isEnabled = false
        }

    }
}