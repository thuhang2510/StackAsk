package com.hang.stackask.service.interfaces;

import com.hang.stackask.data.AddUserData;
import com.hang.stackask.data.UserData;

public interface IUserService {
    UserData create(AddUserData data);
}
