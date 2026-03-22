package T03_DesignPatterns.DP10_TemplateMethod;

import T03_DesignPatterns.DP10_TemplateMethod.ModelTrainers.DecisionTreeTrainer;
import T03_DesignPatterns.DP10_TemplateMethod.ModelTrainers.IModelTrainer;
import T03_DesignPatterns.DP10_TemplateMethod.ModelTrainers.NeuralNetworkTrainer;

public class TemplateMethodPatternMain {
    public static void main(String[] args) {
        System.out.println("=====Neural Network Training=====");
        IModelTrainer nnTrainer = new NeuralNetworkTrainer();
        nnTrainer.trainPipeline("data/images/");

        System.out.println("\n=====Neural Network Training=====");
        IModelTrainer dtTrainer = new DecisionTreeTrainer();
        dtTrainer.trainPipeline("data/iris.csv");
    }
}
