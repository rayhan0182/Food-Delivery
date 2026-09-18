package com.example.khainow.fragments
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.khainow.R
import com.example.khainow.auth.AuthViewModel
import com.example.khainow.auth.reg_e_p.User_e_p
import com.example.khainow.databinding.FragmentRegistrationBinding
import com.example.khainow.utils.DataState
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegistrationFragment : Fragment() {

    private var _binding: FragmentRegistrationBinding? = null
    private val binding get() = _binding!!

    private val authViewModel: AuthViewModel by viewModels()

    lateinit var credentialManager: CredentialManager
    private lateinit var callbackManager: CallbackManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegistrationBinding.inflate(inflater, container, false)

        credentialManager = CredentialManager.create(requireContext())
        callbackManager = CallbackManager.Factory.create()

        // Register Facebook Callback here
        LoginManager.getInstance()
            .registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult) {
                Log.d("RegistrationFragment", "Facebook login success: ${result.accessToken.token}")
                val credential = FacebookAuthProvider.getCredential(result.accessToken.token)
                authViewModel.signInWithFacebook(credential)
            }

            override fun onCancel() {
                Log.d("RegistrationFragment", "Facebook registration cancelled")
            }

            override fun onError(error: FacebookException) {
                Log.e("RegistrationFragment", "Facebook registration error", error)
                Toast.makeText(requireContext(), "Facebook registration failed: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })

        setupUI()
        observe()

        return binding.root
    }

    fun observe() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                authViewModel.authState.collect { state ->
                    when (state) {
                        is DataState.Error -> {
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(requireContext(), "${state.massage}", Toast.LENGTH_LONG).show()
                        }
                        is DataState.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }
                        is DataState.Success -> {
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(requireContext(), "successfully created", Toast.LENGTH_LONG).show()
                            findNavController().navigate(R.id.action_registrationFragment_to_userRoleFragment)
                        }
                        null -> {
                            binding.progressBar.visibility = View.GONE
                        }
                    }
                }
            }
        }
    }

    private fun setupUI() {
        with(binding) {
            tvLoginLink.setOnClickListener {
                findNavController().navigate(R.id.action_registrationFragment_to_loginFragment)
            }

            btnGoogleReg.setOnClickListener {
                googlesignup()
            }

            btnFacebookReg.setOnClickListener {
                facebooksignup()
            }

            btnRegister.setOnClickListener {
                val name = etName.text.toString().trim()
                val email = etEmail.text.toString().trim()
                val password = etPassword.text.toString().trim()

                if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                } else {
                    val userdata = User_e_p(name, email, password, "")
                    authViewModel.userRegistration(userdata)
                }
            }
        }
    }

    fun facebooksignup() {
        LoginManager.getInstance().logInWithReadPermissions(this, callbackManager, listOf("public_profile"))
    }

    @SuppressLint("SuspiciousIndentation")
    fun googlesignup() {

        val googleIdoptions = GetGoogleIdOption.Builder()

            .setServerClientId(getString(R.string.default_web_client_id))

            .setFilterByAuthorizedAccounts(false)

            .setAutoSelectEnabled(true)

            .build()

        val request = GetCredentialRequest.Builder()

            .addCredentialOption(googleIdoptions)

            .build()

           lifecycleScope.launch {

               try {

                   val result = credentialManager.getCredential(

                       context = requireContext(),

                       request = request)

                       val credential = result.credential

                   if (credential is CustomCredential&&credential.type== GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL){

                       try {

                           val googleCredential =
                               GoogleIdTokenCredential
                                   .createFrom(credential.data)


                           val idToken =
                               googleCredential.idToken

                           val credentiall =
                               GoogleAuthProvider.getCredential(
                                   idToken,
                                   null
                               )

                              authViewModel.signInWithGoogle(credentiall)




                       }catch (error: Exception){

                           Toast.makeText(
                               requireContext(),
                               "${error.message}",
                               Toast.LENGTH_LONG
                           ).show()

                       }

                   }else{

                       Toast.makeText(
                           requireContext(),
                           "Unexpected credential type",
                           Toast.LENGTH_LONG
                       ).show()

                   }



               }catch (error: Exception){

                   Toast.makeText(
                       requireContext(),
                       error.message ?: "Google sign-in failed",
                       Toast.LENGTH_LONG
                   ).show()

               }

           }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Pass the activity result back to the Facebook SDK
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }


}
