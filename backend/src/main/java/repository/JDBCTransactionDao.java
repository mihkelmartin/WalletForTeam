package repository;


import model.Event;
import model.Transaction;
import org.springframework.stereotype.Repository;


/**
 * Created by mihkel on 6.04.2018.
 */

@Repository
public class JDBCTransactionDao implements TransactionDao {


    @Override
    public void add(Transaction transaction) {

    }

    @Override
    public void save(Transaction transaction) {

    }
    @Override
    public void remove(Transaction transaction) {

    }

    @Override
    public void loadTransactions(Event event) {

    }
}
