# Arkane Blockchain Providers

**Arkane Blockchain Providers** are a set of APIs and libraries which support interaction with various blockchains.

## Building the libraries   

**Prerequisite**: Java 8 + Maven

```bash
mvn clean install
```

## Contributing?

Do you want support for a new chain or do you want to implement the chain?

Talk to us in [Telegram](https://t.me/ArkaneNetworkiOfficial)! You can just fork this repository, add your own chain submit a PR! After review, we'll 
add it to Arkane Network.

For more details on how to add a new chain, [https://github.com/ArkaneNetwork/blockchain-providers/wiki/Adding-a-new-chain](visit our Wiki).

## Modules

### Provider API

The provider API is the actual API. All provider submodules needs to implement the interfaces of the API.

### Provider AutoConfiguration

**Spring Boot Autoconfiguration** module. Using this module, spring will automatically populate all required elements to interact with the various
blockchain submodules.

### Provider XXX 

For every supported blockchain, a new provider is added. 