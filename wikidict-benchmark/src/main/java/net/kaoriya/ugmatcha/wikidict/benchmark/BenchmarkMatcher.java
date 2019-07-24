package net.kaoriya.ugmatcha.wikidict.benchmark;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import net.kaoriya.ugmatcha.wikidict.Matcher;

public class BenchmarkMatcher {
    Matcher matcher;

    public BenchmarkMatcher(File dict) throws IOException {
        matcher = Matcher.load(dict);
    }

    void dumpResult(PrintStream out, String label, FileConsumer c) {
        out.printf("%s:\n  total: %.6f seconds\n  average_per_line: %d nanoseconds\n  lineCount: %d\n",
                label,
                (double)c.ellapsedNano / 1e9,
                c.ellapsedNano / c.lineCount,
                c.lineCount);
    }

    public void run(File f) throws IOException {
        FileConsumer c0 = new FileConsumer(f);
        c0.consumeAll((s) -> {});
        dumpResult(System.err, "control", c0);

        FileConsumer c1 = new FileConsumer(f);
        c1.consumeAll((s) -> { matcher.match(s); });
        dumpResult(System.err, "matcher", c1);
    }

    public static void main(String[] args) throws Exception {
        BenchmarkMatcher bm = new BenchmarkMatcher(new File("../tmp/wikiwords"));
        for (String fn : args) {
            System.err.printf("benchmark with file:%s\n", fn);
            bm.run(new File(fn));
        }
    }
}
