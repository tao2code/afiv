package iv.question1;


import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class LazySingletonTest {

    @Test
    public void testNThread() throws InterruptedException {

        final String[] strings = new String[1000];
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            final int k = i;
            threads.add(new Thread() {
                @Override
                public void run() {
                    LazySingleton instance = LazySingleton.getInstance();
                    strings[k] = instance.toString();
                }
            });
        }

        //no exception explain is thread safe.
        for (Thread thread : threads) {
            thread.start();
        }

        Thread.sleep(3000);

        //no exception and assert pass
        for (int i = 1; i < 1000; i++) {
            Assert.assertEquals(strings[0], strings[i]);
        }
    }

    @Test(expected = InvocationTargetException.class)
    public void testCreateByReflect() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Assert.assertTrue(LazySingleton.getInstance() != null);

        Class<?> classType = LazySingleton.class;
        Constructor<?> c = classType.getDeclaredConstructor(null);
        c.setAccessible(true);


        LazySingleton reflectOne = (LazySingleton) c.newInstance();
    }


    @Test
    public void testSerializable() throws IOException, ClassNotFoundException {

        try (
                FileOutputStream fos = new FileOutputStream("LazySingleton.obj");
                ObjectOutputStream oos = new ObjectOutputStream(fos);

                FileInputStream fis = new FileInputStream("LazySingleton.obj");
                ObjectInputStream ois = new ObjectInputStream(fis);
        ) {
            LazySingleton normal = LazySingleton.getInstance();
            oos.writeObject(normal);
            LazySingleton serializable = (LazySingleton) ois.readObject();
            Assert.assertEquals(serializable, normal);
        }

    }
}