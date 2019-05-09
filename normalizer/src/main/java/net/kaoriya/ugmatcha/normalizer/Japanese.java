package net.kaoriya.ugmatcha.normalizer;

/**
 * Japanese normalizer for ugmatcha.
 */
public class Japanese {

    public static final Normalizer normalizer;

    /**
     * Normalize a string as Japanese.
     */
    public static String normalize(String s) {
        return normalizer.normalize(s);
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
                "ｱｲｳｴｵｶｷｸｹｺｻｼｽｾｿﾀﾁﾂﾃﾄﾅﾆﾇﾈﾉﾊﾋﾌﾍﾎﾏﾒﾐﾑﾓﾔﾕﾖﾗﾘﾙﾚﾛﾜｵﾝ",
                "アイウエオカキクケコサシスセソタチツテトナニヌネノハヒフヘホマメミムモヤユヨラリルレロワオン");
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
                "\u2014", "\u2015", "\u2500", "\u2501", "\ufe63", "\uff0d",
                "\uff70");

        // remove tilde like characters
        b.putMap("", "~", "∼", "∾", "〜", "〰", "～");
        // TODO: add more chars.

        normalizer = b.build();
    }
}
