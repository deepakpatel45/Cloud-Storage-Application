package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CredentialService {

    private final CredentialMapper credentialMapper;
    private final EncryptionService encryptionService;

    public CredentialService(CredentialMapper credentialMapper,
                             EncryptionService encryptionService) {
        this.credentialMapper = credentialMapper;
        this.encryptionService = encryptionService;
    }

    public int addCredential(Credential credential) {
        String key = UUID.randomUUID().toString().substring(0, 16);
        String encryptedPassword =
                encryptionService.encryptValue(credential.getPassword(), key);

        credential.setKey(key);
        credential.setPassword(encryptedPassword);

        return credentialMapper.insertCredential(credential);
    }

    public List<Credential> getCredentials(Integer userid) {
        return credentialMapper.getCredentials(userid);
    }

    public Credential getCredential(Integer credentialid) {
        return credentialMapper.getCredential(credentialid);
    }

    public int updateCredential(Credential credential) {
        String key = UUID.randomUUID().toString().substring(0, 16);
        String encryptedPassword =
                encryptionService.encryptValue(credential.getPassword(), key);

        credential.setKey(key);
        credential.setPassword(encryptedPassword);

        return credentialMapper.updateCredential(credential);
    }

    public int deleteCredential(Integer credentialid) {
        return credentialMapper.deleteCredential(credentialid);
    }

    public String getDecryptedPassword(Credential credential) {
        return encryptionService.decryptValue(
                credential.getPassword(),
                credential.getKey()
        );
    }
}