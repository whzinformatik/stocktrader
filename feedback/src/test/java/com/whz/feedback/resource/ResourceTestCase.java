package com.whz.feedback.resource;

import com.whz.feedback.infrastructure.Bootstrap;
import com.whz.feedback.infrastructure.persistence.CommandModelJournalProvider;
import com.whz.feedback.infrastructure.persistence.ProjectionDispatcherProvider;
import com.whz.feedback.infrastructure.persistence.QueryModelStateStoreProvider;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.util.concurrent.atomic.AtomicInteger;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertTrue;

abstract class ResourceTestCase {

    private static final AtomicInteger portNumberPool = new AtomicInteger(18080);

    private int portNumber;

    private Bootstrap bootstrap;

    @BeforeEach
    public void setup() throws Exception {
        portNumber = portNumberPool.getAndIncrement();
        bootstrap = new Bootstrap(portNumber);
        boolean startupSuccessful = bootstrap.getServer().startUp().await(100);
        assertTrue(startupSuccessful);
    }

    @AfterEach
    public void cleanup() {
        bootstrap.getServer().stop();

        QueryModelStateStoreProvider.reset();
        ProjectionDispatcherProvider.reset();
        CommandModelJournalProvider.reset();
    }

    protected RequestSpecification givenJsonClient() {
        return given().port(portNumber).accept(ContentType.JSON).contentType(ContentType.JSON);
    }
}
