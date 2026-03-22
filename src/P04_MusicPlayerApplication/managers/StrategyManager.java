package P04_MusicPlayerApplication.managers;

import P04_MusicPlayerApplication.enums.PlayStrategyType;
import P04_MusicPlayerApplication.strategies.CustomQueueStrategy;
import P04_MusicPlayerApplication.strategies.IPlayStrategy;
import P04_MusicPlayerApplication.strategies.RandomPlayStrategy;
import P04_MusicPlayerApplication.strategies.SequentialPlayStrategy;

public class StrategyManager {
    private static StrategyManager instance = null;
    private SequentialPlayStrategy sequentialStrategy;
    private RandomPlayStrategy randomStrategy;
    private CustomQueueStrategy customStrategy;

    private StrategyManager() {
        this.sequentialStrategy = new SequentialPlayStrategy();
        this.randomStrategy = new RandomPlayStrategy();
        this.customStrategy = new CustomQueueStrategy();
    }

    public static synchronized StrategyManager getInstance() {
        if (instance == null) {
            instance = new StrategyManager();
        }
        return instance;
    }

    public IPlayStrategy getStrategy(PlayStrategyType type) {
        if (type == PlayStrategyType.SEQUENTIAL) {
            return sequentialStrategy;
        } else if (type == PlayStrategyType.RANDOM) {
            return randomStrategy;
        } else {
            return customStrategy;
        }
    }
}
