package service;

import model.UserAccount;

/**
 * Most of the code copied and pasted 😂
 * With ♥ by Mike Elkabetz
 * Date: 15/11/2021
 */

public interface UserAccountStoreService {
    UserAccount getUserAccount(String tenantID, String accountIdentifier);

    void setUserAccount(String tenantID, String accountIdentifier, UserAccount accountData);
}
