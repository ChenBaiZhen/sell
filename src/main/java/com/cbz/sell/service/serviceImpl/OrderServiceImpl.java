package com.cbz.sell.service.serviceImpl;

import com.cbz.sell.converter.OrderMaster2OrderDTOConverter;
import com.cbz.sell.dataObject.OrderDetail;
import com.cbz.sell.dataObject.OrderMaster;
import com.cbz.sell.dataObject.ProductInfo;
import com.cbz.sell.dto.OrderDTO;
import com.cbz.sell.enums.OrderStatusEnum;
import com.cbz.sell.enums.PayStatusEnum;
import com.cbz.sell.enums.ResultEnum;
import com.cbz.sell.exception.SellException;
import com.cbz.sell.repository.OrderDetailRepository;
import com.cbz.sell.repository.OrderMasterRepository;
import com.cbz.sell.repository.ProductInfoRepository;
import com.cbz.sell.service.OrderService;
import com.cbz.sell.service.ProductInfoService;
import com.cbz.sell.utils.CreateKeyUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {
    @Autowired
    private ProductInfoRepository productInfoRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private OrderMasterRepository orderMasterRepository;
    @Autowired
    private ProductInfoService productInfoService;

    @Override
    @Transactional
    public OrderDTO create(OrderDTO orderDTO) {
        //1 生成订单Id
        String orderId=CreateKeyUtils.createKey();
        BigDecimal amount=new BigDecimal(BigInteger.ZERO);
        //2 查询商品
        for (OrderDetail orderDetail: orderDTO.getOrderDetailList()) {
            ProductInfo productInfo=productInfoRepository.findOne(orderDetail.getProductId());
            if(productInfo==null){
                throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
            }
            //计算订单总金额
            amount=productInfo.getProductPrice().multiply(new BigDecimal(orderDetail.getProductQuantity())).add(amount);
            //扣库存
            if(productInfo.getProductStock()<orderDetail.getProductQuantity()){
                throw new SellException(ResultEnum.PRODUCT_STOCK_ERROR);
            }
            productInfoService.decreaseStock(productInfo.getProductId(),orderDetail.getProductQuantity());
            //持久化到订单详情数据库
            orderDetail.setDetailId(CreateKeyUtils.createKey());
            orderDetail.setOrderId(orderId);
            BeanUtils.copyProperties(productInfo,orderDetail);
            orderDetailRepository.save(orderDetail);
        }
        //保存订单总表数据到数据库
        OrderMaster orderMaster=new OrderMaster();
        orderMaster.setOrderId(orderId);
        orderMaster.setOrderAmount(amount);
        BeanUtils.copyProperties(orderDTO,orderMaster);
        orderMaster.setCreateTime(new Date());
        orderMaster.setOrderStatus(OrderStatusEnum.NEW.getCode());
        orderMaster.setPayStatus(PayStatusEnum.WAIT.getCode());
        orderMasterRepository.save(orderMaster);
        return orderDTO;
    }

    @Override
    public OrderDTO findOne(String orderId) {
        OrderMaster orderMaster=orderMasterRepository.findOne(orderId);
        if(orderMaster==null){
            throw new SellException(ResultEnum.ORDER_NOT_EXIST);
        }
        List<OrderDetail> orderDetails=orderDetailRepository.findByOrderId(orderId);
        if(orderDetails.size()==0){
            throw new SellException(ResultEnum.ORDER_DETAIL_EMPTY);
        }
        OrderDTO orderDTO=new OrderDTO();
        BeanUtils.copyProperties(orderMaster,orderDTO);
        orderDTO.setOrderDetailList(orderDetails);
        return orderDTO;
    }

    @Override
    public Page<OrderDTO> findList(String buyerOpenid, Pageable pageable) {
        Page<OrderMaster> orderMasterPage=orderMasterRepository.findByBuyerOpenid(buyerOpenid,pageable);
        List<OrderDTO> orderDTOList=OrderMaster2OrderDTOConverter.convert(orderMasterPage.getContent());
        return new PageImpl<>(orderDTOList,pageable,orderMasterPage.getTotalPages());
    }

    @Override
    @Transactional
    public OrderDTO cancel(OrderDTO orderDTO) {
        //判断订单的状态  为新订单才能取消
        if(!orderDTO.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())){
            log.error("【卖家端 订单状态异常，取消失败】，orderId={},orderStatus",orderDTO.getOrderId(),orderDTO.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }
        orderDTO.setOrderStatus(OrderStatusEnum.CANCEL.getCode());
        OrderMaster orderMaster=new OrderMaster();
        BeanUtils.copyProperties(orderDTO,orderMaster);
        OrderMaster orderMasterUpdate=orderMasterRepository.save(orderMaster);
        if(orderMasterUpdate==null){
            log.error("【卖家端 订单更新失败】");
            throw new SellException(ResultEnum.ORDER_UPDATE_FAIL);
        }
        for (OrderDetail orderDetail: orderDTO.getOrderDetailList()) {
            ProductInfo productInfo=productInfoRepository.findOne(orderDetail.getProductId());
            if(productInfo==null){
                log.error("【卖家端 订单商品为空】");
                throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
            }
            //加库存
            productInfoService.increaseStock(productInfo.getProductId(),orderDetail.getProductQuantity());
        }
        //判断支付状态
        if(orderDTO.getPayStatus().equals(PayStatusEnum.SUCCESS.getCode())){
            log.info("【取消订单成功】 退款金额为：amount={},",orderDTO.getOrderAmount());
            //退款
        }
        return orderDTO;
    }

    @Override
    public OrderDTO finish(OrderDTO orderDTO) {
        return null;
    }

    @Override
    public OrderDTO paid(OrderDTO orderDTO) {
        return null;
    }

    @Override
    public Page<OrderDTO> findList(Pageable pageable) {
        return null;
    }

}
