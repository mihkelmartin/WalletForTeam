package repository;

import model.Event;

import java.util.List;

/**
 * Created by mihkel on 6.04.2018.
 */
public interface EventDao extends GeneralDao<Event> {
    Event loadEvent(String id);
    List<Event> loadEventsByemail(String eMail);
    void removeUnusedEvents();
}
