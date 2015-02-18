import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class LibraryTest {
    @Test
    public void testSomeLibraryMethod() {
        // given
        Library classUnderTest = new Library();

        // when
        boolean result = classUnderTest.someLibraryMethod();

        // then
        assertThat(result).isTrue();
    }
}
