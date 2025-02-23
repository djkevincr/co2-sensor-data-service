/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.iot.co2sensor.controller;

import org.iot.co2sensor.exception.BadRequestException;
import org.iot.co2sensor.request.MeasurementRequest;
import org.iot.co2sensor.service.CO2SensorDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * CO2 Sensor Data Controller.
 *
 * @author Kevin Ratnasekera
 */
@RestController
@RequestMapping(path = "/api/v1/sensors")
public class CO2SensorDataController {

  private Logger logger = LoggerFactory.getLogger(CO2SensorDataController.class);

  @Autowired
  CO2SensorDataService co2SensorDataService;

  @PostConstruct
  public void init() {
    // do nothing
  }

  @PreDestroy
  public void cleanUp() {
    // clean up
  }

  @PostMapping(path = "/{uuid}/measurements", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity collectSensorMeasurements(@RequestBody MeasurementRequest measurementRequest,
                                                  @PathVariable("uuid") String uuid) {
    validateSensorCollectRequest(measurementRequest, uuid);
    logger.info("Collecting measurement for Sensor ID {} , CO2 Level {} , Client Time {}", uuid,
            measurementRequest.getCo2(), measurementRequest.getTime());
    co2SensorDataService.collectSensorMeasurements(uuid, measurementRequest.getCo2(), measurementRequest.getTime());
    return ResponseEntity.ok().build();
  }

  @GetMapping(path = "/{uuid}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity getSensorStatus(@PathVariable("uuid") String uuid) {
    validateSensorId(uuid);
    logger.info("Retrieve status for Sensor ID {}", uuid);
    return ResponseEntity.ok(co2SensorDataService.getSensorStatus(uuid).toString());
  }

  @GetMapping(path = "/{uuid}/metrics", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity getSensorMetrics(@PathVariable("uuid") String uuid) {
    validateSensorId(uuid);
    logger.info("Retrieve metrics for Sensor ID {}", uuid);
    return ResponseEntity.ok(co2SensorDataService.getSensorMetrics(uuid).toString());
  }

  private void validateSensorId(String uuid) {
    boolean isBadRequest = uuid == null || uuid.trim().length() == 0;
    if (isBadRequest) {
      throw new BadRequestException("Request validation failed.");
    }
  }

  private void validateSensorCollectRequest(MeasurementRequest measurementRequest, String uuid) {
    boolean isBadRequest = (uuid == null || uuid.trim().length() == 0 ||
            measurementRequest == null || measurementRequest.getCo2() == null ||
            measurementRequest.getTime() == null);
    if (isBadRequest) {
      throw new BadRequestException("Request validation failed.");
    }
  }

}
