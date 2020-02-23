package application.queries

import application.dtos.account.AccountBalanceDto
import application.dtos.account.AccountDto
import domain.account.BankAccountRepository
import java.util.*

class AccountQueries(private val bankAccountRepository: BankAccountRepository) {
    fun getAllAccounts(): List<AccountDto> {
        return bankAccountRepository.getAll().map {
            AccountDto(
                it.id,
                it.balance.amount,
                it.balance.currency
            )
        }
    }

    fun getAccountBalanceById(id: UUID): AccountBalanceDto {
        val account = bankAccountRepository.getById(id)
        return AccountBalanceDto(account.balance.amount, account.balance.currency)
    }
}