package org.eop.asei.example.service;

import org.eop.asei.example.bean.Example;

/**
 * @author lixinjie
 * @since 2018-09-03
 */
public interface IExampleService {

	int insertExample(Example example);
	
	int updateExample(Example example);
	
	int deleteExample(long id);
	
	Example selectExample(long id);
}
