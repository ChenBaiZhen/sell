package com.cbz.sell.service.serviceImpl;

import com.cbz.sell.dataObject.ProductInfo;
import com.cbz.sell.enums.ProductStatusEnum;
import com.cbz.sell.repository.ProductInfoRepository;
import com.cbz.sell.service.ProductInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class ProductInfoServiceImpl implements ProductInfoService {
    @Autowired
    private ProductInfoRepository repository;

    @Override
    public ProductInfo findOne(String productId) {
        return repository.findOne(productId);
    }

    @Override
    public List<ProductInfo> findUpAll() {
        return repository.findAll();
    }

    @Override
    public Page<ProductInfo> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public ProductInfo save(ProductInfo productInfo) {
        return repository.save(productInfo);
    }

    @Override
    public ProductInfo onSale(String productId) {
         ProductInfo productInfo=findOne(productId);
         productInfo.setProductId(productId);
         productInfo.setProductStatus(ProductStatusEnum.UP.getCode());
         return repository.save(productInfo);
    }

    @Override
    public ProductInfo offSale(String productId) {
        ProductInfo productInfo=findOne(productId);
        productInfo.setProductId(productId);
        productInfo.setProductStatus(ProductStatusEnum.DOWN.getCode());
        return repository.save(productInfo);
    }
}
