package dslabs.atmostonce;

import dslabs.framework.Address;
import dslabs.framework.Result;
import dslabs.kvstore.KVStore.KVStoreResult;
import java.util.HashMap;
import lombok.Data;

@Data
public final class AMOResult implements Result {
    // Your code here...
    private final KVStoreResult result;
    private final int sequence;
    public AMOResult(KVStoreResult result, int sequence){
        this.result= result;
        this.sequence=sequence;
    }
}
