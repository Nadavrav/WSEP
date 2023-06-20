package DomainLayer.Users;

import DAL.DTOs.registeredUserDTO;

public class Admin extends RegisteredUser{

    public Admin(String userName, String password) throws Exception{
        super(userName,  password);

    }
    public Admin(registeredUserDTO userDTO)
    {
        super(userDTO);
    }

}
