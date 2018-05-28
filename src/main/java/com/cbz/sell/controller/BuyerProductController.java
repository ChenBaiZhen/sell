package com.cbz.sell.controller;
import com.cbz.sell.dataObject.ProductCategory;
import com.cbz.sell.dataObject.ProductInfo;
import com.cbz.sell.enums.ProductStatusEnum;
import com.cbz.sell.service.CategoryService;
import com.cbz.sell.service.ProductInfoService;
import com.cbz.sell.utils.ResultVOUtil;
import com.cbz.sell.viewObject.ProductInfoVO;
import com.cbz.sell.viewObject.ProductVO;
import com.cbz.sell.viewObject.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/buyer/product")
public class BuyerProductController {
    @Autowired
    private ProductInfoService productInfoService;
    @Autowired
    private CategoryService categoryService;

    /**
     * 查询出所有的上架商品列表
     * @return
     */
    @GetMapping("/list")
    public ResultVO productList(){
        List<ProductCategory> productCategoryList=categoryService.findAll();
        List<ProductVO> productVOList=new ArrayList<>();
        for (ProductCategory category:productCategoryList) {
            List<ProductInfoVO> productInfoVOList=new ArrayList<>();
            ProductVO productVO=new ProductVO();
            productVO.setCategoryName(category.getCategoryName());
            productVO.setCategoryType(category.getCategoryType());
            List<ProductInfo> productInfoList=productInfoService.findByCategoryTypeAndProductStatus(category.getCategoryType(),ProductStatusEnum.UP.getCode());
            for(ProductInfo p:productInfoList){
                ProductInfoVO productInfoVO=new ProductInfoVO();
                productInfoVO.setProductId(p.getProductId());
                productInfoVO.setProductDescription(p.getProductDescription());
                productInfoVO.setProductIcon(p.getProductIcon());
                productInfoVO.setProductName(p.getProductName());
                productInfoVO.setProductPrice(p.getProductPrice());
                productInfoVOList.add(productInfoVO);
            }
            productVO.setProductInfoVOList(productInfoVOList);
            productVOList.add(productVO);
        }
        ResultVO resultVO=ResultVOUtil.success(productVOList);
        return resultVO;
    }
}
