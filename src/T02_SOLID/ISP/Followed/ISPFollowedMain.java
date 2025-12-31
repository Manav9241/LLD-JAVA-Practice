package T02_SOLID.ISP.Followed;

public class ISPFollowedMain {
    static void main(String[] args) {
        Shape2D square = new Square(15);
        Shape3D cube = new Cube(10);
        Shape3D sphere = new Sphere(12);

        cube.Area();
        cube.Volume();
        sphere.Area();
        sphere.Volume();
        System.out.println();
        square.Area();
    }
}
