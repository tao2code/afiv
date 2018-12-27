package iv.question1;

import lombok.extern.slf4j.Slf4j;

import java.io.*;

/**
 * @author TaoTan
 */
@Slf4j
public class LazySingleton implements Serializable {

    private static volatile LazySingleton instance;

    private static boolean created;

    private LazySingleton() {
        if (created) {
            throw new RuntimeException("just create 1 time");
        } else {
            created = true;
        }
    }

    public static LazySingleton getInstance() {
        if (instance == null) {
            synchronized (LazySingleton.class) {
                if (instance == null) {
                    instance = new LazySingleton();
                }
            }
        }
        return instance;
    }

    private Object readResolve() throws ObjectStreamException {
        return getInstance();
    }


}
