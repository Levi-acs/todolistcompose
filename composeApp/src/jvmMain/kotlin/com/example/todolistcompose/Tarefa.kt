package com.example.todolistcompose

enum class Prioridade{
    BAIXA,MEDIA,ALTA
}


data class Tarefa(
    val id: Int,
    val descricao: String,
    val prioridade: Prioridade,
    var concluida: Boolean = false
)
