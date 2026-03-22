package T03_DesignPatterns.DP10_TemplateMethod.ModelTrainers;

public class DecisionTreeTrainer extends IModelTrainer{
    @Override
    protected void trainModel() {
        System.out.println("[DecisionTree] Building decision tree with maz_depth=5.");
    }

    @Override
    protected void evaluateModel() {
        System.out.println("[DecisionTree] Computing classification report (precision/recall).");
    }
}
