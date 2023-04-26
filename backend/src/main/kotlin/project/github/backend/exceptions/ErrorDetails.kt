package project.github.backend.exceptions

/**
 * Data class for error details to be sent as response.
 */
data class ErrorDetails(
    val timestamp: String,
    val status: Int,
    val error: String,
    val message: String?,
    val path: String
)