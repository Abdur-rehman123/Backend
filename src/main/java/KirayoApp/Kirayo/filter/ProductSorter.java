package KirayoApp.Kirayo.filter;

import KirayoApp.Kirayo.returnStatus.ProductRequestResponse;
import KirayoApp.Kirayo.returnStatus.ProductsResponse;
import KirayoApp.Kirayo.returnStatus.SavedProductResponse;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
@Component

public class ProductSorter {

    public List<ProductsResponse> sortProductsByTimeStampDescending(List<ProductsResponse> products) {
        Comparator<ProductsResponse> comparator = Comparator.comparing(ProductsResponse::getTimeStamp);
        Collections.sort(products, comparator.reversed());
        return products;
    }
    public List<SavedProductResponse> sortSavedProductsByTimeStampDescending(List<SavedProductResponse> savedProducts) {
        Comparator<SavedProductResponse> comparator = Comparator.comparing(SavedProductResponse::getTimeStamp);
        Collections.sort(savedProducts, comparator.reversed());
        return savedProducts;
    }
    public List<ProductRequestResponse> sortProductRequestResponsesByTimeStampDescending(List<ProductRequestResponse> productRequestResponses) {
        Comparator<ProductRequestResponse> comparator = Comparator.comparing(ProductRequestResponse::getTimeStamp);
        Collections.sort(productRequestResponses, comparator.reversed());
        return productRequestResponses;
    }

}
