package com.example.appescola

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.appescola.ui.theme.PulseGreen
import com.example.appescola.ui.theme.PulseOrange
import com.example.appescola.ui.theme.PulseOrangeLight
import com.example.appescola.ui.theme.PulseRed

@Composable
fun EvolucaoScreen(vm: AcademiaViewModel, alunoId: String, onVoltar: () -> Unit) {
    val alunos by vm.alunos.collectAsState()
    val aluno = alunos.firstOrNull { it.id == alunoId }
    val flow = remember(alunoId) { vm.evolucoes(alunoId) }
    val registros by flow.collectAsState(initial = emptyList())
    var aba by remember { mutableStateOf(0) }

    var peso by remember { mutableStateOf("") }
    var gordura by remember { mutableStateOf("") }
    var cintura by remember { mutableStateOf("") }
    var braco by remember { mutableStateOf("") }
    var quadril by remember { mutableStateOf("") }
    var nivel by remember { mutableStateOf(NIVEIS.first()) }
    var obs by remember { mutableStateOf("") }

    Column(Modifier.fillMaxSize()) {
        PulseTopBar(titulo = "Evolução Física • ${aluno?.nome ?: ""}", onVoltar = onVoltar)
        Column(Modifier.padding(horizontal = 20.dp)) {
            SegmentedTabs(listOf("Gráficos", "Dados"), aba, { aba = it })
        }
        LazyColumn(
            Modifier.padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(top = 14.dp, bottom = 24.dp)
        ) {
            if (aba == 0) {
                item {
                    PulseCard(padding = 14) {
                        Text("Peso (kg)", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold)
                        if (registros.size >= 2) {
                            Spacer(Modifier.height(8.dp))
                            GraficoLinhaPulse(registros.map { it.peso }, Modifier.fillMaxWidth().height(160.dp))
                            val variacao = registros.last().peso - registros.first().peso
                            Spacer(Modifier.height(8.dp))
                            Text(
                                "Variação: ${if (variacao >= 0) "+" else ""}${"%.1f".format(variacao)} kg",
                                color = if (variacao <= 0) PulseGreen else PulseRed,
                                fontWeight = FontWeight.Bold
                            )
                        } else {
                            Spacer(Modifier.height(8.dp))
                            Text("Registre ao menos 2 medições para ver o gráfico.", color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
                if (registros.isNotEmpty()) {
                    item {
                        val ultimo = registros.last()
                        val anterior = registros.getOrNull(registros.size - 2)
                        PulseCard(padding = 14) {
                            Text("Medidas corporais", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold)
                            Spacer(Modifier.height(8.dp))
                            LinhaMedida("Cintura", ultimo.cintura, anterior?.cintura)
                            LinhaMedida("Braço", ultimo.braco, anterior?.braco)
                            LinhaMedida("Quadril", ultimo.quadril, anterior?.quadril)
                        }
                    }
                }
            } else {
                item {
                    PulseCard(padding = 14) {
                        Text("Novo registro", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(10.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            PulseTextField(peso, { peso = it }, "Peso (kg)", teclado = KeyboardType.Decimal, modifier = Modifier.weight(1f))
                            PulseTextField(gordura, { gordura = it }, "% gordura", teclado = KeyboardType.Decimal, modifier = Modifier.weight(1f))
                        }
                        Spacer(Modifier.height(8.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            PulseTextField(cintura, { cintura = it }, "Cintura (cm)", teclado = KeyboardType.Decimal, modifier = Modifier.weight(1f))
                            PulseTextField(braco, { braco = it }, "Braço (cm)", teclado = KeyboardType.Decimal, modifier = Modifier.weight(1f))
                        }
                        Spacer(Modifier.height(8.dp))
                        PulseTextField(quadril, { quadril = it }, "Quadril (cm)", teclado = KeyboardType.Decimal)
                        Spacer(Modifier.height(8.dp))
                        Seletor("Nível técnico", nivel, NIVEIS, { nivel = NIVEIS[it] })
                        Spacer(Modifier.height(8.dp))
                        PulseTextField(obs, { obs = it }, "Observações (opcional)")
                        Spacer(Modifier.height(12.dp))
                        PulsePrimaryButton(
                            texto = "Salvar",
                            habilitado = peso.replace(',', '.').toDoubleOrNull() != null,
                            onClick = {
                                vm.salvarEvolucao(
                                    Evolucao(
                                        alunoId = alunoId,
                                        peso = peso.replace(',', '.').toDoubleOrNull() ?: 0.0,
                                        gordura = gordura.replace(',', '.').toDoubleOrNull() ?: 0.0,
                                        cintura = cintura.replace(',', '.').toDoubleOrNull() ?: 0.0,
                                        braco = braco.replace(',', '.').toDoubleOrNull() ?: 0.0,
                                        quadril = quadril.replace(',', '.').toDoubleOrNull() ?: 0.0,
                                        nivelTecnico = nivel,
                                        observacao = obs.trim()
                                    )
                                )
                                peso = ""; gordura = ""; cintura = ""; braco = ""; quadril = ""; obs = ""
                            }
                        )
                    }
                }
                items(registros.reversed(), key = { it.id }) { e ->
                    PulseCard(padding = 12) {
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Column {
                                Text(e.data.toDataBr(), style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold)
                                Text("Peso: ${e.peso} kg • Gordura: ${e.gordura}% • ${e.nivelTecnico}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                if (e.observacao.isNotBlank()) Text(e.observacao, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            IconButton(onClick = { vm.excluirEvolucao(e.id) }) { Icon(Icons.Filled.Delete, contentDescription = "Excluir", tint = PulseRed) }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LinhaMedida(rotulo: String, atual: Double, anterior: Double?) {
    val diff = if (anterior != null) atual - anterior else null
    Row(Modifier.fillMaxWidth().padding(vertical = 6.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(rotulo, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Row {
            Text("${anterior?.let { "%.0f".format(it) } ?: "-"} cm", color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text("  →  ", color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text("${"%.0f".format(atual)} cm", color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold)
            if (diff != null) {
                Text(
                    "  ${if (diff >= 0) "+" else ""}${"%.0f".format(diff)} cm",
                    color = if (diff <= 0) PulseGreen else PulseRed,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

/** Gráfico de linha com gradiente no estilo Pulse (laranja). */
@Composable
fun GraficoLinhaPulse(valores: List<Double>, modifier: Modifier = Modifier) {
    Canvas(modifier) {
        if (valores.size < 2) return@Canvas
        val min = valores.min()
        val max = valores.max()
        val faixa = (max - min).let { if (it == 0.0) 1.0 else it }
        val passo = size.width / (valores.size - 1)
        val pontos = valores.mapIndexed { i, v ->
            val y = size.height - (((v - min) / faixa).toFloat() * size.height * 0.75f + size.height * 0.1f)
            Offset(i * passo, y)
        }
        val brush = Brush.horizontalGradient(listOf(PulseOrange, PulseOrangeLight))
        for (i in 0 until pontos.size - 1) {
            drawLine(brush, pontos[i], pontos[i + 1], strokeWidth = 6f, cap = androidx.compose.ui.graphics.StrokeCap.Round)
        }
        pontos.forEach { drawCircle(PulseOrangeLight, radius = 7f, center = it) }
    }
}
