package com.example.khainow.auth.google
import android.util.Log
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.fragment.app.Fragment
import com.example.khainow.auth.AuthViewModel
import com.example.khainow.fragments.RegistrationFragment
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
import com.google.firebase.auth.GoogleAuthProvider


class GoogleProviderSdk(private val authViewModel: AuthViewModel,val fragment: Fragment) {

    lateinit var credentialManager: CredentialManager


  suspend  fun googleuser(request: GetCredentialRequest) {

        credentialManager = CredentialManager.create(fragment.requireContext())

        val result = credentialManager.getCredential(

            context = fragment.requireContext(),

            request = request


        )


       handlesignIn(result.credential)

    }

    private fun handlesignIn(credential: Credential) {

        // Check if credential is of type Google ID
        if (credential is CustomCredential && credential.type == TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {

            val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)

            val idtoken = googleIdTokenCredential.idToken

            val credential = GoogleAuthProvider.getCredential(idtoken,null)

            authViewModel.signInWithGoogle(credential)

        } else {
            Log.d("TAG", "Credential is not of type Google ID!")
        }

    }


}



