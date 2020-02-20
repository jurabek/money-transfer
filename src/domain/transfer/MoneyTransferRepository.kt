package domain.transfer

interface MoneyTransferRepository {
    suspend fun create(transfer: MoneyTransfer): MoneyTransfer
    suspend fun update(transfer: MoneyTransfer): MoneyTransfer
    fun getAll(): Collection<MoneyTransfer>
}