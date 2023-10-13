# devnindo-datatype
A library for serialization and deserialization between POJOs and their JSON representation ***without reflection***.

## Features

1. **Schema**: Compiletime schema class with field name and type-resolver of Java Beans.
2. **Diffing and merging**: Helps Computing differences(delta) and merging of two bean instance
3. **Validation**: Helps writing validators that can report structured constraint violation 

As of now, a companion gradle plugin [devnindo-schemagen-plugin](https://github.com/devnindo/devnindo-schemagen-plugin) is needed to be used for compile time schema generation.  

## Motivation

From our perspective, using annotations as marker is good but to define domain behaviour is a bad practice. For example Google's [dagger](https://github.com/google/dagger) dependancy injection library uses annotation as a marker to define dependancy graph, generates source code during compile time for all the module and components involved.  

Almost all JPA based web framework use annotation to desicribe constraints on fields of Java POJO. This makes data validation and conversion very limiting, reflection dependant, too much verbose and hard to generate a well structure context. 


## Usage Example

### Installation
In a gradle based project: 

1. Add the Devnindo data type dependency in your `build.gradle`:

```groovy
dependencies {
    // ... other dependencies
    implementation 'io.devnindo.core:devnindo-datatype:0.9.8'
}
```

2. Incorporate the schemagen plugin:

```groovy
plugins {
    // ... other plugins
    id 'io.devnindo.devnindo-schemagen' version '0.9.17'
}
```

Upon setup, a Gradle task `generateSchema` will be available. Running `compileJava` or `generateSchema` will create the schema representation for all `DataBean` implementations. 

## Data types
In devnindo land, we use `DataBean` marker interface to indicate domain data objects. Following data types are supported:
- plain data types: `Integer, Long, String, Float, Double, Boolean, Instant`
- Json presentational data types: `JsonObject` and `JsonArray`
- Types that implements DataBean
- List of all plain data types, `List<JsonObject>` and `List<DataBean>`

 Classes that implements `DataBean` should have all fields declared as package private; meaning correlated domain object types should be bounded by pakcage module. 

### Example
Lets consider following `DataBean` implementation:

```java

public class APerson implements DataBean {
    @Required
    Long id;

    @Required
    Integer age;
    Gender gender;
    List<Address> addressList;
    APerson employer;


    public Long getId() {
        return id;
    }


    public Integer getAge() {
        return age;
    }

    public APerson setAge(Integer age$) {
        age = age$;
        return this;
    }

    public APerson getEmployer() {
        return employer;
    }

    public Gender getGender() {
        return gender;
    }

    public List<Address> getAddressList() {
        return addressList;
    }
}
```

Running `compileJava` should generate following schema `$APerson`

```java

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
```

Now for a  json:

```js

{
  "id" : "1234L",
  "gender" : "male",
  "address_list" : [ {
    "city" : "Dhaka",
    "road_list" : [ "Mirpur 10", "Gulshan" ]
  }, {
    "city" : "Barishal",
    "road_list" : [ "Kachpur", "Badlapur" ]
  } ]
}
``` 

a simple conversion is as easy as follows:

```java

JsonObject personJS = new JsonObject(jsonStr);
 
Either<Violation, APerson> personEither = personJS.toBeanEither(APerson.class);

// there is a violation
Assertions.assertTrue(personEither.isLeft());

```


  
