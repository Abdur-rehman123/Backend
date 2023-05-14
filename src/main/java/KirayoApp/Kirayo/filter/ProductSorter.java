package KirayoApp.Kirayo.filter;

import KirayoApp.Kirayo.returnStatus.ProductsResponse;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
@Component

public class ProductSorter {

    public void sortProductsByTimeStampDescending(List<ProductsResponse> products) {
        Comparator<ProductsResponse> comparator = Comparator.comparing(ProductsResponse::getTimeStamp);
        Collections.sort(products, comparator.reversed());
    }

}
