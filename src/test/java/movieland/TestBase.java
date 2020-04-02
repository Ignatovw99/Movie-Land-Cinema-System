package movieland;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public abstract class TestBase {

    @BeforeEach
    private void setupTest() {
        MockitoAnnotations.initMocks(this);
        before();
        setupMockBeansActions();
    }

    protected abstract void before();

    //Mock Beans Class setting, which allows the method to execute successfully
    protected void setupMockBeansActions() {

    }
}
