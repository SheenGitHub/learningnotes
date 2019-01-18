package com.sheen.pc.mapper;

import com.sheen.pc.provider.CommonProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

/**
 * Created by zxj7044 on 2018-11-8.
 */
@Mapper
public interface ICommonMapper {
    @SelectProvider(type = CommonProvider.class,method = "getSequence")
    Integer getSequence(String sequence) throws Exception;
}
