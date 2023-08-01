package io.devnindo.datatype.beanexample;

import io.devnindo.datatype.json.JsonObject;
import io.devnindo.datatype.schema.BeanSchema;
import io.devnindo.datatype.schema.DataDiff;
import io.devnindo.datatype.schema.SchemaField;
import io.devnindo.datatype.util.Either;
import io.devnindo.datatype.validation.ObjViolation;
import io.devnindo.datatype.validation.Violation;

import java.util.List;

public class $Address extends BeanSchema<Address> {
    public static final SchemaField<Address, String> CITY = plainField("city", Address::getCity, String.class, false);

    public static final SchemaField<Address, List<String>> ROAD_LIST = plainListField("road_list", Address::getRoadList, String.class, false);

    @Override
    public Either<Violation, Address> apply(JsonObject data) {
        Either<Violation, String> cityEither = CITY.fromJson(data);
        Either<Violation, List<String>> roadListEither = ROAD_LIST.fromJson(data);

        ObjViolation violation = newViolation(Address.class);
        violation.check(CITY, cityEither);
        violation.check(ROAD_LIST, roadListEither);

        if (violation.hasRequirement()) {
            return Either.left(violation);
        }
        Address bean = new Address();
        bean.city = cityEither.right();
        bean.roadList = roadListEither.right();
        return Either.right(bean);
    }

    @Override
    public JsonObject apply(Address bean) {
        JsonObject js = new JsonObject();
        js.put(CITY.name, CITY.toJson(bean));
        js.put(ROAD_LIST.name, ROAD_LIST.toJson(bean));
        return js;
    }

    @Override
    public DataDiff<Address> diff(Address left, Address right) {
        Address merged = new Address();
        JsonObject delta = new JsonObject();

        merged.city = CITY.diff(left, right, delta::put);
        merged.roadList = ROAD_LIST.diff(left, right, delta::put);

        return new DataDiff<>(delta, merged);
    }
}
