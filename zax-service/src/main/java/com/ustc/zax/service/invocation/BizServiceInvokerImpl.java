package com.ustc.zax.service.invocation;

import java.lang.reflect.Method;

import androidx.annotation.NonNull;

import com.ustc.zax.service.BizService;

/**
 * Time: 2023/7/22
 * Author: chengzhi@kuaishou.com
 */
public class BizServiceInvokerImpl implements BizServiceInvoker {
  private BizService mBizService;
  private BizService mDefault;

  @Override
  public void setDefaultProxy(@NonNull BizService defaultProxy) {
    mDefault = defaultProxy;
  }

  @Override
  public void setBizProxy(@NonNull BizService proxy) {
    mBizService = proxy;
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    if ("getProxy".equals(method.getName())) {
      return this;
    }

    if (mBizService != null) {
      return method.invoke(mBizService, args);
    }

    return method.invoke(mDefault, args);
  }
}
