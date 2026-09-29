# Dynamic Storage
A system for storing of objects dynamically within Java.

## How it works
Objects can produce a `StroageValue<?>` representation of their state. This state can be passed to the `Storage` helper to be stored to a stream or file (and formatted as required).

Stored state may be loaded through the `Storable` helper, and may be used to hydrate objects through the `Loadable` interface.

## Diagrams
### Usage Pipeline
![Usage Pipeline Diagram](/docs/diagrams/StorageUsagePipeline.drawio.svg)

### Architecture
![Architecture Diagram](/docs/diagrams/Storage.drawio.svg)