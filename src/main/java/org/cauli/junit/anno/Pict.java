package org.cauli.junit.anno;

import java.lang.annotation.*;

/**
 * Created with IntelliJ IDEA.
 * User: tianqing.wang
 * Date: 13-8-7
 * Time: 下午4:15
 * To change this template use File | Settings | File Templates.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
public @interface Pict {
    String value();
}
