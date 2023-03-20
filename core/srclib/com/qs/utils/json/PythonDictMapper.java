package com.qs.utils.json;

public class PythonDictMapper {
    public static int getInteger(PythonDict pythonDict, String key, int value) {
        Object val = pythonDict.get(key);
        if (val == null)
            return value;
        if (val instanceof Number)
            return ((Number) val).intValue();
        else
            return value;
    }

    public static long getLong(PythonDict pythonDict, String key, long value) {
        Object val = pythonDict.get(key);
        if (val == null)
            return value;
        if (val instanceof Number)
            return ((Number) val).longValue();
        else
            return value;
    }

    public static float getFloat(PythonDict pythonDict, String key, float value) {
        Object val = pythonDict.get(key);
        if (val == null)
            return value;
        if (val instanceof Number)
            return ((Number) val).floatValue();
        else
            return value;
    }

    public static double getDouble(PythonDict pythonDict, String key, double value) {
        Object val = pythonDict.get(key);
        if (val == null)
            return value;
        if (val instanceof Number)
            return ((Number) val).doubleValue();
        else
            return value;
    }

    public static String getString(PythonDict pythonDict, String key, String value) {
        Object val = pythonDict.get(key);
        if (val == null)
            return value;
        if (val instanceof String)
            return (String) val;
        else
            return value;
    }

    public static boolean getBoolean(PythonDict pythonDict, String key, boolean value) {
        Object val = pythonDict.get(key);
        if (val == null)
            return value;
        if (val instanceof Boolean)
            return ((Boolean) val).booleanValue();
        else
            return value;
    }

//	public static <T> T[] getArray (PythonDict pyresp, String result) {
//		PythonArray o = (PythonArray)pyresp.get(result);
//		T[] t = (T[])new Object[o.size];
//		for (int i = 0; i < o.size; i++) {
//			t[i] = (T)o.get(i);
//		}
//
//		return t;
//	}

    public static int[] getIntArray(PythonDict pyresp, String result) {
        PythonArray o = (PythonArray) pyresp.get(result);
        int[] t = new int[o.size];
        for (int i = 0; i < o.size; i++) {
            Object o1 = o.get(i);
            if (o1 instanceof Number)
                t[i] = ((Number) o1).intValue();
        }
        return t;
    }
}