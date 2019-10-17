# provider-aeternity

## how to start an [æternity](https://github.com/aeternity/aeternity) node with docker

- [official docs](https://github.com/aeternity/aeternity/blob/master/docs/docker.md)
    - a local network e.g. can be easily started through cloning the repository and starting the local 3 node setup via docker-compose (located in the root folder)

## configurable properties for the blockchain-provider
```
## URL that points to the node-api (default: https://sdk-testnet.aepps.com/v2)
network.arkane.aeternity.node.api.baseUrl=http://localhost/v2
## URL that points to the compiler-api (default: https://compiler.aepps.com)
network.arkane.aeternity.compiler.api.baseUrl=http://localhost:3080
## Network (MAINNET | TESTNET | DEVNET)
network.arkane.aeternity.network=DEVNET
## Virtual Machine (FATE | AEVM)
network.arkane.aeternity.vm=FATE
```

## how to use the [aepp-sdk-java](https://github.com/kryptokrauts/aepp-sdk-java)
the documentation is available here:
- https://kryptokrauts.gitbook.io/aepp-sdk-java/

a showcase application demonstrating the features of the java sdk can be found here:
- https://github.com/kryptokrauts/aepps-sdk-showcase

## support us
visit https://kryptokrauts.com

use [ArkaneNetwork](https://arkane.network) and send any coin or token to:
- kryptokrauts@protonmail.com
