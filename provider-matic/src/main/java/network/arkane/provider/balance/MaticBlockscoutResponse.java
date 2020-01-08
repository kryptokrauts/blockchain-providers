package network.arkane.provider.balance;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor
public class MaticBlockscoutResponse<T> {
    private String message;
    private String status;
    private T result;
}
