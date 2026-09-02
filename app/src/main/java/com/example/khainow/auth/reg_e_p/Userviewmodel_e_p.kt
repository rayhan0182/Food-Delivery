package com.example.khainow.auth.reg_e_p
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.khainow.auth.AuthRepo
import com.google.firebase.auth.AuthResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

 @HiltViewModel
 class Userviewmodel_e_p
 @Inject
 constructor(private val authrepo: AuthRepo) : ViewModel() {


     fun adduser(user: User_e_p){


         viewModelScope.launch {

             try {

                 val result: AuthResult = authrepo.register(user)



             }catch (massages: Exception){



             }



         }


     }



}