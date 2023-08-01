package io.devnindo.datatype;

import io.devnindo.datatype.beanexample.Address;
import io.devnindo.datatype.beanexample.DataSample;
import io.devnindo.datatype.json.JsonArray;
import io.devnindo.datatype.json.JsonObject;
import io.devnindo.datatype.schema.typeresolver.TypeResolverFactory;
import io.devnindo.datatype.schema.typeresolver.TypeResolverIF;
import io.devnindo.datatype.util.Either;
import io.devnindo.datatype.validation.Violation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class TypeResolverTest {

    @Test
    void test_factory_registry_singleton() {
        TypeResolverIF<List<Integer>> resolver1 = TypeResolverFactory.plainDataList(Integer.class);
        TypeResolverIF<List<Integer>> resolver2 = TypeResolverFactory.plainDataList(Integer.class);
        Assertions.assertEquals(resolver1, resolver2);

    }


    @Test
    public void test_plain_list_resolve_success() {
        TypeResolverIF<List<Integer>> resolver = TypeResolverFactory.plainDataList(Integer.class);
        Either<Violation, List<Integer>> listIntEither = resolver.evalJsonVal(new JsonArray("[123, 234, 534]"));

        Assertions.assertEquals(Boolean.TRUE, listIntEither.isRight());
        listIntEither.right().forEach(item -> {

            Assertions.assertEquals(Boolean.TRUE, item instanceof Integer);
        });
    }

    @Test
    public void test_plain_list_resolve_fail() {
        TypeResolverIF<List<Integer>> resolver = TypeResolverFactory.plainDataList(Integer.class);
        Either<Violation, List<Integer>> listIntEither = resolver.evalJsonVal(new JsonArray("[123, 234, \"534\"]"));
        Assertions.assertEquals(Boolean.TRUE, listIntEither.isLeft());
        System.out.println(listIntEither.left().constraint);
        Assertions.assertEquals("PLAIN_DATA_LIST_TYPE", listIntEither.left().constraint);
        Assertions.assertEquals("Integer", listIntEither.left().getSingleCtx());

    }

    @Test
    public void test_json_object_list_resolve_success() {
        TypeResolverIF<List<JsonObject>> resolver = TypeResolverFactory.plainDataList(JsonObject.class);
        Either<Violation, List<JsonObject>> listJsEither = resolver.evalJsonVal(DataSample.addressArr());
        Assertions.assertEquals(Boolean.TRUE, listJsEither.isRight());
    }

    @Test
    public void test_bean_list_resolve_success() {
        TypeResolverIF<List<Address>> resolver = TypeResolverFactory.beanList(Address.class);
        Either<Violation, List<Address>> listJsEither = resolver.evalJsonVal(DataSample.addressArr());
        Assertions.assertEquals(Boolean.TRUE, listJsEither.isRight());

        List<Address> addressList = listJsEither.right();
        Assertions.assertEquals(Boolean.TRUE, addressList.get(0) instanceof Address);

    }

    @Test
    public void test_bean_list_resolve_fail() {
        TypeResolverIF<List<Address>> resolver = TypeResolverFactory.beanList(Address.class);
        Either<Violation, List<Address>> listJsEither = resolver.evalJsonVal(DataSample.invalidAddressArr());
        Assertions.assertEquals(Boolean.TRUE, listJsEither.isLeft());
        System.out.println(listJsEither.left().toJson().encodePrettily());


    }
}
