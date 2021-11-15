package service;

import java.io.IOException;

/**
 * Most of the code copied and pasted 😂
 * With ♥ by Mike Elkabetz
 * Date: 15/11/2021
 */

public interface UserStoreService {
    void connectEmulator(String dataType, String host, int port) throws IOException;

    void connect(String dataType) throws IOException;
}
