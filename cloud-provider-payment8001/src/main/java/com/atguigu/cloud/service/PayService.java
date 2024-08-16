package com.atguigu.cloud.service;

import com.atguigu.cloud.entities.Pay;

import java.util.List;

public interface PayService {
    /**
     * 添加
     * @param pay
     * @return
     */
    int add(Pay pay);

    /**
     * 删除
     * @param id
     * @return
     */
    int delete(Integer id);

    /**
     * 修改
     * @param pay
     * @return
     */
    int update(Pay pay);

    /**
     * 查询单个
     * @param id
     * @return
     */
    Pay getById(Integer id);

    /**
     * 查所有
     * @return
     */
    List<Pay> getAll();
}
