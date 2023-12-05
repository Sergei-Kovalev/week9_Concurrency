package kovalev.jdev;

import kovalev.jdev.client.Client;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        int n = 10;
        Client client = new Client(n);
        Thread thread = new Thread(client, "client");

        LocalDateTime start = LocalDateTime.now();

        thread.start();
        thread.join();

        System.out.println("Result = " + client.getAccumulator());

        LocalDateTime finish = LocalDateTime.now();

        System.out.println("Execution time = " + ChronoUnit.SECONDS.between(start, finish) + " seconds.");
    }
}
