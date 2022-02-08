package network.arkane.provider.hedera.sign;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.CustomFee;
import com.hedera.hashgraph.sdk.CustomFixedFee;
import com.hedera.hashgraph.sdk.CustomFractionalFee;
import com.hedera.hashgraph.sdk.CustomRoyaltyFee;
import com.hedera.hashgraph.sdk.FeeAssessmentMethod;
import com.hedera.hashgraph.sdk.Hbar;
import com.hedera.hashgraph.sdk.TokenCreateTransaction;
import com.hedera.hashgraph.sdk.TokenId;
import com.hedera.hashgraph.sdk.TokenSupplyType;
import com.hedera.hashgraph.sdk.Transaction;
import lombok.extern.slf4j.Slf4j;
import network.arkane.provider.hedera.HederaClientFactory;
import network.arkane.provider.hedera.secret.generation.HederaSecretKey;
import network.arkane.provider.hedera.token.HederaCustomFee;
import network.arkane.provider.hedera.token.HederaFeeAssessmentMethod;
import network.arkane.provider.hedera.token.HederaFixedFee;
import network.arkane.provider.hedera.token.HederaFractionalFee;
import network.arkane.provider.hedera.token.HederaRoyaltyFee;
import network.arkane.provider.hedera.token.HederaTokenSupplyType;
import network.arkane.provider.sign.Signer;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Component
@Slf4j
public class TokenCreationSigner extends HederaSigner<HederaTokenCreationSignable, TokenCreateTransaction> implements Signer<HederaTokenCreationSignable, HederaSecretKey> {

    private final HederaClientFactory clientFactory;

    public TokenCreationSigner(final HederaClientFactory clientFactory) {
        this.clientFactory = clientFactory;
    }

    @Override
    protected Transaction<TokenCreateTransaction> createTransaction(final HederaTokenCreationSignable signable,
                                                                    final HederaSecretKey key) {
        final AccountId accountId = AccountId.fromString(signable.getAccountId());
        final TokenCreateTransaction tokenCreateTransaction = new TokenCreateTransaction()
                .setCustomFees(signable.getCustomFees().stream()
                                       .map(this::toTokenCreateTxCustomFee)
                                       .collect(Collectors.toList()))
                .setDecimals(signable.getDecimals())
                .setInitialSupply(signable.getInitialSupply())
                .setFreezeDefault(signable.isFreezeDefault())
                .setMaxSupply(signable.getMaxSupply())
                .setSupplyKey(key.getKey())
                .setFreezeKey(key.getKey())
                .setWipeKey(key.getKey())
                .setFeeScheduleKey(key.getKey())
                .setKycKey(key.getKey())
                .setAdminKey(key.getKey());
        this.setIfNotNull(signable::getTreasuryAccountId, AccountId::fromString, tokenCreateTransaction::setTreasuryAccountId);
        this.setIfNotNull(signable::getTokenName, tokenCreateTransaction::setTokenName);
        this.setIfNotNull(signable::getTokenSymbol, tokenCreateTransaction::setTokenSymbol);
        this.setIfNotNull(signable::getExpirationTime, ZonedDateTime::toInstant, tokenCreateTransaction::setExpirationTime);
        this.setIfNotNull(signable::getAutoRenewPeriod, tokenCreateTransaction::setAutoRenewPeriod);
        this.setIfNotNull(signable::getTokenMemo, tokenCreateTransaction::setTokenMemo);
        this.setIfNotNull(signable::getSupplyType, this::mapTokenSupplyType, tokenCreateTransaction::setSupplyType);
        return tokenCreateTransaction.freezeWith(clientFactory.buildClient(accountId, key.getKey()))
                                     .sign(key.getKey());
    }

    private CustomFee toTokenCreateTxCustomFee(final HederaCustomFee hederaCustomFee) {
        if (hederaCustomFee.toFixedFee().isPresent()) {
            return this.mapFixedFee(hederaCustomFee.toFixedFee().get());
        }
        if (hederaCustomFee.toFractionalFee().isPresent()) {
            return this.mapFractionalFee(hederaCustomFee.toFractionalFee().get());
        }
        if (hederaCustomFee.toRoyaltyFee().isPresent()) {
            return this.mapRoyaltyFee(hederaCustomFee.toRoyaltyFee().get());
        }
        throw new IllegalArgumentException("HederaCustomFee not supported for type: " + hederaCustomFee.getType());
    }


    private CustomFixedFee mapFixedFee(final HederaFixedFee hederaFixedFee) {
        final CustomFixedFee fee = new CustomFixedFee()
                .setAmount(hederaFixedFee.getAmount());
        this.setIfNotNull(hederaFixedFee::getDenominatingTokenId, TokenId::fromString, fee::setDenominatingTokenId);
        this.setIfNotNull(hederaFixedFee::getFeeCollectorAccountId, AccountId::fromString, fee::setFeeCollectorAccountId);
        this.setIfNotNull(hederaFixedFee::getHbarAmount, Hbar::from, fee::setHbarAmount);
        return fee;
    }

    private CustomFractionalFee mapFractionalFee(final HederaFractionalFee hederaFractionalFee) {
        final CustomFractionalFee fee = new CustomFractionalFee()
                .setMin(hederaFractionalFee.getMin())
                .setMax(hederaFractionalFee.getMax())
                .setNumerator(hederaFractionalFee.getNumerator())
                .setDenominator(hederaFractionalFee.getDenominator());
        this.setIfNotNull(hederaFractionalFee::getAssessmentMethod, this::mapAssessmentMethod, fee::setAssessmentMethod);
        this.setIfNotNull(hederaFractionalFee::getFeeCollectorAccountId, AccountId::fromString, fee::setFeeCollectorAccountId);
        return fee;
    }

    private CustomFee mapRoyaltyFee(final HederaRoyaltyFee hederaRoyaltyFee) {
        final CustomRoyaltyFee fee = new CustomRoyaltyFee()
                .setDenominator(hederaRoyaltyFee.getDenominator())
                .setNumerator(hederaRoyaltyFee.getNumerator());
        this.setIfNotNull(hederaRoyaltyFee::getFallbackFee, this::mapFixedFee, fee::setFallbackFee);
        this.setIfNotNull(hederaRoyaltyFee::getFeeCollectorAccountId, AccountId::fromString, fee::setFeeCollectorAccountId);
        return fee;
    }

    private FeeAssessmentMethod mapAssessmentMethod(final HederaFeeAssessmentMethod assessmentMethod) {
        if (assessmentMethod == null) return null;
        if (assessmentMethod == HederaFeeAssessmentMethod.INCLUSIVE) {
            return FeeAssessmentMethod.INCLUSIVE;
        }
        if (assessmentMethod == HederaFeeAssessmentMethod.EXCLUSIVE) {
            return FeeAssessmentMethod.EXCLUSIVE;
        }
        throw new IllegalArgumentException("FeeAssessmentMethod not supported: " + assessmentMethod);
    }

    private TokenSupplyType mapTokenSupplyType(final HederaTokenSupplyType supplyType) {
        if (supplyType == HederaTokenSupplyType.FINITE) {
            return TokenSupplyType.FINITE;
        }
        if (supplyType == HederaTokenSupplyType.INFINITE) {
            return TokenSupplyType.INFINITE;
        }
        throw new IllegalArgumentException("HederaTokenSupplyType not supported: " + supplyType);
    }

    private <T> void setIfNotNull(final Supplier<T> value,
                                  final Consumer<T> setter) {
        if (value.get() != null) {
            setter.accept(value.get());
        }
    }

    private <T, R> void setIfNotNull(final Supplier<T> value,
                                     final Function<T, R> mapper,
                                     final Consumer<R> setter) {
        if (value.get() != null) {
            setter.accept(mapper.apply(value.get()));
        }
    }


}
