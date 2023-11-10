package com.mkurbanov.kfurate.ui.login

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.auth
import com.mkurbanov.kfurate.R
import com.mkurbanov.kfurate.data.config
import com.mkurbanov.kfurate.data.local.UserPreferencesRepository
import com.mkurbanov.kfurate.data.models.Statuses
import com.mkurbanov.kfurate.data.models.User
import com.mkurbanov.kfurate.data.repository.LoginRepository
import com.mkurbanov.kfurate.ui.utils.ErrorRes
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class LoginViewModel(
    private val repository: LoginRepository,
    private val prefRepository: UserPreferencesRepository
) : ViewModel() {
    private val auth: FirebaseAuth by lazy { Firebase.auth }
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private lateinit var storedVerificationId: String
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private var ftoken: String? = null
    private lateinit var phone: String

    val error: MutableLiveData<ErrorRes> = MutableLiveData()
    val messageRes: MutableLiveData<Int> = MutableLiveData()
    val smsSend: MutableLiveData<Boolean> = MutableLiveData()
    val smsConfirm: MutableLiveData<Boolean> = MutableLiveData()
    val user: MutableLiveData<User> = MutableLiveData()


    init {
        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                messageRes.value = R.string.verif_code_senden
            }

            override fun onVerificationFailed(e: FirebaseException) {
                messageRes.value = R.string.error
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken,
            ) {
                smsSend.value = true
                storedVerificationId = verificationId
                resendToken = token
            }
        }
    }

    fun sendSMSCode(phone: String) {
        this.phone = phone
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phone) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            //.setActivity(this) // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    fun confirmSMSCode(smsCode: String) {
        val credential = PhoneAuthProvider.getCredential(
            storedVerificationId,
            smsCode
        )
        signInWithPhoneAuthCredential(credential)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(config.LOG_TAG, "signInWithCredential:success")

                    FirebaseAuth.getInstance().currentUser?.getIdToken(true)?.addOnSuccessListener {
                        Log.i(config.LOG_TAG, "firebase uid = " + it.token)

                        ftoken = it.token
                        if (ftoken != null) login(ftoken!!) else error.value = ErrorRes()
                    }


                } else {
                    Log.w(config.LOG_TAG, "signInWithCredential:failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        messageRes.value = R.string.verif_code_invalid
                    } else error.value = ErrorRes()
                }
            }
    }


    private fun login(ftoken: String) = viewModelScope.launch {
        try {
            val response = repository.login(ftoken)

            if (response.status == Statuses.ok) {

                prefRepository.updateUser(response.user)

                user.value = response.user
            } else {
                smsConfirm.value = true
            }

        } catch (e: Exception) {
            error.value = ErrorRes(e)
        }
    }

    fun registration(name: String, gender: String) = viewModelScope.launch {
        try {
            val response = repository.reg(ftoken!!, name, gender)

            if (response.status == Statuses.ok) {
                var inUser = User(name, phone, response.token, gender)
                prefRepository.updateUser(inUser)
                user.value = inUser
            } else {
                error.value = ErrorRes()
            }

        } catch (e: Exception) {
            error.value = ErrorRes(e)
        }
    }

}