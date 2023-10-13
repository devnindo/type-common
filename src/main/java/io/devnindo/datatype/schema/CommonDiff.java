package io.devnindo.datatype.schema;

import io.devnindo.datatype.json.JsonObject;

public final class CommonDiff
{
    private CommonDiff()
    {

    }
    public static final DataDiff<JsonObject> jsonObjDiff(JsonObject from$, JsonObject to$) {
        JsonObject merged = new JsonObject();
        JsonObject delta = new JsonObject();

        if (from$ == null)
            return new DataDiff<>(delta, to$);

        if (to$ == null)
            return new DataDiff<>(null, from$);


        if (from$.isEmpty())
            return new DataDiff<>(to$, from$);


        from$.forEach(entry$ -> {

            String key = entry$.getKey();
            Object fromVal = entry$.getValue();
            Object toVal = to$.getValue(key);

            if (fromVal != null && !fromVal.equals(toVal)) {
                merged.put(key, fromVal);
                delta.put(key, toVal);
            } else {
                merged.put(key, toVal);
            }
        });

        return new DataDiff<>(delta, merged);
    }
}
