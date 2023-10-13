package io.devnindo.datatype.beanexample;


import io.devnindo.datatype.json.JsonArray;
import io.devnindo.datatype.json.JsonObject;
import io.devnindo.datatype.util.JsonUtil;

public final class DataSample {
    private DataSample() {
    }

    public static JsonArray addressArr() {
        return JsonUtil.tickedJsonArr(
                "[" +
                        "  {" +
                        "    city: `Dhaka`," +
                        "    road_list: [`Mirpur 10`, `Gulshan`]" +
                        "  }," +
                        "  {" +
                        "    city: `Barishal`," +
                        "    road_list: [ `Kachpur`, `Badlapur`]" +
                        "  }" +
                        "]");
    }

    public static JsonArray invalidAddressArr() {
        return JsonUtil.tickedJsonArr(
                "[" +
                        "  {" +
                        "    city: `Dhaka`," +
                        "    road_list: [`Mirpur 10`, `Gulshan`]" +
                        "  }," +
                        "  {" +
                        "    city: 123," +
                        "    road_list: [ `Kachpur`, `Badlapur`]" +
                        "  }," +
                        "  {" +
                        "    city: `Barishal`," +
                        "    road_list: [ `Kachpur`, `Badlapur`]" +
                        "  }" +
                        "]");
    }

    public static JsonObject anAddressJS() {
        return addressArr().getJsonObject(0);
    }

    public static JsonObject person() {
        return new JsonObject()
                .put("id", "1234L")
                .put("age", 48)
                .put("gender", "male")
                .put("employer", JsonUtil.tickedJsonObj("{" +
                        "`id` : `234L`," +
                        "`age` : 54," +
                        "`gender`: `female` " +
                        "}"))
                .put("address_list", addressArr())
                ;
    }

    public static JsonObject personMissingAgeAndGender() {
        return new JsonObject()
                .put("id", "1234L")
                .put("gender", "male")
                /*.put("employer", JsonUtil.tickedJsonObj("{" +
                        "`id` : `234L`," +
                        "`age` : 54," +
                        "`gender`: `female` " +
                        "}"))*/
                .put("address_list", addressArr())
                ;
    }

    public static JsonObject invalidPersonAgeType() {
        return new JsonObject()
                .put("id", 1234)
                .put("age", "56")
                .put("gender", "blah_blah")
                .put("address_list", addressArr())
                ;
    }

    public static JsonObject missingPersonId() {
        return new JsonObject()
                .put("age", "56")
                .put("gender", "blah_blah")
                .put("address_list", addressArr())
                ;
    }

    public static JsonObject employee() {
        JsonObject anEmp = person();
        return anEmp.put("salary", 100);
    }
}
