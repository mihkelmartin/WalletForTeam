package repository;

import model.Transaction;
import model.TransactionItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Repository;

/**
 * Created by mihkel on 17.04.2018.
 */
@Repository
public class MongoDBTransactionItemDao implements TransactionItemDao {

    @Autowired
    @Qualifier("mongoOperations")
    private MongoOperations mongoOps;

    @Autowired
    private TransactionDao transactionDao;

    @Override
    public void add(TransactionItem transactionItem) {
        save(transactionItem);
    }

    @Override
    public void save(TransactionItem transactionItem) {
        transactionDao.save(transactionItem.getTransaction());
    }

    @Override
    public void remove(TransactionItem transactionItem) {
        save(transactionItem);
    }

    @Override
    public void loadTransactionItems(Transaction transaction) {

    }

}
