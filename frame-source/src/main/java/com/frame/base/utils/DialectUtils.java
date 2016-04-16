package com.frame.base.utils;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.dialect.H2Dialect;
import org.hibernate.dialect.MySQL5InnoDBDialect;
import org.hibernate.dialect.Oracle10gDialect;
import org.hibernate.dialect.DB2Dialect;
import org.springframework.jdbc.support.JdbcUtils;

public class DialectUtils {

	/**
	 * 从DataSoure中取出connection, 根据connection的metadata中的jdbcUrl判断Dialect类型.
	 */
	public static String getDialect(DataSource dataSource) {
		// 从DataSource中取出jdbcUrl.
		String jdbcUrl = null;
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			if (connection == null) {
				throw new IllegalStateException(
						"Connection returned by DataSource [" + dataSource
								+ "] was null");
			}
			jdbcUrl = connection.getMetaData().getURL();
		} catch (SQLException e) {
			throw new RuntimeException("Could not get database url", e);
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
				}
			}
		}

		// 根据jdbc url判断dialect
		if (StringUtils.contains(jdbcUrl, ":h2:")) {
			return H2Dialect.class.getName();
		} else if (StringUtils.contains(jdbcUrl, ":mysql:")) {
			return MySQL5InnoDBDialect.class.getName();
		} else if (StringUtils.contains(jdbcUrl, ":oracle:")) {
			return Oracle10gDialect.class.getName();
		} else if (StringUtils.contains(jdbcUrl, ":db2:")) {
			return DB2Dialect.class.getName();
		} else {
			throw new IllegalArgumentException("Unknown Database of " + jdbcUrl);
		}
	}
	
	/**
	 * 从DataSoure中取出connection, 获取数据库类型
	 */
	public static String getDataBaseType(DataSource dataSource) {
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			if (conn == null) {
				throw new IllegalStateException("Connection returned by DataSource [" + conn + "] was null");
			}
		} catch (SQLException e) {
			throw new RuntimeException("Could not get database url", e);
		}
		return getDataBaseType(conn);
	}
	
	public static String getDataBaseType(Connection conn) {
		// 从Connection中取出jdbcUrl.
		String jdbcUrl = null;
		try {
			jdbcUrl = conn.getMetaData().getURL();
		} catch (SQLException e) {
			throw new RuntimeException("Could not get database url", e);
		} finally {
			JdbcUtils.closeConnection(conn);
		}
	
		// 根据jdbc url判断dialect
		if (StringUtils.contains(jdbcUrl, ":h2:")) {
			return "h2";
		} else if (StringUtils.contains(jdbcUrl, ":mysql:")) {
			return "mysql";
		} else if (StringUtils.contains(jdbcUrl, ":oracle:")) {
			return "oracle";
		} else if (StringUtils.contains(jdbcUrl, ":db2:")) {
			return "db2";
		} else {
			throw new IllegalArgumentException("Unknown Database of " + jdbcUrl);
		}
	}
}
