package com.example.appescola

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.appescola.ui.theme.PulseOrange
import com.example.appescola.ui.theme.PulseRed
import com.example.appescola.ui.theme.PulseThemeState

/** Tela de Configurações / Perfil (wireframe "Configurações / Perfil"). */
@Composable
fun ConfiguracoesScreen(nomeUsuario: String, emailUsuario: String, onSair: () -> Unit) {
    var sair by remember { mutableStateOf(false) }

    Column(Modifier.fillMaxSize()) {
        PulseTopBar(titulo = "Configurações")
        Column(Modifier.padding(horizontal = 20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AvatarIniciais(nomeUsuario.ifBlank { "Usuário" }, tamanho = 56)
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(nomeUsuario.ifBlank { "Usuário" }, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold)
                    Text(emailUsuario, color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodySmall)
                }
            }
            Spacer(Modifier.height(20.dp))
            PulseCard(padding = 4) {
                ItemConfig(Icons.Filled.Person, "Meu perfil") {}
                DivisorItem()
                ItemConfig(Icons.Filled.Notifications, "Notificações") {}
                DivisorItem()
                ItemConfigTema()
                DivisorItem()
                ItemConfig(Icons.Filled.HelpOutline, "Ajuda e suporte") {}
            }
            Spacer(Modifier.height(16.dp))
            PulseCard(padding = 4) {
                ItemConfig(Icons.Filled.Logout, "Sair", corTexto = PulseRed) { sair = true }
            }
            Spacer(Modifier.height(24.dp))
        }
    }

    if (sair) {
        PulseAlertDialog(
            titulo = "Sair da conta",
            texto = "Tem certeza que deseja sair?",
            textoConfirmar = "Sair",
            corConfirmar = PulseRed,
            onConfirmar = { sair = false; onSair() },
            onCancelar = { sair = false }
        )
    }
}

@Composable
private fun ItemConfig(icone: ImageVector, texto: String, corTexto: Color = Color.Unspecified, onClick: () -> Unit) {
    Row(
        Modifier.fillMaxWidth().clickable { onClick() }.padding(horizontal = 12.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icone, contentDescription = null, tint = if (corTexto == Color.Unspecified) MaterialTheme.colorScheme.onSurfaceVariant else corTexto)
        Spacer(Modifier.width(14.dp))
        Text(texto, modifier = Modifier.weight(1f), color = if (corTexto == Color.Unspecified) MaterialTheme.colorScheme.onSurface else corTexto, fontWeight = FontWeight.Medium)
        if (corTexto == Color.Unspecified) {
            Icon(Icons.Filled.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun ItemConfigTema() {
    Row(
        Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Filled.DarkMode, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.width(14.dp))
        Text("Tema escuro", modifier = Modifier.weight(1f), color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Medium)
        Switch(
            checked = PulseThemeState.escuro,
            onCheckedChange = { PulseThemeState.escuro = it },
            colors = SwitchDefaults.colors(checkedThumbColor = PulseOrange)
        )
    }
}

@Composable
private fun DivisorItem() {
    HorizontalDivider(color = MaterialTheme.colorScheme.outline, modifier = Modifier.padding(horizontal = 12.dp))
}
