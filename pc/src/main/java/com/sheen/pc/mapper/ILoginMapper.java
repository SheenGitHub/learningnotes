package com.sheen.pc.mapper;

import com.sheen.pc.model.LoginModel;
import com.sheen.pc.provider.LoginProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

/**
 * Created by zxj7044 on 2018-11-8.
 */
@Mapper
public interface ILoginMapper {
    /**
     *
     * @param id
     * @throws Exception
     */
    void deleteByPrimaryKey(Long id) throws Exception;

    /**
     *
     * @param record
     * @throws Exception
     */
    @SelectProvider(type = LoginProvider.class, method = "save")
    void save(LoginModel record) throws Exception;

    /**
     *
     * @param id
     * @return
     * @throws Exception
     */
    LoginModel queryByPrimaryKey(Integer id) throws Exception;

    /**
     *
     * @param userame
     * @return
     * @throws Exception
     */
    @SelectProvider(type = LoginProvider.class, method = "queryByUserName")
    LoginModel queryByUserName(String userame) throws Exception;

    /**
     *
     * @param model
     * @throws Exception
     */
    @UpdateProvider(type = LoginProvider.class, method = "updateByModel")
    void updateByModel(LoginModel model) throws Exception;
}
