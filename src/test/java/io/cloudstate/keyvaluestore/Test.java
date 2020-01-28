package io.cloudstate.keyvaluestore;

import com.google.protobuf.Any;
import com.google.protobuf.ByteString;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class Test {

    public static void main(String... args) throws Exception {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("34.70.194.125", 80).usePlaintext()
                .build();

        KeyValueStoreGrpc.KeyValueStoreBlockingStub client = KeyValueStoreGrpc.newBlockingStub(channel);

        client.saveState(KeyValueStoreProtos.SaveStateEnvelope.newBuilder()
                .setKey("foo")
                .setEtag("hello")
                .setValue(Any.newBuilder()
                        .setValue(ByteString.copyFrom("bar", "utf-8"))
                        .build())
                .build());
        System.out.println("Saved.");

        System.out.println("Got back value: " + client.getState(KeyValueStoreProtos.GetStateEnvelope.newBuilder()
                .setKey("foo")
                .build()).getData().getValue().toStringUtf8());

    }
}
