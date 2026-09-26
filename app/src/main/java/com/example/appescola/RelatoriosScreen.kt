package com.example.appescola

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.appescola.ui.theme.PulseGreen
import com.example.appescola.ui.theme.PulseOrange

/** Lista geral de relatórios (wireframe "Relatórios"). */
@Composable
fun RelatoriosScreen(vm: AcademiaViewModel, onAbrirAluno: (String) -> Unit) {
    val alunos by vm.alunos.collectAsState()
    val presencas by vm.presencas.collectAsState()
    var periodo by remember { mutableStateOf(1) } // 0=7 dias, 1=30 dias, 2=3 meses, 3=1 ano

    val total = presencas.size
    val presentes = presencas.count { it.presente }
    val taxa = if (total == 0) 0 else presentes * 100 / total

    val ranking = presencas.filter { it.presente }
        .groupBy { it.alunoId to it.alunoNome }
        .map { it.key to it.value.size }
        .sortedByDescending { it.second }
        .take(5)

    Column(Modifier.fillMaxSize()) {
        PulseTopBar(titulo = "Relatórios")
        Column(Modifier.padding(horizontal = 20.dp)) {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp), contentPadding = PaddingValues(bottom = 12.dp)) {
                val opcoes = listOf("7 dias", "30 dias", "3 meses", "1 ano")
                items(opcoes.size) { i -> ChipFiltro(opcoes[i], periodo == i) { periodo = i } }
            }
            PulseCard(padding = 14) {
                Text("Total de treinos", color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodySmall)
                Row(verticalAlignment = Alignment.Bottom) {
                    Text("$total", color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Black, style = MaterialTheme.typography.headlineMedium)
                    Spacer(Modifier.width(8.dp))
                    Text("Frequência média: $taxa%", color = corFrequencia(taxa), fontWeight = FontWeight.Bold)
                }
            }
            Spacer(Modifier.height(12.dp))
            PulseCard(padding = 14) {
                Text("Evolução física", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                Text("Acompanhe peso, medidas e frequência de cada aluno abrindo o relatório individual.", color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodySmall)
            }
            Spacer(Modifier.height(12.dp))
            Text("Ranking de assiduidade", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            if (ranking.isEmpty()) {
                EstadoVazio("Sem registros de presença ainda.")
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp), contentPadding = PaddingValues(bottom = 96.dp)) {
                    items(ranking, key = { it.first.first }) { (chave, qtd) ->
                        val (alunoId, nome) = chave
                        PulseCard(
                            modifier = Modifier.clickable { onAbrirAluno(alunoId) },
                            padding = 12
                        ) {
                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    AvatarIniciais(nome, tamanho = 40)
                                    Spacer(Modifier.width(10.dp))
                                    Text(nome, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Medium)
                                }
                                Text("$qtd treinos", color = PulseGreen, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}

/** Relatório individual de um aluno (wireframe "Relatório Individual"). */
@Composable
fun RelatorioIndividualScreen(vm: AcademiaViewModel, alunoId: String, onVoltar: () -> Unit) {
    val alunos by vm.alunos.collectAsState()
    val presencas by vm.presencas.collectAsState()
    val aluno = alunos.firstOrNull { it.id == alunoId }
    val flow = remember(alunoId) { vm.evolucoes(alunoId) }
    val registros by flow.collectAsState(initial = emptyList())
    var aba by remember { mutableStateOf(0) }

    val doAluno = presencas.filter { it.alunoId == alunoId }
    val freq = frequenciaPercentual(alunoId, presencas)

    Column(Modifier.fillMaxSize()) {
        PulseTopBar(titulo = "Relatório Individual", onVoltar = onVoltar)
        Column(Modifier.padding(horizontal = 20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AvatarIniciais(aluno?.nome ?: "", tamanho = 48)
                Spacer(Modifier.width(10.dp))
                Column {
                    Text(aluno?.nome ?: "", color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold)
                    Text(aluno?.modalidade ?: "", color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodySmall)
                }
            }
            Spacer(Modifier.height(12.dp))
            SegmentedTabs(listOf("Físico", "Técnico"), aba, { aba = it })
            Spacer(Modifier.height(12.dp))
            if (aba == 0) {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    PulseCard(modifier = Modifier.weight(1f), padding = 12) {
                        Text("${registros.lastOrNull()?.peso ?: 0.0} kg", color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold)
                        Text("Peso atual", color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodySmall)
                    }
                    PulseCard(modifier = Modifier.weight(1f), padding = 12) {
                        Text("$freq%", color = corFrequencia(freq), fontWeight = FontWeight.Bold)
                        Text("Frequência", color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodySmall)
                    }
                }
                Spacer(Modifier.height(12.dp))
                PulseCard(padding = 14) {
                    Text("Peso ao longo do tempo", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold)
                    if (registros.size >= 2) {
                        Spacer(Modifier.height(8.dp))
                        GraficoLinhaPulse(registros.map { it.peso }, Modifier.fillMaxWidth().height(140.dp))
                    } else {
                        Spacer(Modifier.height(8.dp))
                        Text("Sem dados suficientes ainda.", color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodySmall)
                    }
                }
            } else {
                PulseCard(padding = 14) {
                    Text("Nível técnico atual: ${registros.lastOrNull()?.nivelTecnico ?: "Iniciante"}", color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(6.dp))
                    Text("Treinos registrados: ${doAluno.size} • Presenças: ${doAluno.count { it.presente }}", color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodySmall)
                }
            }
            Spacer(Modifier.height(24.dp))
        }
    }
}
