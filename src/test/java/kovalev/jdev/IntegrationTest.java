package kovalev.jdev;

import kovalev.jdev.client.Client;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class IntegrationTest {

    @Test
    void appTest() throws InterruptedException {
        // given
        int n = 10;
        Client client = new Client(n);
        int expected = (1 + n) * n / 2;

        // when
        Thread thread = new Thread(client);
        thread.start();
        thread.join();
        int actual = client.getAccumulator().get();

        // then
        assertThat(actual)
                .isEqualTo(expected);
    }
}
