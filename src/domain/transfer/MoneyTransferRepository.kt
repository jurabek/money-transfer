package domain.transfer

import java.util.*

interface MoneyTransferRepository {
    fun getById(id: UUID): MoneyTransfer
    fun getAll(): Collection<MoneyTransfer>
    suspend fun add(transfer: MoneyTransfer): MoneyTransfer
    suspend fun update(transfer: MoneyTransfer): MoneyTransfer
}