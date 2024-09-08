package com.example.navegacao1.ui.telas

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.navegacao1.model.dados.Endereco
import com.example.navegacao1.model.dados.RetrofitClient
import com.example.navegacao1.model.dados.Usuario
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@Composable
fun TelaPrincipal(modifier: Modifier = Modifier) {
    var usuarios by remember { mutableStateOf<List<Usuario>>(emptyList()) }
    var nomeUsuario by remember { mutableStateOf("") }
    var senhaUsuario by remember { mutableStateOf("") }
    var usuarioId by remember { mutableStateOf("") }
    var usuarioEncontrado by remember { mutableStateOf<Usuario?>(null) }
    var error by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    Column(modifier = modifier.padding(16.dp)) {
        Text(text = "Tela Principal", style = MaterialTheme.typography.titleLarge)

        OutlinedTextField(
            value = usuarioId,
            onValueChange = { usuarioId = it },
            label = { Text("ID do Usuário") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = nomeUsuario,
            onValueChange = { nomeUsuario = it },
            label = { Text("Nome do Usuário") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = senhaUsuario,
            onValueChange = { senhaUsuario = it },
            label = { Text("Senha do Usuário") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = {
            scope.launch {
                try {
                    val usuarioExistente = findUsuario(usuarioId)
                    if (usuarioExistente != null) {
                        error = "Usuário com o ID fornecido já existe."
                    } else {
                        val newUser = Usuario(id = usuarioId, nome = nomeUsuario, senha = senhaUsuario)
                        createUsuario(newUser)
                        nomeUsuario = ""
                        senhaUsuario = ""
                        usuarioId = ""
                        error = null
                        usuarios = getUsuarios()  // Atualiza a lista de usuários
                    }
                } catch (e: Exception) {
                    error = "Erro ao adicionar usuário."
                }
            }
        }) {
            Text("Adicionar Usuário")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = {
            scope.launch {
                try {
                    deleteUsuario(usuarioId)
                    usuarioId = ""
                    error = null
                    usuarios = getUsuarios()  // Atualiza a lista de usuários
                } catch (e: Exception) {
                    error = "Erro ao remover usuário."
                }
            }
        }) {
            Text("Remover Usuário")
        }

        Spacer(modifier = Modifier.height(16.dp))

        usuarioEncontrado?.let { usuario ->
            Text(text = "Usuário Encontrado:", style = MaterialTheme.typography.titleMedium)
            Text(text = "ID: ${usuario.id}")
            Text(text = "Nome: ${usuario.nome}")
            Text(text = "Senha: ${usuario.senha}")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (error != null) {
            Text(text = error!!, color = MaterialTheme.colorScheme.error)
        }

        LazyColumn {
            items(usuarios) { usuario ->
                Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "ID: ${usuario.id}")
                        Text(text = "Nome: ${usuario.nome}")
                        Text(text = "Senha: ${usuario.senha}")
                    }
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        scope.launch {
            usuarios = getUsuarios()
        }
    }
}

suspend fun getUsuarios(): List<Usuario> {
    return withContext(Dispatchers.IO) {
        RetrofitClient.usuarioService.listar()
    }
}

suspend fun createUsuario(usuario: Usuario): Usuario {
    return withContext(Dispatchers.IO) {
        RetrofitClient.usuarioService.adicionar(usuario)
    }
}

suspend fun deleteUsuario(id: String) {
    return withContext(Dispatchers.IO) {
        RetrofitClient.usuarioService.remover(id)
    }
}

suspend fun findUsuario(id: String): Usuario? {
    return withContext(Dispatchers.IO) {
        try {
            RetrofitClient.usuarioService.buscar(id)
        } catch (e: Exception) {
            null
        }
    }
}

suspend fun getEndereco(): Endereco {
    return withContext(Dispatchers.IO) {
        RetrofitClient.usuarioService.getEndereco()
    }
}