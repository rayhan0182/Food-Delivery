package com.example.khainow.fragments.auth

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.khainow.activity.CustomerActivity
import com.example.khainow.databinding.FragmentUserRoleBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserRoleFragment : Fragment() {

    private var _binding: FragmentUserRoleBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserRoleBinding.inflate(inflater, container, false)

        binding.cardUser.setOnClickListener {
            val intent = Intent(requireContext(), CustomerActivity::class.java)
            startActivity(intent)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}