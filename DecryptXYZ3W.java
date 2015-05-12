import java.io.*;
import javax.crypto.*;
import javax.crypto.spec.*;

/**
 * XYZprinting.com has changed its *.3W format, and now
 * uses AES encrypted ZIP instead of simple base64-encoded ZIP.
 *
 * This code decrypts this new *.3W file and converts it back
 * to *.ZIP file.
 *
 * @see http://voltivo.com/forum/davinci-software/6-ways-to-print-with-3rd-party-slicer?start=20
 */
public class DecryptXYZ3W {
    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.err.println("Usage: DecryptXYZ3W infile.3w outfile.zip");
            System.exit(1);
        }

        String  infile = args[0];
        String outfile = args[1];

        int    len;
        byte[] buf = new byte[8192];

        // read infile as bytedata
        FileInputStream       fis = new FileInputStream(infile);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while ((len = fis.read(buf)) > 0) {
            bos.write(buf, 0, len);
        }
        byte[] data = bos.toByteArray();

        fis.close();
        bos.close();

        // this is the secret key
        String key = "@xyzprinting.com";
        byte[] kbb = key.getBytes("UTF-8");

        // initialize AES cipher engine
        byte[] iv = new byte[16];
        SecretKeySpec spec = new SecretKeySpec(kbb, "AES");
        Cipher aes = Cipher.getInstance("AES/CBC/PKCS5Padding");
        aes.init(Cipher.DECRYPT_MODE, spec, new IvParameterSpec(iv));

        // decrypt each block and save result (ZIP data)
        FileOutputStream fos = new FileOutputStream(args[1]);
        for (int i = 0x2000; i < data.length; i += 0x2010) {
            byte[] dec = aes.doFinal(data, i,
                                     Math.min(data.length - i, 0x2010));
            fos.write(dec);
        }
        fos.close();
    }
}
