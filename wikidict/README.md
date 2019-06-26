# How to generate dictionary from Wikipedia NS0

## The Procedure

1.  Download dictionary files:

    *   <https://dumps.wikimedia.org/jawiki/> - YYYYMMDD/jawiki-YYYYMMDD-all-title-in-ns0.gz
    *   <https://dumps.wikimedia.org/enwiki/> - YYYYMMDD/enwiki-YYYYMMDD-all-title-in-ns0.gz

2.  Normalize JA wiki
    1.  Just normalize.

3.  Normalize EN wiki
    1.  truncate words.
    2.  normalize.

4.  convert both wiki data into a trietree file.

## 使い方

wikidict を使うには以下のステップが必要です。

1. 辞書ファイルを準備する
    1.  辞書データをwikipediaよりダウンロードする
    2.  [wpwordtool][wpwordtool] で辞書データを変換する
    3.  辞書ファイル(wikiwords.stt & wikiwords.stw)を実行環境にデプロイする
2. wikidict を組み込む
    1.  レポジトリと依存関係の設定 (gradle)
    2.  辞書ファイルを指定して `Matcher` を作成する (Java code)
    3.  テキストから既知の単語一覧をとりだす

以下では各ステップの詳細を説明します。

### 1-1. 辞書データをwikipediaよりダウンロードする

Wikipediaのダンプサイトより最新のダンプを取得してください。
必要なダンプは「ネームスペース0の全見出し」を意味する `all-title-in-ns0` という種類で、
日本語版と英語版が必要です。

日本語版のダンプファイルは <https://dumps.wikimedia.org/jawiki/> より
`YYYYMMDD/jawiki-YYYYMMDD-all-title-in-ns0.gz` を取得してください。
YYYYMMDDには定期ダンプの日付が入りますが、
最新の定期ダンプ当日はダンプが出揃っていない場合があることに留意してください。
また
<https://dumps.wikimedia.org/jawiki/latest/jawiki-latest-all-titles-in-ns0.gz>
というリンクもあります。
こちらは更新条件・タイミングが不明です。
利用する際は留意してください。

英語版のダンプファイルは <https://dumps.wikimedia.org/enwiki/> より
`YYYYMMDD/enwiki-YYYYMMDD-all-title-in-ns0.gz` を取得してください。
YYYYMMDDの扱いは日本語版と同等です。
また
<https://dumps.wikimedia.org/enwiki/latest/enwiki-latest-all-titles-in-ns0.gz>
というリンクもあります。
こちらも日本語版同様に更新条件・タイミングが不明です。
利用する際は留意してください。

### 1-2. wpwordtool で辞書データを変換する

[wpwordtool][wpwordtool] はWikipediaのダンプデータを
wikidictで利用できる形に変換するツールです。
wpwordtoolはgolangで記述されているため
コンパイルにはgolang(1.12以降の最新版を推奨)が必要です。

wpwordtool の取得・更新には以下のコマンドを実行してください。

```console
$ GO111MODULE=on go get -u -i github.com/koron/wpwordtool
```

上記がうまくいかない場合は以下のコマンドも試してみてください。

```console
$ go get -u -i github.com/koron/wpwordtool
```

正しく取得ができていれば wpwordtool は `~/go/bin/wpwordtool` に配置されています。
このファイルは同じOSであればコピーするだけで別のコンピューターで動かせます。

変換するには以下のコマンドを実行してください。

```console
$ wpwordtool convert \
    -ja jawiki-20190601-all-titles-in-ns.0.gz \
    -en enwiki-20190601-all-titles-in-ns.0.gz \
    -out wikiwords
```

この例ではカレントディレクトリの辞書ダンプの
`jawiki-20190601-all-titles-in-ns.0.gz` と
`enwiki-20190601-all-titles-in-ns.0.gz` を読み込んで、
カレントディレクトリに辞書ファイルの `wikiwords.stt` と `wikiwords.stw` を出力します。

`20190601` のところは任意の定期ダンプ日時に変更してください。
また `-ja` もしくは `-en` を省略した場合は
それぞれ `20190601` の代わりに `latest` が指定されます。

`-out foo` とした場合には `foo.stt` と `foo.stw` を出力します。
つまり `-out` はファイルのベース名として利用します。
省略した場合はデフォルトで `tmp/wikiwords` をベース名とします。

### 1-3. 辞書ファイルを実行環境にデプロイする

必要に応じた方法で辞書ファイル `wikiwords.stt` と `wikiwords.stw` を
wikidict の実行環境にデプロイしてください。

### 2-1. レポジトリと依存関係の設定

**TO BE WRITTEN**

### 2-2. 辞書ファイルを指定して `Matcher` を作成する

以下に `wikidict.Matcher` を作成するスニペットを示します。
このコード例では `/var/wikidict` ディレクトリに辞書ファイル
`wikiword.stt` と `wikiword.stw` が置いてあることを仮定しています。


```java
import net.kaoriya.ugmatcha.wikidict.Matcher;

import java.io.File;
import java.io.IOException;

try {
  Matcher m = Matcher.load(new File("/var/wikidict/wikiword"));
  // TODO: work with "m".
} catch (IOException e) {
  // TODO: handle the IOException.
}
```

`Matcher.load(File)` スタティックメソッドは
辞書ファイルを読み込んで `Matcher` のインスタンスを作成します。
`Matcher.load(File, File)` を使えば
2つの辞書ファイルをそれぞれ個別に指定できます。

### 2-3. テキストから既知の単語一覧を取り出す

ここまで完了すれば `Matcher.match(String)` メソッドを使って
テキスト内の既知単語を高速に取得できます。
以下にその例を示します。

```java
// TODO: load target text to "text"
String[] words = m.match(text);
// TODO: work with "words".
```

また以下のスニペットは
テキストファイルから1行ずつ単語を検出して
`{元の文}\t{検出単語1}\t{検出単語2}\t...{検出単語n}\n`
というフォーマットで出力する例となります。

```java
import net.kaoriya.ugmatcha.wikidict.Matcher;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public void printMatches(Matcher m, File f) throws IOException {
  try (FileInputStream fs = new FileInputStream(f);
       InputStreamReader r0 = new InputStreamReader(fs, "UTF-8");
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
```

[wpwordtool]:https://github.com/koron/wpwordtool
