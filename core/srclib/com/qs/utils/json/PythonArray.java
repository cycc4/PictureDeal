package com.qs.utils.json;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

/**
 * Created by Administrator on 2017/7/5.
 */
public class PythonArray extends Array<Object> implements Json.Serializable {
    @Override
    public void write(Json json) {
        json.writeArrayStart("items");
        for (int i = 0; i < this.size; i++) {
            json.writeValue(this.get(i));
        }
        json.writeArrayEnd();
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        JsonValue jv = jsonData.child();
        while (jv != null) {
            switch (jv.type()) {
                case object:
                    this.add(json.readValue(PythonDict.class, jv));
                    break;
                case array:
                    this.add(json.readValue(PythonArray.class, jv));
                    break;
                case stringValue:
                    this.add(json.readValue(String.class, jv));
                    break;
                case doubleValue:
                    this.add(json.readValue(Double.class, jv));
                    break;
                case longValue:
                    this.add(json.readValue(Long.class, jv));
                    break;
                case booleanValue:
                    this.add(json.readValue(Boolean.class, jv));
                    break;
                case nullValue:
                    this.add(json.readValue(null, jv));
                    break;
                default:
                    this.add(json.readValue(null, jv));
                    break;
            }
            jv = jv.next();
        }
    }

    public <T> T addk(T value) {
        super.add(value);
        return value;
    }

    public float[] asFloatArray() {
        float[] floatArray = new float[this.size];
        for (int i = 0; i < floatArray.length; i++) {
            floatArray[i] = ((Number) get(i)).floatValue();
        }
        return floatArray;
    }

    public short[] asShortArray() {
        short[] floatarray = new short[this.size];
        for (int i = 0; i < floatarray.length; i++) {
            floatarray[i] = ((Number) get(i)).shortValue();
        }
        return floatarray;
    }

    public int[] asIntArray() {
        int[] res = new int[this.size];
        for (int i = 0; i < this.size; i++) {
            res[i] = ((Number) this.get(i)).intValue();
        }
        return res;
    }

    public long[] asLongArray() {
        long[] res = new long[this.size];
        for (int i = 0; i < this.size; i++) {
            res[i] = ((Number) this.get(i)).longValue();
        }
        return res;
    }

    public float getFloat(int i) {
        return ((Number) get(i)).floatValue();
//		return 0;
    }

    public PythonDict getd(int i) {
        return (PythonDict) get(i);
    }

    public PythonArray geta(int i) {
        return (PythonArray) get(i);
    }

    public String getString(int i) {
        return (String) get(i);
    }

    public static int[][] arrayToTable2D(PythonArray tableArray) {
        int[][] tab = new int[tableArray.size][];

        for (int i = 0; i < tableArray.size; i++) {
            PythonArray arr = (PythonArray) tableArray.get(i);
            tab[i] = new int[arr.size];
            for (int j = 0; j < arr.size; j++) {
                tab[i][j] = ((Number) arr.get(j)).intValue();
            }
        }
        return tab;
    }
}
