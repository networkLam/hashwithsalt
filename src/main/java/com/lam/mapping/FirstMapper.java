package com.lam.mapping;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

@Mapper
public interface FirstMapper {
    @Select("select * from configs")
    public List<Map<String,Object>> getConfig();

    @Insert("insert into configs(`key`,`value`) values (#{key},#{value})")
    public void addOption(String key,String value);

    @Update("update configs set value=#{value} where id=#{id}")
    public void updateOption(String value,Integer id);

    @Insert("insert into userinfo(`act`,`pwd`,`salt`) values (#{act},#{pwd},#{salt})")
    public void registerAccount(String act,String pwd,String salt);
    @Select("select pwd,salt from userinfo where act = #{act}")
    public Map<String,String> login(String act);
}
