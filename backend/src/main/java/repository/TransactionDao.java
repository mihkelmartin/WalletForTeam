package repository;

import model.Event;
import model.Transaction;

/**
 * Created by mihkel on 6.04.2018.
 */
public interface TransactionDao extends GeneralDao<Transaction> {
    void loadTransactions(Event event);
}
