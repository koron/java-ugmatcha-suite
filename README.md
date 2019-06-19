# ugmatcha suite

## Developping memo

tmp/ に wikiwords.stt と wikiwords.stw を置く。
両ファイルは <https://github.com/koron/wpwordtool> で作る。

tmp/ に in.txt を置く

```console
$ ./gradlew wikidict:matchDemo -Pargs='../tmp/in.txt' > tmp/out.txt
```
