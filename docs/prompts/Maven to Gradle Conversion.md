# Maven to Gradle Conversion

- Exported: 19.3.2026, 14:30:16
- Source: https://chatgpt.com/c/69bbf955-4110-832a-93f3-972612c116d0
- Messages: 2

**You:**

convert to gradle?

```XML
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-surefire-plugin</artifactId>
    <version><!--...--></version>
    <configuration>
        <argLine>
            --add-opens java.base/java.util=ALL-UNNAMED
            --add-opens java.base/java.lang=ALL-UNNAMED
        </argLine>
    </configuration>
</plugin>
```

* * *

**ChatGPT:**

You’re trying to translate a Maven plugin configuration for **`maven-surefire-plugin`** into Gradle. In Gradle, this is usually done in the `test` task configuration. The `<argLine>` in Maven maps to `jvmArgs` in Gradle.

Here’s the equivalent in **Gradle (Groovy DSL)**:

```gradle
tasks.test {  
    // JVM arguments to open modules for reflection (like Maven argLine)  
    jvmArgs += [  
        '--add-opens', 'java.base/java.util=ALL-UNNAMED',  
        '--add-opens', 'java.base/java.lang=ALL-UNNAMED'  
    ]  
}
```

If you’re using **Gradle Kotlin DSL** (`build.gradle.kts`):

```Kotlin
tasks.test {  
    // JVM arguments to open modules  
    jvmArgs = listOf(  
        "--add-opens=java.base/java.util=ALL-UNNAMED",  
        "--add-opens=java.base/java.lang=ALL-UNNAMED"  
    )  
}
```

✅ Notes:

* No need to specify a plugin version like Maven; Gradle’s built-in `Test` task handles it.
* These `--add-opens` flags are required if your tests use reflection on JDK internal APIs.

If you want, I can also show the **full Gradle setup with a specific Java version** that matches your Maven config. Do you want me to do that?