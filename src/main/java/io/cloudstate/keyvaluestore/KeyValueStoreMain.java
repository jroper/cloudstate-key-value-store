package io.cloudstate.keyvaluestore;

import com.google.protobuf.AnyProto;
import io.cloudstate.javasupport.CloudState;

public class KeyValueStoreMain {

    public static void main(String... args) {
        new CloudState().registerCrdtEntity(KeyValueStoreEntity.class,
                KeyValueStoreProtos.getDescriptor().findServiceByName("KeyValueStore"),
                KeyValueStoreModel.getDescriptor()
        ).start();
    }

}
