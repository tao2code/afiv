package iv.question3.util;

import lombok.Data;

/**
 * @author Tao Tan on 2018/12/26.
 * @since 0.1
 */
@Data
public class Record implements Comparable<Record> {
    public String id;
    public String groupId;
    public float quota;

    public Record(String input) {
        String[] split = input.split(",");
        this.id = split[0];
        this.groupId = split[1];
        this.quota = Float.valueOf(split[2]);
    }

    @Override
    public String toString() {
        return String.format("%s,%s,%f", id, groupId, quota);
    }

    @Override
    public int compareTo(Record other) {
        return Integer.valueOf(this.id) - Integer.valueOf(other.id);
    }
}
