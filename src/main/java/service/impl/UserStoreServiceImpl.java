package service.impl;

import repository.BigtableRepository;
import service.UserStoreService;

import java.io.IOException;

/**
 * Most of the code copied and pasted 😂
 * With ♥ by Mike Elkabetz
 * Date: 15/11/2021
 */

public class UserStoreServiceImpl implements UserStoreService {
    private static final String PARENT_DATA_TYPE = "user";
    protected BigtableRepository bigtableRepository;

    public void connectEmulator(String dataType, String host, int port) throws IOException {
        bigtableRepository = new BigtableRepository().connectEmulator(PARENT_DATA_TYPE, dataType, host, port);
    }

    public void connect(String dataType) throws IOException {
        bigtableRepository = new BigtableRepository().connect(PARENT_DATA_TYPE, dataType);
    }
}
