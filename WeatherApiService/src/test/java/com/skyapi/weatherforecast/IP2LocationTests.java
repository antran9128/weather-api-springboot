package com.skyapi.weatherforecast;

import com.ip2location.IP2Location;
import com.ip2location.IPResult;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class IP2LocationTests {
    private String DBPath = "ip2locdb/IP2LOCATION-LITE-DB3.BIN";

    @Test
    public void testInvalidIp() throws IOException {
        IP2Location ipLocator = new IP2Location();
        ipLocator.Open(DBPath);

        String ipAddress = "abc";
        IPResult ipResult = ipLocator.IPQuery(ipAddress);

        assertThat(ipResult.getStatus()).isEqualTo("INVALID_IP_ADDRESS");

        System.out.println(ipResult);
    }

    @Test
    public void testValidIp1() throws IOException {
        IP2Location ipLocator = new IP2Location();
        ipLocator.Open(DBPath);

        String ipAddress = "192.168.0.111";
        IPResult ipResult = ipLocator.IPQuery(ipAddress);

        assertThat(ipResult.getStatus()).isEqualTo("OK");

        System.out.println(ipResult);
    }

}
