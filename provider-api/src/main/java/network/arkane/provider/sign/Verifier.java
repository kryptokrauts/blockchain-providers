package network.arkane.provider.sign;

import net.jodah.typetools.TypeResolver;
import network.arkane.provider.sign.domain.Verifiable;

import java.util.regex.Matcher;

import static network.arkane.provider.sign.Signer.HEXADECIMAL_PATTERN;

public interface Verifier<T extends Verifiable> {


    boolean isValidSignature(T verifiable);


    default Class<T> getType() {
        return (Class<T>) TypeResolver.resolveRawArguments(Verifier.class, getClass())[0];
    }

    default boolean isHexadecimal(String input) {
        final Matcher matcher = HEXADECIMAL_PATTERN.matcher(input);
        return matcher.matches();
    }
}
