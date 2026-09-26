package com.example.appescola

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.appescola.ui.theme.PulseBackground
import com.example.appescola.ui.theme.PulseOrange
import com.example.appescola.ui.theme.PulseRed

/** Conteúdo do menu lateral (Drawer), wireframe "Menu Lateral". */
@Composable
fun MenuLateralConteudo(
    nomeUsuario: String,
    emailUsuario: String,
    rotaAtual: String?,
    onNavegar: (String) -> Unit,
    onSair: () -> Unit
) {
    ModalDrawerSheet(drawerContainerColor = PulseBackground) {
        Column(Modifier.fillMaxHeight().padding(vertical = 24.dp)) {
            Row(Modifier.padding(horizontal = 20.dp), verticalAlignment = Alignment.CenterVertically) {
                AvatarIniciais(nomeUsuario.ifBlank { "Usuário" }, tamanho = 48)
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(nomeUsuario.ifBlank { "Usuário" }, color = androidx.compose.ui.graphics.Color.White, fontWeight = FontWeight.Bold)
                    Text(emailUsuario, color = androidx.compose.ui.graphics.Color.White.copy(alpha = 0.6f), style = MaterialTheme.typography.bodySmall)
                }
            }
            Spacer(Modifier.height(24.dp))
            ItemMenu(Icons.Filled.Home, "Início", rotaAtual == "inicio") { onNavegar("inicio") }
            ItemMenu(Icons.Filled.Person, "Alunos", rotaAtual == "alunos") { onNavegar("alunos") }
            ItemMenu(Icons.Filled.Category, "Aulas", rotaAtual == "aulas") { onNavegar("aulas") }
            ItemMenu(Icons.Filled.Groups, "Relatórios", rotaAtual == "relatorios") { onNavegar("relatorios") }
            ItemMenu(Icons.Filled.Settings, "Configurações", rotaAtual == "configuracoes") { onNavegar("configuracoes") }
            Spacer(Modifier.weight(1f))
            HorizontalDivider(color = MaterialTheme.colorScheme.outline, modifier = Modifier.padding(horizontal = 20.dp))
            Spacer(Modifier.height(8.dp))
            ItemMenu(Icons.Filled.Logout, "Sair", false, corTexto = PulseRed, onClick = onSair)
        }
    }
}

@Composable
private fun ItemMenu(
    icone: ImageVector,
    texto: String,
    selecionado: Boolean,
    corTexto: androidx.compose.ui.graphics.Color = androidx.compose.ui.graphics.Color.White,
    onClick: () -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 2.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(if (selecionado) PulseOrange.copy(alpha = 0.18f) else androidx.compose.ui.graphics.Color.Transparent)
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icone, contentDescription = null, tint = if (selecionado) PulseOrange else corTexto)
        Spacer(Modifier.width(14.dp))
        Text(texto, color = if (selecionado) PulseOrange else corTexto, fontWeight = if (selecionado) FontWeight.Bold else FontWeight.Medium)
    }
}
