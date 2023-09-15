package peaksoft.repository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import peaksoft.dto.dtoProduct.ProductResponse;
import peaksoft.enums.Category;
import peaksoft.models.Product;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query(value = "SELECT " +
            "p.id AS productId, " +
            "p.name AS productName, " +
            "p.price AS productPrice, " +
            "pi.images AS productImages, " +
            "p.characteristic AS productCharacteristic, " +
            "p.is_favorite AS productIsFavorite, " +
            "p.made_in AS productMadeIn, " +
            "p.category AS productCategory, " +
            "b.brand_name AS brandName " +
            "FROM products p " +
            "JOIN brand b ON b.id = p.brand_id "  +
            "JOIN product_images pi ON p.id = pi.product_id " +
            "ORDER BY p.price ASC, p.category", nativeQuery = true)
    List<Object[]> getAllByImagesNative();

   default List<ProductResponse> getAllByImages() {
        List<Object[]> resultList = getAllByImagesNative();
        return resultList.stream()
                .map(this::mapToProductResponse)
                .collect(Collectors.toList());
    }

    private ProductResponse mapToProductResponse(Object[] row) {
        ProductResponse response = new ProductResponse();
        response.setId((Long) row[0]);
        response.setName((String) row[1]);
        response.setPrice((BigDecimal) row[2]);
        response.setImages(Collections.singletonList((String) row[3]));
        response.setCharacteristic((String) row[4]);
        response.setFavorite((Boolean) row[5]);
        response.setMadeIn((String) row[6]);
        response.setCategory(Category.valueOf((String) row[7]));
        response.setBrandName((String) row[8]);
        return response;
    }









}