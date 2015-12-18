/**
 * 上午9:44:20
 * @author zhangyh2
 * $
 * DarlyModel.java
 * TODO
 */
package com.darly.oop.model;

import java.util.List;

/**
 * @author zhangyh2 DarlyModel $ 上午9:44:20 TODO
 * 
 *         {"data": "[{ \"_id\" : { \"$oid\" : \"565d6af07086c30b60eab1dd\"} ,
 *         \"name\" : \"小偷\" , \"age\" : 24 , \"sex\" : \"男\" , \"time\" :
 *         \"1986-10-03\"}, { \
 *         "_id\" : { \"$oid\" : \"565d6b927086c30b60eab1de\"} , \"name\" : \"小儿\" , \"age\" : 24 , \"sex\" : \"男\" , \"time\" : \"1986-10-03\"}]"
 *         ,"code":200,"table":"darly"}
 */
public class DarlyModel {
	public List<String> data;
	public int code;
	public String table;
	public String msg;

}
