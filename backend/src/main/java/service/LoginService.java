package service;

import model.Event;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by mihkel on 4.06.2018.
 */
public class LoginService {

    public static String newEventMailContent = "Welcome to Wallet for Team !\n\nYour PIN code is:%PIN%\n\n Thank You!";
    public static String eventLockedMailContent = "Hi from Wallet for Team !\n\nDue to multiple failed logins Your event was locked.\n" +
            "To unlock it, enter PUK code given below to PIN entry field and click to \'Reset PIN with PUK\' label.\n\nPUK code:%PUK%\n\n" +
            "Thank You!";
    public static String newPINMailContent = "Hi from Wallet for Team !\n\nNew PIN code was generated!\n\n" +
            "Your new PIN code is:%PIN%\n\nThank You!";;

    @Autowired
    EventService eventService;

    @Autowired
    EmailServiceImpl emailService;


    static final short maxFailedPINLogins = 3;

    public boolean loginPIN(Event event, Long PIN){
        boolean retVal = false;

        if(event.getFailedLogins() < maxFailedPINLogins){
           if(event.getPIN().equals(PIN)){
               event.setFailedLogins((short)0);
               eventService.save(event, event);
               retVal = true;
           } else {
               event.setFailedLogins((short) (event.getFailedLogins() + 1));
               eventService.save(event, event);
               retVal = false;
           }

           if(event.getFailedLogins() == maxFailedPINLogins){
               event.generatePUK();
               eventService.save(event, event);
               emailService.sendSimpleMessage(event.getOwnerEmail(),
                       event.getName(),eventLockedMailContent.replaceAll("%PUK%",event.getPIN().toString()));
           }
        }
        return retVal;
    }

    public boolean resetPIN(Event event, Long PUK){
        boolean retVal = false;
        if(event.getFailedLogins() >= maxFailedPINLogins) {
            if (event.getPIN().equals(PUK)) {
                event.setFailedLogins((short) 0);
                event.generatePIN();
                eventService.save(event, event);
                emailService.sendSimpleMessage(event.getOwnerEmail(),
                        event.getName(), newPINMailContent.replaceAll("%PIN%",event.getPIN().toString()));
            }
        }
        return retVal;
    }
}
