

import model.AppConfig;
import model.Event;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.test.context.junit4.SpringRunner;
import service.EventService;

/**
 * Created by mihkel on 9.04.2018.
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AppConfig.class)
public class EventTest {

    @Autowired
    @Qualifier("mongoOperations")
    private MongoOperations mongoOperations;

    @Autowired
    EventService eventService;

    @Before public void initialize(){
        mongoOperations.dropCollection(Event.class);
    }

    @Test
    public void EventBasics(){
        String eventName = "Saariselkä 2018";
        String eventNameChg = "Saariselkä 2019";

        // Creation
        Event newEvent = new Event();
        newEvent.setName(eventName);

        Event event = eventService.add(newEvent);
        String eventid = event.getId();
        assertNotNull(event);
        assertNotNull(eventid);
        assertEquals (event.getName(), eventName);

        // Reload
        event = eventService.loadEvent(eventid);
        assertNotNull(event);
        assertEquals (eventid, event.getId());
        assertEquals (event.getName(), eventName);

        // Change
        newEvent.setName(eventNameChg);
        eventService.save(event, newEvent);

        event = eventService.loadEvent(eventid);
        assertNotNull(event);
        assertEquals (eventid, event.getId());
        assertEquals (event.getName(), eventNameChg);

        // Remove
        eventService.remove(event);

        event = eventService.loadEvent(eventid);
        assertNull(event);
    }
}
