package infrastructure.repositories

import domain.transfer.MoneyTransfer
import domain.transfer.MoneyTransferRepository
import java.util.*

class InMemoryTransferRepository : MoneyTransferRepository {
    private val transfers: MutableMap<UUID, MoneyTransfer> = Collections.synchronizedMap(mutableMapOf())

    override suspend fun create(transfer: MoneyTransfer): MoneyTransfer {
        transfers.put(transfer.id, transfer)
        return transfer
    }

    override suspend fun update(transfer: MoneyTransfer): MoneyTransfer {
        transfers.replace(transfer.id, transfer)
        return transfer
    }

    override fun getAll(): Collection<MoneyTransfer> {
        return transfers.values
    }
}