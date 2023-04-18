package org.midnightbsd.appstore.model.magus;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Representation of data from Magus run_ports API.
 * For example, http://www.midnightbsd.org/magus/api/run-ports-list?run=328&status=pass
 * @author Lucas Holt
 */
@Data
public class Port {
    private int id;

    private String port;

    private String version;

    private String osversion;

    private String arch;

    private String summary;

    @JsonProperty("can_reset")
    private Boolean canReset;

    private String description;

    /**
     * One or more licenses separated by spaces
     */
    private String license;

    private String www;

    private String flavor;

    private String cpe;
}