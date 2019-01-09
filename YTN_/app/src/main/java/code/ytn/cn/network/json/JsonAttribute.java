package code.ytn.cn.network.json;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.ANNOTATION_TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface JsonAttribute {
	String USE_DEFAULT_NAME = "";

	String value() default USE_DEFAULT_NAME;

	String comment() default USE_DEFAULT_NAME;

	boolean cascade() default false;
}
