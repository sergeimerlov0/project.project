//package com.javamentor.qa.platform.api;
//
//import com.github.database.rider.core.api.dataset.DataSet;
//import com.javamentor.qa.platform.AbstractApiTest;
//import com.javamentor.qa.platform.webapp.controllers.rest.TestController;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//
//
//
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
///**
// * \* Created with IntelliJ IDEA.
// * \* User: Rustam
// */
//
//
//public class ExampleTest extends AbstractApiTest {
//
//    @Autowired
//    public TestController testController;
//
//    @Test
//    @DataSet("example/exampleDataSet.yml")
//    public void exampleTest() throws Exception{
//
//        this.mvc.perform(MockMvcRequestBuilders
//                .get("/test"))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(content().string("[]"));
//
//
//    }
//
//}
