package com.googoo.festivaldotcom.domain.member.domain.service;

import com.googoo.festivaldotcom.global.BlockedDomainsProperties.BlockedDomainsProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.naming.NamingException;
import javax.naming.directory.InitialDirContext;
import java.util.Hashtable;
import java.util.List;

@Service
public class DomainValidationService {

    @Autowired
    private BlockedDomainsProperties blockedDomainsProperties;

    // 도메인의 MX 레코드를 조회하여 해당 도메인이 이메일을 수신할 수 있는지 확인
    public boolean isDomainValid(String domain) {
        try {
            Hashtable<String, String> env = new Hashtable<>();
            env.put("java.naming.factory.initial", "com.sun.jndi.dns.DnsContextFactory");
            InitialDirContext dirContext = new InitialDirContext(env);

            // MX 레코드 조회
            dirContext.getAttributes(domain, new String[]{"MX"});
            return true;  // 유효한 도메인
        } catch (NamingException e) {
            return false; // 도메인이 유효하지 않거나 MX 레코드가 없는 경우
        }
    }

    // 이메일 도메인이 차단된 도메인인지 확인
    public boolean isBlockedDomain(String domain) {
        List<String> blockedDomains = blockedDomainsProperties.getDomains();
        return blockedDomains.contains(domain);
    }
}

