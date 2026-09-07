package com.example.khainow.auth
import com.example.khainow.auth.reg_e_p.User_e_p
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepo @Inject constructor(private val firebaseAuth: FirebaseAuth) : AuthService {

    override suspend fun register(userEP: User_e_p): AuthResult {
        return firebaseAuth.createUserWithEmailAndPassword(
            userEP.email,
            userEP.password
        ).await()
    }


}