package com.example.learninghubbackend.utils;

import jakarta.servlet.http.HttpServletRequest;
import ua_parser.Client;
import ua_parser.Parser;

import java.io.IOException;

public class ClientUtil {
    private static Parser parser;

    public static Parser getParser() throws IOException {
        if (parser == null) {
            parser = new Parser();
        }

        return parser;
    }

    public static String getOS(HttpServletRequest request) {
        Client deviceInfo = getDeviceInfo(request);
        if (deviceInfo == null) {
            return null;
        }

        return String.format("%s %s", deviceInfo.os.family, deviceInfo.os.major);
    }

    public static String getDevice(HttpServletRequest request) {
        Client deviceInfo = getDeviceInfo(request);
        if (deviceInfo == null) {
            return null;
        }

        return deviceInfo.device.family;
    }

    public static String getBrowser(HttpServletRequest request) {
        Client deviceInfo = getDeviceInfo(request);
        if (deviceInfo == null) {
            return null;
        }

        return String.format("%s %s", deviceInfo.userAgent.family, deviceInfo.userAgent.major);
    }

    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }

    private static Client getDeviceInfo(HttpServletRequest request) {
        String userAgentString = request.getHeader("User-Agent");
        if (userAgentString == null) {
            return null;
        }

        return parser.parse(userAgentString);
    }
}
