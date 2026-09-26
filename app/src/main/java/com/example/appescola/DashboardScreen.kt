package com.example.appescola

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.appescola.ui.theme.PulseGreen
import com.example.appescola.ui.theme.PulseOrange

/** Tela inicial com resumo (wireframe "Dashboard / Home"). */
@Composable
fun DashboardScreen(vm: AcademiaViewModel, nomeUsuario: String, onAbrirMenu: () -> Unit) {
    val alunos by vm.alunos.collectAsState()
    val presencas by vm.presencas.collectAsState()

    val total = presencas.size
    val presentes = presencas.count { it.presente }
    val frequenciaGeral = if (total == 0) 0 else presentes * 100 / total

    val hoje = hojeIso()
    val ultimoMes = presencas.filter { it.dia < hoje }
    val evolucaoMes = if (ultimoMes.isNotEmpty()) {
        val taxaAnterior = ultimoMes.count { it.presente } * 100 / ultimoMes.size
        frequenciaGeral - taxaAnterior
    } else 0

    Column(Modifier.fillMaxSize()) {
        Row(
            Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("Olá, ${nomeUsuario.ifBlank { "atleta" }} 👋", color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge)
                Text("Aqui está o seu resumo de hoje.", color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodySmall)
            }
            Row {
                IconButton(onClick = onAbrirMenu) { Icon(Icons.Filled.Menu, contentDescription = "Menu", tint = MaterialTheme.colorScheme.onSurface) }
                IconButton(onClick = {}) { Icon(Icons.Filled.Notifications, contentDescription = "Notificações", tint = MaterialTheme.colorScheme.onSurface) }
            }
        }
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                PulseCard(modifier = Modifier.weight(1f), padding = 14) {
                    Text("Frequência", color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodySmall)
                    Text("$frequenciaGeral%", color = corFrequencia(frequenciaGeral), fontWeight = FontWeight.Black, style = MaterialTheme.typography.headlineSmall)
                    Text("($presentes/$total aulas)", color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.labelSmall)
                }
                PulseCard(modifier = Modifier.weight(1f), padding = 14) {
                    Text("Evolução física", color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodySmall)
                    Text(
                        "${if (evolucaoMes >= 0) "+" else ""}$evolucaoMes%",
                        color = if (evolucaoMes >= 0) PulseGreen else MaterialTheme.colorScheme.error,
                        fontWeight = FontWeight.Black,
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Text("(último mês)", color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.labelSmall)
                }
            }
            Spacer(Modifier.height(14.dp))
            PulseCard(padding = 14) {
                Text("Sua evolução", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(10.dp))
                val serieSemanal = (1..6).map { i -> (presentes - (6 - i)).coerceAtLeast(0).toDouble() + 1.0 }
                GraficoLinhaPulse(serieSemanal, Modifier.fillMaxWidth().height(120.dp))
            }
            Spacer(Modifier.height(14.dp))
            PulseCard(padding = 14) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconeCirculo(Icons.Filled.Notifications, tamanho = 40)
                    Spacer(Modifier.width(10.dp))
                    Column {
                        Text("Você está evoluindo!", color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold)
                        Text(
                            "Sua frequência aumentou ${if (evolucaoMes >= 0) evolucaoMes else 0}% em relação ao mês passado.",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
            Spacer(Modifier.height(12.dp))
            Text("Alunos ativos: ${alunos.count { it.ativo }} de ${alunos.size}", color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodySmall)
            Spacer(Modifier.height(24.dp))
        }
    }
}
