package com.example.khainow.auth.reg_e_p
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.khainow.auth.AuthRepo
import com.example.khainow.utils.DataState
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class Userviewmodel_e_p @Inject constructor(
    private val authrepo: AuthRepo
) : ViewModel() {

    private val _userRegistrationState = MutableStateFlow<DataState<AuthResult>?>(null)
    val userRegistrationState: StateFlow<DataState<AuthResult>?> = _userRegistrationState


    fun registerUser(user: User_e_p) {
        viewModelScope.launch {
            _userRegistrationState.value = DataState.Loading
            try {
                val result = authrepo.register(user)
                _userRegistrationState.value = DataState.Success(result)
            } catch (e: Exception) {
                _userRegistrationState.value = DataState.Error(e.message ?: "Registration failed")
            }
        }
    }



    fun resetState() {
        _userRegistrationState.value = null
    }
}