package network.arkane.provider.aeternity.sign;

import com.kryptokrauts.aeternity.sdk.constants.VirtualMachine;
import com.kryptokrauts.aeternity.sdk.domain.secret.impl.BaseKeyPair;
import com.kryptokrauts.aeternity.sdk.exception.TransactionCreateException;
import com.kryptokrauts.aeternity.sdk.service.aeternity.impl.AeternityService;
import com.kryptokrauts.aeternity.sdk.service.transaction.type.model.ContractCallTransactionModel;
import com.kryptokrauts.aeternity.sdk.util.EncodingUtils;
import lombok.extern.slf4j.Slf4j;
import network.arkane.provider.aeternity.secret.generation.AeternitySecretKey;
import network.arkane.provider.exceptions.ArkaneException;
import network.arkane.provider.sign.Signer;
import network.arkane.provider.sign.domain.Signature;
import network.arkane.provider.sign.domain.TransactionSignature;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AeternityContractCallTransactionSigner implements
                                                    Signer<AeternityContractCallSignable, AeternitySecretKey> {

    private AeternityService aeternityService;

    public AeternityContractCallTransactionSigner(
            final @Qualifier("aeternity-service") AeternityService aeternityService) {
        this.aeternityService = aeternityService;
    }


    @Override
    public Signature createSignature(final AeternityContractCallSignable signable,
                                     final AeternitySecretKey key) {
        ContractCallTransactionModel contractCallTransactionModel = ContractCallTransactionModel
                .builder()
                // the amount is optional because most contract calls are performed without sending a token
                .amount(signable.getAmount())
                .contractId(signable.getContractId())
                .callData(signable.getCallData())
                .callerId(signable.getCallerId())
                .nonce(signable.getNonce())
                .gas(signable.getGas())
                .gasPrice(signable.getGasPrice())
                // the fee is optional because the SDK can calculate it automatically
                .fee(signable.getFee())
                .virtualMachine(VirtualMachine.valueOf(signable.getTargetVM().name()))
                .build();

        String unsignedTx = aeternityService.transactions
                .blockingCreateUnsignedTransaction(contractCallTransactionModel);
        BaseKeyPair baseKeyPair = EncodingUtils.createBaseKeyPair(key.getKeyPair());
        try {
            String signedTx = aeternityService.transactions
                    .signTransaction(unsignedTx, baseKeyPair.getPrivateKey());
            return TransactionSignature.signTransactionBuilder().signedTransaction(signedTx).build();
        } catch (TransactionCreateException e) {
            log.error("Unable to sign transaction: {}", e.getMessage());
            throw ArkaneException.arkaneException()
                                 .errorCode("A problem occurred trying to sign the aeternity transaction")
                                 .cause(e)
                                 .build();
        }
    }
}
