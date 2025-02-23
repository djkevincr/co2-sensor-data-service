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

import org.iot.co2sensor.model.Alert;
import org.iot.co2sensor.model.SensorStatus;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.OffsetDateTime;
import java.util.UUID;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class AlertRepositoryTests {

  @Autowired
  private AlertRepository alertRepository;

  @BeforeEach
  public void setup() {
    alertRepository.deleteAll();
  }

  @Test
  public void findLatestAlert() {
    String sensorId = UUID.randomUUID().toString();
    Alert alertOne = new Alert();
    alertOne.setSensorStatus(SensorStatus.OK);
    alertOne.setSensorId(sensorId);
    alertOne.setStartTime(OffsetDateTime.now().minusDays(2));
    alertOne.setEndTime(OffsetDateTime.now().minusDays(1));
    alertRepository.save(alertOne);

    Alert alertTwo = new Alert();
    alertTwo.setSensorStatus(SensorStatus.OK);
    alertTwo.setSensorId(sensorId);
    alertTwo.setStartTime(OffsetDateTime.now());
    alertRepository.save(alertTwo);

    Alert latestAlert = alertRepository.findLatestAlert(sensorId);
    Assert.assertEquals(alertTwo, latestAlert);
  }

}
