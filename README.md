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
    implementation 'net.kaoriya.ugmatcha:wikidict:0.0.2'
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
    implementation 'net.kaoriya.ugmatcha:wikidict:0.0.2'
}
```

## Benchmark

Input data is consisted from Japanese Wikipedia's abstracts of all page.
See <https://github.com/koron/wpwordtool#abstract-sub-command> for details.

```console
$ ./gradlew wikidict-benchmark:benchmarkMatcher -Pargs=../tmp/abstract.txt
benchmark with file:../tmp/abstract.txt
control:
  total: 0.504289 seconds
  average_per_line: 435 nanoseconds
  lineCount: 1157686
matcher:
  total: 11.130144 seconds
  average_per_line: 9614 nanoseconds
  lineCount: 1157686
```
