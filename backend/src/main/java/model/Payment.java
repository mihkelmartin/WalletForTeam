package model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.util.UUID;

/**
 * Created by mihkel on 21.06.2018.
 */
public class Payment implements Comparable<Payment> {

    @Id
    @Indexed
    private String id;
    @Size(max = 24)
    private String payor = "";
    @Size(max = 24)
    private String receiver = "";
    @Size(max = 34)
    private String bankAccount = "";
    @Min(0) @Max(999999)
    private double amount = 0.0;
    @Size(max = 64)
    private String receivereMail = "";
    private int order = 0;

    public Payment (){
        setId(UUID.randomUUID().toString());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPayor() {
        return payor;
    }

    public void setPayor(String payor) {
        this.payor = payor;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getReceivereMail() {
        return receivereMail;
    }

    public void setReceivereMail(String receivereMail) {
        this.receivereMail = receivereMail;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    @Override
    public int compareTo(Payment o) {
        return (this.getOrder() < o.getOrder() ? -1 :
                (this.getOrder() == o.getOrder() ? 0 : 1));
    }
}
