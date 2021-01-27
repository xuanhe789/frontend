package com.xuanhe.prize.commons.db.mapper;

import com.xuanhe.prize.commons.db.entity.ViewCardUserHit;
import com.xuanhe.prize.commons.db.entity.ViewCardUserHitExample;
import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectKey;

public interface ViewCardUserHitMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table view_card_user_hit
     *
     * @mbg.generated
     */
    long countByExample(ViewCardUserHitExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table view_card_user_hit
     *
     * @mbg.generated
     */
    int deleteByExample(ViewCardUserHitExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table view_card_user_hit
     *
     * @mbg.generated
     */
    @Insert({
        "insert into view_card_user_hit (title, type, ",
        "uname, realname, ",
        "idcard, phone, level, ",
        "name, price, gameid, ",
        "userid, productid, ",
        "hittime)",
        "values (#{title,jdbcType=VARCHAR}, #{type,jdbcType=VARCHAR}, ",
        "#{uname,jdbcType=VARCHAR}, #{realname,jdbcType=VARCHAR}, ",
        "#{idcard,jdbcType=VARCHAR}, #{phone,jdbcType=VARCHAR}, #{level,jdbcType=VARCHAR}, ",
        "#{name,jdbcType=VARCHAR}, #{price,jdbcType=DECIMAL}, #{gameid,jdbcType=INTEGER}, ",
        "#{userid,jdbcType=INTEGER}, #{productid,jdbcType=INTEGER}, ",
        "#{hittime,jdbcType=TIMESTAMP})"
    })
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="id", before=false, resultType=Integer.class)
    int insert(ViewCardUserHit record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table view_card_user_hit
     *
     * @mbg.generated
     */
    int insertSelective(ViewCardUserHit record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table view_card_user_hit
     *
     * @mbg.generated
     */
    List<ViewCardUserHit> selectByExample(ViewCardUserHitExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table view_card_user_hit
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") ViewCardUserHit record, @Param("example") ViewCardUserHitExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table view_card_user_hit
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") ViewCardUserHit record, @Param("example") ViewCardUserHitExample example);

    Integer getProductsNumByUserId(Integer id);

    List<ViewCardUserHit> selectByUserId(Integer id);

    List<ViewCardUserHit> selectByUserIdAndGameId(@Param("userid") Integer id, @Param("gameid") int gameid);

    int getTotal(@Param("userid") Integer id,@Param("gameid") int gameid);
}