import model.UserAccount;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.impl.UserAccountStoreServiceImpl;

/**
 * Most of the code copied and pasted 😂
 * With ♥ by Mike Elkabetz
 * Date: 15/11/2021
 */

public class UserAccountStoreServiceTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserAccountStoreServiceTest.class);
    private static final String TENANT_ID = "cocaCola";
    final UserAccountStoreServiceImpl storeService = new UserAccountStoreServiceImpl();

    public UserAccountStoreServiceTest() {
        //TODO: Make sure Bigtable emulator is up and running locally
        storeService.connectEmulator("localhost", 9035);
    }

    @Test
    void testSetAndGetData() {
        LOGGER.info("** Test set and get data from Bigtable emulator started **");
        setNewUserAccount();
        UserAccount userAccount = getNewUserAccount();
        if ("it works :)".equalsIgnoreCase(userAccount.getStatus())) {
            LOGGER.info("** Test set and get data from Bigtable emulator completed successfully **");
            assert (true);
        } else {
            LOGGER.error("** Test set and get data from Bigtable emulator failed **");
            assert (false);
        }
    }

    private void setNewUserAccount() {
        UserAccount userAccount = new UserAccount().builder().accountIdentifier("123").status("it works :)").build();
        storeService.setUserAccount(TENANT_ID, userAccount.getAccountIdentifier(), userAccount);
    }

    private UserAccount getNewUserAccount() {
        return storeService.getUserAccount(TENANT_ID, "123");
    }
}
