package org.schabi.newpipe;

import org.junit.Test;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import okhttp3.Dns;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class Ipv4OnlyDnsTest {

    @Test
    public void mixedAddressesReturnOnlyIpv4() throws Exception {
        final InetAddress ipv4 = InetAddress.getByName("192.0.2.1");
        final InetAddress ipv6 = InetAddress.getByName("2001:db8::1");

        final Dns dns = hostname -> Arrays.asList(ipv6, ipv4);
        final List<InetAddress> result = new Ipv4OnlyDns(dns).lookup("example.com");

        assertEquals(Collections.singletonList(ipv4), result);
    }

    @Test
    public void ipv4OnlyReturnsIpv4() throws Exception {
        final InetAddress ipv4 = InetAddress.getByName("192.0.2.1");

        final Dns dns = hostname -> Collections.singletonList(ipv4);
        final List<InetAddress> result = new Ipv4OnlyDns(dns).lookup("example.com");

        assertEquals(1, result.size());
        assertTrue(result.get(0) instanceof Inet4Address);
    }

    @Test
    public void ipv6OnlyThrowsException() throws Exception {
        final InetAddress ipv6 = InetAddress.getByName("2001:db8::1");

        final Dns dns = hostname -> Collections.singletonList(ipv6);

        assertThrows(
                UnknownHostException.class,
                () -> new Ipv4OnlyDns(dns).lookup("example.com")
        );
    }
}
