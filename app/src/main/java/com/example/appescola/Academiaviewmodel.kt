package com.example.appescola

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AcademiaViewModel(
    private val repo: AcademiaRepository = AcademiaRepository()
) : ViewModel() {

    val alunos: StateFlow<List<Aluno>> = repo.alunosFlow()
        .catch { emit(emptyList()) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val aulas: StateFlow<List<Aula>> = repo.aulasFlow()
        .catch { emit(emptyList()) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val presencas: StateFlow<List<Presenca>> = repo.presencasFlow()
        .catch { emit(emptyList()) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val professores: StateFlow<List<Professor>> = repo.professoresFlow()
        .catch { emit(emptyList()) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val modalidades: StateFlow<List<Modalidade>> = repo.modalidadesFlow()
        .catch { emit(emptyList()) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun evolucoes(alunoId: String): Flow<List<Evolucao>> =
        repo.evolucoesFlow(alunoId).catch { emit(emptyList()) }

    fun salvarAluno(a: Aluno) = viewModelScope.launch { runCatching { repo.salvarAluno(a) } }
    fun excluirAluno(id: String) = viewModelScope.launch { runCatching { repo.excluirAluno(id) } }

    fun salvarAula(a: Aula) = viewModelScope.launch { runCatching { repo.salvarAula(a) } }
    fun excluirAula(id: String) = viewModelScope.launch { runCatching { repo.excluirAula(id) } }

    fun salvarChamada(lista: List<Presenca>) = viewModelScope.launch { runCatching { repo.salvarChamada(lista) } }

    fun salvarEvolucao(e: Evolucao) = viewModelScope.launch { runCatching { repo.salvarEvolucao(e) } }
    fun excluirEvolucao(id: String) = viewModelScope.launch { runCatching { repo.excluirEvolucao(id) } }

    fun salvarProfessor(p: Professor) = viewModelScope.launch { runCatching { repo.salvarProfessor(p) } }
    fun excluirProfessor(id: String) = viewModelScope.launch { runCatching { repo.excluirProfessor(id) } }

    fun salvarModalidade(m: Modalidade) = viewModelScope.launch { runCatching { repo.salvarModalidade(m) } }
    fun excluirModalidade(id: String) = viewModelScope.launch { runCatching { repo.excluirModalidade(id) } }
}
