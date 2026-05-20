package com.claw.util;
import org.mindrot.jbcrypt.BCrypt;
/**
 * Utility class for secure password hashing and verification.
 * Uses BCrypt algorithm with a default work factor.
 */
public class PasswordUtils {
    /**
     * Hashes a plain-text password using BCrypt.
     * Hashing is a one-way process (you can&#39;t &quot;un-hash&quot; it back to the
     password).
     * This keeps user data safe even if the database is stolen.
     */
    public static String hashPassword(String plainTextPassword) {
// Step 1: Generate a random &quot;Salt&quot; to make the hash unique
// Step 2: Combine salt + password and scramble it 10 times
        (default)
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
    }
    /**
     * Verifies if a plain-text password matches a hashed password.
     */
    public static boolean checkPassword(String plainTextPassword,
                                        String hashedPassword) {
// Security check: If there is no hash to check against, fail
        immediately
        if (hashedPassword == null ||
                !hashedPassword.startsWith(&quot;$2a$&quot;)) {
            return false;
        }
        try {
// BCrypt.checkpw handles the complex logic of extracting
            the salt

// and re-hashing the input to see if they match.
            return BCrypt.checkpw(plainTextPassword, hashedPassword);
        } catch (Exception e) {
            System.err.println(&quot;Error verifying password: &quot; +
                    e.getMessage());
            return false;
        }
    }
}