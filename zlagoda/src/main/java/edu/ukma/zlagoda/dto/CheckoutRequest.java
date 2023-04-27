package edu.ukma.zlagoda.dto;

import edu.ukma.zlagoda.entities.CustomerCard;
import edu.ukma.zlagoda.entities.Employee;
import edu.ukma.zlagoda.entities.ProductInStore;
import org.springframework.lang.Nullable;

import java.util.List;

public record CheckoutRequest(Employee employee,
                              @Nullable CustomerCard customerCard,
                              List<ProductInStore> goods) {

}
