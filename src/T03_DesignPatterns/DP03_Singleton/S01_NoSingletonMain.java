package T03_DesignPatterns.DP03_Singleton;

public class S01_NoSingletonMain {
    public S01_NoSingletonMain() {
        System.out.println("New Object created");
    }

    public static void main(String[] args) {
        System.out.println("Start");

        S01_NoSingletonMain object1 = new S01_NoSingletonMain();
        S01_NoSingletonMain object2 = new S01_NoSingletonMain();

        System.out.println(object2 == object1);

        System.out.println("End");
    }
}
