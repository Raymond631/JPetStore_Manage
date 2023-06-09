package com.example.jpetstore_manage.Service;

import com.example.jpetstore_manage.Common.CommonResponse;
import com.example.jpetstore_manage.POJO.DataObject.OrderMainDO;

import java.util.List;

/**
 * @author Raymond Li
 * @create 2023-03-20 16:32
 * @description
 */
public interface OrderService {
    /**
     * 查询订单列表
     */
    List<OrderMainDO> getOrderList(int supplier);

    /**
     * 发货，修改订单状态为“已发货”
     */
    CommonResponse ship(int[] orderIdList, int supplier);

    /**
     * 修改收件人信息
     */
    CommonResponse changeReceiver(OrderMainDO orderMainDO, int orderId, int supplier);

    CommonResponse deleteOrder(int[] orderIdList, int supplier);
}
