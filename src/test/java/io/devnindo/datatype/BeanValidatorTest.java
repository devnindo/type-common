package io.devnindo.datatype;

import io.devnindo.datatype.beanexample.$APerson;
import io.devnindo.datatype.beanexample.APerson;
import io.devnindo.datatype.beanexample.DataSample;
import io.devnindo.datatype.beanexample.Gender;
import io.devnindo.datatype.json.JsonObject;
import io.devnindo.datatype.schema.BeanValidator;
import io.devnindo.datatype.util.Either;
import io.devnindo.datatype.validation.Violation;
import org.junit.jupiter.api.Test;

import static io.devnindo.datatype.validation.validators.ValueRules.*;

public class BeanValidatorTest {
    @Test
    public void bean_validation_success() {
        JsonObject personJS = DataSample.person();
        APerson person = personJS.toBean(APerson.class);
        Either<Violation, APerson> personEither = BeanValidator.create("FEMALE_PENSION_ELIGIBLE", APerson.class, $ -> {
            $.required($APerson.AGE).and(gtThan(50));
            $.required($APerson.GENDER).and(equal(Gender.female));
        }).apply(person);

        System.out.println(personEither.left().toJson().encodePrettily());

    }
}
