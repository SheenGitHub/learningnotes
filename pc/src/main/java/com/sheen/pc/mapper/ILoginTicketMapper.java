package com.sheen.pc.mapper;

import com.sheen.pc.model.LoginTicket;
import com.sheen.pc.provider.LoginTicketProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.Map;

/**
 * Created by zxj7044 on 2018-11-9.
 */
@Mapper
public interface ILoginTicketMapper {
    /**
     *
     * @param id
     * @return
     * @throws Exception
     */
    int deleteByPrimaryKey(Integer id) throws Exception;

    /**
     *
     * @param record
     * @return
     * @throws Exception
     */
    @InsertProvider(type = LoginTicketProvider.class, method = "save")
    int save(LoginTicket record) throws Exception;

    /**
     *
     * @param id
     * @return
     * @throws Exception
     */
    LoginTicket queryByPrimaryKey(Integer id) throws Exception;

    /**
     *
     * @param ticket
     * @return
     * @throws Exception
     */
    @SelectProvider(type = LoginTicketProvider.class, method = "queryByTicket")
    LoginTicket queryByTicket(String ticket) throws Exception;

    /**
     *
     * @param record
     * @return
     * @throws Exception
     */
    int updateByPrimaryKey(LoginTicket record) throws Exception;

    /**
     *
     * @param map
     * @return
     * @throws Exception
     */
    int updateStatus(Map<String, Object> map) throws Exception;

}
