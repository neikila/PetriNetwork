package storageModel.events;

import storageModel.Product;

/**
 * Created by neikila on 22.11.15.
 */
public class ProductIncome extends Event {
    private Product product;
    private int amount;

    public ProductIncome(double date, Product product, int amount) {
        super(date, EventType.ProductIncome);
        this.product = product;
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    public Product getProduct() {
        return product;
    }
}
