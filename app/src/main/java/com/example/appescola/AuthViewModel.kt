package com.example.appescola

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val repo: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _usuario = MutableStateFlow(repo.usuarioAtual())
    val usuario: StateFlow<FirebaseUser?> = _usuario.asStateFlow()

    var carregando by androidx.compose.runtime.mutableStateOf(false)
        private set

    var erro by androidx.compose.runtime.mutableStateOf<String?>(null)
        private set

    fun limparErro() { erro = null }

    fun login(email: String, senha: String, aoConcluir: () -> Unit) {
        if (email.isBlank() || senha.isBlank()) {
            erro = "Preencha e-mail e senha."
            return
        }
        carregando = true
        viewModelScope.launch {
            runCatching { repo.login(email.trim(), senha) }
                .onSuccess {
                    _usuario.value = repo.usuarioAtual()
                    carregando = false
                    aoConcluir()
                }
                .onFailure {
                    carregando = false
                    erro = "Não foi possível entrar. Verifique e-mail e senha."
                }
        }
    }

    fun cadastrar(nome: String, email: String, senha: String, confirmar: String, aoConcluir: () -> Unit) {
        when {
            nome.isBlank() || email.isBlank() || senha.isBlank() -> erro = "Preencha todos os campos."
            senha != confirmar -> erro = "As senhas não coincidem."
            senha.length < 6 -> erro = "A senha deve ter ao menos 6 caracteres."
            else -> {
                carregando = true
                viewModelScope.launch {
                    runCatching { repo.cadastrar(nome.trim(), email.trim(), senha) }
                        .onSuccess {
                            _usuario.value = repo.usuarioAtual()
                            carregando = false
                            aoConcluir()
                        }
                        .onFailure {
                            carregando = false
                            erro = it.message ?: "Não foi possível criar a conta."
                        }
                }
            }
        }
    }

    fun redefinirSenha(email: String, aoEnviar: () -> Unit) {
        if (email.isBlank()) {
            erro = "Informe seu e-mail para redefinir a senha."
            return
        }
        viewModelScope.launch {
            runCatching { repo.redefinirSenha(email.trim()) }
                .onSuccess { aoEnviar() }
                .onFailure { erro = "Não foi possível enviar o e-mail de redefinição." }
        }
    }

    fun logout() {
        repo.logout()
        _usuario.value = null
    }
}