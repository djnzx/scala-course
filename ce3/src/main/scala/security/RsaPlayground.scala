package security

import java.nio.charset.StandardCharsets
import java.security.KeyFactory
import java.security.PrivateKey
import java.security.PublicKey
import java.security.Signature
import java.security.spec.MGF1ParameterSpec
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.PSSParameterSpec
import java.security.spec.X509EncodedKeySpec
import java.util.Base64
import javax.crypto.Cipher
import org.scalatest.funsuite.AnyFunSuite

class RsaPlayground extends AnyFunSuite with Files with ToHex {

  test("1") {
    val pub = loadFromResources("keys/rsa-public-a.pem")
    val pvt = loadFromResources("keys/rsa-private-a.pem")

    pprint.pprintln(pvt)
    pprint.pprintln(pub)

    // -----BEGIN PRIVATE KEY-----     means PKCS#8
    // -----BEGIN RSA PRIVATE KEY----- means PKCS#1, RSA-specific, old
  }

  /** | Aspect | RSA                   | Ed25519                      |
    * | ------ | --------------------- | ---------------------------- |
    * | Type   | Old, general-purpose  | Modern, specialized          |
    * | Math   | Integer factorization | Elliptic curves (Curve25519) |
    * | Use    | Sign + encrypt        | Sign only                    |
    * | Status | Legacy but still used | Recommended today            |
    *
    * RSA:
    * sign(private, x) ≠ sign(private, x)
    *
    * ED25519:
    * sign(private, x) == sign(private, x)
    */
  test("RSA: Sign / Verify") {
    val pvtPath = mkPath("keys/rsa-private-a.pem")
    val pubPath = mkPath("keys/rsa-public-a.pem")

    val privateKey = CryptoLab.readPrivateKey(pvtPath)
    val publicKey = CryptoLab.readPublicKey(pubPath)

    val content = "hello".getBytes(StandardCharsets.UTF_8)

    // sign:
    // (content, private key) => signature
    val signer = Signature.getInstance("SHA256withRSA", "BC")
    signer.initSign(privateKey)
    signer.update(content)
    val signature: Array[Byte] = signer.sign
    pprint.log(signature.length) // 384 bytes

    val base64encoded = Base64.getEncoder.encodeToString(signature)
    pprint.log(base64encoded) // 512chars

    // verify
    // (content, signature, public key) => is_valid
    // verify(x, sign(private, x), public) == true
    val verifier = Signature.getInstance("SHA256withRSA", "BC")
    verifier.initVerify(publicKey)
    verifier.update(content)

    val ok: Boolean = verifier.verify(signature)
    System.out.println("valid = " + ok)
  }

  // todo: X25519
  test("Ed25519: Sign / Verify") {
    val pvtPath = mkPath("keys/ed25519-private-c.pem")
    val pubPath = mkPath("keys/ed25519-public-c.pem")

    val privateKey = CryptoLab.readPrivateKey(pvtPath)
    val publicKey = CryptoLab.readPublicKey(pubPath)

    val content = "hello".getBytes(StandardCharsets.UTF_8)

    // sign with private
    val signer = Signature.getInstance("Ed25519", "BC")
    signer.initSign(privateKey)
    signer.update(content)
    val signature = signer.sign
    pprint.log(signature.length) // 64 bytes

    val base64encoded = Base64.getEncoder.encodeToString(signature)
    pprint.log(base64encoded)
    pprint.log(base64encoded.length) // 88 chars

    // verify with public
    val verifier = Signature.getInstance("Ed25519", "BC")
    verifier.initVerify(publicKey)
    verifier.update(content)
    val ok = verifier.verify(signature)

    pprint.log(ok)
  }

  test("RSA: encrypt / decrypt") {
    val pvtPath = mkPath("keys/rsa-private-a.pem")
    val pubPath = mkPath("keys/rsa-public-a.pem")

    val privateKey = CryptoLab.readPrivateKey(pvtPath)
    val publicKey = CryptoLab.readPublicKey(pubPath)

    val plain: Array[Byte] = "hello".getBytes(StandardCharsets.UTF_8) // 5 bytes

    // encrypt
    val encryptor = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding", "BC")
    encryptor.init(Cipher.ENCRYPT_MODE, publicKey)
    val encrypted: Array[Byte] = encryptor.doFinal(plain)

    pprint.log(encrypted.length)                            // 384 byte
    pprint.log(Base64.getEncoder.encodeToString(encrypted)) // 512 chars
    pprint.log(bytesToHex(encrypted))                       // 768 chars

    // decrypt
    val decryptor = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding", "BC")
    decryptor.init(Cipher.DECRYPT_MODE, privateKey)
    val decrypted: Array[Byte] = decryptor.doFinal(encrypted)

    pprint.log(new String(decrypted))

  }

  test("RSA PKCS#1: Sign / Verify") {
    val pvtPath = mkPath("keys/rsa-pkcs1-b.pem")
    val pubPath = mkPath("keys/rsa-public-b.pem")

    val privateKey = CryptoLab.readPrivateKey(pvtPath)
    val publicKey = CryptoLab.readPublicKey(pubPath)

    pprint.log(privateKey) // RSA Private CRT Key
    pprint.log(publicKey)  // RSA Public Key

    val content = "hello".getBytes(StandardCharsets.UTF_8)

    // sign:
    // (content, private key) => signature
    val signer = Signature.getInstance("SHA256withRSA", "BC")
    signer.initSign(privateKey)
    signer.update(content)
    val signature: Array[Byte] = signer.sign
    pprint.log(signature.length) // 256 bytes

    val base64encoded = Base64.getEncoder.encodeToString(signature)
    pprint.log(base64encoded) // 355 chars

    // verify
    // (content, signature, public key) => is_valid
    // verify(x, sign(private, x), public) == true
    val verifier = Signature.getInstance("SHA256withRSA", "BC")
    verifier.initVerify(publicKey)
    verifier.update(content)

    val ok: Boolean = verifier.verify(signature)
    System.out.println("valid = " + ok)
  }

  // DER encoding helpers for PKCS#8 wrapping
  private def derLen(n: Int): Array[Byte] = n match {
    case n if n < 0x80  => Array(n.toByte)
    case n if n < 0x100 => Array(0x81.toByte, n.toByte)
    case _              => Array(0x82.toByte, (n >> 8).toByte, (n & 0xff).toByte)
  }
  private def derSeq(data: Array[Byte]): Array[Byte] = Array(0x30.toByte) ++ derLen(data.length) ++ data
  private def derOctet(data: Array[Byte]): Array[Byte] = Array(0x04.toByte) ++ derLen(data.length) ++ data

  // Wraps a PKCS#1 RSA key (BEGIN RSA PRIVATE KEY) in a PKCS#8 envelope
  // so that Java's PKCS8EncodedKeySpec can load it.
  private def toPkcs8(pkcs1: Array[Byte]): Array[Byte] = {
    // AlgorithmIdentifier for rsaEncryption OID + NULL
    val algId = Array[Byte](
      0x30,
      0x0d,
      0x06,
      0x09,
      0x2a,
      0x86.toByte,
      0x48,
      0x86.toByte,
      0xf7.toByte,
      0x0d,
      0x01,
      0x01,
      0x01,
      0x05,
      0x00
    )
    val version = Array[Byte](0x02, 0x01, 0x00) // INTEGER 0
    derSeq(version ++ algId ++ derOctet(pkcs1))
  }

  test("RSA PKCS#1: Sign / Verify - manual") {
    val content: Array[Byte] = "hello".getBytes(StandardCharsets.UTF_8)

    // RAW > trip prefix, suffix > Base64.decode > pkcs1-> pkcs8 > Spec > Factory > Key
    val privateKey: PrivateKey = {
      // private - loaded as a raw String
      val pem = loadFromResources("keys/rsa-pkcs1-b.pem")
      // private - stripped prefix, suffix and CR/CF
      val keyBase64 = pem
        .replaceAll("-----[^-]+-----", "")
        .replaceAll("\\s+", "")
      pprint.log(keyBase64)
      // private - decoded to raw bytes
      val keyBytes: Array[Byte] = Base64.getDecoder.decode(keyBase64)

      val isPkcs1 = pem.contains("BEGIN RSA PRIVATE KEY")

      // private converted to PKCS#8
      val keyBytesPkcs8 = isPkcs1 match {
        case true  => toPkcs8(keyBytes) // manually convert PKCS#1 -> PKCS#8
        case false => keyBytes
      }

      val keySpec = new PKCS8EncodedKeySpec(keyBytesPkcs8)
      pprint.log(keySpec)
      pprint.log(keySpec.getFormat) // PKCS#8

      val kf = KeyFactory.getInstance("RSA")
      val privateKey: PrivateKey = kf.generatePrivate(keySpec)
      privateKey
    }

    // sign
    val signer = Signature.getInstance("RSASSA-PSS")
    val algParamSpec = new PSSParameterSpec("SHA-256", "MGF1", MGF1ParameterSpec.SHA256, 32, 1)
    signer.setParameter(algParamSpec)
    signer.initSign(privateKey)
    signer.update(content)
    val signature = signer.sign

    val signatureBase64 = Base64.getEncoder.encodeToString(signature)
    pprint.log(signatureBase64)

    // RAW > trip prefix, suffix > Base64.decode > Spec > Factory > Key
    val publicKey: PublicKey = {
      // load public
      val publicKeyRaw: String = loadFromResources("keys/rsa-public-b.pem")

      val publicKeyBase64 = publicKeyRaw
        .replace("-----BEGIN PUBLIC KEY-----", "")
        .replace("-----END PUBLIC KEY-----", "")
        .replaceAll("\\s", "")

      val publicKeyBytes: Array[Byte] = Base64.getDecoder.decode(publicKeyBase64)
      val spec = new X509EncodedKeySpec(publicKeyBytes)
      val kf = KeyFactory.getInstance("RSA")
      val publicKey: PublicKey = kf.generatePublic(spec)
      pprint.log(publicKey)
      publicKey
    }

    // verify
    // (content, signature, public key) => is_valid
    // verify(x, sign(private, x), public) == true
    val verifier = Signature.getInstance("RSASSA-PSS")
    verifier.setParameter(algParamSpec)
    verifier.initVerify(publicKey)
    verifier.update(content)
    val ok = verifier.verify(signature)
    pprint.log(ok)
  }

}
