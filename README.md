![GenCompilation](https://github.com/warburec/GenCompilation/assets/36624401/f2852187-d57e-46ff-8644-894d77b28b3e)

### What is the project?

This project provides an easy-to-use and extensible framework for creating/prototyping languages and grammars.

It also provides support for atypical languages, e.g., sample languages typically used in education.

```
Example sample grammar in BNF:
S’ –> S
S –> AA
A –> aA
A –> b

May produce the sentence: aaaaaabb
```

### Aims of this project

This project aims to provide a foundation for fast prototyping of languages/grammar, reference/an education tool for students.

Compilation speed is not a main concern, although, components should be produced in a way so that they can be replaced for applications that require it.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Installing

Simply clone this project to get started.

```
git clone https://github.com/warburec/Generalised-Compilation.git
```

### Usage

```java
String input = """
x = 1 + 2;
y = x + 3;
x = y + 0;
""";

Compiler compiler = new CompilerBuilder()
.setComponents(
    new ExampleLexicalAnalyserFactory(),
    new ExampleParserFactory(),
    new ExampleCodeGenFactory(),
    new ExampleGrammarBundle()
)
.createCompiler();

String output = compiler.compile(input);
```

## Running the tests

Run the tests with Gradle using the gradle wrapper:

```shell
./gradlew test
```

This project uses [JUnit](https://junit.org/junit5/).
Tests can be found in the [test](test) folder.

If you're using VSCode, it is recommended to use the VSCode [Test Runner for Java](https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-java-test) extension.

## Contributing

Make a pull request. Make an issue for large changes. Ensure unit tests are provided for your code.

## Author

<a href="https://github.com/warburec">
    <span style="display: block;">
        <img src="https://images.weserv.nl/?url=avatars.githubusercontent.com/u/77669019?v=4&fit=cover&mask=circle&maxage=7d" style="width:8%;height:8%;vertical-align: middle;"/>
        <b style="vertical-align: middle;">Ewan Warburton</b>
    </span>
</a>

## Contributors

<a href="https://github.com/warburec/GenCompilation/graphs/contributors">
  <img src="https://contrib.rocks/image?repo=warburec/GenCompilation" />
</a>

## License

This project is licensed under GNU GPLv3 - see the [LICENSE.md](LICENSE.md) file for details
