package net.kaoriya.ugmatcha.trietree;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

class Reader {

    final BufferedInputStream in;

    Reader(InputStream is) {
        in = new BufferedInputStream(is);
    }

    private long readRawLong() throws IOException {
        long x = 0;
        int s = 0;
        for (int i = 0; true; i++) {
            int n = in.read();
            if (n < 0x80) {
                if (i > 9 || i == 9 && n > 1) {
                    throw new IOException("varint overflows a 64-bit integer");
                }
                return x | ((long)n << s);
            }
            x |= ((long)(n & 0x7f)) << s;
            s += 7;
        }
    }

    long readLong() throws IOException {
        long ux = readRawLong();
        long x = ux >>> 1;
        if ((ux & 1) != 0) {
            x = ~x + 1;
        }
        return x;
    }

    int readInt() throws IOException {
        long n = readLong();
        if (n > Integer.MAX_VALUE) {
            throw new IOException("varint overflows a 32-bit integer");
        }
        return (int)n;
    }

    int readRune() throws IOException {
        return readInt();
    }
}
