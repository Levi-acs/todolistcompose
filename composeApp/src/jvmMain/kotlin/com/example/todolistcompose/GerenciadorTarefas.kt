package com.example.todolistcompose

class GerenciadorTarefas {
    private val tarefas = mutableListOf<Tarefa>()
    private var proximoId = 1

    fun adicionarTarefa(descricao: String, prioridade: Prioridade) {
        val novaTarefa = Tarefa(
            id = proximoId++,
            descricao = descricao,
            prioridade = prioridade
        )
        tarefas.add(novaTarefa)
    }

    fun listarTarefas(): List<Tarefa> {
        return tarefas.toList()
    }

    fun marcarComoConcluida(id: Int): Boolean {
        val tarefa = tarefas.find { it.id == id }
        return if (tarefa != null && !tarefa.concluida) {
            tarefa.concluida = true
            true
        } else {
            false
        }
    }

    fun removerTarefa(id: Int): Boolean {
        return tarefas.removeIf { it.id == id }
    }
}