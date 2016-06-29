package com.lianbi.mezone.b.httpresponse;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.ProgressBar;

import com.lianbi.mezone.b.ui.BaseActivity;
import com.zhy.http.okhttp.OkHttpUtils;

import java.io.File;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum OkHttpsImp {
	/**
	 * 单例实例
	 */
	SINGLEOKHTTPSIMP;
	/**
	 * 上下文
	 */
	private Context context;
	/**
	 * md5_key
	 */
	public static final  String md5_key      = "d2a57dc1d883fd21fb9951699df71cc7";
	private static final String SignType     = "MD5";
	private static final String inputCharset = "UTF-8";

	/**
	 * 获得实例
	 */
	public OkHttpsImp newInstance(Context ct) {
		context = ct;

		return SINGLEOKHTTPSIMP;
	}

	/**
	 * 获取绝对路径
	 */
	private String getAbsoluteUrl(String relativeUrl) {
		if (relativeUrl.startsWith("http://"))
			return relativeUrl;
		return API.HOST + relativeUrl;
	}

	/**
	 * 到店服务Url
	 * @param storeId    店铺ID
	 * @param methodName 路径
	 */
	private String getHttpUrl(String storeId, String methodName) {
		// return "http://172.16.103.153:8085/wcm/serviceMall/".concat(storeId)
		// .concat("/").concat(methodName);
		return API.TOSTORESERVICE
				+ "/wcm/serviceMall/".concat(storeId).concat("/")
				.concat(methodName);
	}

	/**
	 * get请求有progress
	 *
	 * @param myResultCallback 请求结果回调
	 * @param params           请求参数
	 * @param url              请求地址
	 */
	private void getProgressResponse(MyResultCallback<String> myResultCallback,
									 Map<String, String> params, String url) {
		myResultCallback.setContext(context);
		myResultCallback.setDialog("");
		OkHttpUtils.get().url(url).params(params).build()
				.execute(myResultCallback);
	}

	/**
	 * get请求有无progress
	 *
	 * @param myResultCallback 请求结果回调
	 * @param params           请求参数
	 * @param url              请求地址
	 */
	private void getNoProgressResponse(
			MyResultCallback<String> myResultCallback,
			Map<String, String> params, String url) {
		myResultCallback.setContext(context);
		OkHttpUtils.get().url(url).params(params).build()
				.execute(myResultCallback);
	}

	/**
	 * post请求有progress
	 *
	 * @param myResultCallback 请求结果回调
	 * @param params           请求参数
	 * @param url              请求地址
	 */
	private void postProgressResponse(
			MyResultCallback<String> myResultCallback,
			Map<String, String> params, String url) {
		myResultCallback.setContext(context);
		myResultCallback.setDialog("");
		OkHttpUtils.post().url(url).params(params).build()
				.execute(myResultCallback);
	}

	/**
	 * post请求有无progress
	 *
	 * @param myResultCallback 请求结果回调
	 * @param params           请求参数
	 * @param url              请求地址
	 */
	private void postNoProgressResponse(
			MyResultCallback<String> myResultCallback,
			Map<String, String> params, String url) {
		myResultCallback.setContext(context);
		OkHttpUtils.post().url(url).params(params).build()
				.execute(myResultCallback);
	}

	/**
	 * download下载请求有progress
	 *
	 * @param myResultCallback 请求结果回调
	 * @param filePath         下载文件存储位置
	 * @param fileName         文件名称
	 * @param url              请求地址
	 */
	private void downloadProgressResponse(
			MyDownLoadResultCallback<String> myResultCallback, String filePath,
			String fileName, String url, ProgressBar p) {
		myResultCallback.setDialog(p);
		myResultCallback.setContext(context);
		OkHttpUtils.get().url(url).build().execute(myResultCallback);
	}

	/**
	 * form 文件上传
	 *
	 * @param myResultCallback
	 */
	public void multiFileUpload(MyResultCallback<String> myResultCallback) {
		File file = new File(Environment.getExternalStorageDirectory(),
				"messenger_01.png");
		File file2 = new File(Environment.getExternalStorageDirectory(),
				"test1.txt");
		if (!file.exists()) {
			return;
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("username", "hgh");
		params.put("password", "123");

		String url = "" + "user!uploadFile";
		OkHttpUtils.post().addFile("mFile", "messenger_01.png", file)
				.addFile("mFile", "test1.txt", file2).url(url).params(params)//
				.build()//
				.execute(myResultCallback);
	}

	public void getUser(MyResultCallback<String> myResultCallback) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("type", "1");
		String url = getAbsoluteUrl("advert/getAdvert.do");
		getNoProgressResponse(myResultCallback, params, url);
	}

	public void downLoad(MyDownLoadResultCallback<String> myResultCallback,
						 ProgressBar p) {
		String url = "https://github.com/hongyangAndroid/okhttp-utils/blob/master/gson-2.2.1.jar?raw=true";
		downloadProgressResponse(myResultCallback, Environment
						.getExternalStorageDirectory().getAbsolutePath(),
				"gson-2.2.1.jar", url, p);
	}

	/**
	 * 获取广告
	 *
	 * @throws Exception
	 */
	public void getAdvert(String bannerCode,
						  MyResultCallback<String> myResultCallback, String serNum,
						  String source, String reqTime) throws Exception {
		Map<String, String> params = new HashMap<String, String>();
		params.put("bannerCode", bannerCode);
		params.put("reqTime", reqTime);
		params.put("serNum", serNum);
		params.put("source", source);
		String sign = getSign(md5_key, params);
		params.put("sign", sign);
		String url = getAbsoluteUrl(API.GETADVERT);
		postNoProgressResponse(myResultCallback, params, url);
	}

	/**
	 * 意见反馈
	 *
	 * @param myResultCallback
	 */
	public void postAddFeedBack(String contant, String title, String userId,
								MyResultCallback<String> myResultCallback) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("contant", contant);
		params.put("title", title);
		params.put("userId", userId);
		String url = getAbsoluteUrl(API.ADDFEEDBACK);
		postProgressResponse(myResultCallback, params, url);
	}

	/**
	 * 获取店铺员工
	 *
	 * @param myResultCallback
	 * @param businessId       店铺ID
	 */
	public void getSalesClerklist(MyResultCallback<String> myResultCallback,
								  String businessId) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("businessId", businessId);
		String url = getAbsoluteUrl(API.GETSALESCLERKLIST);
		getProgressResponse(myResultCallback, params, url);
	}

	/**
	 * 删除员工信息
	 *
	 * @param myResultCallback
	 * @param alesclerkId      员工ID String 类型 如：1,2
	 */
	public void postDelsalesClerkbyid(
			MyResultCallback<String> myResultCallback, int alesclerkId) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("alesclerkId", alesclerkId + "");
		String url = getAbsoluteUrl(API.DELSALESCLERKBYID);
		postProgressResponse(myResultCallback, params, url);
	}

	/**
	 *  新增员工信息
	 */
	public void postAddsalesClerk(MyResultCallback<String> myResultCallback,
								  String alesclerkName, String businessId, String alesclerkPhone,
								  String alesclerkJob, String salesclerkImg) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("alesclerkName", alesclerkName);
		params.put("businessId", businessId);
		params.put("alesclerkPhone", alesclerkPhone);
		params.put("salesclerkImg", salesclerkImg);
		params.put("alesclerkJob", alesclerkJob);
		String url = getAbsoluteUrl(API.ADDSALESCLERK);
		postProgressResponse(myResultCallback, params, url);
	}

	/**
	 *  修改员工信息
	 */
	public void postUpdateSalesClerkbyid(
			MyResultCallback<String> myResultCallback, String alesclerkName,
			int alesclerkId, String alesclerkPhone, String alesclerkJob,
			String salesclerkImg, String businessId) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("salesclerkImg", salesclerkImg);
		params.put("businessId", businessId + "");
		params.put("alesclerkId", alesclerkId + "");
		params.put("alesclerkPhone", alesclerkPhone);
		params.put("alesclerkName", alesclerkName);
		params.put("alesclerkJob", alesclerkJob);
		String url = getAbsoluteUrl(API.UPDATESALESCLERKBYID);
		postProgressResponse(myResultCallback, params, url);
	}

	/**
	 *  获取已有服务商城列表
	 */
	public void getMoreServerMall(MyResultCallback<String> myResultCallback,
								  String storeId) {
		Map<String, String> params = new HashMap<String, String>();
		// params.put("userId", userId);
		// String url = getAbsoluteUrl(API.GETMORESERVERMALL);
		// Map<String, String> params = new HashMap<String, String>();
		params.put("storeId", storeId);
		String url = getHttpUrl(storeId, "getDownloadApps");
		Log.i("tag", "服务商城------》" + url + "   " + storeId);
		postProgressResponse(myResultCallback, params, url);
	}

	/**
	 *  获取可供下载的服务商城列表
	 */
	public void getCandownloadServerMall(
			MyResultCallback<String> myResultCallback, String storeId) {
		Map<String, String> params = new HashMap<String, String>();
		// params.put("userId", userId);
		// String url = getAbsoluteUrl(API.GETMORESERVERMALL);
		// Map<String, String> params = new HashMap<String, String>();
		params.put("storeId", storeId);
		String url = getHttpUrl(storeId, "getAppsList");
		Log.i("tag", "服务商城------》" + url + "   " + storeId);
		postProgressResponse(myResultCallback, params, url);
	}

	/**
	 *  服务商城列表中服务下载
	 */
	public void getdownloadServer(MyResultCallback<String> myResultCallback,
								  String storeId, String serverId) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("storeId", storeId);
		params.put("appId", serverId);
		String url = getHttpUrl(storeId, "downloadApp");
		postProgressResponse(myResultCallback, params, url);
	}

	// /**
	// * 4.10 查询所有待审核的留言
	// *
	// */
	//
	// public void getshowMessages(MyResultCallback<String> myResultCallback,
	// String storeId) {
	// Map<String, String> params = new HashMap<String, String>();
	// params.put("storeId", "BDP200eWiZ16cbs041217820");
	// String url = getHttpUrl("BDP200eWiZ16cbs041217820", "downloadApp");
	// postProgressResponse(myResultCallback, params, url);
	// }
	//
	//

	/**
	 *  获取桌位信息
	 */
	public void getTableInfo(MyResultCallback<String> myResultCallback,
							 String storeId) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("storeId", storeId);
		String url = getHttpUrl(storeId, "getTables");
		postProgressResponse(myResultCallback, params, url);
	}

	/**
	 *  修改桌位状态
	 */
	public void getModifyTableStatus(MyResultCallback<String> myResultCallback,
									 String storeId, String tableIds, String tableStatus) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("storeId", storeId);
		params.put("tableIds", tableIds);
		params.put("tableStatus", tableStatus);
		String url = getHttpUrl(storeId, "modifyTableStatus");
		postProgressResponse(myResultCallback, params, url);
	}

	/**
	 *  修改营业状态
	 */
	public void getmodifyBusinessStatus(
			MyResultCallback<String> myResultCallback, String storeId,
			String tradeFlag) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("storeId", storeId);
		params.put("tradeFlag", tradeFlag);
		String url = getHttpUrl(storeId, "modifyTradeStatus");
		postProgressResponse(myResultCallback, params, url);
	}

	/**
	 *  删除桌位号
	 */
	public void getDeleteTableId(MyResultCallback<String> myResultCallback,
								 String storeId, String tableIds) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("storeId", storeId);
		params.put("tableIds", tableIds);
		String url = getHttpUrl(storeId, "removeTable");
		postProgressResponse(myResultCallback, params, url);
	}

	/**
	 *  添加桌位
	 */
	public void getAddTable(MyResultCallback<String> myResultCallback,
							String storeId, String tableName) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("storeId", storeId);
		params.put("tableName", tableName);
		String url = getHttpUrl(storeId, "addTable");
		postProgressResponse(myResultCallback, params, url);
	}

	/**
	 * 查询所有待审核留言
	 */

	public void getShowMessages(MyResultCallback<String> myResultCallback,
								String storeId) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("storeId", storeId);
		String url = getHttpUrl(storeId, "showMessages");
		postProgressResponse(myResultCallback, params, url);
	}

	/**
	 * 逐条(或批量)审核留言
	 */

	public void getAuditMessages(MyResultCallback<String> myResultCallback,
								 String storeId, String msgIds) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("storeId", storeId);
		params.put("msgIds", msgIds);
		String url = getHttpUrl(storeId, "auditMessages");
		postProgressResponse(myResultCallback, params, url);
	}

	/**
	 * 逐条(或批量)删除留言
	 */

	public void getDeleteMessages(MyResultCallback<String> myResultCallback,
								  String storeId, String msgIds) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("storeId", storeId);
		params.put("msgIds", msgIds);
		String url = getHttpUrl(storeId, "deleteMessages");
		postProgressResponse(myResultCallback, params, url);
	}

	/**
	 * 查询推送消息
	 */
	public void getPushMessages(MyResultCallback<String> myResultCallback,
								String storeId, String msgType) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("storeId", storeId);
		params.put("msgType", msgType);
		String url = getHttpUrl(storeId, "getPushMessages");
		postProgressResponse(myResultCallback, params, url);
	}

	/**
	 * 查询推送消息
	 */
	public void postChangeStatus(MyResultCallback<String> myResultCallback,
								 String storeId, String pushId) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("storeId", storeId);
		params.put("pushId", pushId);
		String url = getHttpUrl(storeId, "modifyPushMessage");
		postProgressResponse(myResultCallback, params, url);
	}

	/**
	 *  添加二维码显示
	 */
	public void getTableDetail(MyResultCallback<String> myResultCallback,
							   String storeId, String tableId) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("storeId", storeId);
		params.put("tableId", tableId);
		String url = getHttpUrl(storeId, "tableDetail");
		postProgressResponse(myResultCallback, params, url);
	}

	/**
	 * 获取服务商城列表
	 *
	 * @param myResultCallback
	 */
	public void getServiceMallList(MyResultCallback<String> myResultCallback) {
		Map<String, String> params = new HashMap<String, String>();
		String url = getAbsoluteUrl(API.GETSERVICEMALLLIST);
		getProgressResponse(myResultCallback, params, url);
	}

	/**
	 * 登陆
	 */
	public void postUserLogin(boolean isShowDialog,
							  MyResultCallback<String> myResultCallback, String serNum,
							  String source, String reqTime, String mobile, String password)
			throws Exception {
		Map<String, String> params = new HashMap<String, String>();
		params.put("mobile", mobile);
		params.put("password", password);
		params.put("reqTime", reqTime);
		params.put("serNum", serNum);
		params.put("source", source);
		String sign = getSign(md5_key, params);
		params.put("sign", sign);
		String url = getAbsoluteUrl(API.USErLOGIN);
		if (isShowDialog) {
			postProgressResponse(myResultCallback, params, url);

		} else {
			postNoProgressResponse(myResultCallback, params, url);

		}
	}

	/**
	 *  获取用户详细
	 */
	public void getUseByiId(MyResultCallback<String> myResultCallback,
							String serNum, String source, String reqTime, String userId)
			throws Exception {
		Map<String, String> params = new HashMap<String, String>();
		String url = getAbsoluteUrl(API.GETUSEBYID);
		params.put("reqTime", reqTime);
		params.put("serNum", serNum);
		params.put("source", source);
		params.put("userId", userId);
		String sign = getSign(md5_key, params);
		params.put("sign", sign);
		getProgressResponse(myResultCallback, params, url);
	}

	/**
	 *  修改用户信息
	 */
	public void postUpdateUseById(MyResultCallback<String> myResultCallback,
								  String serNum, String source, String reqTime, String userId,
								  String username, String userImage, String mobile, String urgent,
								  String urgentPhone) throws Exception {
		Map<String, String> params = new HashMap<String, String>();
		String url = getAbsoluteUrl(API.UPDATEUSEBYID);
		params.put("mobile", mobile);
		params.put("reqTime", reqTime);
		params.put("serNum", serNum);
		params.put("source", source);
		params.put("urgent", urgent);
		params.put("urgentPhone", urgentPhone);
		params.put("userId", userId);
		params.put("userImage", userImage);
		params.put("username", username);
		String sign = getSign(md5_key, params);
		params.put("sign", sign);
		postProgressResponse(myResultCallback, params, url);
	}

	/**
	 * 查询所有的会员卡等级
	 *
	 * @param myResultCallback
	 * @param businessId       门店id
	 */
	public void getAssociatorLevelList(
			MyResultCallback<String> myResultCallback, String businessId) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("businessId", businessId + "");
		String url = getAbsoluteUrl(API.GETASSOCIATORLEVELLIST);
		getProgressResponse(myResultCallback, params, url);
	}

	/**
	 *  查询门店下的所有会员
	 */
	public void getAssociator(MyResultCallback<String> myResultCallback,
							  String businessId) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("businessId", businessId + "");
		String url = getAbsoluteUrl(API.GETASSOCIATOR);
		getProgressResponse(myResultCallback, params, url);
	}

	/**
	 *  删除会员
	 */
	public void delAssociator(MyResultCallback<String> myResultCallback,
							  String id) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("id", id);
		String url = getAbsoluteUrl(API.DELASSOCIATOR);
		postProgressResponse(myResultCallback, params, url);
	}

	/**
	 *  修改会员
	 */
	public void updateAssociatorr(String id, String phone,
								  MyResultCallback<String> myResultCallback) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("id", id);
		params.put("phone", phone);
		String url = getAbsoluteUrl(API.UPDATEASSOCIATORr);
		postProgressResponse(myResultCallback, params, url);
	}

	/**
	 *  根据电话号码检索会员
	 */
	public void getAssociatorByPhone(MyResultCallback<String> myResultCallback,
									 String currentPageNum, String phone) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("currentPageNum", currentPageNum);
		params.put("phone", phone);
		params.put("pageSize", 20 + "");
		String url = getAbsoluteUrl(API.GETASSOCIATORBUPHONE);
		postProgressResponse(myResultCallback, params, url);
	}

	/**
	 *  查询门店下不同等级会员
	 */
	public void getAssociacorByBusinessAndLevel(String businessId,
												String currentPageNum, String level,
												MyResultCallback<String> myResultCallback) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("businessId", businessId + "");
		params.put("currentPageNum", currentPageNum + "");
		params.put("level", level + "");
		params.put("pageSize", 20 + "");
		String url = getAbsoluteUrl(API.GETASSOCIACORBYBUSINESSANDLEVEL);
		getNoProgressResponse(myResultCallback, params, url);
	}

	/**
	 * 获取验证码
	 */
	public void fetchVerifyCode(MyResultCallback<String> myResultCallback,
								String serNum, String source, String reqTime, String mobile)
			throws Exception {
		Map<String, String> params = new HashMap<String, String>();
		params.put("mobile", mobile + "");
		params.put("reqTime", reqTime);
		params.put("serNum", serNum);
		params.put("source", source);
		// params.put("type", type);
		String sign = getSign(md5_key, params);
		params.put("sign", sign);
		String url = getAbsoluteUrl(API.SENDCODE);
		getProgressResponse(myResultCallback, params, url);
	}

	/**
	 * 获取验证码
	 */
	public void fetchVerifyCodeRaF(String sendType,
								   MyResultCallback<String> myResultCallback, String serNum,
								   String source, String reqTime, String mobile) throws Exception {
		Map<String, String> params = new HashMap<String, String>();
		params.put("mobile", mobile + "");
		params.put("reqTime", reqTime);
		params.put("sendType", sendType);
		params.put("serNum", serNum);
		params.put("source", source);
		// params.put("type", type);
		String sign = getSign(md5_key, params);
		params.put("sign", sign);
		String url = getAbsoluteUrl(API.SENDCODE);
		getProgressResponse(myResultCallback, params, url);
	}

	/**
	 * 注册
	 */
	public void registerUser(MyResultCallback<String> myResultCallback,
							 String serNum, String source, String reqTime, String mobile,
							 String password, String qrcode) throws Exception {
		Map<String, String> params = new HashMap<String, String>();
		params.put("mobile", mobile);
		params.put("password", password);
		params.put("qrcode", qrcode);
		params.put("reqTime", reqTime);
		params.put("serNum", serNum);
		params.put("source", source);
		String sign = getSign(md5_key, params);
		params.put("sign", sign);
		String url = getAbsoluteUrl(API.REGISTERUSER);
		postProgressResponse(myResultCallback, params, url);
	}

	/**
	 * 忘记密码
	 */
	public void forgetPassword(String serNum, String source, String reqTime,
							   MyResultCallback<String> myResultCallback, String mobile,
							   String password, String qrcode) throws Exception {
		Map<String, String> params = new HashMap<String, String>();
		params.put("mobile", mobile);
		params.put("password", password);
		params.put("qrcode", qrcode);
		params.put("reqTime", reqTime);
		params.put("serNum", serNum);
		params.put("source", source);
		String sign = getSign(md5_key, params);
		params.put("sign", sign);
		String url = getAbsoluteUrl(API.FORGETPWD);
		postProgressResponse(myResultCallback, params, url);
	}

	/**
	 *  修改会员卡等级
	 */
	public void postUpdateAssociatorLevel(String levelId, String levelName,
										  String bDiscount, String price, String levelDetail,
										  MyResultCallback<String> myResultCallback) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("levelId", levelId);
		params.put("levelName", levelName);
		params.put("bDiscount", bDiscount);
		params.put("price", price);
		params.put("levelDetail", levelDetail);
		String url = getAbsoluteUrl(API.UPDATEASSOCIATORLEVEL);
		postProgressResponse(myResultCallback, params, url);
	}

	/**
	 *  开启关闭会员卡
	 */
	public void postOpenOrCloseLevel(String levelId, String status,
									 MyResultCallback<String> myResultCallback) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("levelId", levelId);
		params.put("status", status);
		String url = getAbsoluteUrl(API.OPENORCLOSELEVEL);
		postProgressResponse(myResultCallback, params, url);
	}

	/**
	 *  显示门店信息
	 */
	public void getMessageList(boolean isShow, String businessId,
							   int currentPageNum, int pageSize,
							   MyResultCallback<String> myResultCallback) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("currentPageNum", currentPageNum + "");
		params.put("pageSize", pageSize + "");
		params.put("businessId", businessId);
		String url = getAbsoluteUrl(API.GETMESSAGELIST);
		if (isShow) {
			getProgressResponse(myResultCallback, params, url);

		} else {
			getNoProgressResponse(myResultCallback, params, url);

		}
	}

	/**
	 *  删除信息
	 */
	public void postDelMessage(String businessId,
							   MyResultCallback<String> myResultCallback) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("messageId", businessId);
		String url = getAbsoluteUrl(API.POSTDELMESSAGE);
		getProgressResponse(myResultCallback, params, url);
	}

	/**
	 *  修改信息状态
	 */
	public void updateMessage(String messageId,
							  MyResultCallback<String> myResultCallback) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("messageId", messageId);
		String url = getAbsoluteUrl(API.UPDATEMESSAGE);
		postNoProgressResponse(myResultCallback, params, url);
	}

	/**
	 *  删除货源
	 */
	public void postDelShop(String productSourceId,
							MyResultCallback<String> myResultCallback) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("productSourceId", productSourceId);
		String url = getAbsoluteUrl(API.DELETESHOP);
		postProgressResponse(myResultCallback, params, url);
	}

	/**
	 *  获取货源列表
	 */
	public void getProductSourceList(String cateId, int currentPageNum,
									 int pageSize, MyResultCallback<String> myResultCallback) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("cateId", cateId);
		params.put("currentPageNum", currentPageNum + "");
		params.put("pageSize", pageSize + "");
		String url = getAbsoluteUrl(API.GETPRODUCTSOURCELIST);
		getProgressResponse(myResultCallback, params, url);
	}

	/**
	 *  新增货源
	 */
	public void addProductSource(String image, String productSourceTitle,
								 String validEndDate, String vailidStartDate, String industryId,
								 String unit, String price, String businessId, String detail,
								 MyResultCallback<String> myResultCallback) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("image", image);
		params.put("productSourceTitle", productSourceTitle);
		params.put("vailidStartDate", vailidStartDate);
		params.put("validEndDate", validEndDate);
		params.put("industryId", industryId);
		params.put("unit", unit);
		params.put("price", price);
		params.put("businessId", businessId);
		params.put("detail", detail);
		String url = getAbsoluteUrl(API.ADDPRODUCTSOURCE);
		postProgressResponse(myResultCallback, params, url);
	}

	/**
	 * B端切换店铺
	 */
	public void changeBussinessOnApp(String serNum, String source,
									 String reqTime, String userId, String businessId,
									 MyResultCallback<String> myResultCallback) throws Exception {
		Map<String, String> params = new HashMap<String, String>();
		params.put("businessId", businessId);
		params.put("reqTime", reqTime);
		params.put("serNum", serNum);
		params.put("source", source);
		params.put("userId", userId);
		String sign = getSign(md5_key, params);
		params.put("sign", sign);
		String url = getAbsoluteUrl(API.CHANGEBUSSINESSONAPP);
		getProgressResponse(myResultCallback, params, url);
	}

	/**
	 *  审核会员申请状态
	 */
	public void updateApplyStatus(String associatorId, String status,
								  MyResultCallback<String> myResultCallback) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("associatorId", associatorId);
		params.put("status", status);
		String url = getAbsoluteUrl(API.UPDATEAPPLYSTATUS);
		postProgressResponse(myResultCallback, params, url);
	}

	/**
	 *  添加货源线下订单
	 */
	public void addProductSourceOrder(String productSrouceId,
									  String businessId, String buyBusinessId, String orderPrice,
									  String userName, String phone, String address, String amount,
									  String price, String num, MyResultCallback<String> myResultCallback) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("plantForm", "1");
		params.put("productSrouceId", productSrouceId);
		params.put("businessId", businessId);
		params.put("buyBusinessId", buyBusinessId);
		params.put("orderPrice", orderPrice);
		params.put("userName", userName);
		params.put("phone", phone);
		params.put("address", address);
		params.put("amount", amount);
		params.put("price", price);
		params.put("num", num);
		String url = getAbsoluteUrl(API.ADDPRODUCTSOURCEORDER);
		postProgressResponse(myResultCallback, params, url);
	}

	/**
	 *  扫一扫 (根据扫出来的编号获取相应的数据)
	 */
	public void getSweep(String code, MyResultCallback<String> myResultCallback) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("code", code);
		String url = getAbsoluteUrl(API.GETSWEEP);
		getProgressResponse(myResultCallback, params, url);
	}

	/**
	 *  获取购买的货源记录
	 */
	public void getPrductSourceOrderListBubusinessId(String businessId,
													 int currentPageNum, int pageSize,
													 MyResultCallback<String> myResultCallback) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("businessId", businessId);
		params.put("currentPageNum", currentPageNum + "");
		params.put("pageSize", pageSize + "");
		String url = getAbsoluteUrl(API.GETPRDUCTSOURCEORDERLISTBUBUSINESSID);
		getProgressResponse(myResultCallback, params, url);
	}

	/**
	 *  获取货源下单列表
	 */
	public void getPrductSourceOrderByBusiness(String businessId,
											   int currentPageNum, int pageSize,
											   MyResultCallback<String> myResultCallback) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("businessId", businessId);
		params.put("currentPageNum", currentPageNum + "");
		params.put("pageSize", pageSize + "");
		String url = getAbsoluteUrl(API.GETPRDUCTSOURCEORDERBYBUSINESS);
		getProgressResponse(myResultCallback, params, url);
	}

	/**
	 *  获取货源详细
	 */
	public void getPrductSourcedeById(String productSourceId,
									  MyResultCallback<String> myResultCallback) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("productSourceId", productSourceId);
		String url = getAbsoluteUrl(API.GETPRDUCTSOURCEDEBYID);
		getProgressResponse(myResultCallback, params, url);
	}

	/**
	 *  查看订单详细
	 */
	public void getOrderById(String orderId,
							 MyResultCallback<String> myResultCallback) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("orderId", orderId);
		String url = getAbsoluteUrl(API.GETORDERBYID);
		getProgressResponse(myResultCallback, params, url);
	}

	/**
	 *  获取店铺下的所有产品池数据
	 */
	public void getProductPoolByBusiness(String useId, String currentPageNum,
										 MyResultCallback<String> myResultCallback) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("userId", useId);
		params.put("currentPageNum", currentPageNum);
		params.put("pageSize", 20 + "");
		String url = getAbsoluteUrl(API.GETPRODUCTPOOLBYBUSINESS);
		getProgressResponse(myResultCallback, params, url);
	}

	/**
	 *  显示货源联系人列表
	 */
	public void getProductSourceContacts(String userId,
										 MyResultCallback<String> myResultCallback) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("userId", userId);
		String url = getAbsoluteUrl(API.GETPRODUCTSOURCECONTACTS);
		getProgressResponse(myResultCallback, params, url);
	}

	/**
	 *  新增货源联系人
	 */
	public void addProductSourceContacts(String userId, String img,
										 String phone, String name, String sourceName, String sourceDetail,
										 MyResultCallback<String> myResultCallback) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("userId", userId);
		params.put("img", img);
		params.put("phone", phone);
		params.put("name", name);
		params.put("sourceName", sourceName);
		params.put("sourceDetail", sourceDetail);
		String url = getAbsoluteUrl(API.ADDPRODUCTSOURCECONTACTS);
		postProgressResponse(myResultCallback, params, url);
	}

	/**
	 *  B端修改订单状态---接受订单2，拒绝订单4
	 */
	public void upDateOrderStatus(String orderId, String status,
								  MyResultCallback<String> myResultCallback) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("orderId", orderId);
		params.put("status", status);
		String url = getAbsoluteUrl(API.UPDATEORDERSTATUS);
		getProgressResponse(myResultCallback, params, url);
	}

	/**
	 *  修改店铺产品池状态－－－修改产品商品服务后会新增一条与该产品一样的商品服务
	 */
	public void updateProductPoolStatus(String productPoolId,
										MyResultCallback<String> myResultCallback) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("productPoolId", productPoolId);
		String url = getAbsoluteUrl(API.UPDATEPRODUCTPOOLSTATUS);
		postProgressResponse(myResultCallback, params, url);
	}

	/**
	 * 切换店铺 取得用户下的所有店铺
	 */
	public void getBusinessByUser(String userId, String serNum, String source,
								  String reqTime, MyResultCallback<String> myResultCallback)
			throws Exception {
		Map<String, String> params = new HashMap<String, String>();
		params.put("reqTime", reqTime);
		params.put("serNum", serNum);
		params.put("source", source);
		params.put("userId", userId);
		String sign = getSign(md5_key, params);
		params.put("sign", sign);
		String url = getAbsoluteUrl(API.GETBUSINESSBYUSER);
		getProgressResponse(myResultCallback, params, url);
	}

	/**
	 *  修改密码
	 */
	public void upDatePassword(String userId, String password,
							   String newPassword, String serNum, String source, String reqTime,
							   MyResultCallback<String> myResultCallback) throws Exception {
		Map<String, String> params = new HashMap<String, String>();
		params.put("newPassword", newPassword);
		params.put("password", password);
		params.put("reqTime", reqTime);
		params.put("serNum", serNum);
		params.put("source", source);
		params.put("userId", userId);
		String sign = getSign(md5_key, params);
		params.put("sign", sign);
		String url = getAbsoluteUrl(API.UPDATEPASSWORD);
		postProgressResponse(myResultCallback, params, url);
	}

	/**
	 *  新增店铺B端
	 */
	public void addBusinessByB(String businessName, String address,
							   String userId, String industryId, String license,
							   String contactName, String phone, String provinceId,
							   MyResultCallback<String> myResultCallback, String serNum,
							   String source, String reqTime) throws Exception {
		Map<String, String> params = new HashMap<String, String>();
		params.put("address", address);
		params.put("businessName", businessName);
		params.put("contactName", contactName);
		params.put("industryId", industryId);
		params.put("licenseUrl", license);
		params.put("phone", phone);
		params.put("provinceId", provinceId);
		params.put("reqTime", reqTime);
		params.put("serNum", serNum);
		params.put("source", source);
		params.put("userId", userId);
		String sign = getSign(md5_key, params);
		params.put("sign", sign);
		String url = getAbsoluteUrl(API.ADDBUSINESSBYB);
		postProgressResponse(myResultCallback, params, url);
	}

	/**
	 * 行业类别
	 *
	 * @param myResultCallback
	 * @throws Exception
	 */
	public void getIndustryListofB(String serNum, String source,
								   String reqTime, MyResultCallback<String> myResultCallback)
			throws Exception {
		Map<String, String> params = new HashMap<String, String>();
		params.put("reqTime", reqTime);
		params.put("serNum", serNum);
		params.put("source", source);
		String sign = getSign(md5_key, params);
		params.put("sign", sign);
		String url = getAbsoluteUrl(API.GETINDUSTRYLISTOFB);
		getProgressResponse(myResultCallback, params, url);
	}

	public void getProvinceList(String serNum, String source, String reqTime,
								MyResultCallback<String> myResultCallback) throws Exception {
		Map<String, String> params = new HashMap<String, String>();
		params.put("reqTime", reqTime);
		params.put("serNum", serNum);
		params.put("source", source);
		String sign = getSign(md5_key, params);
		params.put("sign", sign);
		String url = getAbsoluteUrl(API.GETPROVINCELIST);
		getProgressResponse(myResultCallback, params, url);
	}

	/**
	 *  修改店铺LOGO
	 */
	public void updateBusinessLogo(String serNum, String source,
								   String reqTime, String businessId, String logoUrl,
								   MyResultCallback<String> myResultCallback) throws Exception {
		Map<String, String> params = new HashMap<String, String>();
		params.put("businessId", businessId);
		params.put("logoUrl", logoUrl);
		params.put("reqTime", reqTime);
		params.put("serNum", serNum);
		params.put("source", source);
		String sign = getSign(md5_key, params);
		params.put("sign", sign);
		String url = getAbsoluteUrl(API.UPDATEBUSINESSLOGO);
		postProgressResponse(myResultCallback, params, url);
	}

	/**
	 *  修改店铺介绍
	 */
	public void updateBusinessIntroduce(String serNum, String source,
										String reqTime, String businessId, String introduce,
										String details, String updloadImageUrl, String deleteImageUrl,
										MyResultCallback<String> myResultCallback) throws Exception {
		Map<String, String> params = new HashMap<String, String>();
		params.put("businessId", businessId);
		params.put("deleteImageUrl", deleteImageUrl);
		params.put("details", details);
		params.put("serNum", serNum);
		params.put("source", source);
		params.put("reqTime", reqTime);
		params.put("introduce", introduce);
		params.put("updloadImageUrl", updloadImageUrl);
		String sign = getSign(md5_key, params);
		params.put("sign", sign);
		String url = getAbsoluteUrl(API.UPDATEBUSINESSINTRODUCE);
		postProgressResponse(myResultCallback, params, url);
	}

	/**
	 * @param deviceType       设备为一识别标识
	 * @param myResultCallback
	 * @throws Exception
	 */
	public void postPhoneClientId(String serNum, String source, String reqTime,
								  String userId, String businessId, String deviceNo,
								  String deviceType, MyResultCallback<String> myResultCallback)
			throws Exception {
		Map<String, String> params = new HashMap<String, String>();
		params.put("serNum", serNum);
		params.put("source", source);
		params.put("reqTime", reqTime);
		params.put("userId", userId);
		params.put("businessId", businessId);
		params.put("deviceNo", deviceNo);
		params.put("deviceType", deviceType);
		String sign = getSign(md5_key, params);
		params.put("sign", sign);
		String url = getAbsoluteUrl(API.POSTPHONECLIENTID);
		postNoProgressResponse(myResultCallback, params, url);
	}

	/**
	 *  修改店铺联系人
	 */
	public void updateBusinessContacts(String serNum, String source,
									   String reqTime, String businessId, String contactName,
									   MyResultCallback<String> myResultCallback) throws Exception {
		Map<String, String> params = new HashMap<String, String>();
		params.put("businessId", businessId);
		params.put("contactName", contactName);
		params.put("reqTime", reqTime);
		params.put("serNum", serNum);
		params.put("source", source);
		String sign = getSign(md5_key, params);
		params.put("sign", sign);
		String url = getAbsoluteUrl(API.UPDATEBUSINESSCONTACTS);
		postProgressResponse(myResultCallback, params, url);
	}

	/**
	 *  修改店铺联系电话
	 */
	public void updateBusinessPhone(String serNum, String source,
									String reqTime, String businessId, String phone,
									MyResultCallback<String> myResultCallback) throws Exception {
		Map<String, String> params = new HashMap<String, String>();
		params.put("businessId", businessId);
		params.put("phone", phone);
		params.put("reqTime", reqTime);
		params.put("serNum", serNum);
		params.put("source", source);
		String sign = getSign(md5_key, params);
		params.put("sign", sign);
		String url = getAbsoluteUrl(API.UPDATEBUSINESSPHONE);
		postProgressResponse(myResultCallback, params, url);
	}

	/**
	 *  修改店铺地址
	 */
	public void updateBusinessAddress(String serNum, String source,
									  String reqTime, String businessId, String address,
									  MyResultCallback<String> myResultCallback) throws Exception {
		Map<String, String> params = new HashMap<String, String>();
		params.put("address", address);
		params.put("businessId", businessId);
		params.put("reqTime", reqTime);
		params.put("serNum", serNum);
		params.put("source", source);
		String sign = getSign(md5_key, params);
		params.put("sign", sign);
		String url = getAbsoluteUrl(API.UPDATEBUSINESSADDRESS);
		postProgressResponse(myResultCallback, params, url);
	}

	/**
	 * 获取商铺详细信息
	 */
	public void getBusinessInfo(boolean isShow, String userId, String serNum,
								String source, String reqTime, String businessId,
								MyResultCallback<String> myResultCallback) throws Exception {
		Map<String, String> params = new HashMap<String, String>();
		params.put("businessId", businessId);
		params.put("reqTime", reqTime);
		params.put("serNum", serNum);
		params.put("source", source);
		params.put("userId", userId);
		String sign = getSign(md5_key, params);
		params.put("sign", sign);
		String url = getAbsoluteUrl(API.GETBUSINESSINFO);
		if (isShow) {
			postProgressResponse(myResultCallback, params, url);
		} else {
			postNoProgressResponse(myResultCallback, params, url);

		}
	}

	/**
	 * @param imageList   string 图片多个以英文','逗号拼接
	 * @param type        string 产品类型
	 * @param serviceType string 服务类型
	 * @param startTime   string 开始时间
	 * @param endTime     string 结束时间
	 * @param isPay       string 是否需要预支付
	 * @param productName string 服务名称
	 * @param detail      string 服务详细
	 * @param price       string 价格
	 * @param unit        string 单位
	 */
	public void addProductInfoByB(String businessId, String imageList,
								  String type, String serviceType, String startTime, String endTime,
								  String isPay, String productName, String detail, String price,
								  String unit, String timeType,
								  MyResultCallback<String> myResultCallback) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("imageList", imageList);
		params.put("businessId", businessId);
		params.put("type", type);
		params.put("serviceType", serviceType);
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		params.put("isPay", isPay);
		params.put("productName", productName);
		params.put("detail", detail);
		params.put("timeType", timeType);
		params.put("price", price);
		params.put("unit", unit);
		String url = getAbsoluteUrl(API.ADDPRODUCTINFOBYB);
		postProgressResponse(myResultCallback, params, url);
	}

	/**
	 *  获取店铺下的所有商品
	 */
	public void getProductListByBusiness(String businessId,
										 MyResultCallback<String> myResultCallback) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("businessId", businessId);
		String url = getAbsoluteUrl(API.GETPRODUCTLISTBYBUSINESS);
		getProgressResponse(myResultCallback, params, url);
	}

	/**
	 *  获取最近完成的十笔交易列表
	 */
	public void getTenOrderList(String businessId,
								MyResultCallback<String> myResultCallback) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("businessId", businessId);
		String url = getAbsoluteUrl(API.GETTENORDERLIST);
		getNoProgressResponse(myResultCallback, params, url);
	}

	/**
	 *  获取货源特惠
	 */
	public void getProductSourcePreference(String userId, String businessId,
										   String industryId, MyResultCallback<String> myResultCallback) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("userId", userId);
		params.put("businessId", businessId);
		params.put("industryId", industryId);
		String url = getAbsoluteUrl(API.GETPRODUCTSOURCEPREFERENCE);
		getNoProgressResponse(myResultCallback, params, url);
	}

	/**
	 *  获取银行卡
	 */
	public void getBankList(String md5_key, String accountNo, String product,
							String serNum, String source, String reqTime,
							MyResultCallback<String> myResultCallback) throws Exception {
		Map<String, String> params = new HashMap<String, String>();
		params.put("serNum", serNum);
		params.put("source", source);
		params.put("reqTime", reqTime);

		params.put("accountNo", accountNo);
		// params.put("storeNo", storeNo);
		params.put("product", product);

		String sign = getSign(md5_key, params);
		params.put("sign", sign);
		String url = getAbsoluteUrl(API.GETBANKLIST);
		getProgressResponse(myResultCallback, params, url);
	}

	/**
	 *  绑定银行卡
	 */
	public void addBank(String md5_key, String outerOrderId, String bankId,
						String bankName, String userId, String realName,
						String bankAccountNo, String phone, String payPasswd,
						String confirmPasswd, String product, String serNum, String source,
						String reqTime, MyResultCallback<String> myResultCallback)
			throws Exception {
		Map<String, String> params = new HashMap<String, String>();
		params.put("outerOrderId", outerOrderId);
		params.put("bankId", bankId);
		params.put("bankName", bankName);
		params.put("userId", userId);
		params.put("realName", realName);
		params.put("bankAccountNo", bankAccountNo);
		params.put("phone", phone);

		params.put("serNum", serNum);
		params.put("source", source);
		params.put("reqTime", reqTime);

		params.put("payPasswd", payPasswd);
		params.put("product", product);
		params.put("confirmPasswd", confirmPasswd);

		String sign = getSign(md5_key, params);
		params.put("sign", sign);
		String url = getAbsoluteUrl(API.ADDBANK);
		postNoProgressResponse(myResultCallback, params, url);
	}

	/**
	 *  修改银行卡密码
	 */
	public void updateBankPassword(String md5_key, String accountNo,
								   String product, String oldPassWd, String newPassWd,
								   String cofirmPassWd, String serNum, String source, String reqTime,
								   MyResultCallback<String> myResultCallback) throws Exception {
		Map<String, String> params = new HashMap<String, String>();
		params.put("accountNo", accountNo);
		params.put("product", product);
		params.put("oldPassWd", oldPassWd);
		params.put("newPassWd", newPassWd);
		params.put("cofirmPassWd", cofirmPassWd);

		params.put("serNum", serNum);
		params.put("source", source);
		params.put("reqTime", reqTime);
		String sign = getSign(md5_key, params);
		params.put("sign", sign);
		String url = getAbsoluteUrl(API.UPDATEBANKPASSWORD);
		postProgressResponse(myResultCallback, params, url);
	}

	/**
	 *  删除银行卡
	 */
	public void delBank(String md5_key, String outerOrderId, String accountNo,
						String passWd, String product, String serNum, String source,
						String reqTime, MyResultCallback<String> myResultCallback)
			throws Exception {
		Map<String, String> params = new HashMap<String, String>();

		params.put("outerOrderId", outerOrderId);
		params.put("accountNo", accountNo);
		params.put("passWd", passWd);
		params.put("product", product);

		params.put("serNum", serNum);
		params.put("source", source);
		params.put("reqTime", reqTime);

		String sign = getSign(md5_key, params);
		params.put("sign", sign);

		String url = getAbsoluteUrl(API.DELBANK);
		postNoProgressResponse(myResultCallback, params, url);
	}

	/**
	 *  获取银行列表
	 */
	public void getAllBank(String md5_key, String serNum, String source,
						   String reqTime, MyResultCallback<String> myResultCallback)
			throws Exception {
		Map<String, String> params = new HashMap<String, String>();
		params.put("reqTime", reqTime);
		params.put("serNum", serNum);
		params.put("source", source);
		String sign = getSign(md5_key, params);
		params.put("sign", sign);

		String url = getAbsoluteUrl(API.GETALLBANK);
		getProgressResponse(myResultCallback, params, url);
	}

	/**
	 *  获取否一开通过银行卡信息
	 */
	public void getIsTrue(String md5_key, String accountNo, String product,
						  String serNum, String source, String reqTime,
						  MyResultCallback<String> myResultCallback) throws Exception {
		Map<String, String> params = new HashMap<String, String>();
		params.put("accountNo", accountNo);
		params.put("product", product);
		params.put("reqTime", reqTime);
		params.put("serNum", serNum);
		params.put("source", source);
		// params.put("storeNo", storeNo);
		String sign = getSign(md5_key, params);
		params.put("sign", sign);
		String url = getAbsoluteUrl(API.GETBANKLIST);
		getNoProgressResponse(myResultCallback, params, url);
	}

	/**
	 *  获取经营总收入
	 */
	public void getCountByBusiness(String BusinessId, boolean isShow,
								   MyResultCallback<String> myResultCallback) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("businessId", BusinessId);
		String url = getAbsoluteUrl(API.GETCOUNTBYBUSINESS);
		if (isShow) {
			getProgressResponse(myResultCallback, params, url);
		} else {
			getNoProgressResponse(myResultCallback, params, url);
		}
	}

	/**
	 *  获取某天经营总收入
	 */
	public void getCountByDay(String BusinessId, String dateTime, String type,
							  MyResultCallback<String> myResultCallback) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("businessId", BusinessId);
		params.put("dateTime", dateTime);
		params.put("type", type);
		String url = getAbsoluteUrl(API.GETCOUNTBYDAY);
		getProgressResponse(myResultCallback, params, url);
	}

	/**
	 *  获取本年每月收入数据
	 */
	public void getAmountByMonth(String BusinessId,
								 MyResultCallback<String> myResultCallback) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("businessId", BusinessId);
		String url = getAbsoluteUrl(API.GETAMOUNTBYMONTH);
		getProgressResponse(myResultCallback, params, url);
	}

	/**
	 *  获取上周每日收入数据
	 */
	public void getAmountByWeek(String BusinessId,
								MyResultCallback<String> myResultCallback) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("businessId", BusinessId);
		String url = getAbsoluteUrl(API.GETAMOUNTBYWEEK);
		getProgressResponse(myResultCallback, params, url);
	}

	/**
	 *  获取线下经营总收入
	 */
	public void getOfflineCountByBusiness(String BusinessId,
										  MyResultCallback<String> myResultCallback) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("businessId", BusinessId);
		String url = getAbsoluteUrl(API.GETOFFLINECOUNTBYBUSINESS);
		getProgressResponse(myResultCallback, params, url);
	}

	/**
	 *  获取线上经营总收入
	 */
	public void getOnLineCountByBusiness(String BusinessId,
										 MyResultCallback<String> myResultCallback) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("businessId", BusinessId);
		String url = getAbsoluteUrl(API.GETONLINECOUNTBYBUSINESS);
		getProgressResponse(myResultCallback, params, url);
	}

	/**
	 *  获取上周的线上经营总收入
	 */
	public void getOnLineLastWeekCount(String BusinessId,
									   MyResultCallback<String> myResultCallback) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("businessId", BusinessId);
		String url = getAbsoluteUrl(API.GETONLINELASTWEEKCOUNT);
		getProgressResponse(myResultCallback, params, url);
	}

	/**
	 *  获取上周的线下经营总收入
	 */
	public void getOfflineLastWeekCount(String BusinessId,
										MyResultCallback<String> myResultCallback) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("businessId", BusinessId);
		String url = getAbsoluteUrl(API.GETOFFLINELASTWEEKCOUNT);
		getProgressResponse(myResultCallback, params, url);
	}

	/**
	 *  获取某月线下经营总收入
	 */

	public void getOffLineCountByMonth(String BusinessId, String datetime,
									   MyResultCallback<String> myResultCallback) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("businessId", BusinessId);
		params.put("datetime", datetime);
		String url = getAbsoluteUrl(API.GETOFFLINECOUNTBYMONTH);
		getProgressResponse(myResultCallback, params, url);
	}

	/**
	 *  获取某月线上经营总收入
	 */

	public void getOnLineCountByMonth(String BusinessId, String datetime,
									  MyResultCallback<String> myResultCallback) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("businessId", BusinessId);
		params.put("datetime", datetime);
		String url = getAbsoluteUrl(API.GETONLINECOUNTBYMONTH);
		getProgressResponse(myResultCallback, params, url);
	}

	/**
	 *  获取在线每天收入明细
	 */
	public void getDetailOnLine(String BusinessId, String datetime,
								MyResultCallback<String> myResultCallback) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("businessId", BusinessId);
		params.put("datetime", datetime);
		String url = getAbsoluteUrl(API.GETDETAILONLINE);
		getProgressResponse(myResultCallback, params, url);
	}

	/**
	 * 每天收入明细
	 */
	public void getDetailOffLine(String BusinessId, String datetime,
								 MyResultCallback<String> myResultCallback) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("businessId", BusinessId);
		params.put("datetime", datetime);
		String url = getAbsoluteUrl(API.GETDETAILOFFLINE);
		getProgressResponse(myResultCallback, params, url);
	}

	/**
	 * 每天不同时间段经营收入
	 */
	public void getOfflineCountByTwoHours(String BusinessId, String datetime,
										  MyResultCallback<String> myResultCallback) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("businessId", BusinessId);
		params.put("datetime", datetime);
		String url = getAbsoluteUrl(API.GETOFFLINECOUNTBYTWOHOURS);
		getProgressResponse(myResultCallback, params, url);
	}

	/**
	 * 每天不同时段经营收入
	 */
	public void getOnlineCountByTwoHours(String BusinessId, String datetime,
										 MyResultCallback<String> myResultCallback) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("businessId", BusinessId);
		params.put("datetime", datetime);
		String url = getAbsoluteUrl(API.GETONLINECOUNTBYTWOHOURS);
		getProgressResponse(myResultCallback, params, url);
	}

	/**
	 * 获取不同时间不同状态的订单
	 */
	public void getOrderListByBusinessIdDateStatu(String BusinessId,
												  String startTime, String endTime, String status,
												  String currentPageNum, MyResultCallback<String> myResultCallback) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("businessId", BusinessId);
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		params.put("status", status);
		params.put("currentPageNum", currentPageNum);
		params.put("pageSize", "20");
		String url = getAbsoluteUrl(API.GETORDERLISTBYBUSINESSIDDATESTATU);
		getProgressResponse(myResultCallback, params, url);
	}

	/**
	 * 提现
	 */
	public void withdraw(String md5_key, String outOrderId, String accountNo,
						 String storeNo, String amount, String product, String channel,
						 String serNum, String source, String reqTime, String passWd,
						 MyResultCallback<String> myResultCallback) throws Exception {
		Map<String, String> params = new HashMap<String, String>();
		params.put("outOrderId", outOrderId);
		params.put("accountNo", accountNo);
		params.put("storeNo", storeNo);
		params.put("amount", amount);
		params.put("product", product);
		params.put("channel", channel);
		params.put("passWd", passWd);
		params.put("serNum", serNum);
		params.put("source", source);
		params.put("reqTime", reqTime);
		String sign = getSign(md5_key, params);
		params.put("sign", sign);
		String url = getAbsoluteUrl(API.WITHDRAW);
		postProgressResponse(myResultCallback, params, url);
	}

	/**
	 * 订单明细
	 */
	public void getWithDrawByUserId(String md5_key, String accountNo,
									String storeNo, String currentPageNum, String pageSize,
									String serNum, String source, String reqTime,
									MyResultCallback<String> myResultCallback) throws Exception {
		Map<String, String> params = new HashMap<String, String>();
		params.put("accountNo", accountNo);
		params.put("storeNo", storeNo);
		params.put("currentPageNum", currentPageNum);
		params.put("pageSize", pageSize);

		params.put("serNum", serNum);
		params.put("source", source);
		params.put("reqTime", reqTime);
		String sign = getSign(md5_key, params);
		params.put("sign", sign);

		String url = getAbsoluteUrl(API.GETWITHDRAWBYUSERID);
		getProgressResponse(myResultCallback, params, url);
	}

	/**
	 * 忘记银行卡密码
	 */
	public void forgetPassword(String accountNo, String newPassWd,
							   String cofirmPassWd,
							   String serNum, String source, String reqTime,
							   MyResultCallback<String> myResultCallback) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("accountNo", accountNo);
		params.put("newPassWd", newPassWd);
		params.put("cofirmPassWd", cofirmPassWd);
		params.put("serNum", serNum);
		params.put("source", source);
		params.put("reqTime", reqTime);
		String url = getAbsoluteUrl(API.FORGETPASSWORD);
		postProgressResponse(myResultCallback, params, url);
	}

	/**
	 * 找回密码
	 *
	 * @throws Exception
	 */
	public void restPayPassWd(String md5_key,

							  String accountNo, String newPassWd, String cofirmPassWd,

							  String serNum, String source, String reqTime,
							  MyResultCallback<String> myResultCallback) throws Exception {
		Map<String, String> params = new HashMap<String, String>();
		params.put("accountNo", accountNo);
		params.put("newPassWd", newPassWd);
		params.put("cofirmPassWd", cofirmPassWd);
		params.put("serNum", serNum);
		params.put("source", source);
		params.put("reqTime", reqTime);
		String sign = getSign(md5_key, params);
		params.put("sign", sign);
		String url = getAbsoluteUrl(API.FINDPASSWORD);
		postProgressResponse(myResultCallback, params, url);
	}

	/**
	 * app升级
	 */
	public void getEdition(String serNum, String source, String reqTime,
						   String editionStr, MyResultCallback<String> myResultCallback)
			throws Exception {
		Map<String, String> params = new HashMap<String, String>();
		params.put("deviceType", "ANDROID");
		params.put("editionStr", editionStr);
		params.put("serNum", serNum);
		params.put("source", source);
		params.put("reqTime", reqTime);
		String sign = getSign(md5_key, params);
		params.put("sign", sign);
		String url = getAbsoluteUrl(API.GETEDITION);
		getProgressResponse(myResultCallback, params, url);
	}

	/**
	 * 发送验证码银行卡忘记密码
	 */
	public void sendUpdatePasswordCode(String md5_key, String accountNo,
									   String bankAccountNo, String product, String serNum, String source,
									   String reqTime, MyResultCallback<String> myResultCallback)
			throws Exception {
		Map<String, String> params = new HashMap<String, String>();
		params.put("accountNo", accountNo);
		params.put("bankAccountNo", bankAccountNo);
		params.put("product", product);
		params.put("serNum", serNum);
		params.put("source", source);
		params.put("reqTime", reqTime);
		String sign = getSign(md5_key, params);
		params.put("sign", sign);
		String url = getAbsoluteUrl(API.SENDUPDATEPASSWORDCODE);
		getProgressResponse(myResultCallback, params, url);
	}

	/***
	 * 校验短信验证码
	 *
	 * @throws Exception
	 */
	public void CheckUpdatePasswordCode(String md5_key, String smsCode,
										String phone, String serNum, String source, String reqTime,
										MyResultCallback<String> myResultCallback) throws Exception {
		Map<String, String> params = new HashMap<String, String>();
		params.put("smsCode", smsCode);
		params.put("phone", phone);
		params.put("serNum", serNum);
		params.put("source", source);
		params.put("reqTime", reqTime);
		String sign = getSign(md5_key, params);
		params.put("sign", sign);

		String url = getAbsoluteUrl(API.CHECKUPDATEPASSWORDCODE);
		getProgressResponse(myResultCallback, params, url);
	}

	/**
	 * 余额
	 */
	public void getBalance(String md5_key, String serNum, String source,
						   String reqTime, String accountNo, String storeNo,

						   MyResultCallback<String> myResultCallback) throws Exception {
		Map<String, String> params = new HashMap<String, String>();

		params.put("accountNo", accountNo);
		params.put("storeNo", storeNo);

		params.put("serNum", serNum);
		params.put("source", source);
		params.put("reqTime", reqTime);
		String sign = getSign(md5_key, params);
		params.put("sign", sign);
		String url = getAbsoluteUrl(API.YUE);
		getNoProgressResponse(myResultCallback, params, url);
	}

	/**
	 * 提现中金额
	 *
	 * @param serNum
	 * @param source
	 * @param reqTime
	 * @param accountNo
	 * @param storeNo
	 * @param myResultCallback
	 * @throws Exception
	 */
	public void getqueryStoreWithdraw(String md5_key, String accountNo,
									  String storeNo, String product, String serNum, String source,
									  String reqTime,

									  MyResultCallback<String> myResultCallback) throws Exception {
		Map<String, String> params = new HashMap<String, String>();

		params.put("accountNo", accountNo);
		params.put("storeNo", storeNo);
		params.put("product", product);

		params.put("serNum", serNum);
		params.put("source", source);
		params.put("reqTime", reqTime);
		String sign = getSign(md5_key, params);
		params.put("sign", sign);
		String url = getAbsoluteUrl(API.StoreWithdraw);
		getNoProgressResponse(myResultCallback, params, url);
	}

	/**
	 * @param amount string 金额
	 * @return data:{ code: true:code=200,false:code!=200 dataCode: number
	 * 返回非错误逻辑性异常code resultMsg: string 返回信息 totalCount : int 返回总条数
	 * data:{ urlCode:XXXXX } }
	 */
	public void sweepPay(String userId, String amount,
						 MyResultCallback<String> myResultCallback) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("userId", userId);
		params.put("businessId", BaseActivity.userShopInfoBean.getBusinessId());
		params.put("amount", amount);
		String url = getAbsoluteUrl(API.SWEEPPAY);
		getProgressResponse(myResultCallback, params, url);
	}

	/**
	 * @param sweepStr string 商户扫描获取的微信商户编码
	 * @param amount   string 金额
	 * @author kai.li
	 */
	public void pushCard(String userId, String sweepStr, String amount,
						 String orderId, MyResultCallback<String> myResultCallback) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("userId", userId);
		params.put("businessId", BaseActivity.userShopInfoBean.getBusinessId());
		params.put("sweepStr", sweepStr);
		params.put("amount", amount);
		params.put("orderId", orderId);
		String url = getAbsoluteUrl(API.PUSHCARD);
		getProgressResponse(myResultCallback, params, url);
	}

	/**
	 * 查询微信订单状态
	 *
	 * @param orderId string 订单号
	 * @return data:{ code: true:code=200,false:code!=200 dataCode: number
	 * 返回非错误逻辑性异常code resultMsg: string 返回信息 totalCount : int 返回总条数
	 * data:{ urlCode:XXXXX } }
	 */
	public void orderQuery(String orderId, String isLast,
						   MyResultCallback<String> myResultCallback) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("orderId", orderId);
		params.put("isLast", isLast);
		String url = getAbsoluteUrl(API.ORDERQUERY);
		getProgressResponse(myResultCallback, params, url);
	}

	/**
	 * APP订单明细
	 */
	public void getOrderInfoById(String serNum, String source, String reqTime,
								 String isValid, String sourceType, String md5_key,
								 String merchantSubCode, String currentPageNum, String pageSize,
								 MyResultCallback<String> myResultCallback) throws Exception {
		Map<String, String> params = new HashMap<String, String>();
		params.put("currentPageNum", currentPageNum);
		params.put("pageSize", pageSize);
		params.put("isValid", isValid);
		params.put("merchantSubCode", merchantSubCode);
		params.put("reqTime", reqTime);
		params.put("serNum", serNum);
		params.put("source", source);
		params.put("sourceType", sourceType);
		String sign = getSign(md5_key, params);
		params.put("sign", sign);
		String url = getAbsoluteUrl(API.OREDRINFO);
		getProgressResponse(myResultCallback, params, url);
	}

	/**
	 *每日收入明细
	 */
	public void getDataOrderInfoById(String serNum, String source,
									 String reqTime, String md5_key, String accountNo,
									 String settleDate, String currentPageNum, String pageSize,
									 MyResultCallback<String> myResultCallback) throws Exception {
		Map<String, String> params = new HashMap<String, String>();
		params.put("currentPageNum", currentPageNum);
		params.put("pageSize", pageSize);
		params.put("settleDate", settleDate);
		params.put("serNum", serNum);
		params.put("source", source);
		params.put("reqTime", reqTime);
		params.put("accountNo", accountNo);
		String sign = getSign(md5_key, params);
		params.put("sign", sign);
		String url = getAbsoluteUrl(API.EVERYDAYINCOME);
		getProgressResponse(myResultCallback, params, url);
	}

	/**
	 * 主页 收入明细
	 */
	public void getIncomeDetailById(String serNum, String source,
									String reqTime, String md5_key, String accountNo, String storeNo,
									String currentPageNum, String pageSize,
									MyResultCallback<String> myResultCallback) throws Exception {
		Map<String, String> params = new HashMap<String, String>();
		params.put("accountNo", accountNo);
		params.put("currentPageNum", currentPageNum);
		params.put("storeNo", storeNo);
		params.put("pageSize", pageSize);
		params.put("reqTime", reqTime);
		params.put("serNum", serNum);
		params.put("source", source);
		String sign = getSign(md5_key, params);
		params.put("sign", sign);
		String url = getAbsoluteUrl(API.TODAYDETAIL);
		getProgressResponse(myResultCallback, params, url);
	}

	/**
	 * 主页 当前店铺今日收入
	 */
	public void getTodayShopIncome(String md5_key, String serNum,
								   String source, String reqTime, String storeNo, String accountNo,
								   MyResultCallback<String> myResultCallback) throws Exception {
		Map<String, String> params = new HashMap<String, String>();
		params.put("accountNo", accountNo);
		params.put("reqTime", reqTime);
		params.put("serNum", serNum);
		params.put("source", source);
		params.put("storeNo", storeNo);

		String sign = getSign(md5_key, params);
		params.put("sign", sign);
		String url = getAbsoluteUrl(API.TODAYINCOME);
		getNoProgressResponse(myResultCallback, params, url);
	}

	/**
	 * 主页 当前店铺总收入
	 */
	public void getShopAllIncome(String md5_key, String serNum, String source,
								 String reqTime, String accountNo, String storeNo,
								 MyResultCallback<String> myResultCallback) throws Exception {
		Map<String, String> params = new HashMap<String, String>();
		params.put("accountNo", accountNo);
		params.put("storeNo", storeNo);
		params.put("serNum", serNum);
		params.put("source", source);
		params.put("reqTime", reqTime);

		String sign = getSign(md5_key, params);
		params.put("sign", sign);
		String url = getAbsoluteUrl(API.ALLINCOMENUM);
		getNoProgressResponse(myResultCallback, params, url);
	}

	/**
	 * 财务室 我的总收入
	 */
	public void getMyAccountIncome(String md5_key, String serNum,
								   String source, String reqTime, String accountNo,
								   MyResultCallback<String> myResultCallback) throws Exception {
		Map<String, String> params = new HashMap<String, String>();
		params.put("accountNo", accountNo);

		params.put("reqTime", reqTime);
		params.put("serNum", serNum);
		params.put("source", source);

		String sign = getSign(md5_key, params);
		params.put("sign", sign);
		String url = getAbsoluteUrl(API.MyAllIncome);
		getNoProgressResponse(myResultCallback, params, url);
	}

	/**
	 * 账户总额
	 *
	 * @param md5_key
	 * @param serNum
	 * @param source
	 * @param reqTime
	 * @param accountNo
	 * @param myResultCallback
	 * @throws Exception
	 */
	public void getMyAmount(String md5_key, String serNum, String source,
							String reqTime, String accountNo,
							MyResultCallback<String> myResultCallback) throws Exception {
		Map<String, String> params = new HashMap<String, String>();
		params.put("accountNo", accountNo);

		params.put("reqTime", reqTime);
		params.put("serNum", serNum);
		params.put("source", source);

		String sign = getSign(md5_key, params);
		params.put("sign", sign);
		String url = getAbsoluteUrl(API.MyAllIncome2);
		getNoProgressResponse(myResultCallback, params, url);
	}

	/**
	 * 店铺总额
	 *
	 * @param md5_key
	 * @param serNum
	 * @param source
	 * @param reqTime
	 * @param accountNo
	 * @param storeNo
	 * @param myResultCallback
	 * @throws Exception
	 */
	public void getShopAccount(String md5_key, String serNum, String source,
							   String reqTime, String accountNo, String storeNo,
							   MyResultCallback<String> myResultCallback) throws Exception {
		Map<String, String> params = new HashMap<String, String>();
		params.put("accountNo", accountNo);
		params.put("reqTime", reqTime);
		params.put("serNum", serNum);
		params.put("source", source);
		params.put("storeNo", storeNo);
		String sign = getSign(md5_key, params);
		params.put("sign", sign);
		String url = getAbsoluteUrl(API.SHOPACCOUNT);
		getNoProgressResponse(myResultCallback, params, url);
	}

	/**
	 * 用户今日收入
	 */
	public void queryAccountTodayIncome(String md5_key, String serNum,
										String source, String reqTime, String accountNo,

										MyResultCallback<String> myResultCallback) throws Exception {
		Map<String, String> params = new HashMap<String, String>();

		params.put("accountNo", accountNo);
		params.put("serNum", serNum);
		params.put("source", source);
		params.put("reqTime", reqTime);

		String sign = getSign(md5_key, params);
		params.put("sign", sign);
		String url = getAbsoluteUrl(API.QUERYACCOUNTTODAYINCOME);
		getProgressResponse(myResultCallback, params, url);
	}

	/**
	 * 签名方法
	 */
	private static String getSign(String md5_key, Map<String, String> dataMap)
			throws Exception {
		List<String> keyList = new ArrayList<String>(dataMap.keySet());
		Collections.sort(keyList);
		StringBuilder builder = new StringBuilder();
		for (String mapKey : keyList) {
			// builder.append(mapKey).append("=").append(dataMap.get(mapKey))
			// .append("&");
			if (!isChinese(dataMap.get(mapKey))) {
				builder.append(dataMap.get(mapKey));
			} else {
			}
		}
		// builder.append("key=").append(md5_key);
		builder.append(md5_key);
		Log.i("MD5", "MD5加密前-->" + builder);
		MessageDigest md5 = MessageDigest.getInstance(SignType);
		md5.update(builder.toString().getBytes(inputCharset));
		byte[] md5Bytes = md5.digest();
		StringBuffer hexValue = new StringBuffer();
		for (int i = 0; i < md5Bytes.length; i++) {
			int val = ((int) md5Bytes[i]) & 0xff;
			if (val < 16) {
				hexValue.append("0");
			}
			hexValue.append(Integer.toHexString(val));
		}
		Log.i("MD5", "MD5加密后-->" + hexValue);
		return hexValue.toString();
	}

	/**
	 * 判断是否有中文
	 */
	public static boolean isChinese(String str) {
		if (str.length() < str.getBytes().length) {
			return true;// 中文
		} else {
			return false;
		}
	}
}
