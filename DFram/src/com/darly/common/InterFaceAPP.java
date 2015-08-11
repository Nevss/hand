package com.darly.common;

import com.darly.app.Declare;

/**
 * @ClassName: Constra
 * @Description: TODO(常量類)
 * @author 张宇辉 zhangyuhui@octmami.com
 * @date 2014年11月15日 上午10:49:59
 *
 */
/**
 * @ClassName: Constra
 * @Description: TODO(接口类)
 * @author 张宇辉 zhangyuhui@octmami.com
 * @date 2014年12月2日 下午3:11:33
 *
 */
public class InterFaceAPP {
	// public static final String SEVCIE = "http://128.128.1.41:18889/";
	@Declare(NAME = "SEVCIE", TODO = "服务器地址")
	// public static final String SEVCIE = "http://101.231.80.29:18889/";
	// 正式的
	public static final String SEVCIE = "http://api.octmami.com/index.php?r=api101";
	// public static final String SEVCIE =
	// "http://api.st.octmami.com/index.php?r=api101";
	// public static final String SEVCIE =
	// "http://api.uat.octmami.com/index.php?r=api101";
	@Declare(NAME = "VERSION", TODO = "版本判断接口")
	public static final String VERSION = SEVCIE + "/check/version";
	@Declare(NAME = "BANNER", TODO = "20150305版BANNER广告链接接口")
	public static final String BANNER = SEVCIE + "/home/banner";
	@Declare(NAME = "ADVERTBRANNER", TODO = "第二版，最新上线，肯即将上线接口")
	public static final String ADVERTBRANNER = SEVCIE + "/home/advertbanner";
	@Declare(NAME = "RECPRODUCTS", TODO = "20150305版RECPRODUCTS首页特卖商品列表接口")
	public static final String RECPRODUCTS = SEVCIE + "/home/recproducts";
	@Declare(NAME = "HTTP", TODO = "广告链接地址")
	public static final String HTTP = SEVCIE + "/home/ads";
	@Declare(NAME = "SHOWGOODS", TODO = "商品列表地址")
	public static final String SHOWGOODS = SEVCIE + "/list/brand";
	@Declare(NAME = "DETAIL", TODO = "商品详情")
	public static final String DETAIL = SEVCIE + "/goods/detail";
	@Declare(NAME = "SEARCH", TODO = "商品搜索")
	public static final String SEARCH = SEVCIE + "/list/search";
	@Declare(NAME = "SELECT", TODO = "商品分类筛选条件")
	public static final String SELECT = SEVCIE + "/list/filter";
	@Declare(NAME = "CAT", TODO = "商品分类列表")
	public static final String CAT = SEVCIE + "/list/cat";
	@Declare(NAME = "CATEGORY", TODO = "商品分类筛选")
	public static final String CATEGORY = SEVCIE + "/cat/all";
	@Declare(NAME = "CART", TODO = "查看购物车")
	public static final String CART = SEVCIE + "/cart/index";
	@Declare(NAME = "ADDCART", TODO = "添加购物车")
	public static final String ADDCART = SEVCIE + "/cart/insert";
	@Declare(NAME = "INDEX", TODO = "购物车点击“去结算”")
	public static final String INDEX = SEVCIE + "/check/index";
	@Declare(NAME = "CARTSUM", TODO = "购物车总数")
	public static final String CARTSUM = SEVCIE + "/cart/sum";
	@Declare(NAME = "UPDATA", TODO = "更新购物车")
	public static final String UPDATA = SEVCIE + "/cart/update";
	@Declare(NAME = "DELETE", TODO = "删除购物车一条数据")
	public static final String DELETE = SEVCIE + "/cart/delete";
	@Declare(NAME = "LOGIN", TODO = "用户登录")
	public static final String LOGIN = SEVCIE + "/user/login";
	@Declare(NAME = "ADDR", TODO = "用户地址信息")
	public static final String ADDR = SEVCIE + "/check/addr";
	@Declare(NAME = "DELETEALL", TODO = "删除购物车全部商品")
	public static final String DELETEALL = SEVCIE + "/cart/deleteall";
	@Declare(NAME = "CONFIRM", TODO = "订单确认")
	public static final String CONFIRM = SEVCIE + "/order/confirm";
	@Declare(NAME = "CREATE", TODO = "订单确认")
	public static final String CREATE = SEVCIE + "/order/create";
	@Declare(NAME = "REGION", TODO = "地区显示")
	public static final String REGION = SEVCIE + "/check/region";
	@Declare(NAME = "ADDRLIST", TODO = "收货地址列表")
	public static final String ADDRLIST = SEVCIE + "/check/addrlist";
	@Declare(NAME = "NEWADDRST", TODO = "新增收货地址")
	public static final String NEWADDRST = SEVCIE + "/check/addrinsert";
	@Declare(NAME = "UPDATAADDRST", TODO = "修改收货地址")
	public static final String UPDATAADDRST = SEVCIE + "/check/addrupdate";
	@Declare(NAME = "ADDRSELECT", TODO = "选择收货地址")
	public static final String ADDRSELECT = SEVCIE + "/check/addrselect";
	@Declare(NAME = "ORDER", TODO = "订单管理")
	public static final String ORDER = SEVCIE + "/member/order";
	@Declare(NAME = "CANCEL", TODO = "订单取消接口")
	public static final String CANCEL = SEVCIE + "/member/cancel";
	@Declare(NAME = "PASSWORD", TODO = "会员密码修改")
	public static final String PASSWORD = SEVCIE + "/user/password";
	@Declare(NAME = "SPECIAL", TODO = "品牌特卖页面")
	public static final String SPECIAL = SEVCIE + "/list/special";
	@Declare(NAME = "REGESTER", TODO = "用户注册请求接口")
	public static final String REGESTER = SEVCIE + "/user/register";
	@Declare(NAME = "SMS", TODO = "注册页面调取请求手机验证码接口")
	public static final String SMS = SEVCIE + "/user/sms";
	@Declare(NAME = "SPECPRODUCT", TODO = "商品详情中，点击选择尺码、颜色后请求的接口，返回选中商品的Product_id")
	public static final String SPECPRODUCT = SEVCIE + "/goods/specproduct";
	@Declare(NAME = "SECKILL", TODO = "用户秒杀专区接口")
	public static final String SECKILL = SEVCIE + "/list/seckill";
	@Declare(NAME = "ALIPAY", TODO = "订单支付接口（停用）")
	public static final String ALIPAY = SEVCIE + "/pay/alipay";
	@Declare(NAME = "PAYORDER", TODO = "20150330日更改的订单列表详情，去支付的接口。")
	public static final String PAYORDER = SEVCIE + "/pay/payorder";
	@Declare(NAME = "FORGOTPASSWORD", TODO = "忘记密码调用的请求信息接口")
	public static final String FORGOTPASSWORD = SEVCIE + "/user/forgotpassword";
	@Declare(NAME = "RESETPWD", TODO = "忘记密码调用的重置密码")
	public static final String RESETPWD = SEVCIE + "/user/resetpwd";
	@Declare(NAME = "CHANGEMOBILE", TODO = "更换手机号码接口")
	public static final String CHANGEMOBILE = SEVCIE + "/member/changemobile";
	@Declare(NAME = "PEANTINDEX", TODO = "登录手机后获取花生")
	public static final String PEANTINDEX = SEVCIE + "/member/index";
	@Declare(NAME = "ADDRDEL", TODO = "删除地址")
	public static final String ADDRDEL = SEVCIE + "/check/addrdel";
	@Declare(NAME = "ORDERINFO", TODO = "订单信息")
	public static final String ORDERINFO = SEVCIE + "/member/orderinfo";
	@Declare(NAME = "POINT", TODO = "剩余花生")
	public static final String POINT = SEVCIE + "/order/point";
	@Declare(NAME = "FEEDBACK", TODO = "留言")
	public static final String FEEDBACK = SEVCIE + "/member/feedback";
	@Declare(NAME = "ADDRDEF", TODO = "默认收货地址")
	public static final String ADDRDEF = SEVCIE + "/check/addrdef";
	@Declare(NAME = "GOODSID", TODO = "将product 变更为goodsId")
	public static final String GOODSID = SEVCIE + "/goods/goodsId";
	@Declare(NAME = "CATINDEX", TODO = "分类页面的接口")
	public static final String CATINDEX = SEVCIE + "/cat/index";
	@Declare(NAME = "COMMENT", TODO = "消息")
	public static final String COMMENT = SEVCIE + "/member/comment";
	@Declare(NAME = "CLEARCOMMENT", TODO = "清除消息")
	public static final String CLEARCOMMENT = SEVCIE + "/member/clearcomment";
	@Declare(NAME = "READCOMMENT", TODO = "读消息")
	public static final String READCOMMENT = SEVCIE + "/member/readcomment";
	@Declare(NAME = "DELCOMMENT", TODO = "删除一条消息")
	public static final String DELCOMMENT = SEVCIE + "/member/delcomment";
	@Declare(NAME = "CATLIST", TODO = "分类页面")
	public static final String CATLIST = SEVCIE + "/cat/list";
	@Declare(NAME = "BRANDLIST", TODO = "品牌页面")
	public static final String BRANDLIST = SEVCIE + "/brand/list";
	@Declare(NAME = "GOODSINFO", TODO = "商品信息")
	public static final String GOODSINFO = SEVCIE + "/goods/goodsinfo";
	@Declare(NAME = "BRANDINDEX", TODO = "商品！！！")
	public static final String BRANDINDEX = SEVCIE + "/brand/index";
	@Declare(NAME = "ADVERTINDEX", TODO = "活动专题。")
	public static final String ADVERTINDEX = SEVCIE + "/advert/index";

}
