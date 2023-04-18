package org.midnightbsd.appstore.model.search;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author Lucas Holt
 */
@ToString
@EqualsAndHashCode
public class Instance implements Serializable, Comparable<Instance> {
    private static final long serialVersionUID = -6753971942731865621L;

    @Getter
    @Setter
    private String architecture;

    @Getter
    @Setter
    private String osVersion;

    @Getter
    @Setter
    private String version;

    @Getter
    @Setter
    private String cpe;

    @Getter
    @Setter
    private String flavor;

    @Override
    public int compareTo(final Instance o) {
        return this.architecture.compareTo(o.getArchitecture()) +
                this.osVersion.compareTo(o.getOsVersion()) +
                this.version.compareTo(o.getVersion());
    }
}
