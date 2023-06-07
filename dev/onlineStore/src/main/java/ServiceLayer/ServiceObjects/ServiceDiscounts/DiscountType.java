package ServiceLayer.ServiceObjects.ServiceDiscounts;

import DomainLayer.Stores.Discounts.AdditiveDiscount;
import DomainLayer.Stores.Discounts.ManyDiscounts;
import DomainLayer.Stores.Discounts.MaxSelectiveDiscount;
import DomainLayer.Stores.Discounts.MinSelectiveDiscount;

public enum DiscountType {
    AdditiveDiscount {
        @Override
        public ManyDiscounts getDiscount(String description, int id) {
            return new AdditiveDiscount(description,id);
        }
    },
    MaxBetweenDiscounts {
        @Override
        public ManyDiscounts getDiscount(String description, int id) {
            return new MaxSelectiveDiscount(description, id);
        }
    },
    MinBetweenDiscount {
        @Override
        public ManyDiscounts getDiscount(String description, int id) {
            return new MinSelectiveDiscount(description, id);
        }
    };

    public abstract ManyDiscounts getDiscount(String description,int id);
}
