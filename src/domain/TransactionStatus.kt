package domain

enum class TransactionStatus(val value: Int) {
    COMPLETED(1),
    FAILED(2),
    PENDING(3)
}