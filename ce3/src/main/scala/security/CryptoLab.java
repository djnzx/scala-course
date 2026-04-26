package security;

import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;

import java.io.FileReader;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;

public class CryptoLab {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    static PrivateKey readPrivateKey(String path) throws Exception {
        try (PEMParser parser = new PEMParser(new FileReader(path))) {
            Object obj = parser.readObject();
            JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider("BC");

            if (obj instanceof PrivateKeyInfo info) {
                return converter.getPrivateKey(info);
            }

            if (obj instanceof PEMKeyPair pair) {
                return converter.getPrivateKey(pair.getPrivateKeyInfo());
            }

            throw new IllegalArgumentException("Unsupported private key: " + obj.getClass());
        }
    }

    static PublicKey readPublicKey(String path) throws Exception {
        try (PEMParser parser = new PEMParser(new FileReader(path))) {
            Object obj = parser.readObject();
            JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider("BC");

            if (obj instanceof SubjectPublicKeyInfo info) {
                return converter.getPublicKey(info);
            }

            throw new IllegalArgumentException("Unsupported public key: " + obj.getClass());
        }
    }
}