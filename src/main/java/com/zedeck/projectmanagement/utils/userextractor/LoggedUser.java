package com.zedeck.projectmanagement.utils.userextractor;

import com.zedeck.projectmanagement.models.UserAccount;

;

public interface LoggedUser {

    UserInfo getInfo();

    UserAccount getUser();
}
