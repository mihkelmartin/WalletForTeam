package service;

import model.Event;
import model.Member;
import model.Transaction;

import java.util.List;

/**
 * Created by mihkel on 11.04.2018.
 */
public interface EventService {
    Event add(Event newEvent);
    Event save(Event event, Event updatedEvent);
    Event remove(Event event);
    Event loadEvent(String id);
    List<Event> loadEventsByEmail(String eMail);
    Member findMember(Event event, String id);
    Transaction findTransaction(Event event, String id);
}
