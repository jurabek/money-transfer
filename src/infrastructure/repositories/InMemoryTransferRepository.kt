package infrastructure.repositories

import domain.transfer.MoneyTransfer
import domain.transfer.MoneyTransferRepository
import infrastructure.NotFoundException
import java.util.*

class InMemoryTransferRepository : MoneyTransferRepository {
    private val transfers: MutableMap<UUID, MoneyTransfer> = Collections.synchronizedMap(mutableMapOf())

    override fun getById(id: UUID): MoneyTransfer {
        return transfers[id] ?: throw NotFoundException("Money transfer not found!")
    }

    override suspend fun add(transfer: MoneyTransfer): MoneyTransfer {
        transfers[transfer.id] = transfer
        return transfers[transfer.id] ?: throw NotFoundException("Money transfer not found!")
    }

    override suspend fun update(transfer: MoneyTransfer): MoneyTransfer {
        return transfers.replace(transfer.id, transfer) ?: throw NotFoundException("Money transfer not found!")
    }

    override fun getAll(): Collection<MoneyTransfer> {
        return transfers.values
    }
}