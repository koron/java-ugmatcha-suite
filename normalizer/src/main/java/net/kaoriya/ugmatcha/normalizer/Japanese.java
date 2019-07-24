package net.kaoriya.ugmatcha.normalizer;

import java.lang.Character.UnicodeBlock;

/**
 * Japanese normalizer for ugmatcha.
 */
public class Japanese {

    public static final Normalizer normalizer;

    /**
     * Normalize a string as Japanese.
     */
    public static String normalize(String s) {
        return normalize(s, false);
    }

    public static String normalize(String s, boolean debug) {
        return sanitize(normalizer.normalize(s, debug), debug);
    }

    static String sanitize(String src, boolean debug) {
        StringBuilder dst = new StringBuilder();
        boolean lastSpace = true;
        boolean lastZenbar = false;
        UnicodeBlock lastUB = null;
        for (int i = 0; i < src.length(); i++) {
            char c = src.charAt(i);
            UnicodeBlock ub = UnicodeBlock.of(c);
            switch (c) {
                case ' ':
                    if (lastSpace) {
                        continue;
                    }
                    if (isZenkaku(lastUB)) {
                        continue;
                    }
                    break;
                case 'ー':
                    if (lastZenbar) {
                        continue;
                    }
                    break;
                default:
                    if (lastSpace && isZenkaku(ub) && dst.length() > 0) {
                        dst.setLength(dst.length() - 1);
                        lastSpace = false;
                    }
                    break;
            }
            dst.append(c);
            // update last statuses.
            lastSpace = c == ' ';
            lastZenbar = c == 'ー';
            lastUB = ub;
        }
        // trim a trail space if exists.
        if (lastSpace && dst.length() > 0) {
            dst.setLength(dst.length() - 1);
        }
        return dst.toString();
    }

    static boolean isZenkaku(UnicodeBlock ub) {
        if (ub == UnicodeBlock.KATAKANA) {
            return true;
        }
        if (ub == UnicodeBlock.HIRAGANA) {
            return true;
        }
        if (ub == UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS) {
            return true;
        }
        if (ub == UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION) {
            return true;
        }
        return false;
    }

    static {
        Normalizer.Builder b = new Normalizer.Builder();

        // map Zen-kaku chars to Han-kaku chars.
        b.putEach(
                "０１２３４５６７８９",
                "0123456789");
        b.putEach(
                "ＡＢＣＤＥＦＧＨＩＪＫＬＭＮＯＰＱＲＳＴＵＶＷＸＹＺ",
                "ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        b.putEach(
                "ａｂｃｄｅｆｇｈｉｊｋｌｍｎｏｐｑｒｓｔｕｖｗｘｙｚ",
                "abcdefghijklmnopqrstuvwxyz");
        b.putEach(
                "　！”＃＄％＆’（）＊＋，－．／：；＜＝＞？＠［￥］＾＿｀｛｜｝",
                " !\"#$%&'()*+,-./:;<=>?@[¥]^_`{|}");

        // map Han-kaku chars to Zen-kaku chars
        b.putEach(
                "｡､･｢｣ｰ",
                "。、・「」ー");
        b.putEach(
                "ｱｲｳｴｵｶｷｸｹｺｻｼｽｾｿﾀﾁﾂﾃﾄﾅﾆﾇﾈﾉﾊﾋﾌﾍﾎﾏﾒﾐﾑﾓﾔﾕﾖﾗﾘﾙﾚﾛﾜｦﾝ",
                "アイウエオカキクケコサシスセソタチツテトナニヌネノハヒフヘホマメミムモヤユヨラリルレロワヲン");
        b.putEach(
                "ｧｨｩｪｫｬｭｮｯ",
                "ァィゥェォャュョッ");
        b.putEach(
                "ｳﾞｶﾞｷﾞｸﾞｹﾞｺﾞｻﾞｼﾞｽﾞｾﾞｿﾞﾀﾞﾁﾞﾂﾞﾃﾞﾄﾞﾊﾞﾋﾞﾌﾞﾍﾞﾎﾞﾊﾟﾋﾟﾌﾟﾍﾟﾎﾟ", 2,
                "ヴガギグゲゴザジズゼゾダヂヅデドバビブベボパピプペポ", 1);

        // hyphen/minus
        b.putMap("-",
                "\u02d7", "\u058a", "\u2010", "\u2011", "\u2012", "\u2013",
                "\u2043", "\u207b", "\u208b", "\u2212");

        // Zen-kaku 長音
        b.putMap("ー",
                "\u2014", "\u2015", "\u2500", "\u2501", "\ufe63", "\uff70");

        // remove tilde like characters
        b.putMap("", "~", "∼", "∾", "〜", "〰", "～");

        normalizer = b.build();
    }
}
