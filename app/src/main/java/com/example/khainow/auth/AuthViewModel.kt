package com.example.khainow.auth
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.khainow.auth.reg_e_p.User_e_p
import com.example.khainow.auth.repository.AuthRepo
import com.example.khainow.utils.DataState
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepo: AuthRepo
) : ViewModel() {

    private val _authState = MutableStateFlow<DataState<AuthResult>?>(null)
    val authState: StateFlow<DataState<AuthResult>?> = _authState.asStateFlow()

   fun userRegistration(userm: User_e_p) {
        viewModelScope.launch {
            _authState.value = DataState.Loading()
            try {
                val uresult = authRepo.register(userm)
                _authState.value = DataState.Success(uresult)
            } catch (error: Exception) {
                _authState.value = DataState.Error(error.message.toString())
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = DataState.Loading()
            try {
                val result = authRepo.login(email, password)
                _authState.value = DataState.Success(result)
            } catch (error: Exception) {
                _authState.value = DataState.Error(error.message.toString())
            }
        }
    }

    fun signInWithGoogle(credential: AuthCredential) {
        viewModelScope.launch {
            _authState.value = DataState.Loading()
            try {
                val result = authRepo.signInWithGoogle(credential)
                _authState.value = DataState.Success(result)
            } catch (error: Exception) {
                _authState.value = DataState.Error(error.message.toString())
            }
        }
    }

    fun signInWithFacebook(credential: AuthCredential) {
        viewModelScope.launch {
            _authState.value = DataState.Loading()
            try {
                val result = authRepo.signInWithFacebook(credential)
                _authState.value = DataState.Success(result)
            } catch (error: Exception) {
                _authState.value = DataState.Error(error.message.toString())
            }
        }
    }

    fun resetState() {
        _authState.value = null
    }
}
