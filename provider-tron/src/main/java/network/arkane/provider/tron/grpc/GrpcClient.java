package network.arkane.provider.tron.grpc;

import com.google.protobuf.ByteString;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.tron.api.GrpcAPI;
import org.tron.api.GrpcAPI.AccountNetMessage;
import org.tron.api.GrpcAPI.AccountPaginated;
import org.tron.api.GrpcAPI.AccountResourceMessage;
import org.tron.api.GrpcAPI.AssetIssueList;
import org.tron.api.GrpcAPI.BlockExtention;
import org.tron.api.GrpcAPI.BlockLimit;
import org.tron.api.GrpcAPI.BlockList;
import org.tron.api.GrpcAPI.BlockListExtention;
import org.tron.api.GrpcAPI.BytesMessage;
import org.tron.api.GrpcAPI.EmptyMessage;
import org.tron.api.GrpcAPI.ExchangeList;
import org.tron.api.GrpcAPI.NodeList;
import org.tron.api.GrpcAPI.NumberMessage;
import org.tron.api.GrpcAPI.PaginatedMessage;
import org.tron.api.GrpcAPI.ProposalList;
import org.tron.api.GrpcAPI.Return.response_code;
import org.tron.api.GrpcAPI.TransactionExtention;
import org.tron.api.GrpcAPI.TransactionList;
import org.tron.api.GrpcAPI.TransactionListExtention;
import org.tron.api.GrpcAPI.WitnessList;
import org.tron.api.WalletExtensionGrpc;
import org.tron.api.WalletGrpc;
import org.tron.api.WalletSolidityGrpc;
import org.tron.common.utils.Base58;
import org.tron.common.utils.ByteArray;
import org.tron.common.utils.Sha256Hash;
import org.tron.protos.Contract;
import org.tron.protos.Protocol.Account;
import org.tron.protos.Protocol.Block;
import org.tron.protos.Protocol.SmartContract;
import org.tron.protos.Protocol.Transaction;
import org.tron.protos.Protocol.TransactionInfo;

import javax.annotation.PostConstruct;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.tron.core.Wallet.addressValid;


@Slf4j
@Component
public class GrpcClient {

    private ManagedChannel channelFull = null;
    private ManagedChannel channelSolidity = null;

    private TronNodeProvider tronNodeProvider;

    @Getter
    private WalletGrpc.WalletBlockingStub blockingStubFull = null;
    @Getter
    private WalletSolidityGrpc.WalletSolidityBlockingStub blockingStubSolidity = null;
    @Getter
    private WalletExtensionGrpc.WalletExtensionBlockingStub blockingStubExtension = null;

    public GrpcClient(final TronNodeProvider tronNodeProvider) {
        this.tronNodeProvider = tronNodeProvider;
    }

    private void initializeFullNode() {
        channelFull = ManagedChannelBuilder.forTarget(tronNodeProvider.randomFullNode())
                                           .usePlaintext()
                                           .build();
        blockingStubFull = WalletGrpc.newBlockingStub(channelFull);
    }

    private void initializeSolidityNode() {
        channelSolidity = ManagedChannelBuilder.forTarget(tronNodeProvider.randomSolidityNode())
                                               .usePlaintext()
                                               .build();
        blockingStubSolidity = WalletSolidityGrpc.newBlockingStub(channelSolidity);
        blockingStubExtension = WalletExtensionGrpc.newBlockingStub(channelSolidity);
    }

    private boolean currentNodesAvailable() {
        try {
            return (this.channelFull != null && this.channelSolidity != null) && getBlockingStubFull()
                    .getNowBlock(GrpcAPI.EmptyMessage.getDefaultInstance())
                    .hasBlockHeader() &&
                   getBlockingStubSolidity()
                           .getNowBlock(GrpcAPI.EmptyMessage.getDefaultInstance())
                           .hasBlockHeader();
        } catch (final Exception ex) {
            log.error(ex.getMessage());
            return false;
        }
    }

    @Scheduled(fixedDelay = 60000)
    @PostConstruct
    public void updateNodeAvailability() {
        while (!currentNodesAvailable()) {
            log.error("fullnode or soliditynode is not available, looking for new ones");
            initializeFullNode();
            initializeSolidityNode();
        }
    }

    public void shutdown() throws InterruptedException {
        if (channelFull != null) {
            channelFull.shutdown().awaitTermination(5, TimeUnit.SECONDS);
        }
        if (channelSolidity != null) {
            channelSolidity.shutdown().awaitTermination(5, TimeUnit.SECONDS);
        }
    }

    public Transaction createTransaction(Contract.AccountUpdateContract contract) {
        return blockingStubFull.updateAccount(contract);
    }

    public TransactionExtention createTransaction2(Contract.AccountUpdateContract contract) {
        return blockingStubFull.updateAccount2(contract);
    }

    public Transaction createTransaction(Contract.SetAccountIdContract contract) {
        return blockingStubFull.setAccountId(contract);
    }

    public Transaction createTransaction(Contract.UpdateAssetContract contract) {
        return blockingStubFull.updateAsset(contract);
    }

    public TransactionExtention createTransaction2(Contract.UpdateAssetContract contract) {
        return blockingStubFull.updateAsset2(contract);
    }

    public Transaction createTransaction(Contract.TransferContract contract) {
        return blockingStubFull.createTransaction(contract);
    }

    public TransactionExtention createTransaction2(Contract.TransferContract contract) {
        return blockingStubFull.createTransaction2(contract);
    }

    public Transaction createTransaction(Contract.FreezeBalanceContract contract) {
        return blockingStubFull.freezeBalance(contract);
    }

    public TransactionExtention createTransaction(Contract.BuyStorageContract contract) {
        return blockingStubFull.buyStorage(contract);
    }

    public TransactionExtention createTransaction(Contract.BuyStorageBytesContract contract) {
        return blockingStubFull.buyStorageBytes(contract);
    }

    public TransactionExtention createTransaction(Contract.SellStorageContract contract) {
        return blockingStubFull.sellStorage(contract);
    }

    public TransactionExtention createTransaction2(Contract.FreezeBalanceContract contract) {
        return blockingStubFull.freezeBalance2(contract);
    }

    public Transaction createTransaction(Contract.WithdrawBalanceContract contract) {
        return blockingStubFull.withdrawBalance(contract);
    }

    public TransactionExtention createTransaction2(Contract.WithdrawBalanceContract contract) {
        return blockingStubFull.withdrawBalance2(contract);
    }

    public Transaction createTransaction(Contract.UnfreezeBalanceContract contract) {
        return blockingStubFull.unfreezeBalance(contract);
    }

    public TransactionExtention createTransaction2(Contract.UnfreezeBalanceContract contract) {
        return blockingStubFull.unfreezeBalance2(contract);
    }

    public Transaction createTransaction(Contract.UnfreezeAssetContract contract) {
        return blockingStubFull.unfreezeAsset(contract);
    }

    public TransactionExtention createTransaction2(Contract.UnfreezeAssetContract contract) {
        return blockingStubFull.unfreezeAsset2(contract);
    }

    public Transaction createTransferAssetTransaction(Contract.TransferAssetContract contract) {
        return blockingStubFull.transferAsset(contract);
    }

    public boolean broadcastTransaction(Transaction signaturedTransaction) {
        int i = 10;
        GrpcAPI.Return response = blockingStubFull.broadcastTransaction(signaturedTransaction);
        while (response.getResult() == false && response.getCode() == response_code.SERVER_BUSY
               && i > 0) {
            i--;
            response = blockingStubFull.broadcastTransaction(signaturedTransaction);
            log.info("repeate times = " + (11 - i));
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (response.getResult() == false) {
            log.info("Code = " + response.getCode());
            log.info("Message = " + response.getMessage().toStringUtf8());
        }
        return response.getResult();
    }

    public Block getBlock(long blockNum) {
        if (blockNum < 0) {
            if (blockingStubSolidity != null) {
                return blockingStubSolidity.getNowBlock(EmptyMessage.newBuilder().build());
            } else {
                return blockingStubFull.getNowBlock(EmptyMessage.newBuilder().build());
            }
        }
        NumberMessage.Builder builder = NumberMessage.newBuilder();
        builder.setNum(blockNum);
        if (blockingStubSolidity != null) {
            return blockingStubSolidity.getBlockByNum(builder.build());
        } else {
            return blockingStubFull.getBlockByNum(builder.build());
        }
    }

    public long getTransactionCountByBlockNum(long blockNum) {
        NumberMessage.Builder builder = NumberMessage.newBuilder();
        builder.setNum(blockNum);
        if (blockingStubSolidity != null) {
            return blockingStubSolidity.getTransactionCountByBlockNum(builder.build()).getNum();
        } else {
            return blockingStubFull.getTransactionCountByBlockNum(builder.build()).getNum();
        }
    }

    public BlockExtention getBlock2(long blockNum) {
        if (blockNum < 0) {
            if (blockingStubSolidity != null) {
                return blockingStubSolidity.getNowBlock2(EmptyMessage.newBuilder().build());
            } else {
                return blockingStubFull.getNowBlock2(EmptyMessage.newBuilder().build());
            }
        }
        NumberMessage.Builder builder = NumberMessage.newBuilder();
        builder.setNum(blockNum);
        if (blockingStubSolidity != null) {
            return blockingStubSolidity.getBlockByNum2(builder.build());
        } else {
            return blockingStubFull.getBlockByNum2(builder.build());
        }
    }

    //  public Optional<AccountList> listAccounts() {
    //    AccountList accountList = blockingStubSolidity
    //        .listAccounts(EmptyMessage.newBuilder().build());
    //    return Optional.ofNullable(accountList);
    //
    //  }

    public Optional<WitnessList> listWitnesses() {
        if (blockingStubSolidity != null) {
            WitnessList witnessList = blockingStubSolidity
                    .listWitnesses(EmptyMessage.newBuilder().build());
            return Optional.ofNullable(witnessList);
        } else {
            WitnessList witnessList = blockingStubFull.listWitnesses(EmptyMessage.newBuilder().build());
            return Optional.ofNullable(witnessList);
        }
    }

    public Optional<AssetIssueList> getAssetIssueList() {
        if (blockingStubSolidity != null) {
            AssetIssueList assetIssueList = blockingStubSolidity
                    .getAssetIssueList(EmptyMessage.newBuilder().build());
            return Optional.ofNullable(assetIssueList);
        } else {
            AssetIssueList assetIssueList = blockingStubFull
                    .getAssetIssueList(EmptyMessage.newBuilder().build());
            return Optional.ofNullable(assetIssueList);
        }
    }

    public Optional<AssetIssueList> getAssetIssueList(long offset, long limit) {
        PaginatedMessage.Builder pageMessageBuilder = PaginatedMessage.newBuilder();
        pageMessageBuilder.setOffset(offset);
        pageMessageBuilder.setLimit(limit);
        if (blockingStubSolidity != null) {
            AssetIssueList assetIssueList = blockingStubSolidity.
                                                                        getPaginatedAssetIssueList(pageMessageBuilder.build());
            return Optional.ofNullable(assetIssueList);
        } else {
            AssetIssueList assetIssueList = blockingStubFull
                    .getPaginatedAssetIssueList(pageMessageBuilder.build());
            return Optional.ofNullable(assetIssueList);
        }
    }

    public Optional<ProposalList> getProposalListPaginated(long offset, long limit) {
        PaginatedMessage.Builder pageMessageBuilder = PaginatedMessage.newBuilder();
        pageMessageBuilder.setOffset(offset);
        pageMessageBuilder.setLimit(limit);
        ProposalList proposalList = blockingStubFull
                .getPaginatedProposalList(pageMessageBuilder.build());
        return Optional.ofNullable(proposalList);

    }

    public Optional<ExchangeList> getExchangeListPaginated(long offset, long limit) {
        PaginatedMessage.Builder pageMessageBuilder = PaginatedMessage.newBuilder();
        pageMessageBuilder.setOffset(offset);
        pageMessageBuilder.setLimit(limit);
        ExchangeList exchangeList = blockingStubFull
                .getPaginatedExchangeList(pageMessageBuilder.build());
        return Optional.ofNullable(exchangeList);

    }

    public Optional<NodeList> listNodes() {
        NodeList nodeList = blockingStubFull.listNodes(EmptyMessage.newBuilder().build());
        return Optional.ofNullable(nodeList);
    }

    public Optional<AssetIssueList> getAssetIssueByAccount(byte[] address) {
        ByteString addressBS = ByteString.copyFrom(address);
        Account request = Account.newBuilder().setAddress(addressBS).build();
        AssetIssueList assetIssueList = blockingStubFull.getAssetIssueByAccount(request);
        return Optional.ofNullable(assetIssueList);
    }

    public AccountNetMessage getAccountNet(byte[] address) {
        ByteString addressBS = ByteString.copyFrom(address);
        Account request = Account.newBuilder().setAddress(addressBS).build();
        return blockingStubFull.getAccountNet(request);
    }

    public AccountResourceMessage getAccountResource(byte[] address) {
        ByteString addressBS = ByteString.copyFrom(address);
        Account request = Account.newBuilder().setAddress(addressBS).build();
        return blockingStubFull.getAccountResource(request);
    }

    public Contract.AssetIssueContract getAssetIssueByName(String assetName) {
        ByteString assetNameBs = ByteString.copyFrom(assetName.getBytes());
        BytesMessage request = BytesMessage.newBuilder().setValue(assetNameBs).build();
        if (blockingStubSolidity != null) {
            return blockingStubSolidity.getAssetIssueByName(request);
        } else {
            return blockingStubFull.getAssetIssueByName(request);
        }
    }

    public Optional<AssetIssueList> getAssetIssueListByName(String assetName) {
        ByteString assetNameBs = ByteString.copyFrom(assetName.getBytes());
        BytesMessage request = BytesMessage.newBuilder().setValue(assetNameBs).build();
        if (blockingStubSolidity != null) {
            AssetIssueList assetIssueList = blockingStubSolidity.getAssetIssueListByName(request);
            return Optional.ofNullable(assetIssueList);
        } else {
            AssetIssueList assetIssueList = blockingStubFull.getAssetIssueListByName(request);
            return Optional.ofNullable(assetIssueList);
        }
    }

    public Contract.AssetIssueContract getAssetIssueById(String assetId) {
        ByteString assetIdBs = ByteString.copyFrom(assetId.getBytes());
        BytesMessage request = BytesMessage.newBuilder().setValue(assetIdBs).build();
        if (blockingStubSolidity != null) {
            return blockingStubSolidity.getAssetIssueById(request);
        } else {
            return blockingStubFull.getAssetIssueById(request);
        }
    }

    public NumberMessage getTotalTransaction() {
        return blockingStubFull.totalTransaction(EmptyMessage.newBuilder().build());
    }

    public NumberMessage getNextMaintenanceTime() {
        return blockingStubFull.getNextMaintenanceTime(EmptyMessage.newBuilder().build());
    }

    //  public Optional<AssetIssueList> getAssetIssueListByTimestamp(long time) {
    //    NumberMessage.Builder timeStamp = NumberMessage.newBuilder();
    //    timeStamp.setNum(time);
    //    AssetIssueList assetIssueList = blockingStubSolidity
    //        .getAssetIssueListByTimestamp(timeStamp.build());
    //    return Optional.ofNullable(assetIssueList);
    //  }

    //  public Optional<TransactionList> getTransactionsByTimestamp(long start, long end, int offset,
    //      int limit) {
    //    TimeMessage.Builder timeMessage = TimeMessage.newBuilder();
    //    timeMessage.setBeginInMilliseconds(start);
    //    timeMessage.setEndInMilliseconds(end);
    //    TimePaginatedMessage.Builder timePaginatedMessage = TimePaginatedMessage.newBuilder();
    //    timePaginatedMessage.setTimeMessage(timeMessage);
    //    timePaginatedMessage.setOffset(offset);
    //    timePaginatedMessage.setLimit(limit);
    //    TransactionList transactionList = blockingStubExtension
    //        .getTransactionsByTimestamp(timePaginatedMessage.build());
    //    return Optional.ofNullable(transactionList);
    //  }

    //  public NumberMessage getTransactionsByTimestampCount(long start, long end) {
    //    TimeMessage.Builder timeMessage = TimeMessage.newBuilder();
    //    timeMessage.setBeginInMilliseconds(start);
    //    timeMessage.setEndInMilliseconds(end);
    //    return blockingStubExtension.getTransactionsByTimestampCount(timeMessage.build());
    //  }

    public Optional<TransactionList> getTransactionsFromThis(byte[] address, int offset, int limit) {
        ByteString addressBS = ByteString.copyFrom(address);
        Account account = Account.newBuilder().setAddress(addressBS).build();
        AccountPaginated.Builder accountPaginated = AccountPaginated.newBuilder();
        accountPaginated.setAccount(account);
        accountPaginated.setOffset(offset);
        accountPaginated.setLimit(limit);
        TransactionList transactionList = blockingStubExtension
                .getTransactionsFromThis(accountPaginated.build());
        return Optional.ofNullable(transactionList);
    }

    public Optional<TransactionListExtention> getTransactionsFromThis2(byte[] address, int offset,
                                                                       int limit) {
        ByteString addressBS = ByteString.copyFrom(address);
        Account account = Account.newBuilder().setAddress(addressBS).build();
        AccountPaginated.Builder accountPaginated = AccountPaginated.newBuilder();
        accountPaginated.setAccount(account);
        accountPaginated.setOffset(offset);
        accountPaginated.setLimit(limit);
        TransactionListExtention transactionList = blockingStubExtension
                .getTransactionsFromThis2(accountPaginated.build());
        return Optional.ofNullable(transactionList);
    }

    //  public NumberMessage getTransactionsFromThisCount(byte[] address) {
    //    ByteString addressBS = ByteString.copyFrom(address);
    //    Account account = Account.newBuilder().setAddress(addressBS).build();
    //    return blockingStubExtension.getTransactionsFromThisCount(account);
    //  }

    public Optional<TransactionList> getTransactionsToThis(byte[] address, int offset, int limit) {
        ByteString addressBS = ByteString.copyFrom(address);
        Account account = Account.newBuilder().setAddress(addressBS).build();
        AccountPaginated.Builder accountPaginated = AccountPaginated.newBuilder();
        accountPaginated.setAccount(account);
        accountPaginated.setOffset(offset);
        accountPaginated.setLimit(limit);
        TransactionList transactionList = blockingStubExtension
                .getTransactionsToThis(accountPaginated.build());
        return Optional.ofNullable(transactionList);
    }

    public Optional<TransactionListExtention> getTransactionsToThis2(byte[] address, int offset,
                                                                     int limit) {
        ByteString addressBS = ByteString.copyFrom(address);
        Account account = Account.newBuilder().setAddress(addressBS).build();
        AccountPaginated.Builder accountPaginated = AccountPaginated.newBuilder();
        accountPaginated.setAccount(account);
        accountPaginated.setOffset(offset);
        accountPaginated.setLimit(limit);
        TransactionListExtention transactionList = blockingStubExtension
                .getTransactionsToThis2(accountPaginated.build());
        return Optional.ofNullable(transactionList);
    }
    //  public NumberMessage getTransactionsToThisCount(byte[] address) {
    //    ByteString addressBS = ByteString.copyFrom(address);
    //    Account account = Account.newBuilder().setAddress(addressBS).build();
    //    return blockingStubExtension.getTransactionsToThisCount(account);
    //  }

    public Optional<Transaction> getTransactionById(String txID) {
        ByteString bsTxid = ByteString.copyFrom(ByteArray.fromHexString(txID));
        BytesMessage request = BytesMessage.newBuilder().setValue(bsTxid).build();
        Transaction transaction = blockingStubFull.getTransactionById(request);
        return Optional.ofNullable(transaction);
    }

    public Optional<TransactionInfo> getTransactionInfoById(String txID) {
        ByteString bsTxid = ByteString.copyFrom(ByteArray.fromHexString(txID));
        BytesMessage request = BytesMessage.newBuilder().setValue(bsTxid).build();
        TransactionInfo transactionInfo;
        if (blockingStubSolidity != null) {
            transactionInfo = blockingStubSolidity.getTransactionInfoById(request);
        } else {
            transactionInfo = blockingStubFull.getTransactionInfoById(request);
        }
        return Optional.ofNullable(transactionInfo);
    }

    public Optional<Block> getBlockById(String blockID) {
        ByteString bsTxid = ByteString.copyFrom(ByteArray.fromHexString(blockID));
        BytesMessage request = BytesMessage.newBuilder().setValue(bsTxid).build();
        Block block = blockingStubFull.getBlockById(request);
        return Optional.ofNullable(block);
    }

    public Optional<BlockList> getBlockByLimitNext(long start, long end) {
        BlockLimit.Builder builder = BlockLimit.newBuilder();
        builder.setStartNum(start);
        builder.setEndNum(end);
        BlockList blockList = blockingStubFull.getBlockByLimitNext(builder.build());
        return Optional.ofNullable(blockList);
    }

    public Optional<BlockListExtention> getBlockByLimitNext2(long start, long end) {
        BlockLimit.Builder builder = BlockLimit.newBuilder();
        builder.setStartNum(start);
        builder.setEndNum(end);
        BlockListExtention blockList = blockingStubFull.getBlockByLimitNext2(builder.build());
        return Optional.ofNullable(blockList);
    }

    public Optional<BlockList> getBlockByLatestNum(long num) {
        NumberMessage numberMessage = NumberMessage.newBuilder().setNum(num).build();
        BlockList blockList = blockingStubFull.getBlockByLatestNum(numberMessage);
        return Optional.ofNullable(blockList);
    }

    public Optional<BlockListExtention> getBlockByLatestNum2(long num) {
        NumberMessage numberMessage = NumberMessage.newBuilder().setNum(num).build();
        BlockListExtention blockList = blockingStubFull.getBlockByLatestNum2(numberMessage);
        return Optional.ofNullable(blockList);
    }

    public TransactionExtention updateSetting(Contract.UpdateSettingContract request) {
        return blockingStubFull.updateSetting(request);
    }

    public TransactionExtention updateEnergyLimit(
            Contract.UpdateEnergyLimitContract request) {
        return blockingStubFull.updateEnergyLimit(request);
    }

    public TransactionExtention deployContract(Contract.CreateSmartContract request) {
        return blockingStubFull.deployContract(request);
    }

    public TransactionExtention triggerContract(Contract.TriggerSmartContract request) {
        return blockingStubFull.triggerContract(request);
    }

    public SmartContract getContract(byte[] address) {
        ByteString byteString = ByteString.copyFrom(address);
        BytesMessage bytesMessage = BytesMessage.newBuilder().setValue(byteString).build();
        return blockingStubFull.getContract(bytesMessage);
    }

    public static byte[] decodeFromBase58Check(String addressBase58) {
        if (StringUtils.isEmpty(addressBase58)) {
            log.warn("Warning: Address is empty !!");
            return null;
        }
        byte[] address = decode58Check(addressBase58);
        if (!addressValid(address)) {
            return null;
        }
        return address;
    }

    private static byte[] decode58Check(String input) {
        byte[] decodeCheck = Base58.decode(input);
        if (decodeCheck.length <= 4) {
            return null;
        }
        byte[] decodeData = new byte[decodeCheck.length - 4];
        System.arraycopy(decodeCheck, 0, decodeData, 0, decodeData.length);
        byte[] hash0 = Sha256Hash.hash(decodeData);
        byte[] hash1 = Sha256Hash.hash(hash0);
        if (hash1[0] == decodeCheck[decodeData.length] &&
            hash1[1] == decodeCheck[decodeData.length + 1] &&
            hash1[2] == decodeCheck[decodeData.length + 2] &&
            hash1[3] == decodeCheck[decodeData.length + 3]) {
            return decodeData;
        }
        return null;
    }
}