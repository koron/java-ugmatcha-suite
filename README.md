# ugmatcha suite

ugmatcha suite consists of these sub projects:

1. [wikidict](./wikidict)
2. [normalizer](./normalizer)
3. [trietree](./trietree)

See each projects for details.

## Pre-requirements

java-ugmatcha-suite/trietree is available on [GitHub Packages][gp].
([Japanese version][gp-ja])

[gp]:https://docs.github.com/en/packages
[gp-ja]:https://docs.github.com/ja/packages

### for Maven

1.  Create a personal access token with `read:packages` permission at <https://github.com/settings/tokens>

2.  Put username and token to your ~/.m2/settings.xml file with `<server>` tag.

    ```pom
    <settings>
      <servers>
        <server>
          <id>github</id>
          <username>USERNAME</username>
          <password>YOUR_PERSONAL_ACCESS_TOKEN_WITH_READ</password>
        </server>
      </servers>
    </settings>
    ```

3.  Add a repository to your `repositories` section in project's pom.xml file.

    ```pom
    <repository>
      <id>github</id>
      <url>https://maven.pkg.github.com/koron/java-ugmatcha-suite</url>
    </repository>
    ```

4.  Add a `<dependency>` tag to your `<dependencies>` tag.

    ```pom
    <dependency>
      <groupId>net.kaoriya.ugmatcha</groupId>
      <artifactId>wikidict</artifactId>
      <version>0.0.3</version>
    </dependency>
    ```

Please read [public document](https://docs.github.com/en/packages/guides/configuring-apache-maven-for-use-with-github-packages) also. ([Japanese](https://docs.github.com/ja/packages/guides/configuring-apache-maven-for-use-with-github-packages))

### for Gradle

1.  Create a personal access token with `read:packages` permission at <https://github.com/settings/tokens>

2.  Put username and token to your ~/.gradle/gradle.properties file.

    ```
    gpr.user=YOUR_USERNAME
    gpr.key=YOUR_PERSONAL_ACCESS_TOKEN_WITH_READ:PACKAGES
    ```

3.  Add a repository to your `repositories` section in build.gradle file.

    ```groovy
    maven {
        url = uri("https://maven.pkg.github.com/koron/java-ugmatcha-suite")
        credentials {
            username = project.findProperty("gpr.user") ?: System.getenv("USERNAME")
            password = project.findProperty("gpr.key") ?: System.getenv("TOKEN")
        }
    }
    ```

4.  Add an `implementation` to your `dependencies` section.

    ```groovy
    implementation 'net.kaoriya.ugmatcha:wikidict:0.0.3'
    ```

Please read [public document](https://docs.github.com/en/packages/guides/configuring-gradle-for-use-with-github-packages) also. ([Japanese](https://docs.github.com/ja/packages/guides/configuring-gradle-for-use-with-github-packages)).

## Developping memo

tmp/ に wikiwords.stt と wikiwords.stw を置く。
両ファイルは <https://github.com/koron/wpwordtool> で作る。

tmp/ に in.txt を置く

```console
$ ./gradlew wikidict:matchDemo -Pargs='../tmp/in.txt' > tmp/out.txt
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
