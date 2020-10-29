package org.apache.ibatis.executor.resultset;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.apache.ibatis.cursor.Cursor;

/**
 * @author Clinton Begin
<<<<<<< HEAD
 * @author wangheng
 * @date 2019/10/14
=======
 * @date 20200713
>>>>>>> 09f58344bc35698fde892c59c75df84049fbbcdc
 */
public interface ResultSetHandler {

  <E> List<E> handleResultSets(Statement stmt) throws SQLException;

  <E> Cursor<E> handleCursorResultSets(Statement stmt) throws SQLException;

  void handleOutputParameters(CallableStatement cs) throws SQLException;

}
