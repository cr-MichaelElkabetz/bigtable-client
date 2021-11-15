package service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NoArgsConstructor;
import model.UserAccount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.UserAccountStoreService;

import java.io.IOException;

/**
 * Most of the code copied and pasted 😂
 * With ♥ by Mike Elkabetz
 * Date: 15/11/2021
 */

@NoArgsConstructor
public class UserAccountStoreServiceImpl extends UserStoreServiceImpl implements UserAccountStoreService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserAccountStoreServiceImpl.class);
    private static final String CHILD_DATA_TYPE = "account";
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public UserAccount getUserAccount(String tenantID, String identity) {
        String userAccountString = bigtableRepository.getData(tenantID, identity);
        try {
            return objectMapper.readValue(userAccountString, UserAccount.class);
        } catch (JsonProcessingException e) {
            LOGGER.error("Unable to parse user data: ", e.getMessage(), e);
        }
        return null;
    }

    @Override
    public void setUserAccount(String tenantID, String identity, UserAccount userAccount) {
        try {
            bigtableRepository.setData(tenantID, userAccount.getAccountIdentifier(), objectMapper.writeValueAsString(userAccount));
        } catch (JsonProcessingException e) {
            LOGGER.error("Unable to write create a new user: ", e.getMessage(), e);
        }
    }

    public void connectEmulator(String host, int port) {
        try {
            super.connectEmulator(CHILD_DATA_TYPE, host, port);
        } catch (IOException e) {
            LOGGER.error("Error occurred when tried to connect emulator: ", e.getMessage(), e);
            System.exit(1);
        }
    }

    public void connect() {
        try {
            super.connect(CHILD_DATA_TYPE);
        } catch (IOException e) {
            LOGGER.error("Error occurred when tried to connect GCP: ", e.getMessage(), e);
            System.exit(1);
        }
    }
}
