package com.example.appescola

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appescola.ui.theme.PulseOrange

/** Barra superior padrão do Pulse: título + voltar opcional + ações opcionais. */
@Composable
fun PulseTopBar(
    titulo: String,
    onVoltar: (() -> Unit)? = null,
    acao: (@Composable RowScope.() -> Unit)? = null
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (onVoltar != null) {
            IconButton(onClick = onVoltar) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Voltar", tint = Color.White)
            }
        } else {
            Spacer(Modifier.width(8.dp))
        }
        Text(
            titulo,
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f).padding(start = if (onVoltar == null) 8.dp else 0.dp)
        )
        acao?.let { Row(verticalAlignment = Alignment.CenterVertically, content = it) }
        Spacer(Modifier.width(4.dp))
    }
}

/** Card escuro com borda sutil, usado em quase todas as telas do Pulse. */
@Composable
fun PulseCard(
    modifier: Modifier = Modifier,
    padding: Int = 16,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surface)
            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(16.dp))
            .padding(padding.dp),
        content = content
    )
}

@Composable
fun PulsePrimaryButton(
    texto: String,
    carregando: Boolean = false,
    habilitado: Boolean = true,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        enabled = habilitado && !carregando,
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(containerColor = PulseOrange, contentColor = Color(0xFF201000)),
        modifier = modifier.fillMaxWidth().height(52.dp)
    ) {
        if (carregando) {
            CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color(0xFF201000), strokeWidth = 2.dp)
        } else {
            Text(texto, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
    }
}

@Composable
fun PulseTextField(
    valor: String,
    onValueChange: (String) -> Unit,
    rotulo: String,
    icone: ImageVector? = null,
    ehSenha: Boolean = false,
    teclado: KeyboardType = KeyboardType.Text,
    modifier: Modifier = Modifier.fillMaxWidth()
) {
    var mostrarSenha by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(false) }
    OutlinedTextField(
        value = valor,
        onValueChange = onValueChange,
        placeholder = { Text(rotulo) },
        singleLine = true,
        leadingIcon = icone?.let { { Icon(it, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant) } },
        trailingIcon = if (ehSenha) {
            {
                IconButton(onClick = { mostrarSenha = !mostrarSenha }) {
                    Icon(
                        if (mostrarSenha) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else null,
        visualTransformation = if (ehSenha && !mostrarSenha) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = teclado),
        shape = RoundedCornerShape(14.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            focusedBorderColor = PulseOrange,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline,
            focusedTextColor = MaterialTheme.colorScheme.onSurface,
            unfocusedTextColor = MaterialTheme.colorScheme.onSurface
        ),
        modifier = modifier
    )
}

/** Avatar circular com as iniciais do nome, com cor derivada do próprio nome. */
@Composable
fun AvatarIniciais(nome: String, tamanho: Int = 48) {
    val iniciais = nome.trim().split(" ").filter { it.isNotBlank() }.take(2).map { it.first().uppercaseChar() }.joinToString("")
    Box(
        Modifier
            .size(tamanho.dp)
            .clip(CircleShape)
            .background(PulseOrange.copy(alpha = 0.25f)),
        contentAlignment = Alignment.Center
    ) {
        Text(iniciais.ifBlank { "?" }, color = PulseOrange, fontWeight = FontWeight.Bold, fontSize = (tamanho / 2.6).sp)
    }
}

@Composable
fun IconeCirculo(icone: ImageVector, tamanho: Int = 48) {
    Box(
        Modifier
            .size(tamanho.dp)
            .clip(CircleShape)
            .background(PulseOrange.copy(alpha = 0.18f)),
        contentAlignment = Alignment.Center
    ) {
        Icon(icone, contentDescription = null, tint = PulseOrange, modifier = Modifier.size((tamanho / 2).dp))
    }
}

@Composable
fun IconeQuadrado(icone: ImageVector, tamanho: Int = 48) {
    Box(
        Modifier
            .size(tamanho.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(PulseOrange.copy(alpha = 0.18f)),
        contentAlignment = Alignment.Center
    ) {
        Icon(icone, contentDescription = null, tint = PulseOrange, modifier = Modifier.size((tamanho / 2).dp))
    }
}

@Composable
fun ChipFiltro(texto: String, selecionado: Boolean, onClick: () -> Unit) {
    Box(
        Modifier
            .clip(RoundedCornerShape(50))
            .background(if (selecionado) PulseOrange else MaterialTheme.colorScheme.surfaceVariant)
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            texto,
            color = if (selecionado) Color(0xFF201000) else MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp
        )
    }
}

/** Abas em formato de pílula (ex: "Lista" / "Calendário", "Gráficos" / "Dados"). */
@Composable
fun SegmentedTabs(opcoes: List<String>, selecionado: Int, onSelecionar: (Int) -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(4.dp)
    ) {
        opcoes.forEachIndexed { i, texto ->
            val ativo = i == selecionado
            Box(
                Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(10.dp))
                    .background(if (ativo) PulseOrange else Color.Transparent)
                    .clickable { onSelecionar(i) }
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    texto,
                    color = if (ativo) Color(0xFF201000) else MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp
                )
            }
        }
    }
}

@Composable
fun EstadoVazio(texto: String) {
    Column(
        Modifier.fillMaxWidth().padding(vertical = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(Icons.Filled.Inbox, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(40.dp))
        Spacer(Modifier.height(8.dp))
        Text(texto, color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center)
    }
}

@Composable
fun PulseAlertDialog(
    titulo: String,
    texto: String,
    textoConfirmar: String = "Confirmar",
    corConfirmar: Color = PulseOrange,
    onConfirmar: () -> Unit,
    onCancelar: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onCancelar,
        containerColor = MaterialTheme.colorScheme.surface,
        title = { Text(titulo, color = MaterialTheme.colorScheme.onSurface) },
        text = { Text(texto, color = MaterialTheme.colorScheme.onSurfaceVariant) },
        confirmButton = { TextButton(onClick = onConfirmar) { Text(textoConfirmar, color = corConfirmar, fontWeight = FontWeight.Bold) } },
        dismissButton = { TextButton(onClick = onCancelar) { Text("Cancelar") } }
    )
}

@Composable
fun Seletor(
    rotulo: String,
    valor: String,
    opcoes: List<String>,
    onSelecionar: (Int) -> Unit,
    modifier: Modifier = Modifier.fillMaxWidth()
) {
    var expandido by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(false) }
    Box(modifier) {
        OutlinedTextField(
            value = valor,
            onValueChange = {},
            readOnly = true,
            label = { Text(rotulo) },
            singleLine = true,
            trailingIcon = {
                Icon(
                    imageVector = Icons.Filled.ArrowDropDown,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                focusedBorderColor = PulseOrange,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface
            ),
            modifier = Modifier.fillMaxWidth()
        )
        Box(
            Modifier
                .matchParentSize()
                .clickable { expandido = true }
        )
        DropdownMenu(
            expanded = expandido,
            onDismissRequest = { expandido = false }
        ) {
            opcoes.forEachIndexed { indice, opcao ->
                DropdownMenuItem(
                    text = { Text(opcao) },
                    onClick = {
                        onSelecionar(indice)
                        expandido = false
                    }
                )
            }
        }
    }
}