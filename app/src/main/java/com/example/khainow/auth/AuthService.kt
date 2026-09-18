package com.example.khainow.auth
import com.example.khainow.auth.reg_e_p.User_e_p
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult

interface AuthService {
    suspend fun register(userEP: User_e_p): AuthResult
    suspend fun login(email: String, password: String): AuthResult
    suspend fun signInWithGoogle(credential: AuthCredential): AuthResult
    suspend fun signInWithFacebook(credential: AuthCredential): AuthResult
}