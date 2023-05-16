package ServiceLayer.ServiceObjects.Fiters.StoreFilters;

import DomainLayer.Stores.Store;

public class NameStoreFilter implements StoreFilter{
    public final String nameFilter;

    public NameStoreFilter(String nameFilter) {
        this.nameFilter = nameFilter;
    }

    @Override
    public boolean PassFilter(Store store) {
        if(nameFilter.equals(""))
            return true;
        return store.getName().equals(nameFilter);
    }
}
