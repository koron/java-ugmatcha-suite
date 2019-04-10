package net.kaoriya.ugmatcha.normalizer;

import org.junit.Test;
import static org.junit.Assert.*;

public class JapaneseTest {

    @Test
    public void normalize() {
        // basic test data from https://github.com/neologd/mecab-ipadic-neologd/wiki/Regexp.ja
        assertEquals("0123456789", Japanese.normalize("０１２３４５６７８９"));
        assertEquals("ABCDEFGHIJKLMNOPQRSTUVWXYZ", Japanese.normalize("ＡＢＣＤＥＦＧＨＩＪＫＬＭＮＯＰＱＲＳＴＵＶＷＸＹＺ"));
        assertEquals("abcdefghijklmnopqrstuvwxyz", Japanese.normalize("ａｂｃｄｅｆｇｈｉｊｋｌｍｎｏｐｑｒｓｔｕｖｗｘｙｚ"));
        assertEquals("#$%&'()*+,-./:;<>?@[¥]^_`{|}", Japanese.normalize("！”＃＄％＆’（）＊＋，－．／：；＜＞？＠［￥］＾＿｀｛｜｝"));
        assertEquals("＝。、・「」", Japanese.normalize("＝。、・「」"));
        assertEquals("ハンカク", Japanese.normalize("ﾊﾝｶｸ"));
        assertEquals("o-o", Japanese.normalize("o₋o"));
        assertEquals("majikaー", Japanese.normalize("majika━"));
        assertEquals("わい", Japanese.normalize("わ〰い"));
        assertEquals("スーパー", Japanese.normalize("スーパーーーー"));
        assertEquals("!#", Japanese.normalize("!#"));
        assertEquals("ゼンカクスペース", Japanese.normalize("ゼンカク　スペース"));
        assertEquals("おお", Japanese.normalize("お             お"));
        assertEquals("おお", Japanese.normalize("      おお"));
        assertEquals("おお", Japanese.normalize("おお      "));
        assertEquals("検索エンジン自作入門を買いました!!!", Japanese.normalize("検索 エンジン 自作 入門 を 買い ました!!!"));
        assertEquals("アルゴリズムC", Japanese.normalize("アルゴリズム C"));
        assertEquals("PRML副読本", Japanese.normalize("　　　ＰＲＭＬ　　副　読　本　　　"));
        assertEquals("Coding the Matrix", Japanese.normalize("Coding the Matrix"));
        assertEquals("南アルプスの天然水Sparking Lemonレモン一絞り", Japanese.normalize("南アルプスの　天然水　Ｓｐａｒｋｉｎｇ　Ｌｅｍｏｎ　レモン一絞り"));
        assertEquals("南アルプスの天然水-Sparking*Lemon+レモン一絞り", Japanese.normalize("南アルプスの　天然水-　Ｓｐａｒｋｉｎｇ*　Ｌｅｍｏｎ+　レモン一絞り"));
    }
}
