package com.darly.common;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import com.darly.app.Declare;

public class DeclareTag {
	public static String getTag(String name) {
		String tag = null;
		for (Field fiel : InterFaceAPP.class.getDeclaredFields()) {
			if (fiel.getName().equals(name)) {
				Annotation[] annotation = fiel.getAnnotations();
				if (annotation.length != 0) {
					for (Annotation annotations : annotation) {
						Declare declare = (Declare) annotations;
						tag = declare.TODO();
					}
				}
			}
		}
		return tag;
	}
}
