package model;

import lombok.*;

import java.util.List;

/**
 * Most of the code copied and pasted 😂
 * With ♥ by Mike Elkabetz
 * Date: 15/11/2021
 */

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAccount {
    String accountIdentifier;
    String provider;
    String username;
    String domain;
    String fqdnUsername;
    String displayName;
    String emailNumber;
    String accountType;
    String status;
    String department;
    String manager;
    String phone;
    String title;

    @Singular
    List<String> usernameAliases;

    @Singular
    List<String> emailAddresses;

    @Singular
    List<String> permissions;

    @Singular
    List<String> groups;

    @Singular
    List<String> roles;
}
