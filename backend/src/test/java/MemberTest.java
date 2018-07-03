import model.AppConfig;
import model.Event;
import model.Member;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.test.context.junit4.SpringRunner;
import service.EventService;
import service.MemberService;


import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Created by mihkel on 9.04.2018.
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AppConfig.class)
public class MemberTest {

    @Autowired
    @Qualifier("mongoOperations")
    private MongoOperations mongoOperations;

    @Autowired
    EventService eventService;
    @Autowired
    MemberService memberService;

    private Member mihkelmock = new Member();
    private Member peetermock = new Member();
    private Member tonumock = new Member();
    private Member laurimock = new Member();


    @Before
    public void initialize(){
        mongoOperations.dropCollection(Event.class);
        mihkelmock.setName("Mihkel Märtin"); mihkelmock.setNickName("Miku");mihkelmock.seteMail("mihkelmartin@gmail.com");mihkelmock.setBankAccount("");
        peetermock.setName("Peeter Kutman"); peetermock.setNickName("Peta");
        tonumock.setName("Tõnu Riisalo"); tonumock.setNickName("Tõnu");
        laurimock.setName("Lauri Maisvee"); laurimock.setNickName("Lauri");
    }

    @Test
    public void MemberSingleAdd(){

        // Creation
        Event newEvent = new Event();
        newEvent.setName("Saariselkä 2019");
        Event event = eventService.add(newEvent);
        Member mihkel = memberService.add(event,mihkelmock);
        String mihkelid = mihkel.getId();

        assertNotNull(mihkel);
        assertNotNull(mihkelid);
        assertEquals(event.getMembers().size(),1);

        // Reload
        event = eventService.loadEvent(event.getId());
        mihkel = eventService.findMember(event, mihkelid);
        assertNotNull(mihkel);
        assertEquals (mihkelid, mihkel.getId());
        assertEquals(mihkel.getName(),"Mihkel Märtin");
        assertEquals(mihkel.getNickName(),"Miku");
        assertEquals(mihkel.geteMail(),"mihkelmartin@gmail.com");
        assertEquals(mihkel.getBankAccount(),"");
        assertEquals(event.getMembers().size(),1);
    }

    @Test
    public void MemberSingleChange(){

        Event newEvent = new Event();
        newEvent.setName("Saariselkä 2019");
        Event event = eventService.add(newEvent);
        Member mihkel = memberService.add(event,mihkelmock);
        String mihkelid = mihkel.getId();
        mihkelmock.setName("Mihkel Kaarli poeg Märtin");
        mihkelmock.seteMail("kaubavagun@gmail.com");
        mihkelmock.setNickName("Mikuke");
        mihkelmock.setBankAccount("EESwed");
        memberService.save(mihkel, mihkelmock);

        event = eventService.loadEvent(event.getId());
        mihkel = eventService.findMember(event, mihkelid);
        assertEquals(mihkel.getName(),"Mihkel Kaarli poeg Märtin");
        assertEquals(mihkel.getNickName(),"Mikuke");
        assertEquals(mihkel.geteMail(),"kaubavagun@gmail.com");
        assertEquals(mihkel.getBankAccount(),"EESwed");

    }

    @Test
    public void MemberRemove(){
        Event newEvent = new Event();
        newEvent.setName("Saariselkä 2019");
        Event event = eventService.add(newEvent);
        Member mihkel = memberService.add(event, mihkelmock);
        Member peeter = memberService.add(event, peetermock);
        Member tonu = memberService.add(event, tonumock);
        Member lauri = memberService.add(event, laurimock);
        String mihkelid = mihkel.getId(), peeterid = peeter.getId(), tonuid = tonu.getId(), lauriid = lauri.getId();

        assertEquals(event.getMembers().size(),4);

        // Remove one
        memberService.remove(peeter);
        Collections.reverse(event.getMembers());

        assertEquals(event.getMembers().size(),3);
        assertEquals(event.getMembers().get(0).getOrder(),3);

        event = eventService.loadEvent(event.getId());
        peeter = eventService.findMember(event, peeterid);
        mihkel = eventService.findMember(event, mihkelid);
        tonu = eventService.findMember(event, tonuid);
        lauri = eventService.findMember(event, lauriid);
        Collections.reverse(event.getMembers());

        assertNull(peeter);
        assertNotNull(mihkel);
        assertNotNull(tonu);
        assertNotNull(lauri);
        assertEquals(event.getMembers().size(),3);
        assertEquals(event.getMembers().get(0).getOrder(),3);

        // Remove all
        memberService.remove(mihkel);
        memberService.remove(tonu);
        memberService.remove(lauri);

        assertEquals(event.getMembers().size(),0);

        event = eventService.loadEvent(event.getId());

        assertEquals(event.getMembers().size(),0);
    }

}
