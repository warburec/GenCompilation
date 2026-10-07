# Dynamic Storage
A system for storing of objects dynamically within Java.

## How it works
Objects can produce a `StroageValue<?>` representation of their state. This state can be passed to the `Storage` helper to be stored to a stream or file (and formatted as required).

Stored state may be loaded through the `Storable` helper, and may be used to hydrate objects through the `Loadable` interface.

```java
Storage storage = new Storage()
    .setTargetPath("./testFilepath.bin")
    .setFileEditor(new ExampleStreamEditor())
    .setFormatter(new ExampleValueFormatter());

storage.store(someStorable);
storage.loadInto(new ExampleLoadable());
```

## Additional Features
The [dynamic_loading](/src/main/java/storage/dynamic_loading/) folder holds helpful features to enable dynamic loading of complex objects.

`Factory` methods may be defined for constructing objects, even if they do not implement `Loadable`. They may be defined as lambdas, making it convenient for users to define and interchange them.

`Loader` classes can be created which are simply `Factory` implementations for instantiating objects of a specific type. It is expected that `Loader` classes contain an empty constructor so they may be instantiated through reflection.

Classes may implement `LoadableBy` to explicitly link them to a specific `Loader`. When correctly defined, a `Loader` object may be instantiated through this reference (via reflection) and used to instantiate objects of the original class.

A class may implement `ReflectivelyLoadable` and a constructor intended for reflective use (see [ReflectiveClassConstructor](/src/main/java/storage/dynamic_loading/ReflectiveClassConstructor.java)). Objects of these classes can be loaded using `ReflectiveClassConstructor`.

## Diagrams
### Usage Pipeline
![Usage Pipeline Diagram](/docs/diagrams/StorageUsagePipeline.drawio.svg)

### Architecture
![Architecture Diagram](/docs/diagrams/Storage.drawio.svg)