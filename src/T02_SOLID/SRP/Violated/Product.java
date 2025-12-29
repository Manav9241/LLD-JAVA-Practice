package T02_SOLID.SRP.Violated;

public class Product {
    public double price;
    public String name;

    public Product(String name, double price) {
        this.name = name;
        this.price = price;
    }

    @Override
    public String toString(){
        return name;
    }
}
