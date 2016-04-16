package com.frame.base.repository.jdbc.support;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.springframework.jdbc.core.PreparedStatementSetter;


/**
 * 功能描述：
 * 
 * @version 1.0
 * @since 1.0.1
 */
public class ArgPreparedStatementSetter implements PreparedStatementSetter {
	
	private final Object[] args;

	public ArgPreparedStatementSetter(Object[] args) {
		this.args = args;
	}

	public void setValues(PreparedStatement ps) throws SQLException {
		if (this.args != null) {
			for (int i = 0; i < this.args.length; i++) {
				ps.setObject(i + 1, this.args[i]);
			}
		}
	}

}
