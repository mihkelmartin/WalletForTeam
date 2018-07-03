package service;

import model.Event;
import model.Member;
import model.Transaction;
import model.TransactionItem;
import org.springframework.beans.factory.annotation.Autowired;
import repository.TransactionDao;
import repository.TransactionItemDao;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by mihkel on 11.04.2018.
 */
public class TransactionServiceImpl implements TransactionService{

    @Autowired
    private TransactionDao transactionDao;

    @Autowired
    private TransactionItemDao transactionItemDao;


    public Transaction add(Event event, Transaction newTransaction) {
        // Set technical attributes before copy
        newTransaction.setEvent(event);
        newTransaction.setOrder(event.getNextOrderNr(event.getTransactions()));

        Transaction retVal = new Transaction();
        retVal.update(newTransaction);

        event.getTransactions().add(retVal);
        transactionDao.add(retVal);
        populateTransactionItems(retVal, event.getMembers());
        return retVal;
    }

    public Transaction save(Transaction transaction, Transaction updatedTransaction) {
        // Set technical attributes before copy
        updatedTransaction.setEvent(transaction.getEvent());

        transaction.update(updatedTransaction);
        transactionDao.save(transaction);
        return transaction;
    }

    public Transaction remove(Transaction transaction) {
        removeTransactionItemsFromTransaction(transaction);
        transaction.getEvent().getTransactions().remove(transaction);
        transactionDao.remove(transaction);
        recalculateOrderNumbers(transaction);
        return transaction;
    }

    private void recalculateOrderNumbers(Transaction removed){
        for(Transaction transaction: removed.getEvent().getTransactions()){
            if(transaction.getOrder() > removed.getOrder()) {
                transaction.setOrder(transaction.getOrder() - 1);
                transactionDao.save(transaction);
            }
        }
    }

    public Member addMemberToTransactions(Member member) {
        for(Transaction transaction : member.getEvent().getTransactions()){
            TransactionItem retVal = new TransactionItem(transaction.getId(), member.getId(),
                    0.0, 0.0, true, transaction);
            transaction.getItems().add(retVal);
            transactionItemDao.add(retVal);        }
        return member;
    }

    public Member removeMemberFromTransactions(Member member) {
        for(Transaction transaction : member.getEvent().getTransactions()){
            removeTransactionItemsWithMember(transaction, member);
        }
        return member;
    }

    public TransactionItem addDebitForMember(Transaction transaction, Member member, double debit){
        TransactionItem transactionItem = null;
        for(TransactionItem item : transaction.getItems()){
            if(item.getMemberId().equals(member.getId())) {
                transactionItem = item;
                if(debit >= 0) {
                    transactionItem.update(debit, transactionItem.getCredit(), transactionItem.isBcreditAutoCalculated());
                    transactionItemDao.save(transactionItem);
                }
                break;
            }
        }
        return transactionItem;
    }

    public TransactionItem addCreditForMember(Transaction transaction, Member member, double credit){
        TransactionItem transactionItem = null;
        for(TransactionItem item : transaction.getItems()){
            if(item.getMemberId().equals(member.getId())) {
                transactionItem = item;
                if(credit >= 0) {
                    transactionItem.update(transactionItem.getDebit(), credit, false);
                    transactionItemDao.save(transactionItem);
                }
                break;
            }
        }
        return transactionItem;
    }

    public TransactionItem setAutoCalculationForMember(Transaction transaction, Member member, boolean bAutoCalculation){
        TransactionItem transactionItem = null;
        for(TransactionItem item : transaction.getItems()){
            if(item.getMemberId().equals(member.getId())) {
                transactionItem = item;
                transactionItem.update(transactionItem.getDebit(), transactionItem.getCredit(), bAutoCalculation);
                transactionItemDao.save(transactionItem);
                break;
            }
        }
        return transactionItem;
    }

    public TransactionItem getTransactionItemForMember(Transaction transaction, Member member){
        TransactionItem transactionItem = null;
        for(TransactionItem item : transaction.getItems()){
            if(item.getMemberId().equals(member.getId())) {
                transactionItem = item;
                break;
            }
        }
        return transactionItem;
    }

    private void populateTransactionItems(Transaction transaction, ArrayList<Member> members) {
        for(Member member : members){
            TransactionItem retVal = new TransactionItem(transaction.getId(), member.getId(),
                    0.0, 0.0, true, transaction);
            transaction.getItems().add(retVal);
            transactionItemDao.add(retVal);
        }
    }

    private void removeTransactionItemsWithMember(Transaction transaction, Member member){
        Iterator<TransactionItem> iter = transaction.getItems().iterator();
        while (iter.hasNext()) {
            TransactionItem transactionItem = iter.next();
            if (transactionItem.getMemberId().equals(member.getId())) {
                transactionItemDao.remove(transactionItem);
                iter.remove();
            }
        }
    }

    private void removeTransactionItemsFromTransaction(Transaction transaction){
        Iterator<TransactionItem> iter = transaction.getItems().iterator();
        while (iter.hasNext()) {
            TransactionItem transactionItem = iter.next();
            transactionItemDao.remove(transactionItem);
            iter.remove();
        }
    }

    public void calculateCredits(Transaction transaction){

        double dAutoCalculatedCredit = 0.0;
        int lAutoCalculatedCreditCount = getAutoCalculatedCreditCount(transaction);
        if(lAutoCalculatedCreditCount != 0) {
            dAutoCalculatedCredit = (getDebit(transaction) - getManualCredit(transaction)) /
                    getAutoCalculatedCreditCount(transaction);
            for(TransactionItem item : transaction.getItems()){
                if(item.isBcreditAutoCalculated()) {
                    item.update(item.getDebit(), dAutoCalculatedCredit, item.isBcreditAutoCalculated());
                    transactionItemDao.save(item);                }
            }
        }
    }

    private double getDebit(Transaction transaction){
        double retVal = 0.0;
        for(TransactionItem item : transaction.getItems()){
            retVal += item.getDebit();
        }
        return retVal;
    }

    private double getManualCredit(Transaction transaction){
        double retVal = 0.0;
        for(TransactionItem item : transaction.getItems()){
            if(!item.isBcreditAutoCalculated())
                retVal += item.getCredit();
        }
        return retVal;
    }

    private int getAutoCalculatedCreditCount(Transaction transaction){
        int retVal = 0;
        for(TransactionItem item : transaction.getItems()){
            if(item.isBcreditAutoCalculated())
                retVal ++;
        }
        return retVal;
    }

    public void loadTransactions(Event event) {
        if(event != null) {
            transactionDao.loadTransactions(event);
            for(Transaction transaction : event.getTransactions()){
                transactionItemDao.loadTransactionItems(transaction);
            }
        }
    }
}
