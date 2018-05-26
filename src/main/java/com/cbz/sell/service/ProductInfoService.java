package com.cbz.sell.service;

import com.cbz.sell.dataObject.ProductInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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


    //减库存


    //上架
    ProductInfo onSale(String productId);

    //下架
    ProductInfo offSale(String productId);
}
