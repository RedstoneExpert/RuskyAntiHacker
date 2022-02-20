package me.redstoneexpert.ruskyantihacker.nmsabstract;

import java.awt.*;
import java.lang.reflect.Field;

public class ReflectionUtils {

	public static Object getValue(Object o, String name) {
		try {
			Field f = o.getClass().getDeclaredField(name);
			f.setAccessible(true);
			return f.get(o);
		} catch (NoSuchFieldException | IllegalAccessException e) {
			e.printStackTrace();
			return null;
		}
	}
}
