# アルゴリズム解説

wikidict はテキストから Wikipedia の ns0 に属する全記事タイトルを検出できる
高速な`Matcher` です。
wikidict 自体の使用方法は [wikidict/README.md](https://github.com/koron/java-ugmatcha-suite/blob/master/wikidict/README.md) を参照してください。

wikidict は [trietree (トライ木)][trietree] を基本データ構造にしています。
このトライ木に単語を登録したものに [Aho-Corasick法][aho] を適用することで
1パスで全単語を検出しています。
ただし完全に全単語を検出すると包含関係にある単語
(例: 携帯電話 ∋ 電話, インターネット ∋ ネット, JavaScript ∋ Java)
がそれぞれ別のものととして認識されて都合が悪いので、
最左最長でマッチするように後処理をしています。

```
# 例文
携帯電話がインターネットにつながりJavaScriptを実行できる

# 検出される単語
携帯電話
インターネット
つながり
JavaScript
実行
```

単語は登録前に、テキストはスキャン前にそれぞれ正規化を行って
表記揺れの吸収を試みています。
正規化には [nelogd が提唱しているアルゴリズム][neologd-regexp] を
ベースにわずかに改変したものを用いています。
実装は normalizer パッケージで提供しています。

## トライ木

多分木でルートノードを空文字列とし、1ノードが1文字に相当します。
たとえば「あさ」「あし」「あしか」「いみ」「う」の5単語を登録したトライ木は
以下のようになります。

```
      (空)
        |
    +---+--+---+
    |      |   |
   あ     い  う*
    |      |
 +--+--+  み*
 |     |
さ*   し*
       |
      か*
```

`*` はそのノードに単語があることを示し、
そのノードから上に辿った全ノードの文字を足し合わせたものが単語自身となります。
(例: `か→し→あ→(空)` → `あしか`)

このトライ木のルートから、対象テキストの先頭から1文字づつ探してたどっていけば、
単語が含まれているかどうかがわかります。
なお各ノードで特定の文字が見つからなかったケースを
後述の Aho-Corasick法 でカバーすることで、
1パス=バックトラッキングなしで全単語を検出できるようにしています。

trietree パッケージではトライ木の2つの実装を提供しています。
1ノードを1オブジェクトとして表現する DynamicTree と、
各種データを配列に詰め替えた StaticTree です。
DynamicTree は単語を追加できるのに加えて、StaticTreeを生成することができます。
もう一方の StaticTree は単語の追加はできませんが、ファイルへの書き出しとファイルからの読み出しができます。
Javaは大量のオブジェクトを利用するとパフォーマンスが悪化するため、
オブジェクトの使用量が大きくなる DynamicTree を検索時に利用するのは非効率です。
そのためにプリミティブの配列をベースにした
StaticTree を提供して少ないオブジェクト数で検索を実行できるようにしています。

## Aho-Corasick法

トライ木の各階層(=ノード)において、
対象となる文字が見つからなかったときの遷移先ノードを決めます。
これにより部分文字列などを効率よく発見できます。

詳細な解説は <https://naoya-2.hatenadiary.org/entry/20090405/aho_corasick> を参照してください。

## 最左最長

TBW: 最左最長をO(n)で計算していることを解説。

概要

1. `(単語ID, 単語終端位置)` の配列をテキストの文字数分用意する
2. 単語が見つかったら1の配列の単語先頭に相当する場所に単語とその終端位置を記録する
3. 配列の先頭から見ていき、単語があったら単語を出力し次の検索位置をその単語の終端位置+1とする

## golang版

同様のアルゴリズムの golang 版を [koron-go/trietree][go-trietree] として実装しました。
また Java の StaticTree に読み込ませるファイルを作るためのツール [wpwordtool][wpwordtool] も golang で実装しました。
これらは前述したように Java の DynamicTree で Wikipedia の
全記事のタイトルを扱おうとするとオブジェクト数が多くなり
時間が長くなってしまうのを避けるためです。

そのために wikidict で利用する辞書の生成には
golang 製のツールが必要となっています。

[trietree]:https://ja.wikipedia.org/wiki/%E3%83%88%E3%83%A9%E3%82%A4%E6%9C%A8
[aho]:https://ja.wikipedia.org/wiki/%E3%82%A8%E3%82%A4%E3%83%9B%E2%80%93%E3%82%B3%E3%83%A9%E3%82%B7%E3%83%83%E3%82%AF%E6%B3%95
[neologd-regexp]:https://github.com/neologd/mecab-ipadic-neologd/wiki/Regexp.ja
[go-trietree]:https://github.com/koron-go/trietree
[wpwordtool]:https://github.com/koron/wpwordtool