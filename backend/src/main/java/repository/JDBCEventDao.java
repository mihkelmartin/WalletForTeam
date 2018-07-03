package repository;

import model.Event;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Created by mihkel on 6.04.2018.
 */

@Repository
public class JDBCEventDao implements EventDao {



    @Override
    public void add(Event event) {

    }

    @Override
    public void save(Event event) {

    }

    @Override
    public void remove(Event event) {

    }

    @Override
    public Event loadEvent(String id) {
        Event retVal = null;
        return retVal;

    }

    @Override
    public List<Event> loadEventsByemail(String eMail) {
        return null;
    }

    @Override
    public void removeUnusedEvents() {

    }
}
