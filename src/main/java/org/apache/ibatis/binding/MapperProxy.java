/**
 * Copyright 2009-2019 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.ibatis.binding;

import java.io.Serializable;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

import org.apache.ibatis.reflection.ExceptionUtil;
import org.apache.ibatis.session.SqlSession;

/**
 * @author Clinton Begin
 * @author Eduardo Macarron
 * @author kit
 * @date 20200905
 * 在mybatis中，通过MapperProxy动态代理咱们的dao， 也就是说，
 * 当咱们执行自己写的dao里面的方法的时候，其实是对应的mapperProxy在代理。
 * 那么，咱们就看看怎么获取MapperProxy对象吧
 * 代理模式，Mybatis实现的核心，比如MapperProxy、ConnectionLogger，用的jdk的动态代理
 *
 *  MapperProxy 是 通过 MapperProxyFactory 创建的。
 *  MapperProxy 实现 Mapper接口方法是委托 MapperMethod 执行的。
 */
public class MapperProxy<T> implements InvocationHandler, Serializable {

  private static final long serialVersionUID = -6424540398559729838L;
  private static final int ALLOWED_MODES = MethodHandles.Lookup.PRIVATE | MethodHandles.Lookup.PROTECTED
    | MethodHandles.Lookup.PACKAGE | MethodHandles.Lookup.PUBLIC;
  private static Constructor<Lookup> lookupConstructor;
  // 注意： 这个 SqlSession 实际上是 SqlSessionTemplate
  private final SqlSession sqlSession;
  private final Class<T> mapperInterface;
  private final Map<Method, MapperMethod> methodCache;

  public MapperProxy(SqlSession sqlSession, Class<T> mapperInterface, Map<Method, MapperMethod> methodCache) {
    this.sqlSession = sqlSession;
    this.mapperInterface = mapperInterface;
    this.methodCache = methodCache;
  }

  static {
    try {
      lookupConstructor = MethodHandles.Lookup.class.getDeclaredConstructor(Class.class, int.class);
    } catch (NoSuchMethodException e) {
      try {
        // Since Java 14+8
        lookupConstructor = MethodHandles.Lookup.class.getDeclaredConstructor(Class.class, Class.class, int.class);
      } catch (NoSuchMethodException e2) {
        throw new IllegalStateException("No known constructor found in java.lang.invoke.MethodHandles.Lookup.", e2);
      }
    }
    lookupConstructor.setAccessible(true);
  }

  /**
   * TODO MapperProxy在执行时会触发此方法
   * @param proxy
   * @param method
   * @param args
   * @return
   * @throws Throwable
   */
  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    try {
      // 如果调用的方法是 Object 种定义的方法，直接执行
      if (Object.class.equals(method.getDeclaringClass())) {
        return method.invoke(this, args);
      } else if (method.isDefault()) {
        return invokeDefaultMethod(proxy, method, args);
      }
    } catch (Throwable t) {
      throw ExceptionUtil.unwrapThrowable(t);
    }
    // 接口方法的调用都是通过 MapperMethod 来执行的
    final MapperMethod mapperMethod = cachedMapperMethod(method);
    //主要交给MapperMethod自己去管
    return mapperMethod.execute(sqlSession, args);
  }

  private MapperMethod cachedMapperMethod(Method method) {
    return methodCache.computeIfAbsent(method, k -> new MapperMethod(mapperInterface, method, sqlSession.getConfiguration()));
  }

  private Object invokeDefaultMethod(Object proxy, Method method, Object[] args)
    throws Throwable {
    final Class<?> declaringClass = method.getDeclaringClass();
    final Lookup lookup;
    if (lookupConstructor.getParameterCount() == 2) {
      lookup = lookupConstructor.newInstance(declaringClass, ALLOWED_MODES);
    } else {
      // SInce JDK 14+8
      lookup = lookupConstructor.newInstance(declaringClass, null, ALLOWED_MODES);
    }
    return lookup.unreflectSpecial(method, declaringClass).bindTo(proxy).invokeWithArguments(args);
  }
}
