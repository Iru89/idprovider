package com.tfg.idprovider.model;

import java.util.ArrayList;
import java.util.Collection;

public enum Role {
    ROLE_USER{
       public String getName(){
           return ROLE_USER.toString();
       }

        public Collection<Privilege> getPrivileges(Role role) {
            Collection<Privilege> privileges = new ArrayList<>();
            privileges.add(Privilege.LOG_IN);
            privileges.add(Privilege.EDIT_PROFILE);
            return privileges;
        }
    },
    ROLE_ADMIN{
        public String getName(){
            return ROLE_ADMIN.toString();
        }
        public Collection<Privilege> getPrivileges(Role role) {
            Collection<Privilege> privileges = new ArrayList<>();
            privileges.add(Privilege.LOG_IN);
            privileges.add(Privilege.EDIT_PROFILE);
            privileges.add(Privilege.GET_KEYS);
            return privileges;
        }
    };

}
