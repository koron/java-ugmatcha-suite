package net.kaoriya.ugmatcha.wikidict;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import net.kaoriya.ugmatcha.normalizer.Japanese;
import net.kaoriya.ugmatcha.trietree.StaticTree;

public class Matcher {

    StaticTree st;
    String[] ws;

    private Matcher(StaticTree st, String[] ws) {
        this.st = st;
        this.ws = ws;
    }

    public static Matcher load(File treeFile, File wordFile) throws IOException {
        StaticTree st = loadSTree(treeFile);
        String[] ws = loadWords(wordFile);
        return new Matcher(st, ws);
    }

    public static Matcher load(File base) throws IOException {
        String p = base.getPath();
        return load(new File(p + ".stt"), new File(p + ".stw"));
    }

    static StaticTree loadSTree(File f) throws IOException {
        try (FileInputStream fs = new FileInputStream(f);) {
            return StaticTree.load(fs);
        }
    }

    static String[] loadWords(File f) throws IOException {
        try (FileInputStream fs = new FileInputStream(f);
                InputStreamReader r0 = new InputStreamReader(fs, "UTF-8");
                BufferedReader r = new BufferedReader(r0)) {
            return r.lines().toArray(String[]::new);
        }
    }

    public String[] match(String s) {
        s = Japanese.normalize(s);
        Scanner sc = new Scanner(s);
        st.scan(s, sc);
        ArrayList<String> list = new ArrayList<>();
        for (int id : sc.finish()) {
            if (id <= 0) {
                continue;
            }
            id--;
            if (id < ws.length) {
                list.add(ws[id]);
            }
        }
        return list.stream().toArray(String[]::new);
    }

    public void matchDemo(File f) throws IOException {
        try (FileInputStream fs = new FileInputStream(f);) {
            matchDemo(fs);
        }
    }

    public void matchDemo(InputStream is) throws IOException {
        try (InputStreamReader r0 = new InputStreamReader(is, "UTF-8");
                BufferedReader r = new BufferedReader(r0)) {
            while (true) {
                String s = r.readLine();
                if (s == null) {
                    break;
                }
                System.out.print(s);
                for (String w : match(s)) {
                    System.out.printf("\t%s", w);
                }
                System.out.println();
            }
        }
    }

    public static void main(String[] args) throws Exception {
        Matcher m = load(new File("../tmp/wikiwords"));
        for (String fn : args) {
            System.err.printf("matching against file:%s", fn);
            m.matchDemo(new File(fn));
        }
    }
}
