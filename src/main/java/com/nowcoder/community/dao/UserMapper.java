package com.nowcoder.community.dao;

import com.nowcoder.community.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    User selectById(int id);

    User selectByName(String username);

    User selectByEmail(String email);

    int insertUser(User user); // 返回插入数据行数

    int updateStatus(int id, int status); // 返回修改数据条数

    int updateHeader(int id, String headerUrl);

    int updatePassword(int id, String password);
}
