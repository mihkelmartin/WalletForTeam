package service;

import model.Event;
import model.Member;
import model.Transaction;
import model.TransactionItem;

/**
 * Created by mihkel on 11.04.2018.
 */
public interface TransactionService {
    Transaction add(Event event, Transaction newTransaction);
    Transaction save(Transaction transaction, Transaction updatedTranaction);
    Transaction remove(Transaction transaction);
    Member addMemberToTransactions(Member member);
    Member removeMemberFromTransactions(Member member);
    TransactionItem addDebitForMember(Transaction transaction, Member member, double debit);
    TransactionItem addCreditForMember(Transaction transaction, Member member, double credit);
    TransactionItem setAutoCalculationForMember(Transaction transaction, Member member, boolean bAutoCalculation);
    void calculateCredits(Transaction transaction);
    void loadTransactions(Event event);
    TransactionItem getTransactionItemForMember(Transaction transaction, Member member);
}
