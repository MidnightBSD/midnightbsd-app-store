package org.midnightbsd.appstore.model.magus;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

/**
 * Run data from Magus. For example, see http://www.midnightbsd.org/magus/api/runs
 *
 * {"status":"inactive","created":"2015-05-09 15:12:13.339435","arch":"amd64","osversion":"0.6","id":"298","blessed":0}
 * @author Lucas Holt
 */
@Data
public class Run {
    private int id;

    private String status;

    private Date created;

    private String arch;

    @JsonProperty("osversion")
    private String osVersion;

    private Boolean blessed;
}
