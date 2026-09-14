package com.example.khainow.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.view.isVisible
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
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegistrationFragment : Fragment() {

    private var _binding: FragmentRegistrationBinding? = null
    private val binding get() = _binding!!

    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegistrationBinding.inflate(inflater, container, false)

        setupUI()
        observe()


        return binding.root
    }

     fun observe() {


         viewLifecycleOwner.lifecycleScope.launch {
             viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                 authViewModel.authState.collect { state ->

                   when(state){
                       is DataState.Error ->{

                           binding.progressBar.visibility = View.VISIBLE

                           Toast.makeText(requireContext(),"${state.massage}", Toast.LENGTH_LONG).show()
                       }
                       is DataState.Loading->{

                           binding.progressBar.visibility = View.VISIBLE

                       }
                       is DataState.Success->{

                           binding.progressBar.visibility = View.GONE

                           Toast.makeText(requireContext(),"successfully created", Toast.LENGTH_LONG).show()

                       }

                       null -> null
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
            btnRegister.setOnClickListener {
                val name = etName.text.toString().trim()
                val email = etEmail.text.toString().trim()
                val password = etPassword.text.toString().trim()

                if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }else{

                    val userdata = User_e_p(name,email,password,"")

                    authViewModel.userRegistration(userdata)

                }


            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
