package repository;

import model.Transaction;
import model.TransactionItem;
import org.springframework.stereotype.Repository;


/**
 * Created by mihkel on 6.04.2018.
 */

@Repository
public class JDBCTransactionItemDao implements TransactionItemDao {

      @Override
    public void add(TransactionItem transactionItem) {

    }

    @Override
    public void save(TransactionItem transactionItem) {

    }

    @Override
    public void remove(TransactionItem transactionItem) {

    }

    @Override
    public void loadTransactionItems(Transaction transaction) {

    }

}
