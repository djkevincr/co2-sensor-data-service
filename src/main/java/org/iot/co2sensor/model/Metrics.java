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
package org.iot.co2sensor.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

/**
 * Metrics Domain Class.
 *
 * @author Kevin Ratnasekera
 */
public class Metrics {
  private Long maxCo2;
  private Double averageCo2;

  public Metrics(Long maxCo2, Double averageCo2) {
    this.maxCo2 = maxCo2;
    this.averageCo2 = averageCo2;
  }

  public Long getMaxCo2() {
    return maxCo2;
  }

  public void setMaxCo2(Long maxCo2) {
    this.maxCo2 = maxCo2;
  }

  public Double getAverageCo2() {
    return averageCo2;
  }

  public void setAverageCo2(Double averageCo2) {
    this.averageCo2 = averageCo2;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Metrics metrics = (Metrics) o;
    return Objects.equals(maxCo2, metrics.maxCo2) && Objects.equals(averageCo2, metrics.averageCo2);
  }

  @Override
  public int hashCode() {
    return Objects.hash(maxCo2, averageCo2);
  }

  @Override
  public String toString() {
    JSONObject jsonObject = new JSONObject();
    try {
      jsonObject.put("maxLast30Days", maxCo2);
      jsonObject.put("avgLast30Days", averageCo2);
    } catch (JSONException ex) {
      // do nothing
    }
    return jsonObject.toString();
  }

}
