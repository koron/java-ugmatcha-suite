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
                "！”＃＄％＆’（）＊＋，－．／：；＜＝＞？＠［￥］＾＿｀｛｜｝",
                "!\"#$%&'()*+,-./:;<=>?@[¥]^_`{|}");

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

        normalizer = b.build();
    }
}
