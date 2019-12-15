/**
 * Copyright (c) 2014,2019 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.smarthome.binding.meteoblue.internal.json;

import com.google.gson.annotations.SerializedName;

/**
 * {@link JsonUnits} models the 'metadata' portion of the JSON
 * response to a weather request.
 *
 * @author Chris Carman - Initial contribution
 */
public class JsonMetadata {

    private String name;
    private Double latitude;
    private Double longitude;
    private Integer height;

    // [sic]
    @SerializedName("timezone_abbrevation")
    private String timeZoneAbbreviation;

    public JsonMetadata() {
    }

    public String getName() {
        return name;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Integer getHeight() {
        return height;
    }

    public String getTimeZoneAbbreviation() {
        return timeZoneAbbreviation;
    }
}
