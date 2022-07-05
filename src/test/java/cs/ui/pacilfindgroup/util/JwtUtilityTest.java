package cs.ui.pacilfindgroup.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@RunWith(SpringRunner.class)
class JwtUtilityTest {

    @Autowired
    @Qualifier("jwtutility")
    private JwtUtility jwtUtility;

    @Value("${jwt.secret}")
    private String secret;

    private String jwtToken;
    UserDetails userDetails;

    @BeforeEach
    void setUp() {
        this.userDetails = new User("Test", "secret", new ArrayList<>());
        this.secret = "secret";
        ReflectionTestUtils.setField(this.jwtUtility, "secret", this.secret);

        this.jwtToken = this.jwtUtility.generateToken(this.userDetails);
    }


    @Test
    void testGetUsernameFromTokenShouldReturnTheRightUsername() {
        assertEquals("Test", this.jwtUtility.getUsernameFromToken(this.jwtToken));
    }

    @Test
    void testGetExpirationDateFromTokenShouldReturnTheSameAS() {
        final Date issued = new Date();
        final Date exp = this.jwtUtility.getExpirationDateFromToken(this.jwtToken);
        final long diff = exp.getTime() - issued.getTime();
        final long hour = TimeUnit.MILLISECONDS.toHours(diff);
        assertEquals(9, hour);
    }

    @Test
    void testGetClaimFromTokenForUsername() {
        assertEquals("Test", this.jwtUtility.getClaimFromToken(this.jwtToken, Claims::getSubject));
    }

    @Test
    void testGenerateTokenShouldReturnStringNonNull() {
        assertEquals(String.class, this.jwtUtility.generateToken(this.userDetails).getClass());
        assertNotNull(this.jwtUtility.generateToken(this.userDetails));
    }

    @Test
    void testValidateTokenShouldReturnTrue() {
        assertTrue(this.jwtUtility.validateToken(this.jwtToken, this.userDetails));
    }

    @Test
    void testValidateTokenShouldThrowSignatureException() {
        final var token = this.jwtToken.substring(0, this.jwtToken.length() - 2);
        assertThrows(SignatureException.class, () -> this.jwtUtility.validateToken(token, this.userDetails));
    }

    @Test
    void testValidateTokenShouldThrowSignatureExceptionWhenUsedByDifferentUser() {
        final var user = new User("Halo", "", new ArrayList<>());
        final var token = this.jwtToken.substring(0, this.jwtToken.length() - 2);
        assertThrows(SignatureException.class, () -> this.jwtUtility.validateToken(token, user));
    }
}