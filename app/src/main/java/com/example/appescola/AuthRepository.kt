package com.example.appescola

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.tasks.await

class AuthRepository {

    private val auth = FirebaseAuth.getInstance()

    fun usuarioAtual(): FirebaseUser? = auth.currentUser

    suspend fun login(email: String, senha: String) {
        auth.signInWithEmailAndPassword(email, senha).await()
    }

    suspend fun cadastrar(nome: String, email: String, senha: String) {
        val resultado = auth.createUserWithEmailAndPassword(email, senha).await()
        resultado.user?.updateProfile(
            UserProfileChangeRequest.Builder().setDisplayName(nome).build()
        )?.await()
    }

    suspend fun redefinirSenha(email: String) {
        auth.sendPasswordResetEmail(email).await()
    }

    fun logout() {
        auth.signOut()
    }
}
