# Transactions

The basic idea is a bank funds transfer from account A to account B that, if crediting from account A fails then the debit to account B should either never take place or be rolled back.

### Example

Sample money transfer scenario. It could be implemented like this:

```kotlin
mutex.lock() {
  val accountA = accounts.findByName("A")
  val accountB = accounts.findByName("B")

  accountA.credit(transferAmount)
  accountA.save()

  accountB.debit(transferAmount)
  accountB.save()
  
  transfers.save(Transfer(accountA, accountB))
}
```
## Basics

The money transfer functionality handled by `CreateTransferCommandHandler` this class more critical part of the application because it should be executed on safe concurrency. The class contains a method named `handle`, when the method called and `CreateTransferCommand` instance passed, and it executes synchronized block provided by Kotlin which works for `java threads` too, it means the same account cannot start transfer simultaneously from the different threads.

## DDD based architictural overview

<img src="handler_diagram.png" />

In that diagram the entry point is `Transfer Router` that handles HTTP Post request. The second step would be `CreateTransferCommandHandler` that changes `Account Aggregate` e.g subtracts and adds money for the current balance according to input parameters, during the action it also creates Domain Events such as `Money Debited` and `Money Credited` for each account to be able to trigger side effects accross multiple aggregates in our case it triggers `Transaction Aggregate` using Event Store. Event Store and Comamnd Handler implemented as an in-memory mediator based on an IoC container.

### Endpoints

```shell script
curl --request POST \
  --url http://localhost:8080/transfer \
  --header 'content-type: application/json' \
  --data '{
	"sourceAccountId" : "591f6eb1-de7e-4d25-8afa-bd82d85ee565",
	"targetAccountId" : "8657792e-8329-4723-bf04-837d3649e9af",
	"amount": 10.2,
	"currency": "EUR",
	"reference": "Some message"
  }
```

## References

