package com.example.appescola

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/** Registrar presença de uma aula específica (wireframe "Registrar Presença"). */
@Composable
fun ChamadaAulaScreen(vm: AcademiaViewModel, aulaId: String, onVoltar: () -> Unit) {
    val alunos by vm.alunos.collectAsState()
    val aulas by vm.aulas.collectAsState()
    val presencas by vm.presencas.collectAsState()
    val aula = aulas.firstOrNull { it.id == aulaId }

    val hoje = remember { hojeIso() }
    val marcados: SnapshotStateMap<String, Boolean> = remember(aulaId) { mutableStateMapOf() }
    val alvo = alunos.filter { it.ativo && (aula == null || it.modalidade == aula.modalidade) }
    var salvo by remember { mutableStateOf(false) }

    fun estaPresente(a: Aluno): Boolean =
        marcados[a.id] ?: presencas.firstOrNull { it.aulaId == aulaId && it.alunoId == a.id && it.dia == hoje }?.presente ?: false

    Column(Modifier.fillMaxSize()) {
        PulseTopBar(titulo = "Chamada • ${aula?.nome ?: ""}", onVoltar = onVoltar)
        Column(Modifier.padding(horizontal = 20.dp).weight(1f)) {
            if (alvo.isEmpty()) {
                EstadoVazio("Nenhum aluno ativo para essa modalidade.")
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp), contentPadding = PaddingValues(bottom = 16.dp)) {
                    items(alvo, key = { it.id }) { a ->
                        val presente = estaPresente(a)
                        PulseCard(padding = 10) {
                            Row(
                                Modifier.fillMaxWidth().clickable { marcados[a.id] = !presente; salvo = false },
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                AvatarIniciais(a.nome, tamanho = 40)
                                Spacer(Modifier.width(10.dp))
                                Column(Modifier.weight(1f)) {
                                    Text(a.nome, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurface)
                                    Text(a.modalidade, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                                Checkbox(
                                    checked = presente,
                                    onCheckedChange = { marcados[a.id] = it; salvo = false },
                                    colors = CheckboxDefaults.colors(checkedColor = com.example.appescola.ui.theme.PulseOrange)
                                )
                            }
                        }
                    }
                }
            }
        }
        Column(Modifier.padding(20.dp)) {
            PulsePrimaryButton(
                texto = if (salvo) "Chamada salva ✓" else "Salvar chamada",
                habilitado = alvo.isNotEmpty(),
                onClick = {
                    val a = aula ?: return@PulsePrimaryButton
                    val agora = System.currentTimeMillis()
                    vm.salvarChamada(
                        alvo.map { aluno ->
                            Presenca(
                                alunoId = aluno.id,
                                alunoNome = aluno.nome,
                                aulaId = a.id,
                                aulaNome = a.nome,
                                dia = hoje,
                                data = agora,
                                presente = estaPresente(aluno)
                            )
                        }
                    )
                    salvo = true
                }
            )
        }
    }
}

/** Calendário mensal de frequência (wireframe "Frequência"). */
@Composable
fun FrequenciaScreen(vm: AcademiaViewModel, onVoltar: () -> Unit) {
    val presencas by vm.presencas.collectAsState()
    val total = presencas.size
    val presentes = presencas.count { it.presente }
    val taxa = if (total == 0) 0 else presentes * 100 / total

    val diasComRegistro = presencas.map { it.dia }.distinct()
    val diasPresenca = presencas.filter { it.presente }.map { it.dia }.distinct()
    val diasFalta = diasComRegistro.filterNot { it in diasPresenca }

    Column(Modifier.fillMaxSize()) {
        PulseTopBar(titulo = "Frequência", onVoltar = onVoltar)
        Column(Modifier.padding(horizontal = 20.dp).fillMaxSize()) {
            PulseCard(padding = 14) {
                Text("Legenda", color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.labelLarge)
                Spacer(Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    LegendaPonto(com.example.appescola.ui.theme.PulseGreen, "Presente (${diasPresenca.size})")
                    LegendaPonto(com.example.appescola.ui.theme.PulseRed, "Faltou (${diasFalta.size})")
                }
            }
            Spacer(Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                PulseCard(modifier = Modifier.weight(1f), padding = 12) {
                    Text("$total", color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge)
                    Text("Treinos realizados", color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodySmall)
                }
                PulseCard(modifier = Modifier.weight(1f), padding = 12) {
                    Text("$taxa%", color = corFrequencia(taxa), fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge)
                    Text("Frequência geral", color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodySmall)
                }
            }
            Spacer(Modifier.height(12.dp))
            if (presencas.isEmpty()) {
                EstadoVazio("Nenhuma presença registrada ainda.")
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp), contentPadding = PaddingValues(bottom = 24.dp)) {
                    items(presencas, key = { it.id }) { p ->
                        PulseCard(padding = 12) {
                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                Column {
                                    Text(p.alunoNome, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Medium)
                                    Text("${p.aulaNome} • ${diaIsoToLabel(p.dia)}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                                Text(
                                    if (p.presente) "Presente" else "Faltou",
                                    color = if (p.presente) com.example.appescola.ui.theme.PulseGreen else com.example.appescola.ui.theme.PulseRed,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LegendaPonto(cor: androidx.compose.ui.graphics.Color, texto: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        androidx.compose.foundation.Canvas(Modifier.size(10.dp)) { drawCircle(cor) }
        Spacer(Modifier.width(6.dp))
        Text(texto, color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodySmall)
    }
}
