/**
 * 
 */
package com.frame.base.exception;

/**
 * <pre>
 * Title:BS层公用的异常
 * Description: 在service层受事务管理方法中抛出此异常后,会触发事务回滚
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
public class ServiceException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2471358151196064994L;

	// 业务提示信息
	private ServiceMessage serviceMessage = null;

	public ServiceException() {
		super();
	}

	public ServiceException(String message) {
		super(message);

	}

	public ServiceException(Throwable cause) {
		super(cause);
	}

	public ServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public ServiceException(ServiceMessage serviceMessage) {
		super();
		this.serviceMessage = serviceMessage;
	}

	public ServiceException(ServiceMessage serviceMessage, Throwable cause) {

		super(cause);
		this.serviceMessage = serviceMessage;
	}

	public ServiceMessage getServiceMessage() {
		return serviceMessage;
	}

	public void setServiceMessage(ServiceMessage serviceMessage) {
		this.serviceMessage = serviceMessage;
	}

}
