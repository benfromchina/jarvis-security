package com.stark.jarvis.security.oauth2.authentication.password.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * IP 工具类
 *
 * @author <a href="mailto:mengbin@hotmail.com">Ben</a>
 * @version 1.0.0
 * @since 2026/11/19
 */
public class IpUtils {

    /**
     * 判断指定 IP 地址是否属于给定的 CIDR 网段。
     * 支持 IPv4（前缀长度 0～32）和 IPv6（前缀长度 0～128）。
     *
     * @param ip   待判断的 IP 地址
     * @param cidr CIDR 网段，例如 {@code 192.168.1.0/24} 或 {@code 2001:db8::/32}
     * @return IP 地址属于该网段时返回 {@code true}，否则返回 {@code false}
     * @throws IllegalArgumentException IP 地址、CIDR 格式或前缀长度无效时抛出
     */
    public static boolean isInCidr(String ip, String cidr) {
        String[] parts = cidr.split("/", -1);
        if (parts.length != 2) {
            throw new IllegalArgumentException("CIDR 格式错误");
        }

        byte[] ipBytes = parseIp(ip);
        byte[] networkBytes = parseIp(parts[0]);

        // IPv4 和 IPv6 地址不属于同一地址族
        if (ipBytes.length != networkBytes.length) {
            return false;
        }

        int prefix;
        try {
            prefix = Integer.parseInt(parts[1]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("CIDR 前缀格式错误", e);
        }

        int maxPrefix = ipBytes.length * 8;
        if (prefix < 0 || prefix > maxPrefix) {
            throw new IllegalArgumentException("前缀长度必须在 0 到 " + maxPrefix + " 之间");
        }

        for (int i = 0; i < ipBytes.length; i++) {
            int bits = Math.min(prefix, 8);
            int mask = bits == 0 ? 0 : (0xFF << (8 - bits)) & 0xFF;

            if (((ipBytes[i] & 0xFF) & mask) != ((networkBytes[i] & 0xFF) & mask)) {
                return false;
            }
            prefix -= bits;
        }

        return true;
    }

    /**
     * 将 IPv4 或 IPv6 地址解析为字节数组。
     * IPv4 地址返回 4 字节，IPv6 地址返回 16 字节。
     *
     * @param ip 要解析的 IP 地址
     * @return IP 地址对应的字节数组
     * @throws IllegalArgumentException IP 地址为空或格式无效时抛出
     */
    private static byte[] parseIp(String ip) {
        if (ip == null || ip.isEmpty()) {
            throw new IllegalArgumentException("IP 地址不能为空");
        }

        // IPv4 严格按四段十进制解析，避免把主机名或非标准格式当作 IP
        if (!ip.contains(":")) {
            String[] parts = ip.split("\\.", -1);
            if (parts.length != 4) {
                throw new IllegalArgumentException("不是有效的 IPv4 地址: " + ip);
            }

            byte[] result = new byte[4];
            for (int i = 0; i < 4; i++) {
                if (!parts[i].matches("[0-9]{1,3}")) {
                    throw new IllegalArgumentException("不是有效的 IPv4 地址: " + ip);
                }
                int value = Integer.parseInt(parts[i]);
                if (value > 255) {
                    throw new IllegalArgumentException("不是有效的 IPv4 地址: " + ip);
                }
                result[i] = (byte) value;
            }
            return result;
        }

        // 包含冒号时按 IPv6 解析；限制字符，避免将主机名传入解析器
        if (!ip.matches("[0-9a-fA-F:.]+")) {
            throw new IllegalArgumentException("不是有效的 IPv6 地址: " + ip);
        }
        try {
            byte[] result = InetAddress.getByName(ip).getAddress();
            if (result.length != 16) {
                throw new IllegalArgumentException("不是有效的 IPv6 地址: " + ip);
            }
            return result;
        } catch (UnknownHostException e) {
            throw new IllegalArgumentException("不是有效的 IPv6 地址: " + ip, e);
        }
    }

}
