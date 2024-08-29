package com.attend.mapper;


import com.attend.dto.UserRegisterDTO;
import com.attend.dto.UserUpdateDTO;
import com.attend.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserMapper {
    @Select("select * from user where openid=#{openid}")
    User getByOpenid(String openid);

    // 在 UserMapper 接口中
    @Insert("INSERT INTO user (openid, avatar, nick_name, create_time) VALUES (#{openid}, #{avatar}, #{nickName}, #{createTime})")
    void insert(User user);

    @Select("select * from user where id=#{id}")
    User getInfo(Long id);

    @Update("update user set nick_name=#{nickName},name=#{name},sex=#{sex},age=#{age},phone=#{phone},student_id=#{studentId},id_number=#{idNumber},college=#{college},major=#{major} where id=#{id}")
    void update(UserUpdateDTO userUpdateDTO);

    @Select("select name from user where id=#{id}")
    String getById(Long id);

    @Update("update user set name=#{name}, sex=#{sex}, age=#{age}, phone=#{phone}, student_id=#{studentId}, id_number=#{idNumber}, college=#{college}, major=#{major} where id=#{id}")
    void updateById(UserRegisterDTO userRegisterDTO);
}
