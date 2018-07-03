package model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.core.Ordered;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.lang.NonNull;

import javax.validation.constraints.Size;
import java.util.UUID;


/**
 * Created by mihkel on 5.04.2018.
 */

public class Member implements Comparable<Member>, Ordered {

    @Id
    private String id;
    @Size(max = 24)
    private String name = "";
    @Size(max = 6)
    private String nickName = "";
    @Indexed
    @Size(max = 64)
    private String eMail = "";
    @Size(max = 34)
    private String bankAccount = "";
    private int order = 0;
    private String payor;
    private double debit = 0.0;
    private double credit = 0.0;
    @Transient
    @JsonIgnore
    private Event event = null;

    public Member (){
        setId(UUID.randomUUID().toString());
        setPayor(getId());
    }

    public void update(Member member) {
        setName(member.getName());
        setNickName(member.getNickName());
        seteMail(member.geteMail());
        setBankAccount(member.getBankAccount());
        setOrder(member.getOrder());
        setPayor(member.getPayor());
        setEvent(member.getEvent());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    @Override
    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getPayor() {
        return payor;
    }

    public void setPayor(String payor) {
        this.payor = payor;
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

    public double getBalance(){
        return getDebit()- getCredit();
    }
    @Override
    public int compareTo(Member o) {
        return (this.getOrder() < o.getOrder() ? -1 :
                (this.getOrder() == o.getOrder() ? 0 : 1));
    }
}
