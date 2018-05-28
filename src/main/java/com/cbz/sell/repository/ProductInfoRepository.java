package com.cbz.sell.repository;

import com.cbz.sell.dataObject.ProductInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface ProductInfoRepository extends JpaRepository<ProductInfo,String> {
    List<ProductInfo> findByProductStatus(Integer productStatus);
    List<ProductInfo> findByCategoryTypeAndProductStatus(Integer categoryType,Integer productStatus);
}













