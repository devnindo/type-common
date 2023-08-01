package io.devnindo.datatype;

import io.devnindo.datatype.beanexample.APerson;
import io.devnindo.datatype.beanexample.Address;
import io.devnindo.datatype.beanexample.DataSample;
import io.devnindo.datatype.json.JsonObject;
import io.devnindo.datatype.schema.BeanSchema;
import io.devnindo.datatype.util.Either;
import io.devnindo.datatype.validation.ObjViolation;
import io.devnindo.datatype.validation.Violation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class BeanTest {
    @Test
    public void json_to_bean_using_schema_success() {
        JsonObject addressJS = DataSample.anAddressJS();
        Either<Violation, Address> addressEither = BeanSchema.of(Address.class).apply(addressJS);
        Assertions.assertEquals(Boolean.TRUE, addressEither.isRight());

        Address address = addressEither.right();
        // road list from sample address js
        List<String> roadList = addressJS.getJsonArray("road_list").getList();

        // sample data should match with bean
        Assertions.assertIterableEquals(roadList, address.getRoadList());

    }

    @Test
    public void direct_json_to_bean_success() {
        JsonObject personJS = DataSample.person();
        Either<Violation, APerson> personEither = personJS.toBeanEither(APerson.class);
        Assertions.assertEquals(Boolean.TRUE, personEither.isRight());
        APerson person = personEither.right();
        Assertions.assertEquals(1234L, person.getId());

        // circular or self dependency is not an issue
        Assertions.assertTrue(person.getEmployer() instanceof APerson, "circular/self-referencing should not be an issue");


    }

    @Test
    public void json_to_bean_init_validation_fail() {
        JsonObject personJS = DataSample.invalidPersonAgeType();
        Either<Violation, APerson> addressEither = personJS.toBeanEither(APerson.class);
        Assertions.assertEquals(Boolean.TRUE, addressEither.isLeft());

        Violation violation = addressEither.left();
        Assertions.assertEquals(ObjViolation.class, violation.getClass());


        Assertions.assertTrue(violation.hasVarCtx(), "should have more than one type constraint violated");

        JsonObject ctx = violation.getVarCtx();
        JsonObject genderCtx = ctx.getJsonObject("gender");
        Assertions.assertEquals("Gender{ male, female, unknown }", genderCtx.getString("ctx"));

    }

    @Test
    public void json_to_bean_required_validation_fail() {
        JsonObject personJS = DataSample.missingPersonId();
        Either<Violation, APerson> addressEither = personJS.toBeanEither(APerson.class);
        Assertions.assertEquals(Boolean.TRUE, addressEither.isLeft());

        Violation violation = addressEither.left();
        Assertions.assertEquals(ObjViolation.class, violation.getClass());

        System.out.println(violation.toJson().encodePrettily());
        Assertions.assertTrue(violation.hasVarCtx(), "should have more than one type constraint violated");

        JsonObject ctx = violation.getVarCtx();
        JsonObject genderCtx = ctx.getJsonObject("gender");
        Assertions.assertEquals("Gender{ male, female, unknown }", genderCtx.getString("ctx"));

    }
}
