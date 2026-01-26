package JavaPracticeProjects.Learning.ImmutableConfig.Mutable;

import java.util.Map;

public class MutableConfig {
    private Map<String, String> values;

    public MutableConfig(Map<String, String> map) {
        this.values = map;
    }

    public String get(String key) {
        return values.get(key);
    }

    public void set(String key, String value) {
        values.put(key, value);
    }
}
