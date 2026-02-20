package T03_DesignPatterns.DP08_Facade;

public class FacadeMain {
    public static void main(String[] args) {
        ComputerFacade computer = new ComputerFacade();
        computer.startComputer();
    }
}
