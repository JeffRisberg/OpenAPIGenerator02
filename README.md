## OpenAPIGenerator02 - using Jersey and Gradle


### Generate step:

```
./gradlew clean openApiGenerate
```

### Full build (also carries out Generate Step)

```
./gradlew clean build

```

## Overview
This server was generated by the [OpenAPI Generator](https://openapi-generator.tech) project. By using an
[OpenAPI-Spec](https://openapis.org), you can easily generate a server stub.

This is an example of building a OpenAPI-enabled JAX-RS server.
This example uses the [JAX-RS](https://jax-rs-spec.java.net/) framework.
Jersey is used as JAX-RS implementation, `io.swagger:swagger-jersey2-jaxrs` is used to derive the OpenAPI Specification from the annotated code.
