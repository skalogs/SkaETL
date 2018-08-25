package io.skalogs.skaetl.service.transform;

/*-
 * #%L
 * process-importer-impl
 * %%
 * Copyright (C) 2017 - 2018 SkaLogs
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.maxmind.db.CHMCache;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.*;
import io.skalogs.skaetl.domain.ParameterTransformation;
import io.skalogs.skaetl.domain.TypeValidation;
import io.skalogs.skaetl.service.TransformatorProcess;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;

@Slf4j
public class AddGeoLocalisationTransformator extends TransformatorProcess {

    private DatabaseReader reader;

    public AddGeoLocalisationTransformator(TypeValidation type) {
        super(type);

        // TODO: Replace this bullshit code
        try {
            File tmpFile = File.createTempFile("bwx", "dat");
            tmpFile.deleteOnExit();
            InputStream is = AddGeoLocalisationTransformator.class.getResourceAsStream("/GeoLite2-City.mmdb");
            OutputStream os = new FileOutputStream(tmpFile);

            byte[] buffer = new byte[4000];
            int len;
            while ((len = is.read(buffer)) != -1) {
                os.write(buffer, 0, len);
            }

            is.close();
            os.close();

            reader = new DatabaseReader.Builder(tmpFile).withCache(new CHMCache()).build();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void apply(String idProcess, ParameterTransformation parameterTransformation, ObjectNode jsonValue) {

        String key = parameterTransformation.getKeyField();
        String ipToResolve = getJsonUtils().at(jsonValue, parameterTransformation.getKeyField()).asText();
        log.debug("Start to localise IP address [{}]", ipToResolve);
        if (jsonValue.has(parameterTransformation.getKeyField())
                && jsonValue.path(parameterTransformation.getKeyField()).asText() != null
                && !jsonValue.path(parameterTransformation.getKeyField()).asText().equals("null")) {
            try {
                InetAddress ipAddress = InetAddress.getByName(ipToResolve);
                CityResponse response = reader.city(ipAddress);
                Country country = response.getCountry();
                Subdivision subdivision = response.getMostSpecificSubdivision();
                City city = response.getCity();
                Postal postal = response.getPostal();
                Location location = response.getLocation();


                put(jsonValue, key + "_country_name", country.getName());
                put(jsonValue, key + "_country_isocode", country.getIsoCode());
                put(jsonValue, key + "_city_name", response.getCity().getName());
                put(jsonValue, key + "_subdivision_name", subdivision.getName());
                put(jsonValue, key + "_subdivision_isocode", subdivision.getIsoCode());
                put(jsonValue, key + "_city_name", city.getName());
                put(jsonValue, key + "_city_postalcode", postal.getCode());
                put(jsonValue, key + "_location_gp", location.getLatitude().toString() + "," + location.getLongitude().toString());

            } catch (Exception ex) {
                log.error("Exception during Geo IP Transformation {}", ipToResolve);
                ex.printStackTrace();
            }
        }

        log.debug("End to localise IP address [{}]", ipToResolve);
    }
}
