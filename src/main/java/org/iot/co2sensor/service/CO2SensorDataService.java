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
package org.iot.co2sensor.service;

import org.iot.co2sensor.model.Metrics;
import org.iot.co2sensor.model.SensorStatus;

import java.time.OffsetDateTime;

/**
 * CO2 Sensor Data Service Interface.
 *
 * @author Kevin Ratnasekera
 */
public interface CO2SensorDataService {

  /**
   * collectSensorMeasurements.
   *
   * @param uuid sensor id
   * @param co2 co2 level
   * @param clientTime client time
   *
   * @return void
   */
  void collectSensorMeasurements(String uuid, Long co2, OffsetDateTime clientTime);

  /**
   * getSensorStatus.
   *
   * @param uuid sensor id
   *
   * @return SensorStatus
   */
  SensorStatus getSensorStatus(String uuid);

  /**
   * getSensorMetrics.
   *
   * @param uuid sensor id
   *
   * @return Metrics
   */
  Metrics getSensorMetrics(String uuid);

}
