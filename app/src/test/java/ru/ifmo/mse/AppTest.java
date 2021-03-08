package ru.ifmo.mse;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class AppTest {

    @Test
    void test1() throws FileNotFoundException {
        String filePath = "src/test/resources/urls.txt";
        int maxThreads = 3;
        App.main(new String[]{filePath, Integer.toString(maxThreads)});

        Scanner sc = new Scanner(new File(filePath));
        int index = 0;
        while (sc.hasNextLine()) {
            String url = sc.nextLine();
            String outPath = "context_" + index + ".data";
            try (BufferedInputStream expected = new BufferedInputStream(new URL(url).openStream());
                 BufferedInputStream actual = new BufferedInputStream(new FileInputStream(outPath))) {
                assertArrayEquals(expected.readAllBytes(), actual.readAllBytes());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            index++;
        }
    }

    @AfterAll
    static void after() {
        int i = 0;
        boolean didDelete = true;
        while (didDelete) {
            File currentFile = new File("context_" + i + ".data");
            didDelete = currentFile.delete();
            i++;
        }
    }
}
