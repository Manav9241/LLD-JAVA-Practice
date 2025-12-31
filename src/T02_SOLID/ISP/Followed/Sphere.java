package T02_SOLID.ISP.Followed;

public class Sphere implements Shape3D {
    private double radius;
    private final double PI = 3.14;

    public Sphere(double radius) {
        this.radius = radius;
    }
    @Override
    public void Area() {
        System.out.println("Sphere: Area is: " + (4*PI*radius*radius));
    }

    @Override
    public void Volume() {
        System.out.println("Sphere: Volume is: " + ((4*PI*radius*radius*radius)/3));
    }
}
