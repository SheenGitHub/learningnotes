package com.sheen.pc.mapper;

import com.sheen.pc.model.User;
import com.sheen.pc.provider.UserProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

/**
 * Created by zxj7044 on 2018-10-19.
 */
@Mapper
public interface IUserMapper {
    /**
     * @param id
     * @return
     * @throws Exception
     */
    User findUserByPrimaryKey(int id) throws Exception;

    /**
     * @param map
     * @return
     * @throws Exception
     */
    @SelectProvider(type = UserProvider.class, method = "ListUsers")
    List<User> list(User user) throws Exception;

    @SelectProvider(type = UserProvider.class, method = "findByPage")
    List<User> findByPage() throws Exception;

}
