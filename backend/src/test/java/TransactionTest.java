import model.*;
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
import service.TransactionService;

import java.util.Collections;

import static org.junit.Assert.*;

/**
 * Created by mihkel on 9.04.2018.
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AppConfig.class)
public class TransactionTest {

    @Autowired
    @Qualifier("mongoOperations")
    private MongoOperations mongoOperations;

    @Autowired
    EventService eventService;
    @Autowired
    MemberService memberService;
    @Autowired
    TransactionService transactionService;

    private Event event;
    private Member mihkel, alvar, peeter, tonu;
    private String mihkelid, alvarid, peeterid, tonuid;
    private Transaction taksosoit, poeskaik, saanisoit;
    private String eventid, taksoid, poesid, saanid;
    String taksoname = "Taksosõit Ivalost Saariselkä";
    String poesname = "Kolmapäevane I poeskäik";
    String saanisoitname = "Saanisõit";
    String taksonameChg = "Taksosõit Ivalo lennujaamast Saariselkä";
    String poesnameChg = "Kolmapäevane poeskäik";
    String saanisoitnameChg = "Saanisõit kambaga";
    private double delta = 0.001;

    private Member mihkelmock = new Member();
    private Member peetermock = new Member();
    private Member tonumock = new Member();
    private Member laurimock = new Member();
    private Member alvarmock = new Member();
    private Transaction taksomock = new Transaction();
    private Transaction poesmock = new Transaction();
    private Transaction saanimock = new Transaction();

    @Before
    public void initialize(){
        mongoOperations.dropCollection(Event.class);
        Event newEvent = new Event();
        newEvent.setName("Saariselkä 2019");
        event = eventService.add(newEvent);
        eventid = event.getId();

        mihkelmock.setName("Mihkel Märtin"); mihkelmock.setNickName("Miku");mihkelmock.seteMail("mihkelmartin@gmail.com");mihkelmock.setBankAccount("");
        peetermock.setName("Peeter Kutman"); peetermock.setNickName("Peta");
        tonumock.setName("Tõnu Riisalo"); tonumock.setNickName("Tõnu");
        laurimock.setName("Lauri Maisvee"); laurimock.setNickName("Lauri");
        alvarmock.setName("Alvar Tõruke"); alvarmock.setNickName("Tõru");alvarmock.seteMail("alvar@gmail.com");alvarmock.setBankAccount("");
        taksomock.setName(taksoname);
        poesmock.setName(poesname);
        saanimock.setName(saanisoitname);


        mihkel = memberService.add(event, mihkelmock);
        alvar = memberService.add(event, alvarmock);
        peeter = memberService.add(event, peetermock);
        tonu = memberService.add(event, tonumock);
        mihkelid = mihkel.getId();
        alvarid = alvar.getId();
        peeterid = peeter.getId();
        tonuid = tonu.getId();

        taksosoit = transactionService.add(event, taksomock);
        poeskaik = transactionService.add(event, poesmock);
        saanisoit = transactionService.add(event, saanimock);
        taksoid = taksosoit.getId();
        poesid = poeskaik.getId();
        saanid = saanisoit.getId();

    }

    @Test
    public void TransactionBasics(){
        event = eventService.loadEvent(eventid);
        taksosoit = eventService.findTransaction(event, taksoid);
        poeskaik = eventService.findTransaction(event, poesid);
        saanisoit = eventService.findTransaction(event, saanid);

        assertNotNull(taksosoit);
        assertNotNull(poeskaik);
        assertNotNull(saanisoit);

        assertEquals(taksosoit.getId(), taksoid);
        assertEquals(poeskaik.getId(), poesid);
        assertEquals(saanisoit.getId(), saanid);

        assertEquals(taksosoit.getName(), taksoname);
        assertEquals(poeskaik.getName(), poesname);
        assertEquals(saanisoit.getName(), saanisoitname);

        assertEquals(event.getMembers().size(), 4);
        assertEquals(event.getTransactions().size(), 3);

        assertEquals(taksosoit.getItems().size(),event.getMembers().size());
        assertEquals(poeskaik.getItems().size(), event.getMembers().size());
        assertEquals(saanisoit.getItems().size(), event.getMembers().size());
    }

    @Test
    public void TransactionChange() {
        event = eventService.loadEvent(eventid);
        taksosoit = eventService.findTransaction(event, taksoid);
        poeskaik = eventService.findTransaction(event, poesid);
        saanisoit = eventService.findTransaction(event, saanid);
        taksomock.setName(taksonameChg);
        poesmock.setName(poesnameChg);
        saanimock.setName(saanisoitnameChg);
        transactionService.save(taksosoit, taksomock);
        transactionService.save(poeskaik,poesmock);
        transactionService.save(saanisoit,saanimock);

        event = eventService.loadEvent(eventid);
        taksosoit = eventService.findTransaction(event, taksoid);
        poeskaik = eventService.findTransaction(event, poesid);
        saanisoit = eventService.findTransaction(event, saanid);
        assertNotNull(taksosoit);
        assertNotNull(poeskaik);
        assertNotNull(saanisoit);
        assertEquals(taksosoit.getName(), taksonameChg);
        assertEquals(poeskaik.getName(), poesnameChg);
        assertEquals(saanisoit.getName(), saanisoitnameChg);
    }

    @Test
    public void TransactionWithoutMembers() {
        mongoOperations.dropCollection(Event.class);
        Event newEvent = new Event();
        newEvent.setName("Saariselkä 2020");
        event = eventService.add(newEvent);
        eventid = event.getId();
        taksomock.setName(taksoname);
        taksosoit = transactionService.add(event,taksomock);
        taksoid = taksosoit.getId();

        event = eventService.loadEvent(eventid);
        taksosoit = eventService.findTransaction(event, taksoid);

        assertNotNull(taksosoit);
        assertEquals(taksosoit.getName(), taksoname);
        assertEquals(event.getTransactions().size(), 1);


    }

    @Test
    public void TransactionRemoveTransaction() {
        // Remove one
        transactionService.remove(poeskaik);
        Collections.reverse(event.getTransactions());

        assertEquals(2, event.getTransactions().size());
        assertEquals(2, event.getTransactions().get(0).getOrder());

        event = eventService.loadEvent(eventid);
        poeskaik = eventService.findTransaction(event, poesid);
        taksosoit = eventService.findTransaction(event, taksoid);
        saanisoit = eventService.findTransaction(event, saanid);
        Collections.reverse(event.getTransactions());

        assertNull(poeskaik);
        assertEquals(2, event.getTransactions().size());
        assertEquals(2, event.getTransactions().get(0).getOrder());

        // Remove all
        transactionService.remove(taksosoit);
        transactionService.remove(saanisoit);

        assertEquals(0, event.getTransactions().size());

        event = eventService.loadEvent(eventid);
        taksosoit = eventService.findTransaction(event, taksoid);
        saanisoit = eventService.findTransaction(event, saanid);

        assertNull(taksosoit);
        assertNull(saanisoit);
        assertEquals(0, event.getTransactions().size());

    }

    @Test
    public void TransactionRemoveMember() {
        // Remove two members
        memberService.remove(mihkel);
        memberService.remove(tonu);

        assertEquals(taksosoit.getItems().size(),event.getMembers().size());
        assertEquals(poeskaik.getItems().size(), event.getMembers().size());
        assertEquals(saanisoit.getItems().size(), event.getMembers().size());

        // Reload and test again
        event = eventService.loadEvent(eventid);
        taksosoit = eventService.findTransaction(event, taksoid);
        poeskaik = eventService.findTransaction(event, poesid);
        saanisoit = eventService.findTransaction(event, saanid);

        assertEquals(taksosoit.getItems().size(),event.getMembers().size());
        assertEquals(poeskaik.getItems().size(), event.getMembers().size());
        assertEquals(saanisoit.getItems().size(), event.getMembers().size());

        // Remove all
        peeter = eventService.findMember(event, peeterid);
        alvar = eventService.findMember(event, alvarid);
        memberService.remove(peeter);
        memberService.remove(alvar);

        assertEquals(0, taksosoit.getItems().size());
        assertEquals(0, poeskaik.getItems().size());
        assertEquals(0, saanisoit.getItems().size());

        // Reload and test again
        event = eventService.loadEvent(eventid);
        poeskaik = eventService.findTransaction(event, poesid);
        taksosoit = eventService.findTransaction(event, taksoid);
        saanisoit = eventService.findTransaction(event, saanid);

        assertEquals(0, taksosoit.getItems().size());
        assertEquals(0, poeskaik.getItems().size());
        assertEquals(0, saanisoit.getItems().size());
    }

    @Test
    public void TransactionAddMember() {
        Member lauri = memberService.add(event, laurimock);
        assertEquals(5, taksosoit.getItems().size());
        assertEquals(5, poeskaik.getItems().size());
        assertEquals(5, saanisoit.getItems().size());

        // Reload and test again
        event = eventService.loadEvent(eventid);
        poeskaik = eventService.findTransaction(event, poesid);
        taksosoit = eventService.findTransaction(event, taksoid);
        saanisoit = eventService.findTransaction(event, saanid);

        assertEquals(5, taksosoit.getItems().size());
        assertEquals(5, poeskaik.getItems().size());
        assertEquals(5, saanisoit.getItems().size());

    }

    @Test
    public void TransactionDebitSimpleAdd(){

        double dtaksosoit = 62.5;
        transactionService.addDebitForMember(taksosoit, mihkel, dtaksosoit);
        assertEquals(dtaksosoit/mihkel.getEvent().getMembers().size(),
                transactionService.getTransactionItemForMember(taksosoit, mihkel).getCredit(), delta);

        // After reload
        event = eventService.loadEvent(eventid);
        taksosoit = eventService.findTransaction(event, taksoid);
        mihkel = eventService.findMember(event, mihkelid);
        assertEquals(dtaksosoit/mihkel.getEvent().getMembers().size(),
                transactionService.getTransactionItemForMember(taksosoit, mihkel).getCredit(), delta);

    }

    @Test
    public void TransactionDebitNegativeAdd(){
        // Add debit
        double dtaksosoit = 62.5;
        transactionService.addDebitForMember(taksosoit, mihkel, dtaksosoit);
        // Get debit value
        double ddebit = transactionService.getTransactionItemForMember(taksosoit, mihkel).getDebit();
        // Add negative debit
        transactionService.addDebitForMember(taksosoit, mihkel, -10.10);

        // Debit must be same as before trying to add negative
        assertEquals(ddebit, transactionService.getTransactionItemForMember(taksosoit, mihkel).getDebit(), delta);

        TestSaldoAndNegativeValue(taksosoit);
    }

    @Test
    public void TransactionCreditSimpleAdd(){
        double dtaksosoit = 100.00;
        double dMihkelCredit = 10.00;
        transactionService.addDebitForMember(taksosoit, mihkel, dtaksosoit);
        transactionService.addCreditForMember(taksosoit, mihkel, dMihkelCredit);
        assertEquals(dMihkelCredit,
                transactionService.getTransactionItemForMember(taksosoit, mihkel).getCredit(), delta);
        assertEquals(false,
                transactionService.getTransactionItemForMember(taksosoit, mihkel).isBcreditAutoCalculated());

        TestSaldoAndNegativeValue(taksosoit);

        // After reload
        event = eventService.loadEvent(eventid);
        taksosoit = eventService.findTransaction(event, taksoid);
        mihkel = eventService.findMember(event, mihkelid);

        assertEquals(dMihkelCredit,
                transactionService.getTransactionItemForMember(taksosoit, mihkel).getCredit(), delta);
        assertEquals(false,
                transactionService.getTransactionItemForMember(taksosoit, mihkel).isBcreditAutoCalculated());

        TestSaldoAndNegativeValue(taksosoit);
    }

    @Test
    public void TransactionCreditSimpleCalculation(){
        double dtaksosoit = 100.00;
        double dMihkelCredit = 10.00;
        transactionService.addDebitForMember(taksosoit, mihkel, dtaksosoit);
        transactionService.addCreditForMember(taksosoit, mihkel, dMihkelCredit);

        assertEquals((dtaksosoit - dMihkelCredit) / (alvar.getEvent().getMembers().size() - 1),
                transactionService.getTransactionItemForMember(taksosoit, alvar).getCredit(), delta);
        assertEquals(true,
                transactionService.getTransactionItemForMember(taksosoit, alvar).isBcreditAutoCalculated());

        TestSaldoAndNegativeValue(taksosoit);

        // After reload
        event = eventService.loadEvent(eventid);
        taksosoit = eventService.findTransaction(event, taksoid);
        mihkel = eventService.findMember(event, mihkelid);
        alvar = eventService.findMember(event, alvarid);

        assertEquals((dtaksosoit - dMihkelCredit) / (alvar.getEvent().getMembers().size() - 1),
                transactionService.getTransactionItemForMember(taksosoit, alvar).getCredit(), delta);
        assertEquals(true,
                transactionService.getTransactionItemForMember(taksosoit, alvar).isBcreditAutoCalculated());

        TestSaldoAndNegativeValue(taksosoit);

    }

    @Test
    public void TransactionCreditNegativeAdd(){
        // Add debit
        double dtaksosoit = 62.5;
        transactionService.addDebitForMember(taksosoit, mihkel, dtaksosoit);
        // Get calculated credit value
        double dcredit = transactionService.getTransactionItemForMember(taksosoit, mihkel).getCredit();
        // Add negative credit
        transactionService.addCreditForMember(taksosoit, mihkel, -10.10);

        // Debit must be same as before trying to add negative
        assertEquals(dcredit, transactionService.getTransactionItemForMember(taksosoit, mihkel).getCredit(), delta);

        TestSaldoAndNegativeValue(taksosoit);
    }

    @Test
    public void TransactionAutoCalculationChange(){

    }

    @Test
    public void TransactionAddMemberCalculation(){

    }
    @Test
    public void TransactionRemoveMemberCalculation(){

    }

    private void TestSaldoAndNegativeValue(Transaction transaction){
        double debit = 0.0, credit = 0.0;
        for(TransactionItem transactionItem : transaction.getItems()){
            assertTrue(transactionItem.getCredit() >= 0);
            assertTrue(transactionItem.getDebit() >= 0);
            debit += transactionItem.getDebit();
            credit += transactionItem.getCredit();
        }
        assertEquals(debit, credit, delta);
    }
}
