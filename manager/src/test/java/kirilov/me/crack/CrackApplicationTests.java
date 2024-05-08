package kirilov.me.crack;

import kirilov.me.crack.worker.Worker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

@SpringBootTest
class CrackApplicationTests {
    private final Worker worker;

    @Autowired
    CrackApplicationTests(Worker worker) {
        this.worker = worker;
    }

    @Test
    void md5Test() {
        var password = "password";
        var targetHash = "5f4dcc3b5aa765d61d8327deb882cf99";
        var result = Worker.md5Hash(password);
        Assertions.assertEquals(targetHash, result);
    }

    @Test
    void bruteForceTest() {
        var alphabet = Arrays.asList("abcdefghijklmnopqrstuvwxyz0123456789".split(""));

        var targetPassword = "abcde";
        var targetHash = Worker.md5Hash(targetPassword);

        var result = worker.bruteForce(0, 1, targetHash, 5, alphabet);

        if (result.isEmpty()) {
            Assertions.fail();
        }

        Assertions.assertEquals(result.get(), targetPassword);

        targetPassword = "abcdefg";
        targetHash = Worker.md5Hash(targetPassword);

        result = worker.bruteForce(0, 1, targetHash, 5, alphabet);

        if (result.isPresent()) {
            Assertions.fail();
        }
    }

    @Test
    void indexTest() {
        int size, partNumber, partCount;

        size = 10;
        partNumber = 0;
        partCount = 1;
        var startIndex = Worker.getStartIndex(size, partNumber, partCount);
        var endIndex = Worker.getEndIndex(size, partNumber, partCount);
        Assertions.assertEquals(startIndex, 0);
        Assertions.assertEquals(endIndex, 9);

        size = 10;
        partNumber = 0;
        partCount = 2;
        startIndex = Worker.getStartIndex(size, partNumber, partCount);
        endIndex = Worker.getEndIndex(size, partNumber, partCount);
        Assertions.assertEquals(startIndex, 0);
        Assertions.assertEquals(endIndex, 4);

        size = 10;
        partNumber = 1;
        partCount = 2;
        startIndex = Worker.getStartIndex(size, partNumber, partCount);
        endIndex = Worker.getEndIndex(size, partNumber, partCount);
        Assertions.assertEquals(startIndex, 5);
        Assertions.assertEquals(endIndex, 9);

        size = 11;
        partNumber = 0;
        partCount = 2;
        startIndex = Worker.getStartIndex(size, partNumber, partCount);
        endIndex = Worker.getEndIndex(size, partNumber, partCount);
        Assertions.assertEquals(startIndex, 0);
        Assertions.assertEquals(endIndex, 4);

        size = 11;
        partNumber = 1;
        partCount = 2;
        startIndex = Worker.getStartIndex(size, partNumber, partCount);
        endIndex = Worker.getEndIndex(size, partNumber, partCount);
        Assertions.assertEquals(startIndex, 5);
        Assertions.assertEquals(endIndex, 10);

        size = 10;
        partNumber = 0;
        partCount = 3;
        startIndex = Worker.getStartIndex(size, partNumber, partCount);
        endIndex = Worker.getEndIndex(size, partNumber, partCount);
        Assertions.assertEquals(startIndex, 0);
        Assertions.assertEquals(endIndex, 2);

        size = 10;
        partNumber = 1;
        partCount = 3;
        startIndex = Worker.getStartIndex(size, partNumber, partCount);
        endIndex = Worker.getEndIndex(size, partNumber, partCount);
        Assertions.assertEquals(startIndex, 3);
        Assertions.assertEquals(endIndex, 5);

        size = 10;
        partNumber = 2;
        partCount = 3;
        startIndex = Worker.getStartIndex(size, partNumber, partCount);
        endIndex = Worker.getEndIndex(size, partNumber, partCount);
        Assertions.assertEquals(startIndex, 6);
        Assertions.assertEquals(endIndex, 9);
    }
}
