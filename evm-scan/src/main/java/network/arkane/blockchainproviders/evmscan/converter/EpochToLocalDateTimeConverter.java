package network.arkane.blockchainproviders.evmscan.converter;

import com.fasterxml.jackson.databind.util.StdConverter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class EpochToLocalDateTimeConverter extends StdConverter<Long, LocalDateTime> {

    @Override
    public LocalDateTime convert(Long epochTime) {
        return epochTime != null ? Instant.ofEpochMilli(epochTime * 1000).atZone(ZoneId.systemDefault()).toLocalDateTime() : null;
    }

}
