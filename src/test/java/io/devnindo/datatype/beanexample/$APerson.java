package io.devnindo.datatype.beanexample;

import io.devnindo.datatype.json.JsonObject;
import io.devnindo.datatype.schema.BeanSchema;
import io.devnindo.datatype.schema.DataDiff;
import io.devnindo.datatype.schema.SchemaField;
import io.devnindo.datatype.util.Either;
import io.devnindo.datatype.validation.ObjViolation;
import io.devnindo.datatype.validation.Violation;

import java.util.List;

public class $APerson extends BeanSchema<APerson> {
    public static final SchemaField<APerson, Gender> GENDER = enumField("gender", APerson::getGender, Gender.class, false);

    public static final SchemaField<APerson, List<Address>> ADDRESS_LIST = beanListField("address_list", APerson::getAddressList, Address.class, false);

    public static final SchemaField<APerson, APerson> EMPLOYER = beanField("employer", APerson::getEmployer, APerson.class, false);

    public static final SchemaField<APerson, Long> ID = plainField("id", APerson::getId, Long.class, true);

    public static final SchemaField<APerson, Integer> AGE = plainField("age", APerson::getAge, Integer.class, true);

    @Override
    public Either<Violation, APerson> apply(JsonObject data) {
        Either<Violation, Gender> genderEither = GENDER.fromJson(data);
        Either<Violation, List<Address>> addressListEither = ADDRESS_LIST.fromJson(data);
        Either<Violation, APerson> employerEither = EMPLOYER.fromJson(data);
        Either<Violation, Long> idEither = ID.fromJson(data);
        Either<Violation, Integer> ageEither = AGE.fromJson(data);

        ObjViolation violation = newViolation(APerson.class);
        violation.check(GENDER, genderEither);
        violation.check(ADDRESS_LIST, addressListEither);
        violation.check(EMPLOYER, employerEither);
        violation.check(ID, idEither);
        violation.check(AGE, ageEither);

        if (violation.hasRequirement()) {
            return Either.left(violation);
        }
        APerson bean = new APerson();
        bean.gender = genderEither.right();
        bean.addressList = addressListEither.right();
        bean.employer = employerEither.right();
        bean.id = idEither.right();
        bean.setAge(ageEither.right());
        return Either.right(bean);
    }

    @Override
    public JsonObject apply(APerson bean) {
        JsonObject js = new JsonObject();
        js.put(GENDER.name, GENDER.toJson(bean));
        js.put(ADDRESS_LIST.name, ADDRESS_LIST.toJson(bean));
        js.put(EMPLOYER.name, EMPLOYER.toJson(bean));
        js.put(ID.name, ID.toJson(bean));
        js.put(AGE.name, AGE.toJson(bean));
        return js;
    }

    @Override
    public DataDiff<APerson> diff(APerson left, APerson right) {
        APerson merged = new APerson();
        JsonObject delta = new JsonObject();

        merged.gender = GENDER.diff(left, right, delta::put);
        merged.addressList = ADDRESS_LIST.diff(left, right, delta::put);
        merged.employer = EMPLOYER.diff(left, right, delta::put);
        merged.id = ID.diff(left, right, delta::put);
        merged.age = AGE.diff(left, right, delta::put);

        return new DataDiff<>(delta, merged);
    }
}
