# trietree package

trietree is an implementation of trie for Java.
It provides some text matching algorithms.

* Aho-Corasick
* Longest prefix match

## Getting started

See [pre-requirements](#pre-requirements) also.

### Logest prefix match

``` java
import net.kaoriya.ugmatcha.trietree.DynamicTree;
import net.kaoriya.ugmatcha.trietree.StaticTree;

// create a dynamic tree
var dt = new DynamicTree();

// put all keywords
dt.put("foo");
dt.put("bar");
dt.put("baz");

// search against texts.
dt.longestPrefix("foo");    // returns "foo"
dt.longestPrefix("foobar"); // returns "foo"
dt.longestPrefix("quux");   // returns null (not match)
dt.longestPrefix("ba");     // returns null (not match)
dt.longestPrefix("barbaz"); // returns "bar"

// (option) convert to a static tree for memory compaction
var st = new StaticTree(dt);
st.longestPrefix("foo");    // returns "foo"
```

1. create a `DynamicTree`
2. add words to match
3. call `longestPrefix()` method

### Find all words in a text

```java
import net.kaoriya.ugmatcha.trietree.DynamicTree;
import net.kaoriya.ugmatcha.trietree.ScanEvent;

// create a dynamic tree
var dt = new DynamicTree();

// put all keywords
dt.put("foo");
dt.put("bar");
dt.put("baz");

dt.scan("foo bar baz", (ScanEvent ev) -> {
  // detects "foo", "bar", "baz"
});
```

1. create a `DynamicTree`
2. add words to match
3. call `scan()` method with a consumer.

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
      <artifactId>trietree</artifactId>
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
    implementation 'net.kaoriya.ugmatcha:trietree:0.0.3'
    ```

Please read [public document](https://docs.github.com/en/packages/guides/configuring-gradle-for-use-with-github-packages) also. ([Japanese](https://docs.github.com/ja/packages/guides/configuring-gradle-for-use-with-github-packages)).

## How to release

1. update `version` in build.gradle
2. `./gradlew test`
3. `./gradlew publish`

    Set these properties with correct values in ~/.gradle/gradle.properties

    ```props
    gpr.user=
    gpr.key=
    ```
