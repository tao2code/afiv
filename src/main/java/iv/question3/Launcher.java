package iv.question3;

import iv.question3.service.FileReaderService;
import iv.question3.service.RecordHandler;
import iv.question3.util.Files;
import iv.question3.util.Func;
import iv.question3.util.Record;
import iv.question3.util.SimpleEntry;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * @author Tao Tan on 2018/12/26.
 * @since 0.1
 */
public class Launcher {

    public static void main(String[] args) throws IOException {

        //step 1:boot reader and handle
        FileReaderService fileReader = FileReaderService.getInstance();
        fileReader.start();

        //step 2:waiting finished
        while (!(fileReader.isTerminated() && RecordHandler.getInstance().isTerminated())) {
        }

        final List<Map.Entry<String, SimpleEntry>> list = new ArrayList<>(RecordHandler.map.entrySet());

        //step 3:write to file
        Files.write(new File("data/result.txt"), new Func<FileOutputStream>() {
            @Override
            public void apply(FileOutputStream fos) {
                if (list.size() == 0) {
                    return;
                }

                //sort
                Collections.sort(list, new Comparator<Map.Entry<String, SimpleEntry>>() {
                    @Override
                    public int compare(Map.Entry<String, SimpleEntry> o1, Map.Entry<String, SimpleEntry> o2) {
                        return Integer.valueOf(o1.getKey()) - Integer.valueOf(o2.getKey());
                    }
                });

                //write to file
                for (Map.Entry<String, SimpleEntry> entry : list) {
                    try {
                        StringBuilder buffer = new StringBuilder();
                        buffer.append(entry.getKey()).append(",");
                        for (Record record : entry.getValue().get()) {
                            buffer
                                    .append(record.id).append(",")
                                    .append(record.getQuota()).append(",");
                        }

                        buffer.replace(buffer.length() - 1, buffer.length(), "\n");

                        fos.write(buffer.toString().getBytes());
                        fos.flush();
                    } catch (IOException ignored) {
                    }
                }
            }
        });
    }


}
