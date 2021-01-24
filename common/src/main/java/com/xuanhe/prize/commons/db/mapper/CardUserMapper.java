package com.xuanhe.prize.commons.db.mapper;

import com.xuanhe.prize.commons.db.entity.CardUser;
import com.xuanhe.prize.commons.db.entity.CardUserExample;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.Update;

public interface CardUserMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table card_user
     *
     * @mbg.generated
     */
    long countByExample(CardUserExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table card_user
     *
     * @mbg.generated
     */
    int deleteByExample(CardUserExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table card_user
     *
     * @mbg.generated
     */
    @Delete({
        "delete from card_user",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table card_user
     *
     * @mbg.generated
     */
    @Insert({
        "insert into card_user (uname, passwd, ",
        "realname, idcard, ",
        "phone, level, createtime, ",
        "updatetime)",
        "values (#{uname,jdbcType=VARCHAR}, #{passwd,jdbcType=VARCHAR}, ",
        "#{realname,jdbcType=VARCHAR}, #{idcard,jdbcType=VARCHAR}, ",
        "#{phone,jdbcType=VARCHAR}, #{level,jdbcType=SMALLINT}, #{createtime,jdbcType=TIMESTAMP}, ",
        "#{updatetime,jdbcType=TIMESTAMP})"
    })
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="id", before=false, resultType=Integer.class)
    int insert(CardUser record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table card_user
     *
     * @mbg.generated
     */
    int insertSelective(CardUser record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table card_user
     *
     * @mbg.generated
     */
    List<CardUser> selectByExample(CardUserExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table card_user
     *
     * @mbg.generated
     */
    @Select({
        "select",
        "id, uname, passwd, realname, idcard, phone, level, createtime, updatetime",
        "from card_user",
        "where id = #{id,jdbcType=INTEGER}"
    })
    @ResultMap("com.xuanhe.prize.commons.db.mapper.CardUserMapper.BaseResultMap")
    CardUser selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table card_user
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") CardUser record, @Param("example") CardUserExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table card_user
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") CardUser record, @Param("example") CardUserExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table card_user
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(CardUser record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table card_user
     *
     * @mbg.generated
     */
    @Update({
        "update card_user",
        "set uname = #{uname,jdbcType=VARCHAR},",
          "passwd = #{passwd,jdbcType=VARCHAR},",
          "realname = #{realname,jdbcType=VARCHAR},",
          "idcard = #{idcard,jdbcType=VARCHAR},",
          "phone = #{phone,jdbcType=VARCHAR},",
          "level = #{level,jdbcType=SMALLINT},",
          "createtime = #{createtime,jdbcType=TIMESTAMP},",
          "updatetime = #{updatetime,jdbcType=TIMESTAMP}",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(CardUser record);
}