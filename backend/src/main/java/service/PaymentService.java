package service;

import model.Event;
import model.Member;
import model.Payment;

import java.util.List;

/**
 * Created by mihkel on 11.04.2018.
 */
public interface PaymentService {
    List<Payment> calculatePayments(Event event);
}
