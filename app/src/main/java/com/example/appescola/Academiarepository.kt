package com.example.appescola

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

class AcademiaRepository {

    private val db = FirebaseFirestore.getInstance()
    private val alunos = db.collection("alunos")
    private val aulas = db.collection("aulas")
    private val presencas = db.collection("presencas")
    private val evolucoes = db.collection("evolucoes")
    private val professores = db.collection("professores")
    private val modalidades = db.collection("modalidades")

    private fun <T : Any> escutar(q: Query, clazz: Class<T>): Flow<List<T>> = callbackFlow {
        val reg = q.addSnapshotListener { snap, erro ->
            if (erro != null) {
                close(erro)
                return@addSnapshotListener
            }
            trySend(snap?.toObjects(clazz) ?: emptyList())
        }
        awaitClose { reg.remove() }
    }

    // ---------- ALUNOS ----------
    fun alunosFlow(): Flow<List<Aluno>> =
        escutar(alunos, Aluno::class.java).map { l -> l.sortedBy { it.nome.lowercase() } }

    suspend fun salvarAluno(a: Aluno) {
        val ref = if (a.id.isBlank()) alunos.document() else alunos.document(a.id)
        ref.set(a).await()
    }

    suspend fun excluirAluno(id: String) {
        val batch = db.batch()
        evolucoes.whereEqualTo("alunoId", id).get().await().documents.forEach { batch.delete(it.reference) }
        presencas.whereEqualTo("alunoId", id).get().await().documents.forEach { batch.delete(it.reference) }
        batch.delete(alunos.document(id))
        batch.commit().await()
    }

    // ---------- AULAS ----------
    fun aulasFlow(): Flow<List<Aula>> =
        escutar(aulas, Aula::class.java).map { l -> l.sortedBy { it.nome.lowercase() } }

    suspend fun salvarAula(a: Aula) {
        val ref = if (a.id.isBlank()) aulas.document() else aulas.document(a.id)
        ref.set(a).await()
    }

    suspend fun excluirAula(id: String) {
        aulas.document(id).delete().await()
    }

    // ---------- PRESENÇAS ----------
    fun presencasFlow(): Flow<List<Presenca>> =
        escutar(presencas, Presenca::class.java).map { l -> l.sortedByDescending { it.data } }

    /** ID determinístico (aula_aluno_dia) evita duplicar chamada no mesmo dia. */
    suspend fun salvarChamada(lista: List<Presenca>) {
        val batch = db.batch()
        lista.forEach { p ->
            batch.set(presencas.document("${p.aulaId}_${p.alunoId}_${p.dia}"), p)
        }
        batch.commit().await()
    }

    // ---------- EVOLUÇÃO ----------
    fun evolucoesFlow(alunoId: String): Flow<List<Evolucao>> =
        escutar(evolucoes.whereEqualTo("alunoId", alunoId), Evolucao::class.java)
            .map { l -> l.sortedBy { it.data } }

    suspend fun salvarEvolucao(e: Evolucao) {
        val ref = if (e.id.isBlank()) evolucoes.document() else evolucoes.document(e.id)
        ref.set(e).await()
    }

    suspend fun excluirEvolucao(id: String) {
        evolucoes.document(id).delete().await()
    }

    // ---------- PROFESSORES ----------
    fun professoresFlow(): Flow<List<Professor>> =
        escutar(professores, Professor::class.java).map { l -> l.sortedBy { it.nome.lowercase() } }

    suspend fun salvarProfessor(p: Professor) {
        val ref = if (p.id.isBlank()) professores.document() else professores.document(p.id)
        ref.set(p).await()
    }

    suspend fun excluirProfessor(id: String) {
        professores.document(id).delete().await()
    }

    // ---------- MODALIDADES ----------
    fun modalidadesFlow(): Flow<List<Modalidade>> =
        escutar(modalidades, Modalidade::class.java).map { l -> l.sortedBy { it.nome.lowercase() } }

    suspend fun salvarModalidade(m: Modalidade) {
        val ref = if (m.id.isBlank()) modalidades.document() else modalidades.document(m.id)
        ref.set(m).await()
    }

    suspend fun excluirModalidade(id: String) {
        modalidades.document(id).delete().await()
    }
}
