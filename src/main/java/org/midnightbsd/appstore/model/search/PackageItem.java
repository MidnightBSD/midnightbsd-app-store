package org.midnightbsd.appstore.model.search;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.EntityListeners;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Lucas Holt
 */
@ToString
@EqualsAndHashCode
@Document(indexName = "package")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
@EntityListeners(PackageItem.class)
public class PackageItem implements Serializable, Comparable<PackageItem> {
    @Serial
    private static final long serialVersionUID = 3452319081969591585L;

    @Id
    @Getter
    @Setter
    private Integer id;

    @Field(type = FieldType.Nested)
    @Getter
    @Setter
    private List<Category> categories = new ArrayList<>();

    @Getter
    @Setter
    @Version
    private Long version;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private String description;

    @Getter
    @Setter
    private String url;

    @Getter
    @Setter
    private List<String> licenses;

    @Getter
    @Setter
    private List<Instance> instances;

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
    public int compareTo(final PackageItem o) {
        return this.id.compareTo(o.getId());
    }
}