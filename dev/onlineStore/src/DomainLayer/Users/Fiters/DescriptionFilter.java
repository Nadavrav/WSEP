package DomainLayer.Users.Fiters;

import DomainLayer.Stores.StoreProduct;

public class DescriptionFilter implements Filter{
    private final String description;

    public DescriptionFilter(String description) {
        this.description = description;
    }

    @Override
    public boolean PassFilter(StoreProduct product) {
        return product.getDescription().equals(description);
    }
}
