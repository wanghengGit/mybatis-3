package org.apache.ibatis.transaction;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Wraps a database connection.
 * Handles the connection lifecycle that comprises: its creation, preparation, commit/rollback and close.
 *
 * @author Clinton Begin
 * @author wangheng
 * @date 2019/10/09
 */
public interface Transaction {

  Connection getConnection() throws SQLException;

  void commit() throws SQLException;

  void rollback() throws SQLException;

  void close() throws SQLException;

  Integer getTimeout() throws SQLException;

}
