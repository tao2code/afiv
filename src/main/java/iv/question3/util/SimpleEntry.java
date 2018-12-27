package iv.question3.util;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SimpleEntry {

    private float min = Float.MAX_VALUE;

    private List<Record> list = new ArrayList<>();

    private Lock lock = new ReentrantLock(true);

    public List<Record> get() {
        return list;
    }

    public int put(Record record) {
        if (record == null) {
            return 0;
        }
        try {
            lock.lock();

            float quota = record.getQuota();
            if (quota < min) {
                list.clear();
                min = quota;
                list.add(record);
            } else if (min == quota) {
                list.add(record);
            }
            return 1;
        } finally {
            lock.unlock();
        }
    }

}
