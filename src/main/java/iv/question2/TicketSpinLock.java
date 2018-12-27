package iv.question2;

import java.util.concurrent.atomic.AtomicInteger;


public class TicketSpinLock extends LockAdapter {

    private static final ThreadLocal<Integer> LOCAL = new ThreadLocal<>();
    private AtomicInteger ticket = new AtomicInteger(0);
    private AtomicInteger service = new AtomicInteger(0);

    @Override
    public void lock() {

        //assign the ticket for current thread
        int currentTicket = ticket.getAndIncrement();
        LOCAL.set(currentTicket);

        // if service is not thread and spin
        while (currentTicket != service.get()) {
        }
    }

    @Override
    public void unlock() {

        //get current ticket
        int currentTicket = LOCAL.get();

        //service next one
        service.compareAndSet(currentTicket, currentTicket + 1);

        //clear the thread local variable
        LOCAL.remove();
    }
}
