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
package org.iot.co2sensor.repo;

import org.iot.co2sensor.model.Measurement;
import org.iot.co2sensor.model.Metrics;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class MeasurementRepositoryTests {

  @Autowired
  private MeasurementRepository measurementRepository;

  @BeforeEach
  public void setup() {
    measurementRepository.deleteAll();
  }

  @Test
  public void findLastThreeMeasurements() {
    String sensorId = UUID.randomUUID().toString();
    OffsetDateTime serverNowTime = OffsetDateTime.now();

    Measurement one = createMeasurement(sensorId, serverNowTime.minusDays(10), 1000L);
    Measurement two = createMeasurement(sensorId, serverNowTime.minusDays(9), 2000L);
    Measurement three = createMeasurement(sensorId, serverNowTime.minusDays(8), 3000L);
    Measurement four = createMeasurement(sensorId, serverNowTime.minusDays(7), 3000L);
    Measurement five = createMeasurement(sensorId, serverNowTime.minusDays(6), 3000L);

    List<Measurement> measurements = measurementRepository.findLastThreeMeasurements(sensorId, serverNowTime.minusDays(10));

    Assert.assertEquals(five, measurements.get(0));
    Assert.assertEquals(four, measurements.get(1));
    Assert.assertEquals(three, measurements.get(2));
  }

  private Measurement createMeasurement(String sensorId, OffsetDateTime serverNowTime, Long co2){
    Measurement measurement = new Measurement();
    measurement.setCo2(co2);
    measurement.setServerTime(serverNowTime);
    measurement.setClientTime(OffsetDateTime.now());
    measurement.setSensorId(sensorId);
    measurementRepository.save(measurement);
    return measurement;
  }

  @Test
  public void getSensorMetrics() {
    String sensorId = UUID.randomUUID().toString();
    OffsetDateTime serverNowTime = OffsetDateTime.now();

    createMeasurement(sensorId, serverNowTime.minusDays(10), 1000L);
    createMeasurement(sensorId, serverNowTime.minusDays(9), 1000L);
    createMeasurement(sensorId, serverNowTime.minusDays(8), 3000L);
    createMeasurement(sensorId, serverNowTime.minusDays(7), 3000L);
    createMeasurement(sensorId, serverNowTime.minusDays(6), 12000L);

    Metrics metrics = measurementRepository.getSensorMetrics(sensorId, serverNowTime.minusDays(15));

    Assert.assertEquals(metrics.getMaxCo2(), new Long(12000L));
    Assert.assertEquals(metrics.getAverageCo2(), new Double(4000));
  }

}
