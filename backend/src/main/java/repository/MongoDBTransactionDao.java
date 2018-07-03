package repository;


import model.Event;
import model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Repository;


/**
 * Created by mihkel on 6.04.2018.
 */

@Repository
public class MongoDBTransactionDao implements TransactionDao {

    @Autowired
    @Qualifier("mongoOperations")
    private MongoOperations mongoOps;

    @Override
    public void add(Transaction transaction) {
        save(transaction);
    }

    @Override
    public void save(Transaction transaction) {
        mongoOps.save(transaction.getEvent());
    }
    @Override
    public void remove(Transaction transaction) {
        save(transaction);
    }

    @Override
    public void loadTransactions(Event event) {

    }
}
