package com.example.khainow.di

import androidx.annotation.IntDef
import com.example.khainow.auth.AuthRepo
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides

    @Singleton

    fun firebaseauth(): FirebaseAuth{

       return FirebaseAuth.getInstance()

    }

    @Provides

    @Singleton

    fun authrepo(): AuthRepo{

        return AuthRepo(firebaseauth())

    }

}