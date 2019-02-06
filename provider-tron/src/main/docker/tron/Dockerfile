FROM debian:jessie

EXPOSE 50051
EXPOSE 18891

VOLUME /tron

ADD https://raw.githubusercontent.com/tronprotocol/TronDeployment/master/deploy_tron.sh /tron.sh

CMD ["bash", "/tron.sh", "--app", "FullNode", "--net", "testnet", "--heap_size", "16384m", "--work_space", "/tron"]