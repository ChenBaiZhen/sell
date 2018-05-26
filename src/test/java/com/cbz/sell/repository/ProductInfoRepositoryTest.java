package com.cbz.sell.repository;

import com.cbz.sell.dataObject.ProductInfo;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductInfoRepositoryTest {

    @Autowired
    private ProductInfoRepository repository;
    @Test
    @Transactional
    public void save(){
        ProductInfo productInfo=new ProductInfo();
        productInfo.setProductId("233456");
        productInfo.setCategoryType(1);
        productInfo.setCreateTime(new Date());
        productInfo.setProductDescription("dsf");
        productInfo.setProductIcon("dc");
        productInfo.setProductName("ds");
        productInfo.setProductPrice(new BigDecimal(234));
        productInfo.setProductStock(3);
        productInfo.setCategoryType(22);
        productInfo.setProductStatus(1);
        repository.save(productInfo);

    }

    @Test
    public void findByProductStatus() {
        Assert.assertNotNull(repository.findByProductStatus(1).size());
        System.out.println(repository.findByProductStatus(1).size());
    }
}