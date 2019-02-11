<a href="https://gochain.io"><img src="color_logo_transparent.png" width="480"></a>

# GoChain Docs

Official GoChain documentation.

## GoChain Users

If you want to send GO tokens or interact with smart contracts on GoChain, this section is for you.

* [GoChain Wallet](https://wallet.gochain.io)
* [Block Explorer](https://explorer.gochain.io)

You can also use existing Ethereum tools:

* [MyEtherWallet](https://medium.com/gochain/gochain-is-now-available-on-myetherwallet-e392c7f5c9a2)
* [MyCrypto](https://medium.com/gochain/gochain-is-now-on-mycrypto-com-144a52c7d5ce)
* [Trust Wallet](https://medium.com/gochain/trust-wallet-now-fully-supports-gochain-including-gochain-based-tokens-assets-2ba28a080c2d)
* [TurboWallet](https://turbowallet.io)
* [How to use GoChain with MetaMask](https://medium.com/gochain/how-to-use-gochain-with-metamask-23a258ae39c5)

## Smart Contract / DApp Developers

If you are developing smart contracts or dapps to deploy to GoChain, you'll find the information you need here.

### Who's Using GoChain

There are many great projects developing or already deployed on GoChain, [check them out here](https://help.gochain.io/en/article/whos-using-gochain-wtr9u1/).

### Developing on GoChain

* [GoChain TestNet Information](public-network/testnet/)
* [How to Deploy a Smart Contract to GoChain in 5 Minutes](https://medium.com/gochain/how-to-deploy-a-smart-contract-in-5-minutes-bed2443be23c)
* [Learn why GoChain fees are 7500x cheaper than Ethereum](https://medium.com/gochain/gochain-transaction-fees-are-at-least-7500x-less-than-ethereum-3b7060743717)

### Network RPC URLs

When interacting with GoChain, the easiest way is to use our hosted JSON-RPC API's. The base URLs
are:

* GoChain TestNet - https://testnet-rpc.gochain.io
* GoChain MainNet - https://rpc.gochain.io

### Running a Public Network Node

[Node Documentation](public-network/nodes/)

## Private Networks

If you'd like to run your own GoChain private blockchain network, click here.

[Learn why GoChain is the best option for an Ethereum/web3 compatible private network](https://medium.com/gochain/ethereum-vs-gochain-private-network-showdown-d094096e7d88).

Learn how to [deploy your own GoChain network here](private-networks).

# Running a Node

If you'd like someone else to setup and manage your GoChain node, please check out [Blockdaemon](https://blockdaemon.com/). With three clicks
you'll have a node up and running and they'll make sure it stays running for you.

If you want to do it yourself, please continue below.

This directory contains instructions for configuring and running GoChain with `docker-compose` on the `testnet`, `mainnet`, a private network, or a local development instance.

Instructions for running a *signing* node are [here](../signers/nodes).

## Prerequisites

Install `docker` and `docker-compose`.

* Docker > 18.0 ([install](https://docs.docker.com/install/))
* Docker-compose ([install](https://docs.docker.com/compose/install/))

<details>
  <summary>Simple Install Instructions</summary>

Docker:

```sh
sudo rm /var/lib/apt/lists/*
sudo apt-get update
curl -fsSL https://get.docker.com/ | sudo sh
docker info
```

Docker Compose:

```sh
curl -L https://github.com/docker/compose/releases/download/1.21.2/docker-compose-`uname -s`-`uname -m` -o /usr/local/bin/docker-compose
chmod +x /usr/local/bin/docker-compose
docker-compose --version
```
</details>

## Initial Configuration

\**Note: If you are moving from the testnet to the mainnet, it is best to start fresh in a new folder.*

1. Copy `docker-compose.yml` into your folder from either the [`testnet`](testnet) or [`mainnet`](mainnet) directory.
2. (Optional) Create a file `.env` to override the default variables: (see [`example.env`](example.env) for more details)
```
GOCHAIN_TAG=2.1.16
GOCHAIN_CACHE=2048
```
3. Launch `docker-compose`

```sh
docker-compose up -d
```

4. Make sure that node works.

```sh
docker logs -f node
```

## Common Commands

- Start: `docker-compose up -d`
- Stop: `docker-compose down`
- Follow Logs: `docker logs -f --tail 100 node`
- Restart Container: `docker-compose restart node`
- Restart All: `docker-compose down && docker-compose up -d`
- Console Attach: `docker run --rm -it -v $PWD:/gochain -w /gochain gcr.io/gochain-core/gochain gochain --datadir /gochain/node attach`
- Update image: `docker-compose pull`

### Console Commands

- Enode: `admin.nodeInfo.enode`
- Balance: `eth.getBalance('0xabcd')`
- Coinbase Balance (rewards): `eth.getBalance(eth.coinbase)`
- Send Transaction (transfer rewards): `eth.sendTransaction({from:eth.coinbase,to:'0xabcd',value:1000000000000000000})`

More info on the console is available here: https://github.com/ethereum/go-ethereum/wiki/JavaScript-Console

## Troubleshooting

If you are unable to diagnose a problem, you can try these steps in escalating order:

1) Repair: `docker-compose up -d`
2) Restart node: `docker-compose restart node`
3) Restart all: `docker-compose down && docker-compose up -d`
4) Restart docker: `service docker restart && docker-compose up -d`
5) Reboot machine, `docker-compose up -d`