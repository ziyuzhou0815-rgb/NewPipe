
package org.schabi.newpipe;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.stream.Collectors;

import okhttp3.Dns;

/**
 * DNS resolver that returns IPv4 addresses only.
 */
public final class Ipv4OnlyDns implements Dns {

    private final Dns delegate;

    public Ipv4OnlyDns() {
        this(Dns.SYSTEM);
    }

    Ipv4OnlyDns(final Dns delegate) {
        this.delegate = delegate;
    }

    @Override
    public List<InetAddress> lookup(final String hostname)
            throws UnknownHostException {

        final List<InetAddress> addresses = delegate.lookup(hostname);

        final List<InetAddress> ipv4Addresses = addresses.stream()
                .filter(address -> address instanceof Inet4Address)
                .collect(Collectors.toList());

        if (ipv4Addresses.isEmpty()) {
            throw new UnknownHostException(
                    "No IPv4 address found for " + hostname);
        }

        return ipv4Addresses;
    }
}
