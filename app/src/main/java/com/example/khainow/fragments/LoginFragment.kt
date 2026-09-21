package com.example.khainow.fragments

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialException
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.khainow.R
import com.example.khainow.auth.AuthViewModel
import com.example.khainow.auth.fb.AuthProviderSdk
import com.example.khainow.databinding.FragmentLoginBinding
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
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val authViewModel: AuthViewModel by viewModels()
    private lateinit var callbackManager: CallbackManager

    private lateinit var authProviderSdk: AuthProviderSdk


    @RequiresApi(Build.VERSION_CODES.CINNAMON_BUN)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        callbackManager = CallbackManager.Factory.create()


       with(binding){

          btnLoginFb.setOnClickListener {

              authProviderSdk = AuthProviderSdk(authViewModel,this@LoginFragment)

              authProviderSdk.authuser()

              observer()

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





