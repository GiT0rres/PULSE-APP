package com.example.appescola

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
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
fun ModalidadesScreen(vm: AcademiaViewModel, onVoltar: () -> Unit) {
    val modalidades by vm.modalidades.collectAsState()
    var busca by remember { mutableStateOf("") }
    var dialogo by remember { mutableStateOf<Modalidade?>(null) }
    var excluir by remember { mutableStateOf<Modalidade?>(null) }

    val filtradas = modalidades.filter { it.nome.contains(busca, ignoreCase = true) }

    Box(Modifier.fillMaxSize()) {
        Column(Modifier.fillMaxSize()) {
            PulseTopBar(titulo = "Modalidades", onVoltar = onVoltar)
            Column(Modifier.padding(horizontal = 20.dp)) {
                OutlinedTextField(
                    value = busca,
                    onValueChange = { busca = it },
                    placeholder = { Text("Buscar modalidade...") },
                    leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant) },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                        focusedBorderColor = PulseOrange,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(14.dp))
                if (filtradas.isEmpty()) {
                    EstadoVazio("Nenhuma modalidade cadastrada.")
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        contentPadding = PaddingValues(bottom = 96.dp)
                    ) {
                        items(filtradas, key = { it.id }) { m ->
                            PulseCard(padding = 12) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    IconeCirculo(iconeModalidade(m.nome), tamanho = 48)
                                    Spacer(Modifier.width(12.dp))
                                    Column(Modifier.weight(1f)) {
                                        Text(m.nome, style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold)
                                        if (m.descricao.isNotBlank()) {
                                            Text(m.descricao, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        }
                                    }
                                    IconButton(onClick = { dialogo = m }) { Icon(Icons.Filled.Edit, contentDescription = "Editar", tint = MaterialTheme.colorScheme.onSurfaceVariant) }
                                    IconButton(onClick = { excluir = m }) { Icon(Icons.Filled.Delete, contentDescription = "Excluir", tint = PulseRed) }
                                }
                            }
                        }
                    }
                }
            }
        }
        FloatingActionButton(
            onClick = { dialogo = Modalidade() },
            containerColor = PulseOrange,
            contentColor = Color(0xFF201000),
            modifier = Modifier.align(Alignment.BottomEnd).padding(20.dp)
        ) { Icon(Icons.Filled.Add, contentDescription = "Nova modalidade") }
    }

    dialogo?.let { base ->
        var nome by remember(base) { mutableStateOf(base.nome) }
        var descricao by remember(base) { mutableStateOf(base.descricao) }

        AlertDialog(
            onDismissRequest = { dialogo = null },
            containerColor = MaterialTheme.colorScheme.surface,
            title = { Text(if (base.id.isBlank()) "Nova modalidade" else "Editar modalidade") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    PulseTextField(nome, { nome = it }, "Nome da modalidade")
                    PulseTextField(descricao, { descricao = it }, "Descrição curta")
                }
            },
            confirmButton = {
                TextButton(
                    enabled = nome.isNotBlank(),
                    onClick = {
                        vm.salvarModalidade(base.copy(nome = nome.trim(), descricao = descricao.trim()))
                        dialogo = null
                    }
                ) { Text("Salvar", color = PulseOrange, fontWeight = FontWeight.Bold) }
            },
            dismissButton = { TextButton(onClick = { dialogo = null }) { Text("Cancelar") } }
        )
    }

    excluir?.let { m ->
        PulseAlertDialog(
            titulo = "Excluir modalidade",
            texto = "Excluir ${m.nome}?",
            textoConfirmar = "Excluir",
            corConfirmar = PulseRed,
            onConfirmar = { vm.excluirModalidade(m.id); excluir = null },
            onCancelar = { excluir = null }
        )
    }
}
