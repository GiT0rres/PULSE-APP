package com.example.appescola

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.ChecklistRtl
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.appescola.ui.theme.PulseOrange
import com.example.appescola.ui.theme.PulseRed

@Composable
fun AulasScreen(vm: AcademiaViewModel, onChamada: (String) -> Unit, onProfessores: () -> Unit, onModalidades: () -> Unit) {
    val aulas by vm.aulas.collectAsState()
    var aba by remember { mutableStateOf(0) }
    var dialogo by remember { mutableStateOf<Aula?>(null) }
    var excluir by remember { mutableStateOf<Aula?>(null) }

    Box(Modifier.fillMaxSize()) {
        Column(Modifier.fillMaxSize()) {
            PulseTopBar(
                titulo = "Aulas",
                acao = {
                    IconButton(onClick = onProfessores) { Icon(Icons.Filled.Groups, contentDescription = "Professores", tint = Color.White) }
                    IconButton(onClick = onModalidades) { Icon(Icons.Filled.Category, contentDescription = "Modalidades", tint = Color.White) }
                }
            )
            Column(Modifier.padding(horizontal = 20.dp)) {
                SegmentedTabs(listOf("Lista", "Calendário"), aba, { aba = it })
                Spacer(Modifier.height(14.dp))
                if (aulas.isEmpty()) {
                    EstadoVazio("Nenhuma aula cadastrada.")
                } else if (aba == 0) {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        contentPadding = PaddingValues(bottom = 96.dp)
                    ) {
                        items(aulas, key = { it.id }) { a -> CardAula(a, onChamada, { dialogo = a }, { excluir = a }) }
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(bottom = 96.dp)
                    ) {
                        items(DIAS, key = { it }) { dia ->
                            val doDia = aulas.filter { it.diaSemana == dia }
                            if (doDia.isNotEmpty()) {
                                Column {
                                    Text(dia, style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    Spacer(Modifier.height(8.dp))
                                    doDia.forEach { a ->
                                        CardAula(a, onChamada, { dialogo = a }, { excluir = a })
                                        Spacer(Modifier.height(8.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        FloatingActionButton(
            onClick = { dialogo = Aula() },
            containerColor = PulseOrange,
            contentColor = Color(0xFF201000),
            modifier = Modifier.align(Alignment.BottomEnd).padding(20.dp)
        ) { Icon(Icons.Filled.Add, contentDescription = "Nova aula") }
    }

    dialogo?.let { base ->
        var nome by remember(base) { mutableStateOf(base.nome) }
        var modalidade by remember(base) { mutableStateOf(base.modalidade.ifBlank { MODALIDADES.first() }) }
        var instrutor by remember(base) { mutableStateOf(base.instrutor) }
        var dia by remember(base) { mutableStateOf(base.diaSemana) }
        var horario by remember(base) { mutableStateOf(base.horario) }
        var duracao by remember(base) { mutableStateOf(if (base.duracaoMin > 0) base.duracaoMin.toString() else "60") }

        AlertDialog(
            onDismissRequest = { dialogo = null },
            containerColor = MaterialTheme.colorScheme.surface,
            title = { Text(if (base.id.isBlank()) "Nova aula" else "Editar aula") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    PulseTextField(nome, { nome = it }, "Nome da aula")
                    Seletor("Modalidade", modalidade, MODALIDADES, { modalidade = MODALIDADES[it] })
                    PulseTextField(instrutor, { instrutor = it }, "Instrutor")
                    Seletor("Dia", dia, DIAS, { dia = DIAS[it] })
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        PulseTextField(horario, { horario = it }, "Horário (ex: 19:00)", modifier = Modifier.weight(1f))
                        PulseTextField(duracao, { duracao = it }, "Duração (min)", teclado = androidx.compose.ui.text.input.KeyboardType.Number, modifier = Modifier.weight(1f))
                    }
                }
            },
            confirmButton = {
                TextButton(
                    enabled = nome.isNotBlank(),
                    onClick = {
                        vm.salvarAula(
                            base.copy(
                                nome = nome.trim(),
                                modalidade = modalidade,
                                instrutor = instrutor.trim(),
                                diaSemana = dia,
                                horario = horario.trim(),
                                duracaoMin = duracao.toIntOrNull() ?: 60
                            )
                        )
                        dialogo = null
                    }
                ) { Text("Salvar", color = PulseOrange, fontWeight = FontWeight.Bold) }
            },
            dismissButton = { TextButton(onClick = { dialogo = null }) { Text("Cancelar") } }
        )
    }

    excluir?.let { a ->
        PulseAlertDialog(
            titulo = "Excluir aula",
            texto = "Excluir a aula ${a.nome}?",
            textoConfirmar = "Excluir",
            corConfirmar = PulseRed,
            onConfirmar = { vm.excluirAula(a.id); excluir = null },
            onCancelar = { excluir = null }
        )
    }
}

@Composable
private fun CardAula(a: Aula, onChamada: (String) -> Unit, onEditar: () -> Unit, onExcluir: () -> Unit) {
    PulseCard(padding = 12) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconeQuadrado(iconeModalidade(a.modalidade))
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(a.nome, style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold)
                Text("${a.diaSemana} • ${a.horario} | ${a.duracaoMin} min", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                if (a.instrutor.isNotBlank()) {
                    Text("Prof. ${a.instrutor}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            IconButton(onClick = { onChamada(a.id) }) { Icon(Icons.Filled.ChecklistRtl, contentDescription = "Fazer chamada", tint = PulseOrange) }
            IconButton(onClick = onEditar) { Icon(Icons.Filled.Edit, contentDescription = "Editar", tint = MaterialTheme.colorScheme.onSurfaceVariant) }
            IconButton(onClick = onExcluir) { Icon(Icons.Filled.Delete, contentDescription = "Excluir", tint = PulseRed) }
        }
    }
}
