package com.example.appescola

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.appescola.ui.theme.PulseBackground
import com.example.appescola.ui.theme.PulseOrange
import kotlinx.coroutines.launch

private data class Aba(val rota: String, val icone: androidx.compose.ui.graphics.vector.ImageVector, val rotulo: String)

private val ABAS = listOf(
    Aba("inicio", Icons.Filled.Home, "Início"),
    Aba("alunos", Icons.Filled.Person, "Alunos"),
    Aba("aulas", Icons.Filled.CalendarMonth, "Aulas"),
    Aba("relatorios", Icons.Filled.Groups, "Relatórios"),
    Aba("perfil", Icons.Filled.Settings, "Perfil")
)

@Composable
fun AppNavigation(vm: AcademiaViewModel, authVm: AuthViewModel) {
    val usuario by authVm.usuario.collectAsState()
    val navRaiz = rememberNavController()

    NavHost(navRaiz, startDestination = if (usuario != null) "app" else "splash") {
        composable("splash") {
            SplashScreen(
                onComecar = { navRaiz.navigate("cadastro") },
                onEntrar = { navRaiz.navigate("login") }
            )
        }
        composable("login") {
            LoginScreen(
                authVm,
                onEntrou = { navRaiz.navigate("app") { popUpTo("splash") { inclusive = true } } },
                onCadastro = { navRaiz.navigate("cadastro") }
            )
        }
        composable("cadastro") {
            CadastroScreen(
                authVm,
                onVoltar = { navRaiz.popBackStack() },
                onCadastrado = { navRaiz.navigate("app") { popUpTo("splash") { inclusive = true } } }
            )
        }
        composable("app") {
            AppPrincipal(vm, authVm) {
                authVm.logout()
                navRaiz.navigate("splash") { popUpTo("app") { inclusive = true } }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppPrincipal(vm: AcademiaViewModel, authVm: AuthViewModel, onSair: () -> Unit) {
    val usuario by authVm.usuario.collectAsState()
    val nav = rememberNavController()
    val entrada by nav.currentBackStackEntryAsState()
    val rota = entrada?.destination?.route
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val escopo = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            MenuLateralConteudo(
                nomeUsuario = usuario?.displayName ?: "",
                emailUsuario = usuario?.email ?: "",
                rotaAtual = rota,
                onNavegar = { destino ->
                    escopo.launch { drawerState.close() }
                    nav.navigate(destino) {
                        popUpTo("inicio") { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onSair = { escopo.launch { drawerState.close() }; onSair() }
            )
        }
    ) {
        Scaffold(
            containerColor = PulseBackground,
            bottomBar = {
                if (ABAS.any { it.rota == rota }) {
                    NavigationBar(containerColor = PulseBackground) {
                        ABAS.forEach { aba ->
                            NavigationBarItem(
                                selected = rota == aba.rota,
                                onClick = {
                                    nav.navigate(aba.rota) {
                                        popUpTo("inicio") { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                },
                                icon = { Icon(aba.icone, contentDescription = aba.rotulo) },
                                label = { Text(aba.rotulo) },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = PulseOrange,
                                    selectedTextColor = PulseOrange,
                                    indicatorColor = PulseOrange.copy(alpha = 0.15f)
                                )
                            )
                        }
                    }
                }
            }
        ) { pad ->
            NavHost(nav, startDestination = "inicio", modifier = Modifier.padding(pad)) {
                composable("inicio") {
                    DashboardScreen(vm, usuario?.displayName ?: "") {
                        escopo.launch { drawerState.open() }
                    }
                }
                composable("alunos") {
                    AlunosScreen(
                        vm,
                        onNovo = { nav.navigate("aluno_form/novo") },
                        onAbrirPerfil = { nav.navigate("perfil_aluno/$it") }
                    )
                }
                composable("aluno_form/{id}") { e ->
                    AlunoFormScreen(vm, e.arguments?.getString("id") ?: "novo") { nav.popBackStack() }
                }
                composable("perfil_aluno/{id}") { e ->
                    val id = e.arguments?.getString("id") ?: ""
                    PerfilAlunoScreen(
                        vm, id,
                        onVoltar = { nav.popBackStack() },
                        onEditar = { nav.navigate("aluno_form/$it") },
                        onEvolucao = { nav.navigate("evolucao/$it") }
                    )
                }
                composable("evolucao/{id}") { e ->
                    val id = e.arguments?.getString("id") ?: ""
                    EvolucaoScreen(vm, id) { nav.popBackStack() }
                }
                composable("aulas") {
                    AulasScreen(
                        vm,
                        onChamada = { nav.navigate("chamada/$it") },
                        onProfessores = { nav.navigate("professores") },
                        onModalidades = { nav.navigate("modalidades") }
                    )
                }
                composable("chamada/{id}") { e ->
                    val id = e.arguments?.getString("id") ?: ""
                    ChamadaAulaScreen(vm, id) { nav.popBackStack() }
                }
                composable("professores") {
                    ProfessoresScreen(vm) { nav.popBackStack() }
                }
                composable("modalidades") {
                    ModalidadesScreen(vm) { nav.popBackStack() }
                }
                composable("relatorios") {
                    RelatoriosScreen(vm) { nav.navigate("relatorio_individual/$it") }
                }
                composable("relatorio_individual/{id}") { e ->
                    val id = e.arguments?.getString("id") ?: ""
                    RelatorioIndividualScreen(vm, id) { nav.popBackStack() }
                }
                composable("perfil") {
                    ConfiguracoesScreen(
                        nomeUsuario = usuario?.displayName ?: "",
                        emailUsuario = usuario?.email ?: "",
                        onSair = onSair
                    )
                }
            }
        }
    }
}