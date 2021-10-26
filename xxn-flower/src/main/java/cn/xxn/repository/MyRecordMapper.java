package cn.xxn.repository;

import cn.xxn.domain.MyRecord;
import cn.xxn.domain.MyRecordExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface MyRecordMapper {
    int countByExample(MyRecordExample example);

    int deleteByExample(MyRecordExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(MyRecord record);

    int insertSelective(MyRecord record);

    List<MyRecord> selectByExample(MyRecordExample example);

    MyRecord selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") MyRecord record, @Param("example") MyRecordExample example);

    int updateByExample(@Param("record") MyRecord record, @Param("example") MyRecordExample example);

    int updateByPrimaryKeySelective(MyRecord record);

    int updateByPrimaryKey(MyRecord record);
}