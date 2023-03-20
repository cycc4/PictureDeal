package com.qs.utils.json;

import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;

/**
 * Created by Administrator on 2017/7/5.
 */
public class PythonDict extends ArrayMap<String, Object> implements Json.Serializable {
    @Override
    public void write(Json json) {
//        json.writeObjectStart();
        for (String k : this.keys()) {
            Object value = this.<Object>get(k);
            if (value instanceof PythonArray) {
                PythonArray pa = (PythonArray) value;
                json.writeArrayStart(k.toString());
                for (int i = 0; i < pa.size; i++) {
                    json.writeValue(pa.get(i));
                }
                json.writeArrayEnd();
            } else {
                json.writeValue(k.toString(), value);
            }
        }
//        json.writeObjectEnd();
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        JsonValue jv = jsonData.child();
        while (jv != null) {
            switch (jv.type()) {
                case object:
                    this.put(jv.name, json.readValue(PythonDict.class, jv));
                    break;
                case array:
                    this.put(jv.name, json.readValue(PythonArray.class, jv));
                    break;
                case stringValue:
                    this.put(jv.name, json.readValue(String.class, jv));
                    break;
                case doubleValue:
                    this.put(jv.name, json.readValue(Double.class, jv));
                    break;
                case longValue:
                    this.put(jv.name, json.readValue(Long.class, jv));
                    break;
                case booleanValue:
                    this.put(jv.name, json.readValue(Boolean.class, jv));
                    break;
                case nullValue:
                    this.put(jv.name, json.readValue(null, jv));
                    break;
                default:
                    this.put(jv.name, json.readValue(null, jv));
                    break;
            }
            jv = jv.next();
        }
    }

//	public <T> void putdef(String key, T value, T def) {
//		if (value.equals(def))
//			;
//		else
//			super.put(key, value);
//	}
//
//	public <T> T putk(String key, T value) {
//		super.put(key, value);
//		return value;
//	}

//	public <T extends Object> T tget(String key) {
//		return (T) super.get(key);
//	}

//	public <T> T tget(String key, T o) {
//		Object o1 = super.get(key);
//		if (o1 != null)
//			return (T) o1;
//		else
//			return o;
//	}

    public PythonDict getd(String key) {
        Object o = this.get(key);
        if (o == null)
            return new PythonDict();
        if (o instanceof PythonArray) {
            if (((PythonArray) o).size == 0)
                return new PythonDict();
        }
        return (PythonDict) o;
    }

//	public PythonArray require(String key) {
//		return (PythonArray) this.get(key);
//	}

    public PythonArray geta(String key) {
        Object o = this.get(key);
        if (o == null)
            return new PythonArray();
        if (o instanceof PythonDict) {
            if (((PythonDict) o).size == 0)
                return new PythonArray();
        }
        return (PythonArray) o;
    }

    public float[] getFloatArray(String key){
        Object o=this.get(key);
        if(o instanceof PythonArray){
            return ((PythonArray)o).asFloatArray();
        }
        return new float[]{};
    }

    public int[] getIntArray(String key) {
        Object o = this.get(key);
        if (o instanceof PythonArray)
            return ((PythonArray) o).asIntArray();
        return new int[]{};
    }

    public long[] getLongArray(String key) {
        Object o = this.get(key);
        if (o instanceof PythonArray)
            return ((PythonArray) o).asLongArray();
        return new long[]{};
    }

    public String getString(String key) {
        if (containsKey(key))
            return (String) get(key);
        return null;
    }

    public float getFloat(String key) {
        if (containsKey(key)) return ((Number) get(key)).floatValue();
        return 0;
    }

    public int getInteger(String key) {
        if (containsKey(key)) return ((Number) get(key)).intValue();
        return 0;
    }

    public long getLong(String key) {
        if (containsKey(key)) return ((Number) get(key)).longValue();
        return 0;
    }

    public boolean getBoolean(String key) {
        return (Boolean) get(key);
    }

    public String getString(String key, String value) {
        Object o = get(key);
        if (o == null)
            return value;
        return (String) o;
    }

    public float getFloat(String key, float value) {
        Object o = get(key);
        if (o == null)
            return value;
        return ((Number) o).floatValue();
    }

    public double getDouble(String key, double value) {
        Object o = get(key);
        if (o == null)
            return value;
        return ((Number) o).doubleValue();
    }

    public boolean getBoolean(String key, boolean value) {
        Object o = get(key);
        if (o == null)
            return value;
        return (Boolean) o;
    }

    public int getInteger(String key, int value) {
        Object o = get(key);
        if (o == null)
            return value;
        return ((Number) o).intValue();
    }

    public long getLong(String key, long value) {
        Object o = get(key);
        if (o == null)
            return value;
        return ((Number) o).longValue();
    }


    public String toJsonStr() {
        return new Json(JsonWriter.OutputType.json).toJson(this);
    }


    public <T> T toJClass(Class<T> type) {
        return new Json().fromJson(type, toJsonStr());
    }
}
