package io.cloudstate.keyvaluestore;

import com.google.protobuf.ByteString;
import com.google.protobuf.Empty;
import io.cloudstate.javasupport.crdt.*;
import io.cloudstate.keyvaluestore.KeyValueStoreProtos.*;

import java.util.Optional;

@CrdtEntity
public class KeyValueStoreEntity {

    private final LWWRegister<KeyValueStoreModel.State> state;

    public KeyValueStoreEntity(Optional<LWWRegister<KeyValueStoreModel.State>> state, CrdtCreationContext ctx) {
        this.state = state.orElseGet(() -> ctx.newLWWRegister(KeyValueStoreModel.State.getDefaultInstance()));
    }

    @CommandHandler
    public GetStateResponseEnvelope getState(GetStateEnvelope envelope, CommandContext ctx) {
        checkEtagMatch(envelope.getEtagBytes(), ctx);
        return GetStateResponseEnvelope.newBuilder()
                .setData(state.get().getValue())
                .setEtagBytes(state.get().getEtagBytes())
                .build();

    }

    @CommandHandler
    public Empty saveState(SaveStateEnvelope envelope) {
        state.set(KeyValueStoreModel.State.newBuilder()
            .setValue(envelope.getValue())
                .setEtagBytes(envelope.getEtagBytes())
                .build()
        );
        return Empty.getDefaultInstance();
    }

    @CommandHandler
    public Empty deleteState(DeleteStateEnvelope envelope, CommandContext ctx) {
        checkEtagMatch(envelope.getEtagBytes(), ctx);
        state.set(KeyValueStoreModel.State.getDefaultInstance());
        return Empty.getDefaultInstance();
    }

    private void checkEtagMatch(ByteString etagBytes, CommandContext ctx) {
        if (!etagBytes.isEmpty() && !etagBytes.equals(state.get().getEtagBytes())) {
            ctx.fail("etag doesn't match");
        }
    }
}
