package kovalev.jdev;

import kovalev.jdev.client.Client;
import kovalev.jdev.transaction.TransactionData;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

class ClientTest {

    @Test
    void accumulate() throws InterruptedException {
        // given
        Client client = new Client(0);
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        int expectedCounter = 10;
        int expectedAccumulator = 20;

        // when     отправляем 10 запросов с числом 2
        for (int i = 0; i < 10; i++) {
            executorService.execute(() -> client.accumulate(new TransactionData(2)));
        }
        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.DAYS);

        int actualAccumulator = client.getAccumulator().get();
        int actualCounter = client.getCounter().get();

        // then
        assertThat(actualAccumulator)
                .isEqualTo(expectedAccumulator);

        assertThat(actualCounter)
                .isEqualTo(expectedCounter);
    }

    @Test
    void removeShouldRemoveExactlyNeededCountOfElements() {
        // given
        int numberCycles = 3;
        int n = 10;
        Client client = new Client(n);

        int expected = n - numberCycles;

        // when
        for (int i = 0; i < numberCycles; i++) {
            client.remove();
        }
        int actual = client.getNumbersList().size();

        // then
        assertThat(actual)
                .isEqualTo(expected);
    }

    @Test
    void removeAllAfterStartThread() throws InterruptedException {
        // given
        int n = 3;
        Client client = new Client(n);
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        // when
        executorService.execute(client);
        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.DAYS);

        List<Integer> actual = client.getNumbersList();

        // then
        assertThat(actual)
                .isEmpty();
    }

    @Test
    void constructorMethodCheck() {
        // given
        int n = 5;
        Client client = new Client(n);

        List<Integer> expected = List.of(1, 2, 3, 4, 5);

        // when
        List<Integer> actual = client.getNumbersList();

        // then
        assertThat(actual)
                .containsExactlyElementsOf(expected);

    }
}