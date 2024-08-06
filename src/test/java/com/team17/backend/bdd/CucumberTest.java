package com.team17.backend.bdd;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import io.cucumber.spring.SpringFactory;
import org.junit.runner.RunWith;




@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources/features",
    glue={"com.team17.backend.bdd.steps"},
    plugin = {"pretty","json:target/cucumber-report.json","html:target/cucumber-report/cucumber-pretty.html"},
    objectFactory= SpringFactory.class
)
public class CucumberTest {

    
}