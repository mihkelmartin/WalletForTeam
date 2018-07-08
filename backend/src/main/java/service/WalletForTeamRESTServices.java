package service;

import com.fasterxml.jackson.core.JsonProcessingException;
import model.Event;
import model.Member;
import model.Payment;
import model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

/**
 * Created by mihkel on 6.06.2018.
 */
@Controller
public class WalletForTeamRESTServices {

    static final short SECURITY_TOKEN_VALID = 0;
    @Autowired
    EventService eventService;
    @Autowired
    MemberService memberService;
    @Autowired
    TransactionService transactionService;
    @Autowired
    PaymentService paymentService;
    @Autowired
    RecaptchaService recaptchaService;
    @Autowired
    EmailServiceImpl emailService;
    @Autowired
    LoginService loginService;

    @CrossOrigin(origins = "${clientcors.url}")
    @RequestMapping(value = "/")
    public String index() {
        return "index.html";
    }

    @CrossOrigin(origins = "${clientcors.url}")
    @GetMapping(path = "/Payments/{eventid}/{token}", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public List<Payment> calculatePayments(@PathVariable String eventid, @PathVariable  String token) throws JsonProcessingException {
        List<Payment> retVal = null;
        Event event = eventService.loadEvent(eventid);
        if(event != null) {
            if (event.validateToken(token) == SECURITY_TOKEN_VALID)
                retVal = paymentService.calculatePayments(event);
            else
                throw new TokenNotValidException("Session expired or security token not valid");
        }
        return retVal;
    }

    @CrossOrigin(origins = "${clientcors.url}")
    @PostMapping(path = "/SendPayments/{eventid}/{token}")
    @ResponseBody
    public void sendPayments(@PathVariable String eventid, @PathVariable  String token,
                                       @RequestBody List<Payment> payments) throws JsonProcessingException {
        Event event = eventService.loadEvent(eventid);
        if(event != null) {
            if (event.validateToken(token) == SECURITY_TOKEN_VALID)
                paymentService.sendPayments(event, payments);
            else
                throw new TokenNotValidException("Session expired or security token not valid");
        }
    }

    @CrossOrigin(origins = "${clientcors.url}")
    @GetMapping(path = "/Event/find/email/{email}", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public List<Event> findEventsByEmail(@PathVariable String email) throws JsonProcessingException {
        return eventService.loadEventsByEmail(email);
    }

    @CrossOrigin(origins = "${clientcors.url}")
    @GetMapping(path = "/login/{eventid}/{PIN}/{ReCAPTCHAToken}", produces = "text/plain")
    @ResponseBody
    public String loginEvent(@PathVariable String eventid, @PathVariable  Long PIN, @PathVariable String ReCAPTCHAToken) {
        String retVal = "";
        if(recaptchaService.verifyRecaptcha("",ReCAPTCHAToken).equals("")) {
            Event event = eventService.loadEvent(eventid);
            if (event != null)
                if (loginService.loginPIN(event, PIN)) {
                    retVal = event.generateToken();
                    eventService.save(event, event);
                }
        }
        return retVal;
    }

    @CrossOrigin(origins = "${clientcors.url}")
    @GetMapping(path = "/puk/{eventid}/{PUK}/{ReCAPTCHAToken}")
    @ResponseBody
    public void resetPIN(@PathVariable String eventid, @PathVariable  Long PUK, @PathVariable String ReCAPTCHAToken) {
        if(recaptchaService.verifyRecaptcha("",ReCAPTCHAToken).equals("")) {
            Event event = eventService.loadEvent(eventid);
            if (event != null)
                loginService.resetPIN(event, PUK);
        }
    }

    @CrossOrigin(origins = "${clientcors.url}")
    @PostMapping(path = "/Event/add/{ReCAPTCHAToken}", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Event addEvent(@PathVariable String ReCAPTCHAToken, @RequestBody Event newEvent) {

        Event event = null;
        if(recaptchaService.verifyRecaptcha("",ReCAPTCHAToken).equals("")){
            event = eventService.add(newEvent);

            // Add default Member
            Member member = new Member();
            member.setNickName("John");
            member.seteMail(event.getOwnerEmail());
            memberService.add(event, member);
            member.setNickName("Laila");
            member.seteMail("laila@gmail.com");
            memberService.add(event, member);

            // Add default Transaction
            Transaction transaction = new Transaction();
            transaction.setName("Foodmarket");
            transactionService.add(event, transaction);

            emailService.sendSimpleMessage(event.getOwnerEmail(), event.getName(),
                    LoginService.newEventMailContent.replaceAll("%PIN%",event.getPIN().toString()));
        }
        return event;
    }


    @CrossOrigin(origins = "${clientcors.url}")
    @PostMapping(path = "/Event/update/{token}")
    @ResponseBody
    public void updateEvent(@PathVariable String token, @RequestBody Event updatedEvent) {
        Event event = eventService.loadEvent(updatedEvent.getId());
        if(event != null) {
            if (event.validateToken(token) == SECURITY_TOKEN_VALID)
                eventService.save(event, updatedEvent);
            else
                throw new TokenNotValidException("Session expired or security token not valid");
        }
    }

    @CrossOrigin(origins = "${clientcors.url}")
    @GetMapping(path = "/Event/load/{eventid}/{token}", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Event loadEvent(@PathVariable String eventid, @PathVariable  String token) {
        Event retVal = null;
        Event event = eventService.loadEvent(eventid);
        if(event != null)
            if(event.validateToken(token) == SECURITY_TOKEN_VALID)
                retVal = event;
            else
                throw new TokenNotValidException("Session expired or security token not valid");


        return retVal;
    }

    @CrossOrigin(origins = "${clientcors.url}")
    @GetMapping(path = "/Event/remove/{eventid}/{token}")
    @ResponseBody
    public void removeEvent(@PathVariable String eventid, @PathVariable  String token) {
        Event event = eventService.loadEvent(eventid);
        if(event != null) {
            if (event.validateToken(token) == SECURITY_TOKEN_VALID)
                eventService.remove(event);
            else
                throw new TokenNotValidException("Session expired or security token not valid");
        }
    }

    // Members

    @CrossOrigin(origins = "${clientcors.url}")
    @GetMapping(path = "/Members/{eventid}/{token}", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Collection<Member> GetMembers(@PathVariable String eventid, @PathVariable  String token) {
        Collection<Member> retVal = null;
        Event event = eventService.loadEvent(eventid);
        if(event != null) {
            if (event.validateToken(token) == SECURITY_TOKEN_VALID)
                retVal = event.getMembers();
            else
                throw new TokenNotValidException("Session expired or security token not valid");
        }
        return retVal;
    }

    @CrossOrigin(origins = "${clientcors.url}")
    @GetMapping(path = "/Members/add/{eventid}/{token}")
    @ResponseBody
    public void AddMember(@PathVariable String eventid, @PathVariable  String token) {
        Event event = eventService.loadEvent(eventid);
        if(event != null) {
            if (event.validateToken(token) == SECURITY_TOKEN_VALID) {
                // Add new Member
                Member member = new Member();
                member.setNickName("New");
                member.seteMail("new@gmail.com");
                memberService.add(event, member);
            } else
                throw new TokenNotValidException("Session expired or security token not valid");
        }

    }

    @CrossOrigin(origins = "${clientcors.url}")
    @PostMapping(path = "/Members/update/{eventid}/{token}")
    @ResponseBody
    public void updateMember(@PathVariable String eventid, @PathVariable String token, @RequestBody Member updatedMember) {
        Event event = eventService.loadEvent(eventid);
        if(event != null) {
            if (event.validateToken(token) == SECURITY_TOKEN_VALID) {
                Member member = eventService.findMember(event, updatedMember.getId());
                if (member != null)
                    memberService.save(member, updatedMember);
            } else
                throw new TokenNotValidException("Session expired or security token not valid");
        }

    }

    @CrossOrigin(origins = "${clientcors.url}")
    @GetMapping(path = "/Members/remove/{eventid}/{token}/{memberid}")
    @ResponseBody
    public void RemoveMember(@PathVariable String eventid, @PathVariable  String token, @PathVariable  String memberid) {
        Event event = eventService.loadEvent(eventid);
        if(event != null) {
            if (event.validateToken(token) == SECURITY_TOKEN_VALID) {
                memberService.remove(eventService.findMember(event, memberid));
            } else
                throw new TokenNotValidException("Session expired or security token not valid");
        }
    }


    // Transactions

    @CrossOrigin(origins = "${clientcors.url}")
    @GetMapping(path = "/Transactions/{eventid}/{token}", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Collection<Transaction> GetTransactions(@PathVariable String eventid, @PathVariable  String token) {
        Collection<Transaction> retVal = null;
        Event event = eventService.loadEvent(eventid);
        if(event != null) {
            if (event.validateToken(token) == SECURITY_TOKEN_VALID)
                retVal = event.getTransactions();
            else
                throw new TokenNotValidException("Session expired or security token not valid");
        }
        return retVal;
    }

    @CrossOrigin(origins = "${clientcors.url}")
    @GetMapping(path = "/Transactions/add/{eventid}/{token}")
    @ResponseBody
    public void AddTransaction(@PathVariable String eventid, @PathVariable  String token) {
        Event event = eventService.loadEvent(eventid);
        if(event != null) {
            if (event.validateToken(token) == SECURITY_TOKEN_VALID) {
                // Add new transaction
                Transaction transaction = new Transaction();
                transaction.setName("New spending");
                transactionService.add(event, transaction);
            } else
                throw new TokenNotValidException("Session expired or security token not valid");
        }

    }

    @CrossOrigin(origins = "${clientcors.url}")
    @PostMapping(path = "/Transactions/update/{eventid}/{token}")
    @ResponseBody
    public void updateTransaction(@PathVariable String eventid, @PathVariable String token,
                                  @RequestBody Transaction updatedTransaction) {
        Event event = eventService.loadEvent(eventid);
        if(event != null) {
            if (event.validateToken(token) == SECURITY_TOKEN_VALID) {
                Transaction transaction = eventService.findTransaction(event, updatedTransaction.getId());
                if (transaction != null)
                    transactionService.save(transaction, updatedTransaction);
            } else
                throw new TokenNotValidException("Session expired or security token not valid");
        }
    }

    @CrossOrigin(origins = "${clientcors.url}")
    @GetMapping(path = "/Transactions/remove/{eventid}/{token}/{transactionid}")
    @ResponseBody
    public void RemoveTransaction(@PathVariable String eventid, @PathVariable  String token,
                                  @PathVariable  String transactionid) {
        Event event = eventService.loadEvent(eventid);
        if(event != null) {
            if (event.validateToken(token) == SECURITY_TOKEN_VALID) {
                transactionService.remove(eventService.findTransaction(event, transactionid));
            } else
                throw new TokenNotValidException("Session expired or security token not valid");
        }
    }

    @CrossOrigin(origins = "${clientcors.url}")
    @GetMapping(path = "/Transactions/addDebit/{eventid}/{token}/{transactionid}/{memberid}/{debit}")
    @ResponseBody
    public void addDebitToTransaction(@PathVariable String eventid, @PathVariable String token,
                                      @PathVariable  String transactionid, @PathVariable  String memberid,
                                      @PathVariable  double debit) {
        Event event = eventService.loadEvent(eventid);
        if(event != null) {
            if (event.validateToken(token) == SECURITY_TOKEN_VALID) {
                Transaction transaction = eventService.findTransaction(event, transactionid);
                Member member = eventService.findMember(event, memberid);
                if (transaction != null && member != null)
                    transactionService.addDebitForMember(transaction, member, debit);
            } else
                throw new TokenNotValidException("Session expired or security token not valid");
        }
    }

    @CrossOrigin(origins = "${clientcors.url}")
    @GetMapping(path = "/Transactions/addCredit/{eventid}/{token}/{transactionid}/{memberid}/{credit}")
    @ResponseBody
    public void addCreditToTransaction(@PathVariable String eventid, @PathVariable String token,
                                      @PathVariable  String transactionid, @PathVariable  String memberid,
                                      @PathVariable  double credit) {
        Event event = eventService.loadEvent(eventid);
        if(event != null) {
            if (event.validateToken(token) == SECURITY_TOKEN_VALID) {
                Transaction transaction = eventService.findTransaction(event, transactionid);
                Member member = eventService.findMember(event, memberid);
                if (transaction != null && member != null)
                    transactionService.addCreditForMember(transaction, member, credit);
            } else
                throw new TokenNotValidException("Session expired or security token not valid");
        }
    }

    @CrossOrigin(origins = "${clientcors.url}")
    @GetMapping(path = "/Transactions/setAutoCalc/{eventid}/{token}/{transactionid}/{memberid}/{bAutoCalc}")
    @ResponseBody
    public void setTransactionItemAutoCalc(@PathVariable String eventid, @PathVariable String token,
                                       @PathVariable  String transactionid, @PathVariable  String memberid,
                                       @PathVariable  boolean bAutoCalc) {
        Event event = eventService.loadEvent(eventid);
        if(event != null) {
            if (event.validateToken(token) == SECURITY_TOKEN_VALID) {
                Transaction transaction = eventService.findTransaction(event, transactionid);
                Member member = eventService.findMember(event, memberid);
                if (transaction != null && member != null)
                    transactionService.setAutoCalculationForMember(transaction, member, bAutoCalc);
            } else
                throw new TokenNotValidException("Session expired or security token not valid");
        }
    }

}
