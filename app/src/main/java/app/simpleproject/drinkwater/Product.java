package app.simpleproject.drinkwater;

public class Product {

    public String name;
    public String price;
    public int image;
    public String numberId;
    public String imena;
    public String barPosition;
    public String productID;
    public String date;
    public int lastItem;

    public Product(String _describe, String _price, int _image, String _numberId, String _imena, String _barPosition, String _productID, String _date, int _lastItem) {
        name = _describe;
        price = _price;
        image = _image;
        numberId = _numberId;
        imena = _imena;
        barPosition = _barPosition;
        productID = _productID;
        date = _date;
        lastItem = _lastItem;
    }
}
