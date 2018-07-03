package repository;

import model.Transaction;
import model.TransactionItem;

/**
 * Created by mihkel on 6.04.2018.
 */
public interface TransactionItemDao extends GeneralDao<TransactionItem> {
    void loadTransactionItems(Transaction transaction);
}
