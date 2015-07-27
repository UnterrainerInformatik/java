/**************************************************************************
 * <pre>
 *
 * Copyright (c) Unterrainer Informatik OG.
 * This source is subject to the Microsoft Public License.
 *
 * See http://www.microsoft.com/opensource/licenses.mspx#Ms-PL.
 * All other rights reserved.
 *
 * (In other words you may copy, use, change and redistribute it without
 * any restrictions except for not suing me because it broke something.)
 *
 * THIS CODE AND INFORMATION IS PROVIDED "AS IS" WITHOUT WARRANTY OF ANY
 * KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND/OR FITNESS FOR A PARTICULAR
 * PURPOSE.
 *
 * </pre>
 ***************************************************************************/
package info.unterrainer.java.tools.utils;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

import lombok.experimental.UtilityClass;

import com.google.common.io.BaseEncoding;

/**
 * This class provides some implementations of the most common encryption-methods currently available.
 */
@UtilityClass
public class EncryptionUtils {

	/**
	 * For the salt to be effective it needs to be long. A rule of thumb is to make the salt at least as long as the output from the hash algorithm. In the case
	 * of SHA-512 this means the output should be 512 bits (= 64 bytes).
	 */
	private static final int SALT_LENGTH_IN_BYTES = 64;

	/**
	 * Generates salt using the standard secure random-provider.
	 *
	 * @return the string containing the salt
	 */
	public static String generateSalt() {
		final Random r = new SecureRandom();
		byte[] salt = new byte[SALT_LENGTH_IN_BYTES];
		r.nextBytes(salt);
		return BaseEncoding.base64().encode(salt);
	}

	/**
	 * Returns the SHA-1 hash of a salted password for you to save or compare it to already saved user-credentials.
	 *
	 * @param password the password to hash
	 * @param passwordEncoding the encoding that was used to encode the password-string (use 'UTF-16LE' when dealing with ASP.net MembershipProvider table
	 *            entries)
	 * @param salt the salt to prepend when hashing
	 * @return the calculated hash
	 * @throws NoSuchAlgorithmException if the SHA-1 algorithm is not supported by your JAVA installation
	 */
	public static String getSha1Hash(String password, Charset passwordEncoding, String salt) throws NoSuchAlgorithmException {
		return getHash(password, passwordEncoding, salt, "SHA-1");
	}

	/**
	 * Returns the SHA-512 hash of a salted password for you to save or compare it to already saved user-credentials.
	 *
	 * @param password the password to hash
	 * @param passwordEncoding the encoding that was used to encode the password-string
	 * @param salt the salt to prepend when hashing
	 * @return the calculated hash
	 * @throws NoSuchAlgorithmException if the SHA-512 algorithm is not supported by your JAVA installation
	 */
	public static String getSha512Hash(String password, Charset passwordEncoding, String salt) throws NoSuchAlgorithmException {
		return getHash(password, passwordEncoding, salt, "SHA-512");
	}

	private static String getHash(String password, Charset passwordEncoding, String salt, String algorithm) throws NoSuchAlgorithmException {

		MessageDigest md;
		byte[] saltBytes = BaseEncoding.base64().decode(salt);
		byte[] pwdBytes = password.getBytes(passwordEncoding);

		md = MessageDigest.getInstance(algorithm);
		md.update(saltBytes);
		md.update(pwdBytes);
		byte[] digest = md.digest();

		return BaseEncoding.base64().encode(digest);
	}
}
