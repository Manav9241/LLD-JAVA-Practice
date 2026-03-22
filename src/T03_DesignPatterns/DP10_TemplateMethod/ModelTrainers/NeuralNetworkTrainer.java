package T03_DesignPatterns.DP10_TemplateMethod.ModelTrainers;

public class NeuralNetworkTrainer extends IModelTrainer{
    @Override
    protected void trainModel() {
        System.out.println("[NeuralNetwork] Training Neural Network for 100 epochs.");
    }

    @Override
    protected void evaluateModel() {
        System.out.println("[NeuralNetwork] Evaluating accuracy and loss on validation set.");
    }

    @Override
    protected void saveModel() {
        System.out.println("[NeuralNetwork] Serializing network weights to .h5 file");
    }
}
