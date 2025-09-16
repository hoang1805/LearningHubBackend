package com.example.learninghubbackend.commons;


import com.example.learninghubbackend.utils.ClientUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ClientInfo {
    String ipAddress;
    String browser;
    String os;
    String device;

    public static ClientInfo getClientInfo(HttpServletRequest request) {
        ClientInfo clientInfo = new ClientInfo();
        clientInfo.setIpAddress(ClientUtil.getIpAddress(request));
        clientInfo.setBrowser(ClientUtil.getBrowser(request));
        clientInfo.setDevice(ClientUtil.getDevice(request));
        clientInfo.setOs(ClientUtil.getOS(request));
        return clientInfo;
    }
}
