package kovalev.jdev;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        int n = 10;
        Client client = new Client(n);
        Server server = new Server();

        ExecutorService executorService = Executors.newFixedThreadPool(10);

        LocalDateTime start = LocalDateTime.now();

        for (int i = 0; i < n; i++) {
            Request transaction = new Request(client, server);
            Future<TransactionData> future = executorService.submit(transaction);
            TransactionData transactionData = future.get();
            client.accumulate(transactionData);
        }
        executorService.shutdown();

        System.out.println("Result = " + client.getAccumulator());

        LocalDateTime finish = LocalDateTime.now();

        System.out.println("Execution time = " + ChronoUnit.SECONDS.between(start, finish) + " seconds.");
    }
}
