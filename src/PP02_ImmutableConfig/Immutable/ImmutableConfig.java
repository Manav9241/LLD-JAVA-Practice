package PP02_ImmutableConfig.Immutable;

import java.util.Map;

public final class ImmutableConfig {
    private final Map<String, String> values;

    public ImmutableConfig(Map<String, String> map) {
        this.values = map;
    }

    public String get(String key) {
        return values.get(key);
    }
}
