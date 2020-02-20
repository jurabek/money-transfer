package application.queries

import application.dtos.account.AccountBalanceDto
import application.dtos.account.AccountDto
import domain.account.AccountRepository
import java.util.*

class AccountQueries(private val accountRepository: AccountRepository) {
    fun getAllAccounts(): List<AccountDto> {
        return accountRepository.getAll().map {
            AccountDto(
                it.id,
                it.balance,
                it.currency
            )
        }
    }

    fun getAccountBalanceById(id: UUID): AccountBalanceDto {
        val account = accountRepository.getById(id)
        return AccountBalanceDto(account.balance, account.currency)
    }
}