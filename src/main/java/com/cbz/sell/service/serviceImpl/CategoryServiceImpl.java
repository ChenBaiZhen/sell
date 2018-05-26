package com.cbz.sell.service.serviceImpl;

import com.cbz.sell.dataObject.ProductCategory;
import com.cbz.sell.repository.ProductCategoryRepository;
import com.cbz.sell.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private ProductCategoryRepository repository;
    @Override
    public ProductCategory findOne(Integer id) {
        return repository.findOne(id);
    }

    @Override
    public List<ProductCategory> findAll() {
        return repository.findAll();
    }

    @Override
    public List<ProductCategory> findByCategoryTypeIn(List<Integer> tepeList) {
        return repository.findByCategoryTypeIn(tepeList);
    }

    @Override
    public ProductCategory save(ProductCategory productCategory) {
        return repository.save(productCategory);
    }
}
