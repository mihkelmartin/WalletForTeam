package model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Transient;
import org.springframework.lang.NonNull;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.ArrayList;


/**
 * Created by mihkel on 5.04.2018.
 */
public class TransactionItem {

    private String transactionId;
    private String memberId;
    @Min(0) @Max(999999)
    private double debit = 0.0;
    @Min(0) @Max(999999)
    private double credit = 0.0;
    private boolean bcreditAutoCalculated = true;
    @Transient
    @JsonIgnore
    private Transaction transaction = null;

    public TransactionItem(){

    }

    public TransactionItem(String transactionId, String memberId, double debit, double credit, boolean bcreditAutoCalculated, Transaction transaction) {
        this.transactionId = transactionId;
        this.memberId = memberId;
        this.debit = debit;
        this.credit = credit;
        this.bcreditAutoCalculated = bcreditAutoCalculated;
        this.transaction = transaction;
    }

    public void update (double debit, double credit, boolean bcreditAutoCalculated) {
        this.debit = debit;
        this.credit = credit;
        this.bcreditAutoCalculated = bcreditAutoCalculated;
    }

    public String getTransactionId() {
        return transactionId;
    }
    public String getMemberId() {
        return memberId;
    }

    public double getDebit() {
        return debit;
    }
    public void setDebit(double debit) {
        this.debit = debit;
    }

    public double getCredit() {
        return credit;
    }
    public void setCredit(double credit) {
        this.credit = credit;
    }

    public boolean isBcreditAutoCalculated() {
        return bcreditAutoCalculated;
    }
    public void setBcreditAutoCalculated(boolean bcreditAutoCalculated) {
        this.bcreditAutoCalculated = bcreditAutoCalculated;
    }

    public Transaction getTransaction() {
        return transaction;
    }
    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }
}
