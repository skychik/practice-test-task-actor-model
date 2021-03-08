package ru.ifmo.mse;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class App {

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Require two arguments: filePath to file with URLs and amount of parallel threads");
            return;
        }
        String filePath = args[0];
        int size = Integer.parseInt(args[1]);
        if (size <= 0) {
            System.out.println("Amount of parallel threads should be a positive number");
            return;
        }

        proceed(filePath, size);
    }

    public static void proceed(String filePath, int size) {
        try (Scanner sc = new Scanner(new File(filePath))) {
            int index = 0;
            ExecutorService service = Executors.newFixedThreadPool(size);
            while (sc.hasNextLine()) {
                String url = sc.nextLine();
                int finalIndex = index;
                service.submit(() -> fetchData(finalIndex, url));
                index++;
            }
            service.shutdown();
            service.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (InterruptedException e) {
            System.out.println("Interrupted, worked too long: " + e.getMessage());
        }
    }

    public static void fetchData(int index, String url) {
        try (BufferedInputStream in = new BufferedInputStream(new URL(url).openStream());
             FileOutputStream fileOutputStream = new FileOutputStream("context_" + index + ".data")) {
            byte[] dataBuffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
            }
        } catch (MalformedURLException e) {
            System.out.println("Wrong url: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Can't open stream: " + e.getMessage());
        }
    }
}