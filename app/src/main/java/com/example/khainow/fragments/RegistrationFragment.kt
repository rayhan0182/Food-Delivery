package com.example.khainow.fragments
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.credentials.GetCredentialRequest
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.khainow.R
import com.example.khainow.auth.AuthViewModel
import com.example.khainow.auth.fb.AuthProviderSdk
import com.example.khainow.auth.google.GoogleProviderSdk
import com.example.khainow.auth.reg_e_p.User_e_p
import com.example.khainow.databinding.FragmentRegistrationBinding
import com.example.khainow.utils.DataState
import com.facebook.CallbackManager
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@Suppress("DEPRECATION")
@AndroidEntryPoint
class RegistrationFragment : Fragment() {

    private var _binding: FragmentRegistrationBinding? = null
    private val binding get() = _binding!!

    private val authViewModel: AuthViewModel by viewModels()


    private lateinit var callbackManager: CallbackManager

    private lateinit var authProviderSdk: AuthProviderSdk

    private lateinit var googleProviderSdk: GoogleProviderSdk

    @RequiresApi(Build.VERSION_CODES.CINNAMON_BUN)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegistrationBinding.inflate(inflater, container, false)

        googleProviderSdk  = GoogleProviderSdk(authViewModel,this@RegistrationFragment)

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

                        }
                        null -> {
                            binding.progressBar.visibility = View.GONE
                        }
                    }
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.CINNAMON_BUN)
    private fun setupUI() {
        with(binding) {
            tvLoginLink.setOnClickListener {
                findNavController().navigate(R.id.action_registrationFragment_to_loginFragment)
            }

            btnGoogleReg.setOnClickListener {

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

                        googleProviderSdk.googleuser(request)

                        observe()

                    }catch (error: Exception){

                        Toast.makeText(requireContext(),"Registration ${error.message}", Toast.LENGTH_LONG).show()
                    }



                }
            }

            btnFacebookReg.setOnClickListener {

                authProviderSdk = AuthProviderSdk(authViewModel, fragment = this@RegistrationFragment)

                authProviderSdk.authuser()

                observe()
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


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Pass the activity result back to the Facebook SDK
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }


}
