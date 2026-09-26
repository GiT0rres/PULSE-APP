package com.example.appescola

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import com.example.appescola.ui.theme.PulseBackground
import com.example.appescola.ui.theme.PulseOrange
import com.example.appescola.ui.theme.PulseOrangeLight

@Composable
private fun LogoPulse(tamanho: Int = 90) {
    Canvas(Modifier.size(tamanho.dp)) {
        val largura = size.width
        val altura = size.height
        val meio = altura / 2
        val caminho = Path().apply {
            moveTo(0f, meio)
            lineTo(largura * 0.28f, meio)
            lineTo(largura * 0.40f, meio - altura * 0.32f)
            lineTo(largura * 0.52f, meio + altura * 0.38f)
            lineTo(largura * 0.64f, meio - altura * 0.18f)
            lineTo(largura * 0.74f, meio)
            lineTo(largura, meio)
        }
        drawPath(
            caminho,
            brush = Brush.horizontalGradient(listOf(PulseOrange, PulseOrangeLight)),
            style = Stroke(
                width = altura * 0.09f,
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )
        )
    }
}

@Composable
fun SplashScreen(onComecar: () -> Unit, onEntrar: () -> Unit) {
    Box(
        Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF23150C), PulseBackground, Color(0xFF0B0C0E))
                )
            )
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(horizontal = 28.dp, vertical = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                LogoPulse(110)
                Spacer(Modifier.height(12.dp))
                Text("PULSE", color = Color.White, fontSize = 34.sp, fontWeight = FontWeight.Black, letterSpacing = 4.sp)
                Spacer(Modifier.height(8.dp))
                Text("Seu ritmo. Sua evolução.", color = Color.White.copy(alpha = 0.75f), fontSize = 15.sp)
            }
            PulsePrimaryButton(texto = "Começar", onClick = onComecar)
            Spacer(Modifier.height(16.dp))
            Row {
                Text("Já tem uma conta? ", color = Color.White.copy(alpha = 0.7f))
                Text(
                    "Entrar",
                    color = PulseOrangeLight,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onEntrar() }
                )
            }
        }
    }
}

@Composable
fun LoginScreen(vm: AuthViewModel, onEntrou: () -> Unit, onCadastro: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var lembrar by remember { mutableStateOf(true) }
    val snackbar = remember { SnackbarHostState() }
    val escopo = rememberCoroutineScope()

    Scaffold(
        containerColor = PulseBackground,
        snackbarHost = { SnackbarHost(snackbar) }
    ) { pad ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(pad)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(40.dp))
            LogoPulse(72)
            Spacer(Modifier.height(6.dp))
            Text("PULSE", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Black, letterSpacing = 3.sp)
            Spacer(Modifier.height(28.dp))
            Text(
                "Bem-vindo de volta!",
                color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                "Faça login para continuar sua jornada.",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(24.dp))

            PulseTextField(
                valor = email,
                onValueChange = { email = it },
                "E-mail ou usuário", // Removido 'hint =' ou 'label =' para evitar erro de parâmetro
                icone = Icons.Filled.Person,
                teclado = KeyboardType.Email
            )

            Spacer(Modifier.height(12.dp))

            PulseTextField(
                valor = senha,
                onValueChange = { senha = it },
                "Senha",
                icone = Icons.Filled.Lock,
                ehSenha = true
            )

            Spacer(Modifier.height(12.dp))

            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = lembrar,
                        onCheckedChange = { lembrar = it },
                        colors = CheckboxDefaults.colors(checkedColor = PulseOrange)
                    )
                    Text("Lembrar de mim", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 13.sp)
                }
                Text(
                    "Esqueceu sua senha?",
                    color = PulseOrangeLight,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.clickable {
                        if (email.isNotBlank()) {
                            vm.redefinirSenha(email) {
                                escopo.launch { snackbar.showSnackbar("E-mail de redefinição enviado.") }
                            }
                        } else {
                            escopo.launch { snackbar.showSnackbar("Digite seu e-mail primeiro.") }
                        }
                    }
                )
            }

            Spacer(Modifier.height(20.dp))

            vm.erro?.takeIf { it.isNotBlank() }?.let { erro ->
                Text(
                    text = erro,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(bottom = 8.dp),
                    fontWeight = FontWeight.Medium
                )
            }

            PulsePrimaryButton(texto = "Entrar", carregando = vm.carregando, onClick = {
                vm.limparErro()
                vm.login(email, senha, onEntrou)
            })

            Spacer(Modifier.height(20.dp))

            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                HorizontalDivider(Modifier.weight(1f), color = MaterialTheme.colorScheme.outlineVariant)
                Text("  ou  ", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
                HorizontalDivider(Modifier.weight(1f), color = MaterialTheme.colorScheme.outlineVariant)
            }

            Spacer(Modifier.height(20.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                BotaoSocial("G") { escopo.launch { snackbar.showSnackbar("Login com Google em breve.") } }
                BotaoSocial("f") { escopo.launch { snackbar.showSnackbar("Login com Facebook em breve.") } }
            }

            Spacer(Modifier.height(28.dp))

            Row {
                Text("Não tem uma conta? ", color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text("Cadastre-se", color = PulseOrangeLight, fontWeight = FontWeight.Bold, modifier = Modifier.clickable { onCadastro() })
            }
            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun BotaoSocial(texto: String, onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .size(52.dp)
            .clip(CircleShape)
            .clickable { onClick() },
        color = Color.White,
        shape = CircleShape
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(texto, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.Black)
        }
    }
}

@Composable
fun CadastroScreen(vm: AuthViewModel, onVoltar: () -> Unit, onCadastrado: () -> Unit) {
    var nome by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var confirmar by remember { mutableStateOf("") }

    Scaffold(containerColor = PulseBackground) { pad ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(pad)
                .verticalScroll(rememberScrollState())
        ) {
            PulseTopBar(titulo = "", onVoltar = onVoltar)
            Column(Modifier.padding(horizontal = 24.dp)) {
                Text("Crie sua conta", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Text(
                    "Comece agora a acompanhar sua evolução.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.height(24.dp))

                PulseTextField(nome, { nome = it }, "Nome completo", icone = Icons.Filled.Person)
                Spacer(Modifier.height(12.dp))
                PulseTextField(email, { email = it }, "E-mail", icone = Icons.Filled.Email, teclado = KeyboardType.Email)
                Spacer(Modifier.height(12.dp))
                PulseTextField(senha, { senha = it }, "Senha", icone = Icons.Filled.Lock, ehSenha = true)
                Spacer(Modifier.height(12.dp))
                PulseTextField(confirmar, { confirmar = it }, "Confirmar senha", icone = Icons.Filled.Lock, ehSenha = true)

                Spacer(Modifier.height(20.dp))

                vm.erro?.takeIf { it.isNotBlank() }?.let { erro ->
                    Text(
                        text = erro,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                PulsePrimaryButton(texto = "Cadastrar", carregando = vm.carregando, onClick = {
                    vm.limparErro()
                    vm.cadastrar(nome, email, senha, confirmar, onCadastrado)
                })

                Spacer(Modifier.height(20.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    Text("Já tem uma conta? ", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("Entrar", color = PulseOrangeLight, fontWeight = FontWeight.Bold, modifier = Modifier.clickable { onVoltar() })
                }
                Spacer(Modifier.height(24.dp))
            }
        }
    }
}