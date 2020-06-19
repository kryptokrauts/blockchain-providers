<p align="center">
  <a href="https://kryptokrauts.com">
    <img alt="kryptokrauts" src="https://kryptokrauts.com/img/logo.svg" width="60" />
  </a>
</p>
<h1 align="center">
  kryptokrauts.com
</h1>

## How to start an [æternity](https://github.com/aeternity/aeternity) node with docker

- [official docs](https://github.com/aeternity/aeternity/blob/master/docs/docker.md)

## Configurable properties for the blockchain-provider
```
## URL that points to the node-api (default: https://sdk-testnet.aepps.com)
network.arkane.aeternity.node.api.baseUrl=https://sdk-testnet.aepps.com

## URL that points to the compiler-api (default: https://compiler.aepps.com)
network.arkane.aeternity.compiler.api.baseUrl=https://compiler.aepps.com

## URL that points to the aeternal-middleware-api (default: https://testnet.aeternal.io)
network.arkane.aeternity.aeternal.api.baseUrl=https://testnet.aeternal.io

## Network (MAINNET | TESTNET | DEVNET)
network.arkane.aeternity.network=TESTNET

## Virtual Machine (FATE | AEVM)
network.arkane.aeternity.vm=FATE
```

There are further configuration properties available but these are the most important. We refer to the SDK documentation to read more.

## How to use the [aepp-sdk-java](https://github.com/kryptokrauts/aepp-sdk-java)
The documentation is available here:
- https://kryptokrauts.gitbook.io/aepp-sdk-java

## Support us
If you like the æternity integration we would appreciate your support. You can find multiple ways to support us here:

- https://kryptokrauts.com/support