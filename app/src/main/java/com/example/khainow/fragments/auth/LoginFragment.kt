package com.example.khainow.fragments.auth

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
import com.example.khainow.databinding.FragmentLoginBinding
import com.example.khainow.utils.DataState
import com.facebook.CallbackManager
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
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
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        callbackManager = CallbackManager.Factory.create()

        googleProviderSdk  = GoogleProviderSdk(authViewModel, this@LoginFragment)

       with(binding) {

           btnLoginFb.setOnClickListener {

               authProviderSdk = AuthProviderSdk(authViewModel, this@LoginFragment)

               authProviderSdk.authuser()

               observer()

           }

           btnLoginGoogle.setOnClickListener {

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

                       observer()

                   } catch (error: Exception) {

                       Toast.makeText(
                           requireContext(),
                           "Registration ${error.message}",
                           Toast.LENGTH_LONG
                       ).show()
                   }

               }

           }

       }
        return binding.root
    }
    private fun observer() {

        viewLifecycleOwner.lifecycleScope.launch {

            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){

                authViewModel.authState.collect { state->

                    when (state) {
                        is DataState.Error -> {
                            binding.progressBarLogin.visibility = View.GONE
                            Toast.makeText(requireContext(), "${state.massage}", Toast.LENGTH_LONG).show()
                        }
                        is DataState.Loading -> {
                            binding.progressBarLogin.visibility = View.VISIBLE
                        }
                        is DataState.Success -> {
                            binding.progressBarLogin.visibility = View.GONE
                            Toast.makeText(requireContext(), "successfully", Toast.LENGTH_LONG).show()
                            findNavController().navigate(R.id.action_loginFragment_to_userRoleFragment)

                        }
                        null -> {
                            binding.progressBarLogin.visibility = View.GONE
                        }
                    }
                }

                }
            }
        }
    }