package com.piotrglazar.lookup;

import com.piotrglazar.lookup.configuration.ApplicationConfiguration;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import junitparams.JUnitParamsRunner;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.rules.ExternalResource;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestContextManager;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.file.Path;
import java.nio.file.Paths;

@RunWith(JUnitParamsRunner.class)
@SpringApplicationConfiguration(classes = ApplicationConfiguration.class)
@ActiveProfiles("test")
@SuppressWarnings("all")
@WebAppConfiguration
@SuppressFBWarnings
public abstract class AbstractContextTest {

    @Autowired
    protected WebApplicationContext webApplicationContext;

    protected MockMvc mockMvc;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @BeforeClass
    public static void removeTestIndexDirectory() {
        final String currentLocation = System.getProperty("user.dir");
        final Path testIndex = Paths.get(currentLocation, "testIndex");
        testIndex.toFile().delete();
    }

    @Rule
    public SpringInJUnitParams spring = new SpringInJUnitParams(getClass(), this);

    private static final class SpringInJUnitParams extends ExternalResource {

        private final Class<?> testClass;

        private final AbstractContextTest testInstance;

        private SpringInJUnitParams(final Class<?> testClass, final AbstractContextTest testInstance) {
            this.testClass = testClass;
            this.testInstance = testInstance;
        }

        @Override
        protected void before() throws Throwable {
            final TestContextManager testContextManager = new TestContextManager(testClass);
            testContextManager.prepareTestInstance(testInstance);
        }
    }
}
