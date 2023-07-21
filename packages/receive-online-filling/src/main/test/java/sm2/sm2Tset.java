package sm2;

import com.dr.archive.receive.online.utils.KeyUtils;
import com.dr.archive.receive.online.utils.SM2Util;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

/**
 * @author: qiuyf
 * @date: 2022/3/8 12:01
 */
public class sm2Tset {
    public static void main(String[] args) throws Exception{
        //获取密钥 [0] 公钥
        String[] keys = KeyUtils.generateSmKey();
        String test = "测试sm2加密解密";
        PublicKey publicKey= KeyUtils.createPublicKey(keys[0]);
        PrivateKey privateKey = KeyUtils.createPrivateKey(keys[1]);
        System.out.println("公钥:"+ keys[0]);
        System.out.println("私钥:"+ keys[1]);
        //用公钥加密
        byte[] encrypt = SM2Util.encrypt(test.getBytes(), publicKey);
        String str1 = Base64.getEncoder().encodeToString(encrypt);
        System.out.println("加密数据：" + str1);
        //用私钥解密
        byte[] decrypt = SM2Util.decrypt(encrypt, privateKey);

        System.out.println("解密数据："+new String(decrypt));

        byte[] sign = SM2Util.signByPrivateKey(test.getBytes(), privateKey);
        System.out.println("数据签名："+ Base64.getEncoder().encodeToString(sign));

        boolean b = SM2Util.verifyByPublicKey(test.getBytes(), publicKey,sign);
        System.out.println("数据验签："+ b);
    }
}
