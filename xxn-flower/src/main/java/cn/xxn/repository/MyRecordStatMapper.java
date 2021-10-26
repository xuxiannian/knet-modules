package cn.xxn.repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository
public interface MyRecordStatMapper {

	int totalByIn();

	int totalByOut();
	
	List<Map<?,?>> statByDate(Date date);
	
	
	

}
