package network.arkane.blockchainproviders.evmscan.converter;

import com.fasterxml.jackson.databind.util.StdConverter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class EpochMillisToLocalDateTimeConverter extends StdConverter<Long, LocalDateTime> {

    @Override
    public LocalDateTime convert(Long epochMillis) {
        return epochMillis != null ? Instant.ofEpochMilli(epochMillis).atZone(ZoneId.systemDefault()).toLocalDateTime() : null;
    }


}
