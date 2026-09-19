package com.example.khainow.fragments
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.khainow.R
import com.example.khainow.databinding.FragmentStartBinding
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.reflect.Constructor
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds
@AndroidEntryPoint
class StartFragment : Fragment() {

     @Inject
     lateinit var firebaseAuth: FirebaseAuth

    private var _binding: FragmentStartBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStartBinding.inflate(inflater, container, false)

        lifecycleScope.launch {

            delay(2000.milliseconds)

            currentusercheak()

        }
        return binding.root
    }

    private fun currentusercheak() {

        if (firebaseAuth.currentUser != null) {

            findNavController().navigate(R.id.action_startFragment_to_userRoleFragment)

        } else {
            findNavController().navigate(R.id.action_startFragment_to_registrationFragment)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}