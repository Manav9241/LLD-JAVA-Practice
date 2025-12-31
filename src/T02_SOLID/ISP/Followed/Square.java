package T02_SOLID.ISP.Followed;

public class Square implements Shape2D{
    private double side;

    public Square(double side) {
        this.side = side;
    }

    @Override
    public void Area() {
        System.out.println("Square: Area is: " + (side*side));
    }
}
