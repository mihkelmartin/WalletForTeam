package service;

import model.Payment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static java.lang.Math.abs;

/**
 * Created by mihkel on 22.06.2018.
 */
public class PaymentCombination {
    List<Payment> payments = new ArrayList<>();
    HashMap<String, Double> debitors = new HashMap<String, Double>();
    HashMap<String, Double> creditors = new HashMap<String, Double>();

    public PaymentCombination (){

    }

    public PaymentCombination (List<Payment> payments,
                               HashMap<String, Double> debitors,
                               HashMap<String, Double> creditors){
        this.payments.addAll(payments);
        this.debitors.putAll(debitors);
        this.creditors.putAll(creditors);
    }

    public List<Payment> getPayments() {
        return payments;
    }

    public void calculatePaymentCombinations(List<PaymentCombination> paymentCombinations){

        for (HashMap.Entry<String, Double> creditor : creditors.entrySet()) {
            for (HashMap.Entry<String, Double> debitor : debitors.entrySet()) {
                Double amount = 0.0;
                PaymentCombination paymentCombination = new PaymentCombination(this.payments,
                        this.debitors, this.creditors);
                paymentCombinations.add(paymentCombination);
                if(debitor.getValue() <= creditor.getValue()){
                    amount = debitor.getValue();
                    if(abs(creditor.getValue() - amount) < 0.01)
                        paymentCombination.creditors.remove(creditor.getKey());
                    else
                        paymentCombination.creditors.replace(creditor.getKey(), creditor.getValue() - amount);
                    paymentCombination.debitors.remove(debitor.getKey());
                } else {
                    amount = creditor.getValue();
                    if(abs(debitor.getValue() - amount) < 0.01)
                        paymentCombination.debitors.remove(debitor.getKey());
                    else
                        paymentCombination.debitors.replace(debitor.getKey(), debitor.getValue() - amount);
                    paymentCombination.creditors.remove(creditor.getKey());
                }
                paymentCombination.addPayment(debitor.getKey(), creditor.getKey(), amount);
            }
        }
    }

    public boolean isDone(){
        return debitors.size() == 0;
    }

    public HashMap<String, Double> getDebitors() {
        return debitors;
    }

    public HashMap<String, Double> getCreditors() {
        return creditors;
    }

    private void addPayment(String payor, String receiver, Double amount){
        Payment payment = new Payment();
        payment.setPayor(payor);
        payment.setReceiver(receiver);
        payment.setAmount(amount);
        int newOrderNr = 1;
        if(!getPayments().isEmpty()){
            Collections.sort(getPayments());
            Collections.reverse(getPayments());
            newOrderNr = getPayments().get(0).getOrder() + 1;
        }
        payment.setOrder(newOrderNr);
        payments.add(payment);
    }
}
