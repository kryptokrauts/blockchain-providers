package network.arkane.provider.sign.domain;

public enum SignatureType {
    HEX_SIGNATURE(Values.HEX_SIGNATURE),
    SUBMITTED_AND_SIGNED_TRANSACTION_SIGNATURE(Values.SUBMITTED_AND_SIGNED_TRANSACTION_SIGNATURE),
    TRANSACTION_SIGNATURE(Values.TRANSACTION_SIGNATURE),
    RAW_SIGNATURE(Values.RAW_SIGNATURE);

    SignatureType(final String stringValue) {
        if (!this.name().equals(stringValue)) {
            throw new IllegalStateException("Incorrect String value for SignatureType");
        }
    }

    public static class Values {
        public static final String HEX_SIGNATURE = "HEX_SIGNATURE";
        public static final String SUBMITTED_AND_SIGNED_TRANSACTION_SIGNATURE = "SUBMITTED_AND_SIGNED_TRANSACTION_SIGNATURE";
        public static final String TRANSACTION_SIGNATURE = "TRANSACTION_SIGNATURE";

        public static final String RAW_SIGNATURE = "RAW_SIGNATURE";
    }
}


