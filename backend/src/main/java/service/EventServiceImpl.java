package service;

import model.Event;
import model.Member;
import model.Transaction;
import model.TransactionItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import repository.EventDao;

import java.util.Date;
import java.util.List;

/**
 * Created by mihkel on 11.04.2018.
 */
public class EventServiceImpl implements EventService {

    @Autowired
    MemberService memberService;
    @Autowired
    TransactionService transactionService;

    @Autowired
    private EventDao eventDao;

    @Override
    public Event add(Event newEvent) {
        Event event = new Event();
        event.update(newEvent);
        eventDao.save(event);
        return event;
    }

    @Override
    public Event save(Event event, Event updatedEvent) {
        event.update(updatedEvent);
        eventDao.save(event);
        return event;
    }

    @Override
    public Event remove(Event event) {
        eventDao.remove(event);
        return event;
    }

    @Override
    public Event loadEvent(String id) {
        Event event = eventDao.loadEvent(id);

        if(event != null) {
            event.setEventAccessedTS(new Date());
            eventDao.save(event);
        }

        loadMembers(event);
        loadTransactions(event);
        updateForeignKeys(event);
        return event;
    }

    @Override
    public List<Event> loadEventsByEmail(String eMail) {
        return eventDao.loadEventsByemail(eMail);
    }

    // 1 hour
    @Scheduled(fixedDelay=3600000)
    public void removeUnusedEvents() {
        eventDao.removeUnusedEvents();
    }

    @Override
    public Member findMember(Event event, String id) {
        Member retVal = null;
        for( Member member : event.getMembers()){
            if(member.getId().equals(id))
                retVal = member;
        }
        return retVal;
    }

    @Override
    public Transaction findTransaction(Event event, String id) {
        Transaction retVal = null;
        for( Transaction transaction: event.getTransactions()){
            if(transaction.getId().equals(id))
                retVal = transaction;
        }
        return retVal;
    }

    private void loadMembers(Event event){
        memberService.loadMembers(event);
    }

    private void loadTransactions(Event event){
        transactionService.loadTransactions(event);
    }

    private void updateForeignKeys(Event event){
        if(event != null) {
            for (Member member : event.getMembers())
                member.setEvent(event);
            for (Transaction transaction : event.getTransactions()) {
                transaction.setEvent(event);
                for(TransactionItem transactionItem : transaction.getItems())
                    transactionItem.setTransaction(transaction);
            }
        }
    }
}

