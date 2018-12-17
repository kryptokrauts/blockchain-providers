# Arkane Blockchain Providers

**Arkane Blockchain Providers** are a set of APIs and libraries which support interaction with various blockchains.

## Building the libraries   

**Prerequisite**: Java 8 + Maven

```bash
mvn clean install
```

## Modules

### Provider API

The provider API is the actual API. All provider submodules needs to implement the interfaces of the API.

### Provider AutoConfiguration

**Spring Boot Autoconfiguration** module. Using this module, spring will automatically populate all required elements to interact with the various
blockchain submodules.

### Provider XXX 

For every supported blockchain, a new provider is added.