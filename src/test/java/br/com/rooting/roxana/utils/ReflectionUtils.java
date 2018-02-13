package br.com.rooting.roxana.utils;

import java.lang.reflect.Modifier;

public final class ReflectionUtils {

	private ReflectionUtils() {}
	
	public static boolean isPackagePrivate(int modifier) {
		if(!Modifier.isPrivate(modifier) && 
				!Modifier.isProtected(modifier) && 
				!Modifier.isPublic(modifier)) {
			return true;
		}
		return false;
	}
	
}