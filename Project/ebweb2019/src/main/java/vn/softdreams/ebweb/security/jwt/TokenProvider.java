package vn.softdreams.ebweb.security.jwt;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;

import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import io.github.jhipster.config.JHipsterProperties;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import vn.softdreams.ebweb.security.EbUserDetails;
import vn.softdreams.ebweb.security.SecurityUtils;
import vn.softdreams.ebweb.web.rest.errors.BadRequestAlertException;

@Component
public class TokenProvider {

    private final Logger log = LoggerFactory.getLogger(TokenProvider.class);

    private static final String AUTHORITIES_KEY = "auth";

    private Key key;

    private long tokenValidityInMilliseconds;

    private long tokenValidityInMillisecondsForRememberMe;

    private final JHipsterProperties jHipsterProperties;

    public TokenProvider(JHipsterProperties jHipsterProperties) {
        this.jHipsterProperties = jHipsterProperties;
    }

    @Autowired
    RedisTemplate redisTemplate;

    @PostConstruct
    public void init() {
        byte[] keyBytes;
        String secret = jHipsterProperties.getSecurity().getAuthentication().getJwt().getSecret();
        if (!StringUtils.isEmpty(secret)) {
            log.warn("Warning: the JWT key used is not Base64-encoded. " +
                "We recommend using the `jhipster.security.authentication.jwt.base64-secret` key for optimum security.");
            keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        } else {
            log.debug("Using a Base64-encoded JWT secret key");
            keyBytes = Decoders.BASE64.decode(jHipsterProperties.getSecurity().getAuthentication().getJwt().getBase64Secret());
        }
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.tokenValidityInMilliseconds =
            1000 * jHipsterProperties.getSecurity().getAuthentication().getJwt().getTokenValidityInSeconds();
        this.tokenValidityInMillisecondsForRememberMe =
            1000 * jHipsterProperties.getSecurity().getAuthentication().getJwt()
                .getTokenValidityInSecondsForRememberMe();
    }

    public String createToken(Authentication authentication, boolean rememberMe, UUID org, UUID orgGetData) {
        String authorities = authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(","));

        long now = (new Date()).getTime();
        Date validity;
        if (rememberMe) {
            validity = new Date(now + this.tokenValidityInMillisecondsForRememberMe);
        } else {
            validity = new Date(now + this.tokenValidityInMilliseconds);
        }

        return Jwts.builder()
            .setSubject(authentication.getName())
            .claim("org", org != null ? org.toString() : "")
            .claim("orgGetData", orgGetData != null ? orgGetData.toString() : "")
            .claim(AUTHORITIES_KEY, authorities)
            .signWith(key, SignatureAlgorithm.HS512)
            .setExpiration(validity)
            .compact();
    }

    public String createToken(String name, List<String> auths, UUID org, UUID orgGetdata) {
        String authorities = String.join(",", auths);

        long now = (new Date()).getTime();
        Date validity = new Date(now + this.tokenValidityInMillisecondsForRememberMe);

        return Jwts.builder()
            .setSubject(name)
            .claim("org", org != null ? org.toString() : "")
            .claim("orgGetData", orgGetdata != null ? orgGetdata.toString() : "")
            .claim(AUTHORITIES_KEY, authorities)
            .signWith(key, SignatureAlgorithm.HS512)
            .setExpiration(validity)
            .compact();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parser()
            .setSigningKey(key)
            .parseClaimsJws(token)
            .getBody();

        Collection<? extends GrantedAuthority> authorities =
            Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        UUID org = !Strings.isNullOrEmpty(String.valueOf(claims.get("org"))) ? UUID.fromString(claims.get("org").toString()) : null;
        UUID orgGetData = (claims.get("orgGetData") != null && !Strings.isNullOrEmpty(String.valueOf(claims.get("orgGetData")))) ? UUID.fromString(claims.get("orgGetData").toString()) : null;
//        UUID org = UUID.fromString("b2bc3e97-bac1-c24f-afa3-935470752e2c");
        EbUserDetails principal = new EbUserDetails(claims.getSubject(), "", authorities, org, org == null, orgGetData);

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(key).parseClaimsJws(authToken);
            // add by Hautv check restore
//            if (redisTemplate.hasKey("restoreData")) {
////                List<UUID> lstCompanyIDRestore = redisTemplate.opsForList().range("restoreData", 0, -1);
////                if (lstCompanyIDRestore != null && lstCompanyIDRestore.size() > 0) {
////                    if (lstCompanyIDRestore.contains(SecurityUtils.getCurrentUserLoginAndOrg().get().getOrg())) {
////                        throw new BadRequestAlertException("Time Out - đang sao lưu dữ liệu", "", "");
////                    }
////                }
//                log.info("Go Here");
//            }
//            if (redisTemplate.hasKey("backupData")) {
//                List<UUID> lstCompanyIDBackup = redisTemplate.opsForList().range("backupData", 0, -1);
//                if (lstCompanyIDBackup != null && lstCompanyIDBackup.size() > 0) {
//                    if (lstCompanyIDBackup.contains(SecurityUtils.getCurrentUserLoginAndOrg().get().getOrg())) {
//                        throw new BadRequestAlertException("Time Out - đang khôi phục dữ liệu", "", "");
//                    }
//                }
//            }

            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT signature.");
            log.trace("Invalid JWT signature trace: {}", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token.");
            log.trace("Expired JWT token trace: {}", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token.");
            log.trace("Unsupported JWT token trace: {}", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT token compact of handler are invalid.");
            log.trace("JWT token compact of handler are invalid trace: {}", e);
        }
        return false;
    }
}
