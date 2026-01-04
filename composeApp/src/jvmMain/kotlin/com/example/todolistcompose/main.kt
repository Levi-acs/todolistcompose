package com.example.todolistcompose

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "To-Do List - Kotlin"
    ) {
        App()
    }
}

@Composable
@Preview
fun App() {

    val gerenciador = remember { GerenciadorTarefas() }

    MaterialTheme {
        TodoListScreen(gerenciador)
    }
}
@Composable
fun TodoListScreen(gerenciador: GerenciadorTarefas) {
    var tarefas by remember { mutableStateOf(gerenciador.listarTarefas()) }
    var novaTarefaTexto by remember { mutableStateOf("") }
    var prioridadeSelecionada by remember { mutableStateOf(Prioridade.MEDIA) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "📝 Minhas Tarefas",
            style = MaterialTheme.typography.h4,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            elevation = 4.dp
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                TextField(
                    value = novaTarefaTexto,
                    onValueChange = { novaTarefaTexto = it },
                    label = { Text("Nova Tarefa") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Prioridade:")

                    Row {
                        Prioridade.values().forEach { prioridade ->
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                RadioButton(
                                    selected = prioridadeSelecionada == prioridade,
                                    onClick = { prioridadeSelecionada = prioridade }
                                )
                                Text(prioridade.name)
                                Spacer(modifier = Modifier.width(8.dp))
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        if (novaTarefaTexto.isNotBlank()) {
                            gerenciador.adicionarTarefa(novaTarefaTexto, prioridadeSelecionada)
                            tarefas = gerenciador.listarTarefas()
                            novaTarefaTexto = ""
                        }
                    },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("➕ Adicionar")
                }
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                items = tarefas,
                key = { it.id }  // IMPORTANTE: usa ID como key
            ) { tarefa ->
                TarefaItem(
                    tarefa = tarefa,
                    onConcluir = {
                        gerenciador.marcarComoConcluida(tarefa.id)
                        gerenciador.forcarAtualizacao()
                        tarefas = gerenciador.listarTarefas()
                    },
                    onRemover = {
                        gerenciador.removerTarefa(tarefa.id)
                        tarefas = gerenciador.listarTarefas()
                    }
                )
            }
        }
    }
}
@Composable
fun TarefaItem(
    tarefa: Tarefa,
    onConcluir: () -> Unit,
    onRemover: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = 2.dp,
        backgroundColor = if (tarefa.concluida)
            MaterialTheme.colors.surface.copy(alpha = 0.6f)
        else
            MaterialTheme.colors.surface
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = tarefa.descricao,
                    style = MaterialTheme.typography.body1,
                    textDecoration = if (tarefa.concluida)
                        TextDecoration.LineThrough
                    else
                        null,
                    color = if (tarefa.concluida)
                        Color.Gray
                    else
                        Color.Black
                )

                Spacer(modifier = Modifier.height(4.dp))

                val corPrioridade = when (tarefa.prioridade) {
                    Prioridade.BAIXA -> Color.Green
                    Prioridade.MEDIA -> Color.Blue
                    Prioridade.ALTA -> Color.Red
                }

                Text(
                    text = "● ${tarefa.prioridade.name}" + if (tarefa.concluida) " - CONCLUÍDA" else "",
                    style = MaterialTheme.typography.caption,
                    color = if (tarefa.concluida) Color.Gray else corPrioridade
                )
            }

            Row {
                if (!tarefa.concluida) {
                    Button(
                        onClick = onConcluir,
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color.Green
                        )
                    ) {
                        Text("✓")
                    }

                    Spacer(modifier = Modifier.width(8.dp))
                }

                Button(
                    onClick = onRemover,
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.Red
                    )
                ) {
                    Text("✕")
                }
            }
        }
    }
}