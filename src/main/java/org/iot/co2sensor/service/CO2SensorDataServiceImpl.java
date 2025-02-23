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

import org.iot.co2sensor.constant.CO2SensorConstants;
import org.iot.co2sensor.exception.InternalServerException;
import org.iot.co2sensor.exception.SensorNotFoundException;
import org.iot.co2sensor.model.Alert;
import org.iot.co2sensor.model.Measurement;
import org.iot.co2sensor.model.Metrics;
import org.iot.co2sensor.model.SensorStatus;
import org.iot.co2sensor.repo.AlertRepository;
import org.iot.co2sensor.repo.MeasurementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;

/**
 * CO2 Sensor Data Service Implementation.
 *
 * @author Kevin Ratnasekera
 */
@Service
public class CO2SensorDataServiceImpl implements CO2SensorDataService {

  @Autowired
  private AlertRepository alertRepository;
  @Autowired
  private MeasurementRepository measurementRepository;

  @Transactional
  public void collectSensorMeasurements(String uuid, Long co2, OffsetDateTime clientTime) {
    try {
      OffsetDateTime serverNowTime = OffsetDateTime.now();
      saveMeasurement(uuid, co2, clientTime, serverNowTime);
      Alert latestAlert = alertRepository.findLatestAlert(uuid);
      if (Objects.isNull(latestAlert)) {
        saveInitialAlert(uuid, co2, serverNowTime);
      } else {
        if (co2 > CO2SensorConstants.CO2_SENSOR_THRESHOLD) {
          if (latestAlert.getSensorStatus() == SensorStatus.ALERT) {
            // do nothing
          } else if (latestAlert.getSensorStatus() == SensorStatus.WARN) {
            List<Measurement> lastMeasurements = measurementRepository.findLastThreeMeasurements(uuid, latestAlert.getStartTime());
            boolean isLastThreeExceedsThreshold = lastMeasurements
                    .stream()
                    .filter((input) -> input.getCo2() > CO2SensorConstants.CO2_SENSOR_THRESHOLD)
                    .count() == 3;
            if (isLastThreeExceedsThreshold) {
              storeNewAlert(latestAlert, uuid, serverNowTime, SensorStatus.ALERT);
            } else {
              // do nothing
            }
          } else {
            storeNewAlert(latestAlert, uuid, serverNowTime, SensorStatus.WARN);
          }
        } else if (co2 == CO2SensorConstants.CO2_SENSOR_THRESHOLD) {
          if (latestAlert.getSensorStatus() == SensorStatus.ALERT) {
            // do nothing
          } else if (latestAlert.getSensorStatus() == SensorStatus.WARN) {
            // do nothing
          } else {
            storeNewAlert(latestAlert, uuid, serverNowTime, SensorStatus.WARN);
          }
        } else {
          if (latestAlert.getSensorStatus() == SensorStatus.ALERT) {
            List<Measurement> lastMeasurements = measurementRepository.findLastThreeMeasurements(uuid, latestAlert.getStartTime());
            boolean isLastThreeBelowThreshold = lastMeasurements
                    .stream()
                    .filter((input) -> input.getCo2() < CO2SensorConstants.CO2_SENSOR_THRESHOLD)
                    .count() == 3;
            if (isLastThreeBelowThreshold) {
              storeNewAlert(latestAlert, uuid, serverNowTime, SensorStatus.OK);
            } else {
              // do nothing
            }
          } else if (latestAlert.getSensorStatus() == SensorStatus.WARN) {
            storeNewAlert(latestAlert, uuid, serverNowTime, SensorStatus.OK);
          } else {
            // do nothing
          }
        }
      }
    } catch (Throwable ex) {
      throw new InternalServerException("Internal Server Error.", ex);
    }
  }

  public SensorStatus getSensorStatus(String uuid) {
    try {
      Alert latestAlert = alertRepository.findLatestAlert(uuid);
      if (Objects.isNull(latestAlert)) {
        throw new SensorNotFoundException("Sensor Not Found.");
      }
      return latestAlert.getSensorStatus();
    } catch (Throwable ex) {
      throw new InternalServerException("Internal Server Error.", ex);
    }
  }

  public Metrics getSensorMetrics(String uuid) {
    try {
      Metrics metrics = measurementRepository.getSensorMetrics(uuid, OffsetDateTime.now().minusDays(30));
      if (Objects.isNull(metrics)) {
        throw new SensorNotFoundException("Sensor Not Found.");
      }
      return metrics;
    } catch (Throwable ex) {
      throw new InternalServerException("Internal Server Error.", ex);
    }
  }

  private void saveMeasurement(String uuid, Long co2, OffsetDateTime clientTime, OffsetDateTime serverNowTime) {
    Measurement measurement = new Measurement();
    measurement.setCo2(co2);
    measurement.setServerTime(serverNowTime);
    measurement.setClientTime(clientTime);
    measurement.setSensorId(uuid);
    measurementRepository.save(measurement);
  }

  private void saveInitialAlert(String uuid, Long co2, OffsetDateTime serverNowTime) {
    Alert alert = new Alert();
    if (co2 >= CO2SensorConstants.CO2_SENSOR_THRESHOLD) {
      alert.setSensorStatus(SensorStatus.WARN);
    } else {
      alert.setSensorStatus(SensorStatus.OK);
    }
    alert.setSensorId(uuid);
    alert.setStartTime(serverNowTime);
    alertRepository.save(alert);
  }

  private void storeNewAlert(Alert currentAlert,
                             String uuid,
                             OffsetDateTime serverNowTime,
                             SensorStatus newStatus) {
    currentAlert.setEndTime(serverNowTime);
    alertRepository.save(currentAlert);

    Alert alert = new Alert();
    alert.setSensorStatus(newStatus);
    alert.setSensorId(uuid);
    alert.setStartTime(serverNowTime);
    alertRepository.save(alert);
  }

}
