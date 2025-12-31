package T02_SOLID.ISP.Violated;

public class Cube implements IShape{
    private double edge;

    public Cube(double edge) {
        this.edge = edge;
        System.out.println("Cuve: Edge Length set to: " + this.edge);
    }

    @Override
    public double Area() {
        return (6 * edge * edge);
    }

    @Override
    public double Volume() {
        return (edge * edge * edge);
    }
}
