# ugmatcha suite

## Developping memo

tmp/ に wikiwords.stt と wikiwords.stw を置く。
両ファイルは <https://github.com/koron/wpwordtool> で作る。

tmp/ に in.txt を置く

```console
$ ./gradlew wikidict:matchDemo -Pargs='../tmp/in.txt' > tmp/out.txt
```

## How to Use

Merge below description to your `build.gradle`

```gradle
repositories {
    jcenter()
}

dependencies {
    compile 'net.kaoriya.ugmatcha:wikidict:0.0.2'
}
```

or try this description.

```gradle
repositories {
    maven {
        url 'https://dl.bintray.com/koron/maven'
    }
}

dependencies {
    compile 'net.kaoriya.ugmatcha:wikidict:0.0.2'
}
```
