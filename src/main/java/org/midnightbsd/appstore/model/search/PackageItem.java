package org.midnightbsd.appstore.model.search;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
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
public class PackageItem implements Serializable, Comparable<PackageItem> {
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
    
    @Override
    public int compareTo(final PackageItem o) {
        return this.id.compareTo(o.getId());
    }
}