package com.dr;

import com.dr.archive.fuzhou.bsp.autoconfig.BspConfig;
import com.dr.archive.fuzhou.bsp.utils.SM4Utils;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

/**
 * 用来测试sm4功能
 */
public class Sm4Test {
    public static void main(String[] args) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, NoSuchProviderException {
        BspConfig bspConfig = new BspConfig();
        String source = "admin";
        byte[] bytes = SM4Utils.encrypt_ECB_Padding(bspConfig.getSm4Key().getBytes(StandardCharsets.UTF_8), source.getBytes(StandardCharsets.UTF_8));
        System.out.println("加密后内容：" + ByteUtils.toHexString(bytes));
        System.out.println("加密后内容：" + new String(bytes, StandardCharsets.UTF_8));


        byte[] decode = SM4Utils.decrypt_ECB_Padding(bspConfig.getSm4Key().getBytes(StandardCharsets.UTF_8), bytes);
        System.out.println("加密后内容：" + new String(decode));
        System.out.println(new String(SM4Utils.decrypt_ECB_NoPadding(bspConfig.getSm4Key().getBytes(StandardCharsets.UTF_8), "cab81bd91f63e7d9c6cad7a6d35cafe4".getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8));
    }


}
