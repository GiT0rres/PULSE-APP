package com.example.appescola

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.SportsGymnastics
import androidx.compose.material.icons.filled.SportsHandball
import androidx.compose.material.icons.filled.SportsKabaddi
import androidx.compose.material.icons.filled.SportsMma
import androidx.compose.material.icons.filled.Pool
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.appescola.ui.theme.PulseGreen
import com.example.appescola.ui.theme.PulseOrange
import com.example.appescola.ui.theme.PulseRed
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

val MODALIDADES = listOf("Musculação", "Muay Thai", "Jiu-Jitsu", "Boxe", "Crossfit", "Funcional", "Pilates", "Natação")
val PLANOS = listOf("Mensal", "Trimestral", "Semestral", "Anual")
val DIAS = listOf("Segunda", "Terça", "Quarta", "Quinta", "Sexta", "Sábado", "Domingo")
val NIVEIS = listOf("Iniciante", "Intermediário", "Avançado", "Competidor")

private val brLocale = Locale("pt", "BR")

fun Long.toDataBr(): String = SimpleDateFormat("dd/MM/yyyy", brLocale).format(Date(this))

fun hojeIso(): String = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())

fun diaIsoToLabel(diaIso: String): String = try {
    val entrada = SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(diaIso)
    if (entrada != null) SimpleDateFormat("dd/MM/yyyy", brLocale).format(entrada) else diaIso
} catch (e: Exception) {
    diaIso
}

/** Percentual de frequência de um aluno com base no histórico de presenças. */
fun frequenciaPercentual(alunoId: String, presencas: List<Presenca>): Int {
    val lista = presencas.filter { it.alunoId == alunoId }
    if (lista.isEmpty()) return 0
    return (lista.count { it.presente } * 100) / lista.size
}

@Composable
fun corFrequencia(percentual: Int) = when {
    percentual >= 80 -> PulseGreen
    percentual >= 50 -> PulseOrange
    else -> PulseRed
}

fun iconeModalidade(modalidade: String): ImageVector = when {
    modalidade.contains("Muscul", true) -> Icons.Filled.FitnessCenter
    modalidade.contains("Muay", true) || modalidade.contains("Boxe", true) -> Icons.Filled.SportsMma
    modalidade.contains("Jiu", true) -> Icons.Filled.SportsKabaddi
    modalidade.contains("Cross", true) -> Icons.Filled.Whatshot
    modalidade.contains("Pilates", true) -> Icons.Filled.SelfImprovement
    modalidade.contains("Nata", true) -> Icons.Filled.Pool
    modalidade.contains("Funcional", true) -> Icons.Filled.SportsGymnastics
    else -> Icons.Filled.SportsHandball
}
