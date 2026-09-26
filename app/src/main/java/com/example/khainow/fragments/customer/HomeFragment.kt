package com.example.khainow.fragments.customer

import android.Manifest
import android.Manifest.permission
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.khainow.R
import com.example.khainow.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import java.security.Permission

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

     private val locationpermissionlauncher =  registerForActivityResult(

         ActivityResultContracts.RequestMultiplePermissions())

      {permission->


          val fineLocation =
              permission[Manifest.permission.ACCESS_FINE_LOCATION] ?: false

          val caorselocation = permission[Manifest.permission.ACCESS_FINE_LOCATION]?:false

          if (fineLocation||caorselocation){

              Toast.makeText(requireContext(),"permission granted", Toast.LENGTH_LONG).show()

          }else{

              Toast.makeText(requireContext(),"permission granted", Toast.LENGTH_LONG).show()

          }


      }




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.btnViewRestaurants.setOnClickListener {

            locationpermissionlauncher.launch(

                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)

            )
        }

        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
