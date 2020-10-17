/**
 *    Copyright 2009-2015 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.apache.ibatis.datasource;

import java.util.Properties;
import javax.sql.DataSource;

/**
 * @author Clinton Begin
 * @date 20200411
 * 工厂方法模式
 * 工厂接口（Factory）：DataSourceFactory扮演工厂接口角色
 * 具体工厂类（ConcreteFactory）：UnpooledDataSourceFactory和PooledDataSourceFactory
 * 产品接口角色（Product）：java.sql.DataSource
 * 具体产品类角色（ConcreteFactory）：UnpooledDataSource和PooledDataSource
 */
public interface DataSourceFactory {

  void setProperties(Properties props);

  DataSource getDataSource();

}
