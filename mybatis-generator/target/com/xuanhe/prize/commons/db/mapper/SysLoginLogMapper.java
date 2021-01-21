package com.xuanhe.prize.commons.db.mapper;

import com.xuanhe.prize.commons.db.entity.SysLoginLog;
import com.xuanhe.prize.commons.db.entity.SysLoginLogExample;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.Update;

public interface SysLoginLogMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_login_log
     *
     * @mbg.generated
     */
    long countByExample(SysLoginLogExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_login_log
     *
     * @mbg.generated
     */
    int deleteByExample(SysLoginLogExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_login_log
     *
     * @mbg.generated
     */
    @Delete({
        "delete from sys_login_log",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_login_log
     *
     * @mbg.generated
     */
    @Insert({
        "insert into sys_login_log (username, session_id, ",
        "ip, is_success, remark, ",
        "create_time)",
        "values (#{username,jdbcType=VARCHAR}, #{sessionId,jdbcType=VARCHAR}, ",
        "#{ip,jdbcType=VARCHAR}, #{isSuccess,jdbcType=INTEGER}, #{remark,jdbcType=VARCHAR}, ",
        "#{createTime,jdbcType=TIMESTAMP})"
    })
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="id", before=false, resultType=Integer.class)
    int insert(SysLoginLog record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_login_log
     *
     * @mbg.generated
     */
    int insertSelective(SysLoginLog record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_login_log
     *
     * @mbg.generated
     */
    List<SysLoginLog> selectByExample(SysLoginLogExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_login_log
     *
     * @mbg.generated
     */
    @Select({
        "select",
        "id, username, session_id, ip, is_success, remark, create_time",
        "from sys_login_log",
        "where id = #{id,jdbcType=INTEGER}"
    })
    @ResultMap("com.xuanhe.prize.commons.db.mapper.SysLoginLogMapper.BaseResultMap")
    SysLoginLog selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_login_log
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") SysLoginLog record, @Param("example") SysLoginLogExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_login_log
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") SysLoginLog record, @Param("example") SysLoginLogExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_login_log
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(SysLoginLog record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_login_log
     *
     * @mbg.generated
     */
    @Update({
        "update sys_login_log",
        "set username = #{username,jdbcType=VARCHAR},",
          "session_id = #{sessionId,jdbcType=VARCHAR},",
          "ip = #{ip,jdbcType=VARCHAR},",
          "is_success = #{isSuccess,jdbcType=INTEGER},",
          "remark = #{remark,jdbcType=VARCHAR},",
          "create_time = #{createTime,jdbcType=TIMESTAMP}",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(SysLoginLog record);
}