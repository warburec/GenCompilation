# GenComp

### What is the project?
Java project. 

Provides an easy-to-use and extensible framework for creating/prototyping languages and grammars.

Provides support for atypical languages, e.g., sample languages typically used in education.

```
Example sample grammar in BNF:
S’ –> S
S –> AA
A –> aA
A –> b

May produce the sentence: aaaaaabb
```

### Aims of this project

This project aims to provide a foundation for fast prototyping of langauges/grammar, reference/an education tool for students, and a sandbox. 

Compilation speed is not a main concern, although, compnents should be produced in a way so that they can be replaced for applications that require it.

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

JUnit tests
Use https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-java-test in VScode

Explain how to run the automated tests for this system

### Break down into end to end tests

Explain what these tests test and why

```
Give an example
```

### And coding style tests

Explain what these tests test and why

```
Give an example
```

## Contributing

Please read [CONTRIBUTING.md](https://gist.github.com/PurpleBooth/b24679402957c63ec426) for details on our code of conduct, and the process for submitting pull requests to us.

Make a pull request. Make an issue for large changes. Ensure unit tests are provided for your code.

## Versioning

We use [SemVer](http://semver.org/) for versioning. For the versions available, see the [tags on this repository](https://github.com/your/project/tags). 

## Authors

* **Ewan Warburton** - *Initial work*

See also the list of [contributors](https://github.com/your/project/contributors) who participated in this project.

## License

This project is licensed under GNU GPLv3 - see the [LICENSE.md](LICENSE.md) file for details

## Acknowledgments

* Hat tip to anyone whose code was used
* Inspiration
* etc