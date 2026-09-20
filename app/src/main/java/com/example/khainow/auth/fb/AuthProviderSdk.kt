package com.example.khainow.auth.fb

import android.content.Context
import android.content.Intent
import android.os.Build
import android.text.ShowSecretsSetting.registerCallback
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.credentials.CredentialManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.khainow.auth.AuthService
import com.example.khainow.auth.AuthViewModel
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider
import kotlin.getValue

class AuthProviderSdk(private val authViewModel: AuthViewModel,

    private val fragment: Fragment

    ) {

    private lateinit var  callbackManager: CallbackManager


    @RequiresApi(Build.VERSION_CODES.CINNAMON_BUN)
    fun authuser(){

        callbackManager = CallbackManager.Factory.create()

        LoginManager.getInstance().logInWithReadPermissions(fragment,callbackManager,listOf("public_profile"))

        LoginManager.getInstance()
            .registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult) {

                    val credential = FacebookAuthProvider.getCredential(result.accessToken.token)

                    Log.d("TAG", "Facebook login success: ${credential}")
                    authViewModel.signInWithFacebook(credential)
                }

                override fun onCancel() {
                    Log.d("RegistrationFragment", "Facebook registration cancelled")
                }

                override fun onError(error: FacebookException) {
                    Log.e("RegistrationFragment", "Facebook registration error", error)

                }
            })

    }

    fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        callbackManager.onActivityResult(
            requestCode,
            resultCode,
            data
        )
    }




}