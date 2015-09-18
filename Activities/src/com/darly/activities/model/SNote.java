package com.darly.activities.model;

import java.io.Serializable;

import com.darly.activities.db.SnoteDAO;

/**
 * @ClassName: SNote
 * @Description: TODO(数据库表字段Model)
 * @author 张宇辉 zhangyuhui@octmami.com
 * @date 2014年11月26日 上午8:53:07
 *
 */
public class SNote implements Serializable, SnoteDAO {
	/**
	 * @Fields serialVersionUID : TODO(TODO)
	 */
	private static final long serialVersionUID = -2843674339720624934L;
	public int id;
	public int product_id;
	public int product_num;
	public String product_url;
	public String product_name;
	public String product_price;
	public String product_orprice;
	public String product_image;
	public String product_spec;

	public int product_reless;

	public SNote(int id, int product_id, int product_num, String product_url,
			String product_name, String product_price, String product_orprice,
			String product_image, String product_spec, int product_reless) {
		super();
		this.id = id;
		this.product_id = product_id;
		this.product_num = product_num;
		this.product_url = product_url;
		this.product_name = product_name;
		this.product_price = product_price;
		this.product_orprice = product_orprice;
		this.product_image = product_image;
		this.product_spec = product_spec;
		this.product_reless = product_reless;
	}

	public SNote(int product_id, int product_num, String product_url,
			String product_name, String product_price, String product_orprice,
			String product_image, String product_spec, int product_reless) {
		super();
		this.product_id = product_id;
		this.product_num = product_num;
		this.product_url = product_url;
		this.product_name = product_name;
		this.product_price = product_price;
		this.product_orprice = product_orprice;
		this.product_image = product_image;
		this.product_spec = product_spec;
		this.product_reless = product_reless;
	}

}
