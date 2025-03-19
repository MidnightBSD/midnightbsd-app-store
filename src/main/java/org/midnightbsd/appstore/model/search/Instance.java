package org.midnightbsd.appstore.model.search;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.LastModifiedDate;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Lucas Holt
 */
@ToString
@EqualsAndHashCode
@EntityListeners(Instance.class)
public class Instance implements Serializable, Comparable<Instance> {
    @Serial
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

    @Getter
    @Setter
    @LastModifiedDate
    private LocalDateTime lastModifiedDate;

    @PrePersist
    public void prePersist() {
        this.lastModifiedDate = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.lastModifiedDate = LocalDateTime.now();
    }

    @Override
    public int compareTo(final Instance o) {
        return this.architecture.compareTo(o.getArchitecture()) +
                this.osVersion.compareTo(o.getOsVersion()) +
                this.version.compareTo(o.getVersion());
    }
}
