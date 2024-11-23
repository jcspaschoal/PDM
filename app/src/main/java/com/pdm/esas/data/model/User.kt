package com.pdm.esas.data.model

import com.google.firebase.Timestamp

data class User(
    val firstName: String,
    val lastName: String,
    var phone: String,
    val email: String,
    var permissions: List<String> = emptyList(),
    var docId: String? = null,
    val createdAt: Timestamp = Timestamp.now(),
    val updatedAt: Timestamp = Timestamp.now()
) {
    init {
        require(firstName.isNotBlank()) { "Primeiro nome não pode estar em branco." }
        require(lastName.isNotBlank()) { "Apelido não pode estar em branco." }
        require(phone.matches(Regex("^9\\d{8}$"))) {
            "O número de telemóvel deve começar por 9 e ter 9 dígitos."
        }
        require(email.matches(Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"))) {
            "Formato de email inválido."
        }
    }
}
