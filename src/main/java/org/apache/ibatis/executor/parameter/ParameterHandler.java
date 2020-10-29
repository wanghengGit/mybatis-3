package org.apache.ibatis.executor.parameter;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * A parameter handler sets the parameters of the {@code PreparedStatement}.
 *
 * @author Clinton Begin
<<<<<<< HEAD
 * @date 2019/10/14
 * 处理查询结果
=======
 * @date 20200713
>>>>>>> 09f58344bc35698fde892c59c75df84049fbbcdc
 */
public interface ParameterHandler {

  Object getParameterObject();

  void setParameters(PreparedStatement ps)
      throws SQLException;

}
