# Arkane Blockchain Providers<img align="right" src="https://github.com/ArkaneNetwork.png?size=30" />
> Arkane Blockchain Providers are a set of APIs and libraries which support interaction with various blockchains.

If you are a Java developer and want to add support for a new blockchain in Arkane, first of all great, awesome 🎉!!
Secondly we like to invite you to our [Telegram](https://t.me/ArkaneNetworkOfficial) channel so we can answer any questions you may have and help you where needed.

## Why add a blockchain provider?
Arkane network consists out of multiple building blocks, by adding support for an extra blockchain, the blockchain is not only supported by the [Arkane web wallet](https://arkane.network) but by **ALL** Arkane components. Allowing the creation of userfriendly blockchain applications without the need for browser extensions. 


![Arkane Components](https://i.imgur.com/T5sWhZa.png)


* [Example of a blockchain crypto pooling service build with Arkane (watch video 🍿)](https://www.youtube.com/watch?v=qz3cUEYYtU0)

## Getting Started
* [Building the project](https://github.com/ArkaneNetwork/blockchain-providers/wiki/Building-The-Project)
* [Adding a new chain](https://github.com/ArkaneNetwork/blockchain-providers/wiki/Adding-a-new-chain)

## Modules

### Provider API

The provider API is the actual API. All provider submodules needs to implement the interfaces of the API.

### Provider AutoConfiguration

**Spring Boot Autoconfiguration** module. Using this module, spring will automatically populate all required elements to interact with the various
blockchain submodules.

### Provider XXX 

For every supported blockchain, a new provider is added. 

## Chains already implemented
* [Ethereum](https://github.com/ArkaneNetwork/blockchain-providers/tree/master/provider-ethereum "Have a look at the code")
* [VeChain](https://github.com/ArkaneNetwork/blockchain-providers/tree/master/provider-vechain "Have a look at the code")

# What is Arkane Network
Not sure yet what Arkane Network is all about, where are some resources to help you get a better view:
* [An eli5 about Arkane](https://medium.com/arkane-network/eli5-arkane-network-44bb10d0e68f)
* [What is Arkane and what can it do for me?](https://medium.com/arkane-network/what-is-arkane-network-ad536e9984a1)
* [Our wallet security explained](https://medium.com/arkane-network/wallet-security-explained-5b540d746583)
* [I'm a developer what can Arkane do for me? (Watch video)](https://www.youtube.com/watch?&v=fsBZg450drQ)
* [I'm a crypto-enthusiasts what can Arkane do for me? (Watch video)](https://www.youtube.com/watch?v=XIAi4lFcolo)

[![alt text](https://i.imgur.com/L1ZDzlH.png)](https://www.youtube.com/watch?&v=fsBZg450drQ " I’m a developer what can Arkane do for me?")





