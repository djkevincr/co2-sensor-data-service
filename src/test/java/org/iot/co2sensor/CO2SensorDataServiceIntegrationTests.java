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
package org.iot.co2sensor;

import org.iot.co2sensor.controller.CO2SensorDataController;
import org.iot.co2sensor.repo.AlertRepository;
import org.iot.co2sensor.repo.MeasurementRepository;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class CO2SensorDataServiceIntegrationTests {

  public static final String URL_TEMPLATE = "/api/v1/sensors/{uuid}";

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private CO2SensorDataController co2SensorDataController;

  @Autowired
  private AlertRepository alertRepository;

  @Autowired
  private MeasurementRepository measurementRepository;

  @Autowired
  private WebApplicationContext webApplicationContext;

  @BeforeEach
  public void setup() {
    alertRepository.deleteAll();
    measurementRepository.deleteAll();
  }

  @Test
  public void contexLoads() {
    assertThat(co2SensorDataController).isNotNull();
  }

  @Test
  public void testSensorDataCollectorService() throws Exception {
    String sensorId = UUID.randomUUID().toString();
    String createMeasurementUrl = URL_TEMPLATE.replace("{uuid}", sensorId) + "/measurements";

    this.mockMvc.perform(post(createMeasurementUrl)
            .contentType(MediaType.APPLICATION_JSON)
            .content(createMeasurementRequest(1000L)))
            .andExpect(status().is2xxSuccessful());
    String getSensorStatusUrl = URL_TEMPLATE.replace("{uuid}", sensorId);
    this.mockMvc.perform(get(getSensorStatusUrl))
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.status", is("OK")));
    this.mockMvc.perform(post(createMeasurementUrl)
            .contentType(MediaType.APPLICATION_JSON)
            .content(createMeasurementRequest(2000L)))
            .andExpect(status().is2xxSuccessful());
    this.mockMvc.perform(get(getSensorStatusUrl))
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.status", is("WARN")));
    this.mockMvc.perform(post(createMeasurementUrl)
            .contentType(MediaType.APPLICATION_JSON)
            .content(createMeasurementRequest(2000L)))
            .andExpect(status().is2xxSuccessful());
    this.mockMvc.perform(get(getSensorStatusUrl))
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.status", is("WARN")));
    this.mockMvc.perform(post(createMeasurementUrl)
            .contentType(MediaType.APPLICATION_JSON)
            .content(createMeasurementRequest(2000L)))
            .andExpect(status().is2xxSuccessful());
    this.mockMvc.perform(get(getSensorStatusUrl))
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.status", is("ALERT")));
    this.mockMvc.perform(post(createMeasurementUrl)
            .contentType(MediaType.APPLICATION_JSON)
            .content(createMeasurementRequest(1000L)))
            .andExpect(status().is2xxSuccessful());
    this.mockMvc.perform(get(getSensorStatusUrl))
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.status", is("ALERT")));
    this.mockMvc.perform(post(createMeasurementUrl)
            .contentType(MediaType.APPLICATION_JSON)
            .content(createMeasurementRequest(4000L)))
            .andExpect(status().is2xxSuccessful());
    this.mockMvc.perform(get(getSensorStatusUrl))
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.status", is("ALERT")));
    this.mockMvc.perform(post(createMeasurementUrl)
            .contentType(MediaType.APPLICATION_JSON)
            .content(createMeasurementRequest(1000L)))
            .andExpect(status().is2xxSuccessful());
    this.mockMvc.perform(get(getSensorStatusUrl))
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.status", is("ALERT")));
    this.mockMvc.perform(post(createMeasurementUrl)
            .contentType(MediaType.APPLICATION_JSON)
            .content(createMeasurementRequest(1000L)))
            .andExpect(status().is2xxSuccessful());
    this.mockMvc.perform(get(getSensorStatusUrl))
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.status", is("ALERT")));
    this.mockMvc.perform(post(createMeasurementUrl)
            .contentType(MediaType.APPLICATION_JSON)
            .content(createMeasurementRequest(1000L)))
            .andExpect(status().is2xxSuccessful());
    this.mockMvc.perform(get(getSensorStatusUrl))
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.status", is("OK")));
    this.mockMvc.perform(post(createMeasurementUrl)
            .contentType(MediaType.APPLICATION_JSON)
            .content(createMeasurementRequest(1000L)))
            .andExpect(status().is2xxSuccessful());
    this.mockMvc.perform(get(getSensorStatusUrl))
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.status", is("OK")));

    String getMetricsUrl = URL_TEMPLATE.replace("{uuid}", sensorId) + "/metrics";
    this.mockMvc.perform(get(getMetricsUrl))
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.maxLast30Days", is(4000)))
            .andExpect(jsonPath("$.avgLast30Days", is(1600)));
  }

  @Test
  public void testSensorDataCollectorServiceStartWithWarn() throws Exception {
    String sensorId = UUID.randomUUID().toString();
    String createMeasurementUrl = URL_TEMPLATE.replace("{uuid}", sensorId) + "/measurements";

    this.mockMvc.perform(post(createMeasurementUrl)
            .contentType(MediaType.APPLICATION_JSON)
            .content(createMeasurementRequest(5000L)))
            .andExpect(status().is2xxSuccessful());
    String getSensorStatusUrl = URL_TEMPLATE.replace("{uuid}", sensorId);
    this.mockMvc.perform(get(getSensorStatusUrl))
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.status", is("WARN")));
    this.mockMvc.perform(post(createMeasurementUrl)
            .contentType(MediaType.APPLICATION_JSON)
            .content(createMeasurementRequest(2000L)))
            .andExpect(status().is2xxSuccessful());
    this.mockMvc.perform(get(getSensorStatusUrl))
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.status", is("WARN")));
    this.mockMvc.perform(post(createMeasurementUrl)
            .contentType(MediaType.APPLICATION_JSON)
            .content(createMeasurementRequest(1000L)))
            .andExpect(status().is2xxSuccessful());
    this.mockMvc.perform(get(getSensorStatusUrl))
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.status", is("OK")));
    this.mockMvc.perform(post(createMeasurementUrl)
            .contentType(MediaType.APPLICATION_JSON)
            .content(createMeasurementRequest(2000L)))
            .andExpect(status().is2xxSuccessful());
    this.mockMvc.perform(get(getSensorStatusUrl))
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.status", is("WARN")));
    this.mockMvc.perform(post(createMeasurementUrl)
            .contentType(MediaType.APPLICATION_JSON)
            .content(createMeasurementRequest(2000L)))
            .andExpect(status().is2xxSuccessful());
    this.mockMvc.perform(get(getSensorStatusUrl))
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.status", is("WARN")));
    this.mockMvc.perform(post(createMeasurementUrl)
            .contentType(MediaType.APPLICATION_JSON)
            .content(createMeasurementRequest(2000L)))
            .andExpect(status().is2xxSuccessful());
    this.mockMvc.perform(get(getSensorStatusUrl))
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.status", is("ALERT")));
    this.mockMvc.perform(post(createMeasurementUrl)
            .contentType(MediaType.APPLICATION_JSON)
            .content(createMeasurementRequest(1000L)))
            .andExpect(status().is2xxSuccessful());
    this.mockMvc.perform(get(getSensorStatusUrl))
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.status", is("ALERT")));
    this.mockMvc.perform(post(createMeasurementUrl)
            .contentType(MediaType.APPLICATION_JSON)
            .content(createMeasurementRequest(4000L)))
            .andExpect(status().is2xxSuccessful());
    this.mockMvc.perform(get(getSensorStatusUrl))
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.status", is("ALERT")));
    this.mockMvc.perform(post(createMeasurementUrl)
            .contentType(MediaType.APPLICATION_JSON)
            .content(createMeasurementRequest(1000L)))
            .andExpect(status().is2xxSuccessful());
    this.mockMvc.perform(get(getSensorStatusUrl))
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.status", is("ALERT")));
    this.mockMvc.perform(post(createMeasurementUrl)
            .contentType(MediaType.APPLICATION_JSON)
            .content(createMeasurementRequest(1000L)))
            .andExpect(status().is2xxSuccessful());
    this.mockMvc.perform(get(getSensorStatusUrl))
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.status", is("ALERT")));
    this.mockMvc.perform(post(createMeasurementUrl)
            .contentType(MediaType.APPLICATION_JSON)
            .content(createMeasurementRequest(1000L)))
            .andExpect(status().is2xxSuccessful());
    this.mockMvc.perform(get(getSensorStatusUrl))
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.status", is("OK")));

    String getMetricsUrl = URL_TEMPLATE.replace("{uuid}", sensorId) + "/metrics";
    this.mockMvc.perform(get(getMetricsUrl))
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.maxLast30Days", is(5000)))
            .andExpect(jsonPath("$.avgLast30Days", is(2000)));
  }

  private String createMeasurementRequest(Long co2) {
    JSONObject jsonObject = new JSONObject();
    try {
      jsonObject.put("co2", co2);
      jsonObject.put("time", "2019-02-01T18:55:47+00:00");
    } catch (JSONException ex) {
      // do nothing
    }
    return jsonObject.toString();
  }

}
