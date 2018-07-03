package service;

import model.Event;
import model.Member;
import model.Transaction;
import model.TransactionItem;
import org.springframework.beans.factory.annotation.Autowired;
import repository.MemberDao;

import java.util.ArrayList;

/**
 * Created by mihkel on 11.04.2018.
 */
public class MemberServiceImpl implements MemberService {

    @Autowired
    TransactionService transactionService;

    @Autowired
    private MemberDao memberDao;

    @Override
    public Member add(Event event, Member newMember) {
        // Set technical attributes before copy
        newMember.setEvent(event);
        newMember.setOrder(event.getNextOrderNr(event.getMembers()));

        Member retVal = new Member();
        retVal.update(newMember);
        // Set Payour to itself
        retVal.setPayor(retVal.getId());

        event.getMembers().add(retVal);
        memberDao.add(retVal);
        transactionService.addMemberToTransactions(retVal);
        return retVal;
    }

    @Override
    public Member save(Member member, Member updatedMember) {
        // Set technical attributes before copy
        updatedMember.setEvent(member.getEvent());

        member.update(updatedMember);
        memberDao.save(member);
        return member;
    }

    @Override
    public Member remove(Member member) {
        transactionService.removeMemberFromTransactions(member);
        member.getEvent().getMembers().remove(member);
        reassignRemovedPayor(member);
        memberDao.remove(member);
        recalculateOrderNumbers(member);
        return member;
    }

    private void reassignRemovedPayor(Member removedMember){
        for(Member member : removedMember.getEvent().getMembers()) {
            if(member.getPayor().equals(removedMember.getId())) {
                member.setPayor(member.getId());
                memberDao.save(member);
            }
        }
    }

    private void calculateMemberMoney(Member member) {

        member.setDebit(0.0);
        member.setCredit(0.0);

        Event event = member.getEvent();
        ArrayList<Transaction> transactions= event.getTransactions();

        for(Member slave : event.getMembers()) {
            if(member.getId().equals(slave.getPayor())) {
                for (Transaction transaction : transactions) {
                    for (TransactionItem transactionItem : transaction.getItems()) {
                        if (transactionItem.getMemberId().equals(slave.getId())) {
                            member.setCredit(member.getCredit() + transactionItem.getCredit());
                            member.setDebit(member.getDebit() + transactionItem.getDebit());
                        }
                    }
                }
            }
        }
    }

    @Override
    public void calculateMembersMoney(Event  event) {
        for(Member member : event.getMembers()){
            calculateMemberMoney(member);
            memberDao.save(member);
        }
    }

    @Override
    public void loadMembers(Event event) {
        if(event != null)
            memberDao.loadMembers(event);
    }

    private void recalculateOrderNumbers(Member removed){
        for(Member member : removed.getEvent().getMembers()){
            if(member.getOrder() > removed.getOrder()){
                member.setOrder(member.getOrder() - 1);
                memberDao.save(member);
            }
        }
    }
}
