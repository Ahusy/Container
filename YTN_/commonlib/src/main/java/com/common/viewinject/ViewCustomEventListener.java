package com.common.viewinject;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public interface ViewCustomEventListener {
	void setEventListener(Object handler, ViewFinder finder, Annotation annotation, Method method);
}
