package com.example.appescola

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
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
fun ProfessoresScreen(vm: AcademiaViewModel, onVoltar: () -> Unit) {
    val professores by vm.professores.collectAsState()
    var filtro by remember { mutableStateOf("Todos") }
    var dialogo by remember { mutableStateOf<Professor?>(null) }
    var excluir by remember { mutableStateOf<Professor?>(null) }

    val opcoesFiltro = listOf("Todos") + MODALIDADES
    val filtrados = professores.filter { filtro == "Todos" || it.modalidade == filtro }

    Box(Modifier.fillMaxSize()) {
        Column(Modifier.fillMaxSize()) {
            PulseTopBar(titulo = "Professores", onVoltar = onVoltar)
            LazyRow(
                Modifier.padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(bottom = 12.dp)
            ) {
                items(opcoesFiltro) { op -> ChipFiltro(op, filtro == op) { filtro = op } }
            }
            if (filtrados.isEmpty()) {
                EstadoVazio("Nenhum professor cadastrado.")
            } else {
                LazyColumn(
                    Modifier.padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(bottom = 96.dp)
                ) {
                    items(filtrados, key = { it.id }) { p ->
                        PulseCard(padding = 12) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                IconeQuadrado(iconeModalidade(p.modalidade))
                                Spacer(Modifier.width(12.dp))
                                Column(Modifier.weight(1f)) {
                                    Text(p.modalidade.ifBlank { p.nome }, style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold)
                                    Text("${p.diaSemana} - ${p.horario}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    Text("Professor: ${p.nome}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                                IconButton(onClick = { dialogo = p }) { Icon(Icons.Filled.Edit, contentDescription = "Editar", tint = MaterialTheme.colorScheme.onSurfaceVariant) }
                                IconButton(onClick = { excluir = p }) { Icon(Icons.Filled.Delete, contentDescription = "Excluir", tint = PulseRed) }
                            }
                        }
                    }
                }
            }
        }
        FloatingActionButton(
            onClick = { dialogo = Professor() },
            containerColor = PulseOrange,
            contentColor = Color(0xFF201000),
            modifier = Modifier.align(Alignment.BottomEnd).padding(20.dp)
        ) { Icon(Icons.Filled.Add, contentDescription = "Novo professor") }
    }

    dialogo?.let { base ->
        var nome by remember(base) { mutableStateOf(base.nome) }
        var email by remember(base) { mutableStateOf(base.email) }
        var telefone by remember(base) { mutableStateOf(base.telefone) }
        var modalidade by remember(base) { mutableStateOf(base.modalidade.ifBlank { MODALIDADES.first() }) }
        var dia by remember(base) { mutableStateOf(base.diaSemana) }
        var horario by remember(base) { mutableStateOf(base.horario) }

        AlertDialog(
            onDismissRequest = { dialogo = null },
            containerColor = MaterialTheme.colorScheme.surface,
            title = { Text(if (base.id.isBlank()) "Novo professor" else "Editar professor") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    PulseTextField(nome, { nome = it }, "Nome completo")
                    PulseTextField(email, { email = it }, "E-mail", teclado = androidx.compose.ui.text.input.KeyboardType.Email)
                    PulseTextField(telefone, { telefone = it }, "Telefone", teclado = androidx.compose.ui.text.input.KeyboardType.Phone)
                    Seletor("Modalidade", modalidade, MODALIDADES, { indice -> modalidade = MODALIDADES[indice] })
                    Seletor("Dia", dia, DIAS, { indice -> dia = DIAS[indice] })
                    PulseTextField(horario, { horario = it }, "Horário (ex: 10:00)")
                }
            },
            confirmButton = {
                TextButton(
                    enabled = nome.isNotBlank(),
                    onClick = {
                        vm.salvarProfessor(
                            base.copy(
                                nome = nome.trim(), email = email.trim(), telefone = telefone.trim(),
                                modalidade = modalidade, diaSemana = dia, horario = horario.trim()
                            )
                        )
                        dialogo = null
                    }
                ) { Text("Salvar", color = PulseOrange, fontWeight = FontWeight.Bold) }
            },
            dismissButton = { TextButton(onClick = { dialogo = null }) { Text("Cancelar") } }
        )
    }

    excluir?.let { p ->
        PulseAlertDialog(
            titulo = "Excluir professor",
            texto = "Excluir ${p.nome}?",
            textoConfirmar = "Excluir",
            corConfirmar = PulseRed,
            onConfirmar = { vm.excluirProfessor(p.id); excluir = null },
            onCancelar = { excluir = null }
        )
    }
}
