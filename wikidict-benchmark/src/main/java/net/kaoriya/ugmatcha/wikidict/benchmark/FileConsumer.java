package net.kaoriya.ugmatcha.wikidict.benchmark;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.function.Consumer;

public class FileConsumer {

    File inFile;

    public long ellapsedNano;
    public long lineCount;

    public FileConsumer(File f) {
        inFile = f;
    }

    public void consumeAll(Consumer<String> c) throws IOException {
        ellapsedNano = 0;
        lineCount = 0;
        long start = System.nanoTime();
        try (FileInputStream fs = new FileInputStream(inFile);
                InputStreamReader r0 = new InputStreamReader(fs, "UTF-8");
                BufferedReader r = new BufferedReader(r0);) {
            while (true) {
                String s = r.readLine();
                if (s == null) {
                    break;
                }
                c.accept(s);
                ++lineCount;
            }
        }
        ellapsedNano += System.nanoTime() - start;
    }
}
