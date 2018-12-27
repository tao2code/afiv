package iv.question2;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

@Slf4j
public class TicketSpinLockTest {

    private ExecutorService executorService = Executors.newFixedThreadPool(500);

    @Test
    public void exec() throws InterruptedException {
        Lock lock = new TicketSpinLock();
        test(lock, 10000);
    }

    private void test(final Lock lock, int num) throws InterruptedException {
        final int[] array = new int[]{0};
        for (int i = 0; i < num; i++) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    lock.lock();
                    try {
                        array[0] = array[0] + 1;
                    } finally {
                        lock.unlock();
                    }
                }
            });
        }

        //wait exec finished
        if (!executorService.awaitTermination(30, TimeUnit.SECONDS)) {
            List<Runnable> runnables = executorService.shutdownNow();
            log.info("runables {}", runnables.size());
        }

        Assert.assertEquals(num, array[0]);
    }

}