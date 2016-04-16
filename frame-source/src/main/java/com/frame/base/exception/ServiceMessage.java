/**
 * 
 */
package com.frame.base.exception;

import java.io.UnsupportedEncodingException;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;

/**
 * <pre>
 * Title:业务提示信息类
 * Description: 封装业务异常提示信息，供异常处理层统一处理，以更友好的方式在界面显示
 *              需要在servie_msg.properties中配置属性信息项，格式如下
 *              
 *              0001=当前用户{}被锁定，禁止删除
 *              //其中{}代表被替换项，在构造函数中传递参数数组用于替换这些信息项
 * </pre>
 * 
 * @author mengzx
 * @version 1.00.00
 * 
 *          <pre>
 * 修改记录
 *    修改后版本:     修改人：  修改日期:     修改内容:
 * </pre>
 */
public class ServiceMessage {

	private static Logger logger = LoggerFactory
			.getLogger(ServiceMessage.class);
	// 错误码
	private String msgCode;
	private String[] values;
	private String msgInfo;

	private static Configuration config = null;

	static {

		try {
			config = new PropertiesConfiguration("exception/");
		} catch (ConfigurationException e) {

			logger.error("读取配置文件bizMessage.properties失败,请检查文件是否存在!", e);
		}
		


	}

	/**
	 * 构造函数
	 * 
	 * @param msgCode
	 *            信息码
	 * @param values
	 *            参数数组，用于替换字符串中的{}
	 */
	public ServiceMessage(String msgCode, String... values) {

		this.msgCode = msgCode;

		this.values = values;
	}

	public String getMsgCode() {
		return msgCode;
	}

	public void setMsgCode(String msgCode) {
		this.msgCode = msgCode;
	}

	public String getMsgInfo() {

		msgInfo = config.getString(this.msgCode);

		if (!StringUtils.isBlank(msgInfo)) {

			FormattingTuple ft;
			try {
				ft = MessageFormatter.arrayFormat(
						new String(msgInfo.getBytes("ISO-8859-1"), "UTF-8"),
						values);
				this.msgInfo = ft.getMessage();
			} catch (UnsupportedEncodingException e) {

				logger.error("字符编码转换失败!",e);
			}
			
		}

		return msgInfo;
	}

}
