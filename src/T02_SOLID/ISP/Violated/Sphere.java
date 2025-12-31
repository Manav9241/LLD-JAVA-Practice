package T02_SOLID.ISP.Violated;

public class Sphere implements IShape{
    private final double PI = 3.142;
    private double radius;

    public Sphere(double radius) {
        this.radius = radius;
        System.out.println("Sphere: Radius of sphere set to: " + this.radius);
    }

    @Override
    public double Area() {
        return (4 * PI * radius * radius);
    }

    @Override
    public double Volume() {
        return (4 * PI * radius * radius * radius)/3;
    }
}
