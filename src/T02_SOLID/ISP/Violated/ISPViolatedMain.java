package T02_SOLID.ISP.Violated;

public class ISPViolatedMain {
    static void main(String[] args) {
        IShape cube = new Cube(10);
        IShape sphere = new Sphere(375);
        IShape square = new Square(25);

        System.out.println("Area of Cube: " + cube.Area());
        System.out.println("Volume of Cube: " + cube.Volume());
        System.out.println("Area of Sphere: " + sphere.Area());
        System.out.println("Volume of Sphere: " + sphere.Volume());

        System.out.println("\nArea of Square: " + square.Area());
        try {
            System.out.println("Volume of Square: " + square.Volume());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}
