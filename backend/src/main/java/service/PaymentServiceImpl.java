package service;

import model.Event;
import model.Member;
import model.Payment;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

import static java.lang.Math.abs;

/**
 * Created by mihkel on 21.06.2018.
 */
public class PaymentServiceImpl implements PaymentService{

    @Autowired
    EventService eventService;
    @Autowired
    EmailServiceImpl emailService;

    @Override
    public List<Payment> calculatePayments(Event event) {

        List<Payment> retVal = null;
        List<PaymentCombination> runningCombinations = new ArrayList<>();
        List<PaymentCombination> finalCombinations = new ArrayList<>();
        PaymentCombination initialPaymentCombination = new PaymentCombination();
        initialLoad(event, initialPaymentCombination);
        runningCombinations.add(initialPaymentCombination);

        createBestCombination(runningCombinations, finalCombinations);
        retVal = findBest(finalCombinations);
        fillPaymentData(event, retVal);
        if(retVal == null)
            retVal = new ArrayList<>();
        return retVal;
    }

    @Override
    public void sendPayments(Event event, List<Payment> payments) {
        StringBuilder subject = new StringBuilder(event.getName() + " payments");
        List<String> emails = new ArrayList<>();
        StringBuilder content = new StringBuilder("Hello from WalletForTeam !\n\nHere are Your payments to be made.\n\n");
        if(payments != null){
            for(Payment payment : payments){
                if(!emails.equals(payment.getPayorEmail()))
                    emails.add(payment.getPayorEmail());
                if(!emails.equals(payment.getReceivereMail()))
                    emails.add(payment.getReceivereMail());
                content.append(payment.getPayor() + " pays to " + payment.getReceiver() + " ");
                content.append((double)Math.round(payment.getAmount()*100)/100d);
                content.append(" to bankaccount " + payment.getBankAccount() + "\n");
            }
            if(emails.size() != 0) {
                content.append("\n\nThank You!\nYour WalletForTeam team\nwalletforteam.herokuapp.com");
                for(String email : emails){
                    if(!email.isEmpty())
                        emailService.sendSimpleMessage(email, subject.toString(), content.toString());
                }
            }
        }
    }

    private void initialLoad(Event event, PaymentCombination paymentCombination){
        event.getMembers().forEach(member ->
         {if(abs(member.getBalance()) >= 0.01) {
             if (member.getBalance() > 0)
                 paymentCombination.getCreditors().put(member.getId(), member.getBalance());
             else if (member.getBalance() < 0)
                 paymentCombination.getDebitors().put(member.getId(), abs(member.getBalance()));
         }
         });
    }

    private void createBestCombination(List<PaymentCombination> runningCombinations,
                                          List<PaymentCombination> finalCombinations){

        List<PaymentCombination> localrunningCombinations = new ArrayList<>();
        List<PaymentCombination> localfinalCombinations = new ArrayList<>();

        if(runningCombinations.size() == 0)
            return;

        Iterator<PaymentCombination> iter = runningCombinations.iterator();
        while (iter.hasNext()) {
            PaymentCombination paymentCombination = iter.next();

            List<PaymentCombination> newPaymentCombinations = new ArrayList<>();
            paymentCombination.calculatePaymentCombinations(newPaymentCombinations);

            iter.remove();

            for(PaymentCombination newPaymentCombination : newPaymentCombinations){
                if(newPaymentCombination.isDone())
                    localfinalCombinations.add(newPaymentCombination);
                else
                    localrunningCombinations.add(newPaymentCombination);
            }
        }

        runningCombinations.addAll(localrunningCombinations);
        finalCombinations.addAll(localfinalCombinations);
        createBestCombination(runningCombinations, finalCombinations);
    }

    private List<Payment> findBest(List<PaymentCombination> paymentCombinations){
        List<Payment> retVal = null;
        for (PaymentCombination paymentCombination : paymentCombinations){
            if(retVal == null){
                retVal = paymentCombination.getPayments();
            } else
            if(retVal.size() > paymentCombination.payments.size()){
                retVal = paymentCombination.payments;
            }
        }
        return retVal;
    }

    private void fillPaymentData(Event event, List<Payment> paymentList){
        if(paymentList != null) {
            for (Payment payment : paymentList) {
                Member payor = eventService.findMember(event, payment.getPayor());
                Member receiver = eventService.findMember(event, payment.getReceiver());
                if (payor != null) {
                    payment.setPayor(payor.getNickName());
                    payment.setPayorEmail(payor.geteMail());
                }
                if (receiver != null) {
                    payment.setReceiver(receiver.getNickName());
                    payment.setReceivereMail(receiver.geteMail());
                    payment.setBankAccount(receiver.getBankAccount());
                }
            }
        }
    }
}
