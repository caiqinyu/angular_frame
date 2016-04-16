/**
 * <pre>
 * Title:title
 * Description: description
 * </pre>
 * 
 * @author caiqy caiqinyu@jd.com

 * 修改记录
 *    修改后版本:     修改人：  修改日期:     修改内容:
 * </pre>
 */
package com.frame.base.common;

import java.io.Serializable;

public class Pager implements Serializable{

	private static final long serialVersionUID = 7596682326124763825L;

	// 每页个数
	private int number;
	// 起始index
	private int start;
	// 总数
	private int totalItemCount;
	
	public Pager() {
		super();
	}
	
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public int getTotalItemCount() {
		return totalItemCount;
	}
	public void setTotalItemCount(int totalItemCount) {
		this.totalItemCount = totalItemCount;
	}
	
}
