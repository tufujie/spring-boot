package com.jef.dao;

import com.jef.entity.Order;
import org.springframework.stereotype.Repository;

/**
 * 订单DAO层
 *
 * @author Jef
 * @create 2018/5/15 19:18
 */
@Repository
public interface IOrderDao extends IBaseDao {

    Order getByID(Long id);

    boolean insert(Order order);

}
