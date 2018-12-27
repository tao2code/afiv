package iv.question3.service;

import iv.question3.util.Record;
import iv.question3.util.SimpleEntry;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Tao Tan on 2018/12/26.
 * @since 0.1
 */
@Slf4j
public class RecordHandler {

    private static volatile iv.question3.service.RecordHandler instance;

    public static iv.question3.service.RecordHandler getInstance() {
        if (instance == null) {
            synchronized (iv.question3.service.RecordHandler.class) {
                if (instance == null) {
                    instance = new iv.question3.service.RecordHandler();
                }
            }
        }
        return instance;
    }

    private RecordHandler() {
    }

    ExecutorService executorService = Executors.newFixedThreadPool(2);

    public static Map<String, SimpleEntry> map = new ConcurrentHashMap<>();

    public void handle(String line) {
        executorService.execute(new Handler(line));
    }

    public void sendShutdownCommand() {
        executorService.shutdown();
    }

    public static class Handler implements Runnable {

        Record record;

        public Handler(String line) {
            if (line != null && !"".equals(line.trim())) {
                record = new Record(line);
            }
        }

        @Override
        public void run() {
            if (null != record) {

                String groupId = record.getGroupId();
                SimpleEntry list = map.get(groupId);
                if (list == null) {
                    list = new SimpleEntry();
                    map.put(groupId, list);
                }
                list.put(record);
                log.info(">>> map.size:{}", map.size());
            }
        }
    }

    public boolean isTerminated() {
        return executorService.isTerminated();
    }
}
