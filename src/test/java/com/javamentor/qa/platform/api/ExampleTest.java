package com.javamentor.qa.platform.api;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.api.DBRider;
import com.javamentor.qa.platform.AbstractApiTest;
import com.javamentor.qa.platform.webapp.controllers.rest.TestController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: Rustam
 */


public class ExampleTest extends AbstractApiTest {

    @Autowired
    public TestController testController;

    @Test
    @DataSet("example/exampleDataSet.yml")
    public void exampleTest() throws Exception{

        List<String> test = testController.getTest();
        assertThat(test).isEmpty();
    }

}
