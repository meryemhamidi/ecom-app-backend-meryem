package ma.hamidi.billingservice.repository;

import ma.hamidi.billingservice.entities.ProductItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductItemRepository extends JpaRepository<ProductItem, String> {
}

