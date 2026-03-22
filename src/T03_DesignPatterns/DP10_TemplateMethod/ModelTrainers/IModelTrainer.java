package T03_DesignPatterns.DP10_TemplateMethod.ModelTrainers;

public abstract class IModelTrainer {
    public final void trainPipeline(String dataPath) {
        loadData(dataPath);
        preprocessData();
        trainModel();
        evaluateModel();
        saveModel();
    }

    protected void loadData(String dataPath) {
        System.out.println("[Common] Loading dataset from " + dataPath + ".");
    }

    protected void preprocessData() {
        System.out.println("[Common] Splitting into train/test and normalizing dataset.");
    }

    protected abstract void trainModel();

    protected abstract void evaluateModel();

    protected void saveModel() {
        System.out.println("[Common] Saving model to disk as default format");
    }
}
