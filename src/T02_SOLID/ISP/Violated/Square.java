package T02_SOLID.ISP.Violated;

public class Square implements IShape{
    private double side;

    public Square(double side) {
        this.side = side;
        System.out.println("Square: Side of square set to: " + this.side);
    }
    @Override
    public double Area() {
        return side * side;
    }

    @Override
    public double Volume() {
        throw new UnsupportedOperationException("Exception! Volume of Square not possible");
    }
}
