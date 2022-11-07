package com.nowcoder.community.dao;

import org.springframework.stereotype.Repository;

// 声明 Bean 的名字
@Repository("alphaHibernate")
public class AlphaDaoHibernateImpl implements AlphaDao {
    @Override
    public String select() {
        return "Hibernate";
    }
}
