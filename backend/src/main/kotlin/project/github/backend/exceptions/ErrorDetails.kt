package project.github.backend.exceptions

data class ErrorDetails(
    val timestamp: String,
    val status: Int,
    val error: String,
    val message: String,
    val path: String
)