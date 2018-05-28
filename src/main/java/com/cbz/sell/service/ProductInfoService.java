package com.cbz.sell.service;

import com.cbz.sell.dataObject.ProductInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

public interface ProductInfoService {
    ProductInfo findOne(String productId);

    /**
     * 查询所有在架商品列表
     */
    List<ProductInfo> findUpAll();

    Page<ProductInfo> findAll(Pageable pageable);

    ProductInfo save(ProductInfo productInfo);

    //加库存
    void increaseStock(String productId,Integer number);

    //减库存
    void decreaseStock(String productId,Integer number);

    //上架
    ProductInfo onSale(String productId);

    //下架
    ProductInfo offSale(String productId);

    //根据商品的状态和类目查询商品列表
    List<ProductInfo> findByCategoryTypeAndProductStatus(Integer categoryType,Integer productStatus);
}
