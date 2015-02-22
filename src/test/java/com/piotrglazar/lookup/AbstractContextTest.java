package com.piotrglazar.lookup;

import com.piotrglazar.lookup.configuration.ApplicationConfiguration;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import junitparams.JUnitParamsRunner;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.rules.ExternalResource;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestContextManager;

import java.nio.file.Path;
import java.nio.file.Paths;

@RunWith(JUnitParamsRunner.class)
@ContextConfiguration(classes = ApplicationConfiguration.class)
@ActiveProfiles("test")
@SuppressWarnings("all")
@SuppressFBWarnings
public abstract class AbstractContextTest {

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
