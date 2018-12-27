package iv.question3.service;

import iv.question3.util.Files;
import iv.question3.util.Func;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Tao Tan on 2018/12/26.
 * @since 0.1
 */
@Slf4j
public class FileReaderService  {

    private static volatile FileReaderService instance;

    public static FileReaderService getInstance() {
        if (instance == null) {
            synchronized (FileReaderService.class) {
                if (instance == null) {
                    instance = new FileReaderService();
                }
            }
        }
        return instance;
    }

    private FileReaderService() {
    }

    ExecutorService executorService = Executors.newFixedThreadPool(10);

    private int fileNum = 100;

    private static AtomicInteger wc = new AtomicInteger();


    public void start() {

        Executors
                .newSingleThreadExecutor()
                .execute(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < fileNum; i++) {
                            executorService.execute(new ReadWorker(i));
                        }

                        //exec finished and shutdown executorService
                        executorService.shutdown();
                        RecordHandler recordHandler = RecordHandler.getInstance();

                        // waiting reader finished and shutdown
                        while (!executorService.isTerminated()) {
                        }
                        recordHandler.sendShutdownCommand();

                    }
                });

        log.info("start FileReaderService service successfully");
    }


    public boolean isTerminated() {
        return executorService.isTerminated();
    }


    /**
     * read worker
     */
    public static class ReadWorker implements Runnable {

        int index;

        public ReadWorker(int index) {
            this.index = index;
        }

        @Override
        public void run() {
            String filePath = String.format("data/%d.txt", index);

            try {
                Files.handleLine(new File(filePath), new Func<String>() {
                    @Override
                    public void apply(String line) {
                        log.info("read line:{}", line);
                        //send to handle service
                        RecordHandler.getInstance().handle(line);
                        wc.incrementAndGet();
                    }
                });
            } catch (IOException ignored) {
                log.info("ignored exception", ignored);
            }
        }
    }
}
