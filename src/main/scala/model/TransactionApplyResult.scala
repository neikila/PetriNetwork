package model

/**
  * Created by neikila.
  */
object TransactionApplyResult extends Enumeration {
  type TransactionApplyResult = Value
  val NotEnoughMarks, NoApplicableTransactions, NoTransactionWithSuchID, Success = Value
}
