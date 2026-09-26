package com.example.appescola

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appescola.ui.theme.PulseOrange
import com.example.appescola.ui.theme.PulseRed

@Composable
fun AlunosScreen(
    vm: AcademiaViewModel,
    onNovo: () -> Unit,
    onAbrirPerfil: (String) -> Unit
) {
    val alunos by vm.alunos.collectAsState()
    val presencas by vm.presencas.collectAsState()
    var busca by remember { mutableStateOf("") }
    var apenasAtivos by remember { mutableStateOf(false) }

    val filtrados = alunos
        .filter { it.nome.contains(busca, ignoreCase = true) }
        .filter { !apenasAtivos || it.ativo }

    Box(Modifier.fillMaxSize()) {
        Column(Modifier.fillMaxSize()) {
            PulseTopBar(titulo = "Alunos")
            Column(Modifier.padding(horizontal = 20.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = busca,
                        onValueChange = { busca = it },
                        placeholder = { Text("Buscar aluno...") },
                        leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant) },
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                            focusedBorderColor = PulseOrange,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline
                        ),
                        modifier = Modifier.weight(1f)
                    )
                    Box(
                        Modifier
                            .size(52.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(if (apenasAtivos) PulseOrange else MaterialTheme.colorScheme.surfaceVariant)
                            .clickable { apenasAtivos = !apenasAtivos },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Filled.FilterList, contentDescription = "Somente ativos",
                            tint = if (apenasAtivos) Color(0xFF201000) else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                Spacer(Modifier.height(14.dp))
                if (filtrados.isEmpty()) {
                    EstadoVazio("Nenhum aluno encontrado.")
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        contentPadding = PaddingValues(bottom = 96.dp)
                    ) {
                        items(filtrados, key = { it.id }) { a ->
                            val freq = frequenciaPercentual(a.id, presencas)
                            CardAluno(nome = a.nome, modalidade = a.modalidade, ativo = a.ativo, frequencia = freq) {
                                onAbrirPerfil(a.id)
                            }
                        }
                    }
                }
            }
        }
        FloatingActionButton(
            onClick = onNovo,
            containerColor = PulseOrange,
            contentColor = Color(0xFF201000),
            modifier = Modifier.align(Alignment.BottomEnd).padding(20.dp)
        ) { Icon(Icons.Filled.Add, contentDescription = "Novo aluno") }
    }
}

@Composable
private fun CardAluno(nome: String, modalidade: String, ativo: Boolean, frequencia: Int, onClick: () -> Unit) {
    PulseCard(modifier = Modifier.clickable { onClick() }, padding = 12) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            AvatarIniciais(nome, tamanho = 48)
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(nome, style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold)
                Text(
                    modalidade.ifBlank { "Sem modalidade" } + if (!ativo) " • Inativo" else "",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Text("$frequencia%", color = corFrequencia(frequencia), fontWeight = FontWeight.Bold)
                Icon(Icons.Filled.ShowChart, contentDescription = null, tint = corFrequencia(frequencia), modifier = Modifier.size(16.dp))
            }
        }
    }
}

/** Formulário de novo aluno / edição (wireframes "Novo Aluno" e "Editar Aluno"). */
@Composable
fun AlunoFormScreen(vm: AcademiaViewModel, id: String, onVoltar: () -> Unit) {
    val alunos by vm.alunos.collectAsState()
    val existente = alunos.firstOrNull { it.id == id }
    val ehNovo = id == "novo" || existente == null

    var nome by remember(existente?.id) { mutableStateOf(existente?.nome ?: "") }
    var email by remember(existente?.id) { mutableStateOf(existente?.email ?: "") }
    var telefone by remember(existente?.id) { mutableStateOf(existente?.telefone ?: "") }
    var nascimento by remember(existente?.id) { mutableStateOf(existente?.dataNascimento ?: "") }
    var modalidade by remember(existente?.id) { mutableStateOf(existente?.modalidade ?: MODALIDADES.first()) }
    var plano by remember(existente?.id) { mutableStateOf(existente?.plano ?: PLANOS.first()) }
    var ativo by remember(existente?.id) { mutableStateOf(existente?.ativo ?: true) }

    Column(Modifier.fillMaxSize()) {
        PulseTopBar(titulo = if (ehNovo) "Novo Aluno" else "Editar Aluno", onVoltar = onVoltar)
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Spacer(Modifier.height(4.dp))
            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                AvatarIniciais(nome.ifBlank { "Novo Aluno" }, tamanho = 84)
            }
            Spacer(Modifier.height(4.dp))
            PulseTextField(nome, { nome = it }, "Nome completo")
            PulseTextField(email, { email = it }, "E-mail", teclado = KeyboardType.Email)
            PulseTextField(telefone, { telefone = it }, "Telefone", teclado = KeyboardType.Phone)
            PulseTextField(nascimento, { nascimento = it }, "Data de nascimento (dd/mm/aaaa)")
            Seletor("Modalidade", modalidade, MODALIDADES, { modalidade = MODALIDADES[it] })
            Seletor("Plano", plano, PLANOS, { plano = PLANOS[it] })
            Row(verticalAlignment = Alignment.CenterVertically) {
                Switch(checked = ativo, onCheckedChange = { ativo = it }, colors = SwitchDefaults.colors(checkedThumbColor = PulseOrange))
                Spacer(Modifier.width(8.dp))
                Text(if (ativo) "Aluno ativo" else "Aluno inativo", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Spacer(Modifier.height(8.dp))
            PulsePrimaryButton(
                texto = "Salvar",
                habilitado = nome.isNotBlank(),
                onClick = {
                    vm.salvarAluno(
                        Aluno(
                            id = existente?.id ?: "",
                            nome = nome.trim(),
                            email = email.trim(),
                            telefone = telefone.trim(),
                            dataNascimento = nascimento.trim(),
                            modalidade = modalidade,
                            plano = plano,
                            ativo = ativo,
                            dataMatricula = existente?.dataMatricula ?: System.currentTimeMillis()
                        )
                    )
                    onVoltar()
                }
            )
            Spacer(Modifier.height(24.dp))
        }
    }
}

/** Perfil do aluno com resumo, abas e ações (wireframe "Perfil do Aluno"). */
@Composable
fun PerfilAlunoScreen(
    vm: AcademiaViewModel,
    alunoId: String,
    onVoltar: () -> Unit,
    onEditar: (String) -> Unit,
    onEvolucao: (String) -> Unit
) {
    val alunos by vm.alunos.collectAsState()
    val presencas by vm.presencas.collectAsState()
    val aluno = alunos.firstOrNull { it.id == alunoId }
    var aba by remember { mutableStateOf(0) }
    var excluir by remember { mutableStateOf(false) }

    if (aluno == null) {
        Column(Modifier.fillMaxSize()) { PulseTopBar(titulo = "Perfil do Aluno", onVoltar = onVoltar); EstadoVazio("Aluno não encontrado.") }
        return
    }

    val freq = frequenciaPercentual(aluno.id, presencas)
    val historico = presencas.filter { it.alunoId == aluno.id }.sortedByDescending { it.data }

    Column(Modifier.fillMaxSize()) {
        PulseTopBar(
            titulo = "Perfil do Aluno",
            onVoltar = onVoltar,
            acao = {
                IconButton(onClick = { onEditar(aluno.id) }) { Icon(Icons.Filled.Edit, contentDescription = "Editar", tint = Color.White) }
                IconButton(onClick = { excluir = true }) { Icon(Icons.Filled.Delete, contentDescription = "Excluir", tint = PulseRed) }
            }
        )
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AvatarIniciais(aluno.nome, tamanho = 64)
                Spacer(Modifier.width(14.dp))
                Column {
                    Text(aluno.nome, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold)
                    Text(
                        "Aluno desde ${aluno.dataMatricula.toDataBr()}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.height(4.dp))
                    Box(
                        Modifier
                            .clip(RoundedCornerShape(50))
                            .background(PulseOrange.copy(alpha = 0.18f))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) { Text(aluno.modalidade.ifBlank { "Sem modalidade" }, color = PulseOrange, fontSize = 12.sp) }
                }
            }
            Spacer(Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                PulseCard(modifier = Modifier.weight(1f), padding = 12) {
                    Text("$freq%", color = corFrequencia(freq), fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge)
                    Text("Frequência", color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodySmall)
                }
                PulseCard(modifier = Modifier.weight(1f), padding = 12) {
                    Text(aluno.plano, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                    Text("Plano", color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodySmall)
                }
            }
            Spacer(Modifier.height(16.dp))
            SegmentedTabs(listOf("Visão geral", "Evolução", "Histórico"), aba, { aba = it })
            Spacer(Modifier.height(14.dp))
            when (aba) {
                0 -> {
                    PulseCard(padding = 14) {
                        LinhaInfo("E-mail", aluno.email.ifBlank { "-" })
                        LinhaInfo("Telefone", aluno.telefone.ifBlank { "-" })
                        LinhaInfo("Nascimento", aluno.dataNascimento.ifBlank { "-" })
                        LinhaInfo("Status", if (aluno.ativo) "Ativo" else "Inativo")
                    }
                    Spacer(Modifier.height(12.dp))
                    PulsePrimaryButton(texto = "Ver evolução física", onClick = { onEvolucao(aluno.id) })
                }
                1 -> PulsePrimaryButton(texto = "Abrir evolução física", onClick = { onEvolucao(aluno.id) })
                2 -> {
                    if (historico.isEmpty()) {
                        EstadoVazio("Nenhum treino registrado ainda.")
                    } else {
                        historico.take(20).forEach { p ->
                            PulseCard(modifier = Modifier.padding(bottom = 8.dp), padding = 12) {
                                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                    Column {
                                        Text(p.aulaNome, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Medium)
                                        Text(diaIsoToLabel(p.dia), color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodySmall)
                                    }
                                    Icon(
                                        Icons.Filled.ShowChart,
                                        contentDescription = null,
                                        tint = if (p.presente) com.example.appescola.ui.theme.PulseGreen else PulseRed
                                    )
                                }
                            }
                        }
                    }
                }
            }
            Spacer(Modifier.height(24.dp))
        }
    }

    if (excluir) {
        PulseAlertDialog(
            titulo = "Excluir aluno",
            texto = "Excluir ${aluno.nome} junto com presenças e evolução?",
            textoConfirmar = "Excluir",
            corConfirmar = PulseRed,
            onConfirmar = { vm.excluirAluno(aluno.id); excluir = false; onVoltar() },
            onCancelar = { excluir = false }
        )
    }
}

@Composable
private fun LinhaInfo(rotulo: String, valor: String) {
    Row(Modifier.fillMaxWidth().padding(vertical = 6.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(rotulo, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(valor, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Medium)
    }
}
