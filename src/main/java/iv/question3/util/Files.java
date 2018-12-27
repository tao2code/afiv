package iv.question3.util;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * @author Tao Tan on 2018/12/26.
 * @since 0.1
 */
@Slf4j
public class Files {

    //100 k 大小
    public static int SIZE_100K = 100 * 1024;

    public static void touch(File file) throws IOException {
        if (!file.exists() && file.createNewFile()) {
            log.info("touch file ({}) successfully", file.getAbsolutePath());
        }
    }


    public static void write(File file, String content) throws IOException {
        touch(file);
        try (
                FileOutputStream fos = new FileOutputStream(file);
        ) {
            fos.write(content.getBytes());
            fos.flush();
        }
    }

    public static void write(File file, Func<FileOutputStream> func) throws IOException {
        touch(file);
        try (
                FileOutputStream fos = new FileOutputStream(file)
        ) {
            func.apply(fos);
            fos.flush();
        }
    }

    public static List<String> readToList(File file) throws IOException {
        if (!file.exists()) {
            return Collections.emptyList();
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            List<String> result = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                result.add(line);
            }
            return result;
        }
    }


    public static void handleLine(File file, Func<String> func) throws IOException {
        if (!file.exists()) {
            return;
        }

        try (FileReader in = new FileReader(file);
             BufferedReader br = new BufferedReader(in)) {
            String line;
            while ((line = br.readLine()) != null) {
                func.apply(line);
            }
        }
    }

    private static Random random = new Random();

    public static String generateContent(int num) {

        String separator = System.getProperty("line.separator");

        StringBuilder buffer = new StringBuilder();

        for (int i = 0; i < num; i++) {
            buffer.append(
                    String.format(
                            "%d,%d,%s",
                            random.nextInt(Integer.MAX_VALUE),
                            random.nextInt(1000),
                            (float) Math.round(random.nextFloat() * 10000) / 100
                    )
            )
                    .append(separator);

        }

        return buffer.toString();
    }

    public static void generateSortFiles(String folderPath, int num) throws IOException {
        File folder = new File(folderPath);
        if (!folder.exists() && folder.mkdirs()) {
            log.info("touch file ({}) successfully", folder.getAbsolutePath());
        }
        for (int i = 0; i < num; i++) {
            write(new File(folderPath + i + ".txt"), generateContent(random.nextInt(SIZE_100K / 20)));
        }
    }

    public static void main(String[] args) throws IOException {
        generateSortFiles("data/", 100);
//        System.out.println(readToList(new File("data/1.txt")));
    }


}
