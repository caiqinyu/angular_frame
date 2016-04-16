package com.frame.base.repository.jdbc.support;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.springframework.jdbc.core.PreparedStatementSetter;

import com.frame.base.exception.ServiceException;

/**
 * 功能描述：
 * 
 * @version 1.0
 * @since 1.0.1
 */
public class ArgTypePreparedStatementSetter implements PreparedStatementSetter {

	private final Object[] args;
	private final int[] types;

	public ArgTypePreparedStatementSetter(Object[] args, int[] types) {
		if ((args != null && types == null) || (args == null && types != null)
				|| (args != null && args.length != types.length)) {
			throw new ServiceException("ArgTypePreparedStatementSetter.ARG_NOT_MATCH");
		}
		this.args = args;
		this.types = types;
	}

	public void setValues(PreparedStatement ps) throws SQLException {
		if (this.args != null) {
			for (int i = 0; i < this.args.length; i++) {
				ps.setObject(i + 1, this.args[i], this.types[i]);
			}
		}
	}

}
