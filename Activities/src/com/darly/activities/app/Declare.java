package com.darly.activities.app;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


@Retention(RetentionPolicy.RUNTIME)
public @interface Declare {
	public String NAME();
	public String TODO();
}

