package com.example.appescola

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoricoScreen(vm: AcademiaViewModel) {
    val alunos by vm.alunos.collectAsState()
    val presencas by vm.presencas.collectAsState()
    var filtro by remember { mutableStateOf<Aluno?>(null) }

    val lista = presencas.filter { filtro == null || it.alunoId == filtro?.id }
    val total = lista.size
    val presentes = lista.count { it.presente }
    val taxa = if (total == 0) 0 else presentes * 100 / total

    val ranking = presencas.filter { it.presente }
        .groupBy { it.alunoNome }
        .map { it.key to it.value.size }
        .sortedByDescending { it.second }
        .take(3)

    Scaffold(topBar = { TopAppBar(title = { Text("Histórico de frequência") }) }) { pad ->
        Column(Modifier.padding(pad).padding(horizontal = 16.dp)) {
            Seletor(
                rotulo = "Aluno",
                valor = filtro?.nome ?: "Todos",
                opcoes = listOf("Todos") + alunos.map { it.nome },
                onSelecionar = { filtro = if (it == 0) null else alunos[it - 1] }
            )
            Spacer(Modifier.height(8.dp))

            Card(
                Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Column(Modifier.padding(12.dp)) {
                    Text("Presenças: $presentes de $total registros ($taxa%)", style = MaterialTheme.typography.titleMedium)
                    if (filtro == null && ranking.isNotEmpty()) {
                        Spacer(Modifier.height(4.dp))
                        Text("Mais assíduos:", style = MaterialTheme.typography.labelLarge)
                        ranking.forEachIndexed { i, (nome, qtd) ->
                            val medalha = listOf("🥇", "🥈", "🥉")[i]
                            Text("$medalha $nome — $qtd presenças")
                        }
                    }
                }
            }
            Spacer(Modifier.height(8.dp))

            if (lista.isEmpty()) {
                Text("Sem registros de presença ainda.")
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(lista, key = { it.id }) { p ->
                        Card(Modifier.fillMaxWidth()) {
                            Row(Modifier.padding(12.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                                Column(Modifier.weight(1f)) {
                                    Text(p.alunoNome, style = MaterialTheme.typography.titleSmall)
                                    Text("${p.aulaNome} • ${p.data.toDataBr()}", style = MaterialTheme.typography.bodySmall)
                                }
                                Text(if (p.presente) "✅ Presente" else "❌ Faltou")
                            }
                        }
                    }
                }
            }
        }
    }
}