package model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.core.Ordered;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.lang.NonNull;

import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.UUID;


/**
 * Created by mihkel on 5.04.2018.
 */
public class Transaction implements Comparable<Transaction>, Ordered {

    @Id
    private String id;
    @Size(max = 48)
    private String name = "";
    private boolean bmanualCalculation = false;
    private int order = 0;
    private ArrayList<TransactionItem> items = new ArrayList<>();
    @Transient
    @JsonIgnore
    private Event event = null;

    public Transaction(){
        this.id = UUID.randomUUID().toString();
    }

    public void update(Transaction transaction) {
        setName(transaction.getName());
        setBmanualCalculation(transaction.isBmanualCalculation());
        setOrder(transaction.getOrder());
        setEvent(transaction.getEvent());
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isBmanualCalculation() {
        return bmanualCalculation;
    }

    public void setBmanualCalculation(boolean bmanualCalculation) {
        this.bmanualCalculation = bmanualCalculation;
    }

    public ArrayList<TransactionItem> getItems() {
        return items;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    @Override
    public int getOrder() {
        return this.order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    @Override
    public int compareTo(Transaction o) {
        return (this.getOrder() < o.getOrder() ? -1 :
                (this.getOrder() == o.getOrder() ? 0 : 1));
    }

}
