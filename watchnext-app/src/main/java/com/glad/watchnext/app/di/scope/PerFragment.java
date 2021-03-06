package com.glad.watchnext.app.di.scope;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;

import javax.inject.Scope;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by Gautam Lad
 */
@Scope
@Documented
@Retention (RUNTIME)
public @interface PerFragment {
}
