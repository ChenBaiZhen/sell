package com.cbz.sell.repository;

import com.cbz.sell.dataObject.ProductCategory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductCategoryRepositoryTest {
    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @Test
    public void findOne(){
        ProductCategory productCategory = productCategoryRepository.findOne(1);
        System.out.println(productCategory.getCategoryName());
    }

    @Test
    @Transactional   //事务注解  该注解在测试方法中 测试完成之后并不影响数据库中的数据
    public void save(){
        ProductCategory productCategory = new ProductCategory();
        productCategory.setCategoryName("cbzsdsada");
        productCategory.setCategoryType(2);
        productCategory.setCreateTime(new Date());
        ProductCategory save = productCategoryRepository.save(productCategory);
        Assert.assertNotNull(save);
    }



}