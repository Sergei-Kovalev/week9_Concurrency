package kovalev.jdev;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        int n = 100;
        Client client = new Client(n);
        Server server = new Server();

        ExecutorService executorService = Executors.newFixedThreadPool(n);


        for (int i = 0; i < n; i++) {
            Request transaction = new Request(client, server);
            Future<TransactionData> future = executorService.submit(transaction);
            TransactionData transactionData = future.get();
            client.accumulate(transactionData);
        }
        executorService.shutdown();

        System.out.println(client.getAccumulator());
    }
}
