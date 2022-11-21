package br.com.rollo.rafael.tuitrapi.security;

import br.com.rollo.rafael.tuitrapi.domain.users.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.acls.domain.GrantedAuthoritySid;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.MutableAcl;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.security.acls.model.Permission;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class ACLPermissionRecord<T> {
    private MutableAclService aclService;

    @Autowired
    public ACLPermissionRecord(MutableAclService aclService) {
        this.aclService = aclService;
    }

    public void executeFor(T object, Permission... permissions) {
        MutableAcl acl = aclService.createAcl(new ObjectIdentityImpl(object));
        PrincipalSid sid = (PrincipalSid) acl.getOwner();

        Arrays.asList(permissions).forEach(permission -> {
            acl.insertAce(acl.getEntries().size(),
                    permission, sid, true);
            applyPermissionForAdmins(acl, permission);
        });

        aclService.updateAcl(acl);
    }

    private void applyPermissionForAdmins(MutableAcl acl, Permission permission) {
        GrantedAuthoritySid authoritySid = new GrantedAuthoritySid(Role.ADMIN);
        acl.insertAce(acl.getEntries().size(), permission, authoritySid, true);
    }
}
