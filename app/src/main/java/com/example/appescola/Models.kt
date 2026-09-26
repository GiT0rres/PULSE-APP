package com.example.appescola

import com.google.firebase.firestore.DocumentId

data class Aluno(
    @DocumentId val id: String = "",
    val nome: String = "",
    val email: String = "",
    val telefone: String = "",
    val dataNascimento: String = "",
    val modalidade: String = "",
    val plano: String = "Mensal",
    val ativo: Boolean = true,
    val dataMatricula: Long = System.currentTimeMillis()
)

data class Aula(
    @DocumentId val id: String = "",
    val nome: String = "",
    val modalidade: String = "",
    val instrutor: String = "",
    val diaSemana: String = "Segunda",
    val horario: String = "",
    val duracaoMin: Int = 60
)

data class Presenca(
    @DocumentId val id: String = "",
    val alunoId: String = "",
    val alunoNome: String = "",
    val aulaId: String = "",
    val aulaNome: String = "",
    val dia: String = "",          // yyyy-MM-dd
    val data: Long = System.currentTimeMillis(),
    val presente: Boolean = true,
    val observacao: String = ""
)

data class Evolucao(
    @DocumentId val id: String = "",
    val alunoId: String = "",
    val data: Long = System.currentTimeMillis(),
    val peso: Double = 0.0,
    val gordura: Double = 0.0,
    val cintura: Double = 0.0,
    val braco: Double = 0.0,
    val quadril: Double = 0.0,
    val nivelTecnico: String = "Iniciante",
    val observacao: String = ""
)

data class Professor(
    @DocumentId val id: String = "",
    val nome: String = "",
    val email: String = "",
    val telefone: String = "",
    val modalidade: String = "",
    val diaSemana: String = "Segunda",
    val horario: String = ""
)

data class Modalidade(
    @DocumentId val id: String = "",
    val nome: String = "",
    val descricao: String = ""
)
