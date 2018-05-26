package com.cbz.sell.service;

import com.cbz.sell.dataObject.ProductCategory;

import java.util.List;

public interface CategoryService {
    ProductCategory findOne(Integer id);
    List<ProductCategory> findAll();
    List<ProductCategory> findByCategoryTypeIn(List<Integer> tepeList);
    ProductCategory save(ProductCategory productCategory);
}
