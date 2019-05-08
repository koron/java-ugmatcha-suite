package net.kaoriya.ugmatcha.normalizer;

import org.junit.Test;
import static org.junit.Assert.*;

public class JapaneseTest {

    // basic test data from https://github.com/neologd/mecab-ipadic-neologd/wiki/Regexp.ja

    @Test
    public void zen2han() {
        assertEquals("0123456789", Japanese.normalize("０１２３４５６７８９"));
        assertEquals("ABCDEFGHIJKLMNOPQRSTUVWXYZ", Japanese.normalize("ＡＢＣＤＥＦＧＨＩＪＫＬＭＮＯＰＱＲＳＴＵＶＷＸＹＺ"));
        assertEquals("abcdefghijklmnopqrstuvwxyz", Japanese.normalize("ａｂｃｄｅｆｇｈｉｊｋｌｍｎｏｐｑｒｓｔｕｖｗｘｙｚ"));
        assertEquals(" !\"#$%&'()*+,-./:;<=>?@[¥]^_`{|}", Japanese.normalize("　！”＃＄％＆’（）＊＋，－．／：；＜＝＞？＠［￥］＾＿｀｛｜｝"));
    }

    @Test
    public void han2zen() {
        assertEquals("。、・「」", Japanese.normalize("｡､･｢｣"));
        assertEquals("ハンカク", Japanese.normalize("ﾊﾝｶｸ"));
        //assertEquals("ゼンカク", Japanese.normalize("ｾﾞﾝｶｸ"));
    }

    @Test
    public void bar() {
        assertEquals("o-o", Japanese.normalize("o₋o"));
        assertEquals("majikaー", Japanese.normalize("majika━"));
        assertEquals("わい", Japanese.normalize("わ〰い"));
        assertEquals("スーパー", Japanese.normalize("スーパーーーー"));
    }

    @Test
    public void spaces() {
        assertEquals("ゼンカクスペース", Japanese.normalize("ゼンカク　スペース"));
        assertEquals("おお", Japanese.normalize("お             お"));
        assertEquals("おお", Japanese.normalize("      おお"));
        assertEquals("おお", Japanese.normalize("おお      "));
        assertEquals("検索エンジン自作入門を買いました!!!", Japanese.normalize("検索 エンジン 自作 入門 を 買い ました!!!"));
        assertEquals("アルゴリズムC", Japanese.normalize("アルゴリズム C"));
        assertEquals("PRML副読本", Japanese.normalize("　　　ＰＲＭＬ　　副　読　本　　　"));
        assertEquals("Coding the Matrix", Japanese.normalize("Coding the Matrix"));
    }

    @Test
    public void normalize() {
        // basic test data from https://github.com/neologd/mecab-ipadic-neologd/wiki/Regexp.ja
        assertEquals("!#", Japanese.normalize("!#"));
        assertEquals("南アルプスの天然水Sparking Lemonレモン一絞り", Japanese.normalize("南アルプスの　天然水　Ｓｐａｒｋｉｎｇ　Ｌｅｍｏｎ　レモン一絞り"));
        assertEquals("南アルプスの天然水-Sparking*Lemon+レモン一絞り", Japanese.normalize("南アルプスの　天然水-　Ｓｐａｒｋｉｎｇ*　Ｌｅｍｏｎ+　レモン一絞り"));
    }
}
