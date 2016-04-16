package com.frame.base.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <pre>
 * Title:SQL工具类
 * Description: 提供SQL语句处理相关的操作方法。
 * 
 * </pre>
 * 
 * @author caiqy
 * @version 1.00.00
 */
public class SQLUtils {

	/**
	 * 根据查询列表SQL语句自动构造查询记录总数的SQL语句
	 * 
	 * @param strSQL
	 *            String
	 * @return String
	 */
	public static String buildCountSQL(Object strSQL) {

		StringBuffer countBuff = new StringBuffer();
		if (strSQL != null) {
			String sql = null;
			if (strSQL instanceof String) {
				sql = (String) strSQL;
			} else if (strSQL instanceof StringBuffer) {
				sql = ((StringBuffer) strSQL).toString();
			}
			if (containsDistinctKeywords(sql)) {

				// 查询字段
				String queryField = sql.substring(
						findStrPosition(sql, "distinct") + 8,
						findStrPosition(sql, "from")).trim();

				countBuff.append("select count(distinct ").append(queryField)
						.append(") ");
				countBuff.append(removeOrderBy(trimFrom(sql)));
			} else if (containsGroupByKeywords(sql)) {
				// 如果包含group by
				countBuff.append("select count(*) from ( ");
				countBuff.append(sql).append(" ) c_1");
			} else {
				countBuff.append("select count(*) ");
				countBuff.append(removeOrderBy(trimFrom(sql)));
			}

		}
		return countBuff.toString();
	}

	/**
	 * 取sql语句从"from"之后的字符串
	 * 
	 * @param sql
	 *            String
	 * @return String
	 */
	public static String trimFrom(String sql) {
		String patternString = "[Ff][Rr][Oo][Mm]";
		Pattern pattern = Pattern.compile(patternString);
		Matcher matcher = pattern.matcher(sql);

		return matcher.find() ? sql.substring(matcher.start()) : ""; // 后面的字符
	}

	/**
	 * 判断SQL语句中是否包含distinct关键字
	 * 
	 * @param sql
	 * @return
	 */
	public static boolean containsDistinctKeywords(String sql) {

		StringBuffer patternString = new StringBuffer();
		patternString.append("\\s*").append(buildRegexStr("select"))
				.append("\\s*").append(buildRegexStr("distinct"));

		Pattern pattern = Pattern.compile(patternString.toString());
		Matcher matcher = pattern.matcher(sql);

		return matcher.find() && matcher.start() == 0 ? true : false;
	}

	/**
	 * 判断SQL语句中是否包含group by 关键字
	 * 
	 * @param sql
	 * @return
	 */
	public static boolean containsGroupByKeywords(String sql) {
		String patternString = "\\s*[Gg][Rr][Oo][Uu][Pp]\\s+[Bb][Yy]\\s*";
		Pattern pattern = Pattern.compile(patternString);
		Matcher matcher = pattern.matcher(sql);

		return matcher.find();
	}

	/**
	 * 根据字符串生成正则表达式 比如where生成[Ww][Hh][Ee][Rr]的正则表达式
	 * 
	 * @param str
	 *            String
	 * @return String
	 */
	public static String buildRegexStr(String str) {

		// 转出大写
		String upperCaseStr = str.toUpperCase();

		char[] strArr = upperCaseStr.toCharArray();
		char[] regexArr = new char[str.length() * 4];
		for (int i = 0; i < strArr.length; i++) {
			regexArr[4 * i] = '[';
			regexArr[4 * i + 1] = strArr[i];
			regexArr[4 * i + 2] = (char) (strArr[i] + 32); // to lower case
			regexArr[4 * i + 3] = ']';
		}

		return String.copyValueOf(regexArr);
	}

	/**
	 * 替换不规则的order by 子句为" ORDER BY "
	 * 
	 * @param sql
	 *            String
	 * @return String
	 */
	public static String replaceOrderBy(String sql) {
		String patternString = "\\s*[Oo][Rr][Dd][Ee][Rr]\\s+[Bb][Yy]\\s*";
		Pattern pattern = Pattern.compile(patternString);
		Matcher matcher = pattern.matcher(sql);
		String wantStr = matcher.replaceAll(" ORDER BY "); // 后面的字符
		return wantStr;
	}

	/**
	 * 过滤 sql语句中的order by 子句
	 * 
	 * @param sql
	 * @return String
	 */
	public static String removeOrderBy(String sql) {
		String patternString = "\\sORDER\\sBY\\s[a-zA-Z0-9\\.\\_\\,\\s]+";
		Pattern pattern = Pattern.compile(patternString);
		Matcher matcher = pattern.matcher(replaceOrderBy(sql));
		String resultStr = matcher.replaceAll("");
		return resultStr;
	}

	/**
	 * 查找匹配字符串的位置
	 * 
	 * @param sql
	 * @param targetStr
	 * @return
	 */
	public static int findStrPosition(String sql, String targetStr) {
		String patternString = buildRegexStr(targetStr);
		Pattern pattern = Pattern.compile(patternString);
		Matcher matcher = pattern.matcher(sql);

		return matcher.find() ? matcher.start() : -1;
	}
}
