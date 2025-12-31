package T02_SOLID.ISP.Followed;

public class Cube implements Shape3D{
    private double edge;

    public Cube(double edge) {
        this.edge = edge;
    }

    @Override
    public void Area() {
        System.out.println("Cube: Area is: " + (6*edge*edge));
    }

    @Override
    public void Volume() {
        System.out.println("Cube: Volume is: " + (edge*edge*edge));
    }
}
