package aspects;

import model.Member;
import model.Transaction;
import model.TransactionItem;
import service.MemberService;
import service.TransactionService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by mihkel on 10.04.2018.
 */

@Aspect
public class MoneyCalculationAspect {

    @Autowired
    private TransactionService transactionService;
    @Autowired
    private MemberService memberService;

    @AfterReturning(value="(execution(* service.TransactionService.addDebitForMember(..)) || " +
            "execution(* service.TransactionService.addCreditForMember(..)) || " +
            "execution(* service.TransactionService.setAutoCalculationForMember(..)))" +
            " && target(bean)",
    returning = "retVal")
    public void doCalculationTransaction(JoinPoint jp, TransactionService bean, TransactionItem retVal) {
        bean.calculateCredits(retVal.getTransaction());
        memberService.calculateMembersMoney(retVal.getTransaction().getEvent());
    }

    @AfterReturning(value="execution(* service.MemberService.add(..)) || " +
            "execution(* service.MemberService.remove(..))",
    returning = "retVal")
    public void doCalculationEventTransactions(JoinPoint jp, Member retVal) {
        for(Transaction transaction : retVal.getEvent().getTransactions()){
            transactionService.calculateCredits(transaction);
        }
        memberService.calculateMembersMoney(retVal.getEvent());
    }

    @AfterReturning(value="execution(* service.MemberService.save(..))",
            returning = "retVal")
    public void doCalculateMembersMoneyMember(JoinPoint jp, Member retVal) {
        memberService.calculateMembersMoney(retVal.getEvent());
    }

    @AfterReturning(value="execution(* service.TransactionService.add(..)) || " +
            "execution(* service.TransactionService.remove(..))",
            returning = "retVal")
    public void doCalculateMembersMoneyTransaction(JoinPoint jp, Transaction retVal) {
        memberService.calculateMembersMoney(retVal.getEvent());
    }

}

