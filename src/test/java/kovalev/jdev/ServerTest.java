package kovalev.jdev;

import kovalev.jdev.server.Server;
import kovalev.jdev.transaction.Request;
import kovalev.jdev.transaction.TransactionData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;

@ExtendWith(MockitoExtension.class)
class ServerTest {

    @Spy
    private Server server;

    @Test
    void fillList() throws ExecutionException, InterruptedException {
        // given
        List<Integer> expected = List.of(0, 1, 2, 3, 4);

        doNothing().when(server).sendResponse();
        doNothing().when(server).sleeping();

        // when
        for (int i = 0; i < 5; i++) {
            server.fillList(new TransactionData(i));
        }
        List<Integer> actual = server.getList();

        // then
        assertThat(actual)
                .hasSameSizeAs(expected)
                .containsExactlyElementsOf(expected);
    }

    @Test
    void fillListWithTransactions() throws ExecutionException, InterruptedException {
        // given
        List<Integer> list = List.of(1, 2, 3, 4, 5);
        ExecutorService executorService = Executors.newFixedThreadPool(5);

        doNothing().when(server).sendResponse();
        doNothing().when(server).sleeping();

        // when
        list.forEach(element -> {
            Request request = new Request(new TransactionData(element), server);
            executorService.execute(request);
        });

        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.DAYS);

        List<Integer> actual = server.getList();
        System.out.println(actual);

        // then
        assertThat(actual)
                .hasSameSizeAs(list)
                .containsExactlyInAnyOrderElementsOf(list);
    }
}