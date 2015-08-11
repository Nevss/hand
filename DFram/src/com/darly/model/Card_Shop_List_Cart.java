package com.darly.model;

import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

/*
 * {"show_cart":
 * [{"obj_ident":"goods_182_994","obj_type":"goods","goods_id":"182","product_id":"994","quantity":"1",
 * "product_name":"\u5341\u6708\u5988\u54aa \u9632\u8f90\u5c04\u5b55\u5987\u88c5\u6b63\u54c1 \u53ef\u8131\u5378\u94f6\u7ea4\u7ef4\u9632\u8f90\u5c04\u809a\u515c\u5bb6\u5c45\u56f4\u88d9",
 * "price":"539.000","mktprice":"680.000","spec":"\u5c3a\u7801\uff1a\u5747\u7801\u3001\u989c\u8272\uff1a\u7ea2\u8272",
 * "image":"http:\/\/128.128.1.41:18888\/public\/images\/fb\/0b\/25\/bf5e8038f841e28c600783784acf4d780e2375b6.jpg"},{"obj_ident":"goods_182_995","obj_type":"goods","goods_id":"182","product_id":"995","quantity":"1","product_name":"\u5341\u6708\u5988\u54aa \u9632\u8f90\u5c04\u5b55\u5987\u88c5\u6b63\u54c1 \u53ef\u8131\u5378\u94f6\u7ea4\u7ef4\u9632\u8f90\u5c04\u809a\u515c\u5bb6\u5c45\u56f4\u88d9","price":"539.000","mktprice":"680.000","spec":"\u5c3a\u7801\uff1a\u5747\u7801\u3001\u989c\u8272\uff1a\u84dd\u8272","image":"http:\/\/128.128.1.41:18888\/public\/images\/fb\/0b\/25\/bf5e8038f841e28c600783784acf4d780e2375b6.jpg"},{"obj_ident":"goods_187_981","obj_type":"goods","goods_id":"187","product_id":"981","quantity":"2","product_name":"\u5341\u6708\u5988\u54aa\u9632\u8f90\u5c04\u670d\u6b63\u54c1\u5b55\u5987\u88c5\u6625\u590f\u6b3e\u857e\u4e1d\u62fc\u63a5\u5b55\u5987\u9632\u8f90\u8863\u670d\u56db\u5b63\u901a\u7528","price":"999.000","mktprice":"1480.000","spec":"\u5c3a\u7801\uff1aM\u3001\u989c\u8272\uff1a\u7070\u8272","image":"http:\/\/128.128.1.41:18888\/public\/images\/22\/6b\/81\/22c2f2ac2b668386e50062a43c45149ef646b2f9.jpg"},{"obj_ident":"goods_197_964","obj_type":"goods","goods_id":"197","product_id":"964","quantity":"1","product_name":"\u5341\u6708\u5988\u54aa\u5b55\u5987\u62a4\u80a4\u54c1\u5957\u88c5\u7eaf\u5929\u7136\u8865\u6c34\u7136\u65b9\u6b63\u54c1\u4fdd\u6e7f\u5b55\u5987\u5316\u5986\u54c1 3\u4ef6\u5957","price":"495.000","mktprice":"727.000","spec":null,"image":"http:\/\/128.128.1.41:18888\/public\/images\/ee\/50\/f5\/d50dab5078455bf399d5c38b61522e30c2082e78.jpg"}]}
 * 
 * 
 */
public class Card_Shop_List_Cart implements Parcelable {
	public String obj_ident;
	public String obj_type;
	public int goods_id; // 商品ID
	public int product_id; // 货品ID
	public int quantity; // 购买数量
	public String product_name; // 货品名称
	public String url;
	public String price; // 现价格
	public String mktprice; // 原价格
	public String spec; // 规格
	public List<Card_SHop_Spec_info> spec_info; // 规格列表
	public String image; // 图片
	public int reless; // 剩余商品

	public Card_Shop_List_Cart() {
		// TODO Auto-generated constructor stub
	}

	public Card_Shop_List_Cart(String obj_ident, String obj_type, int goods_id,
			int product_id, int quantity, String product_name, String url,
			String price, String mktprice, String spec,
			List<Card_SHop_Spec_info> spec_info, String image, int reless) {
		super();
		this.obj_ident = obj_ident;
		this.obj_type = obj_type;
		this.goods_id = goods_id;
		this.product_id = product_id;
		this.quantity = quantity;
		this.product_name = product_name;
		this.url = url;
		this.price = price;
		this.mktprice = mktprice;
		this.spec = spec;
		this.spec_info = spec_info;
		this.image = image;
		this.reless = reless;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(obj_ident);
		dest.writeString(obj_type);
		dest.writeInt(goods_id);
		dest.writeInt(product_id);
		dest.writeInt(quantity);
		dest.writeString(product_name);
		dest.writeString(price);
		dest.writeString(mktprice);
		dest.writeString(spec);
		dest.writeString(image);
		dest.writeString(url);
		dest.writeInt(reless);
	}

	public static final Parcelable.Creator<Card_Shop_List_Cart> CREATOR = new Creator<Card_Shop_List_Cart>() {

		@Override
		public Card_Shop_List_Cart[] newArray(int size) {
			// TODO Auto-generated method stub
			return new Card_Shop_List_Cart[size];
		}

		@Override
		public Card_Shop_List_Cart createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			Card_Shop_List_Cart list = new Card_Shop_List_Cart();
			list.obj_ident = source.readString();
			list.obj_type = source.readString();
			list.goods_id = source.readInt();
			list.product_id = source.readInt();
			list.quantity = source.readInt();
			list.product_name = source.readString();
			list.price = source.readString();
			list.mktprice = source.readString();
			list.spec = source.readString();
			list.image = source.readString();
			list.url = source.readString();
			list.reless = source.readInt();
			return list;
		}
	};
}
